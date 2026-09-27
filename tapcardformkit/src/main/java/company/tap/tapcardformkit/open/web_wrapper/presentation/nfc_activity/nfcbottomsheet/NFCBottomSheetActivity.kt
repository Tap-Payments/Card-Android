package company.tap.tapcardformkit.open.web_wrapper.presentation.nfc_activity.nfcbottomsheet

import android.app.AlertDialog
import android.content.Intent
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.HapticFeedbackConstants
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import company.tap.nfcreader.open.reader.TapEmvCard
import company.tap.tapcardformkit.R
import company.tap.tapcardformkit.open.CardDataConfiguration
import company.tap.tapcardformkit.open.web_wrapper.TapCardKit
import company.tap.tapcardformkit.open.web_wrapper.presentation.nfc_activity.TapNfcReaderModeHelper
import company.tap.taplocalizationkit.LocalizationManager
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Transparent host for [NfcBottomSheet]. Owns the NFC reader-mode session and hands the
 * scanned card back to the web form.
 */
class NFCBottomSheetActivity : AppCompatActivity(), TapNfcReaderModeHelper.Listener {
    private lateinit var nfcReader: TapNfcReaderModeHelper
    private var nfcBottomSheet: NfcBottomSheet? = null
    private var enableNfcDialog: AlertDialog? = null
    private var cardDelivered = false
    private val mainHandler = Handler(Looper.getMainLooper())
    private val resetStatus = Runnable { nfcBottomSheet?.showIdle() }
    private val lostTimeout = Runnable { showReadError("cardMoved") }
    private var awaitingRediscovery = false
    /** Short POS-style beep when the card is detected; created lazily, released in onDestroy. */
    private val toneGenerator: ToneGenerator? by lazy {
        runCatching { ToneGenerator(AudioManager.STREAM_NOTIFICATION, BEEP_VOLUME) }.getOrNull()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_three_ds_web_view)
        LocalizationManager.setLocale(this, Locale(CardDataConfiguration.lanuage.toString()))

        nfcReader = TapNfcReaderModeHelper(this, this)

        if (savedInstanceState == null) {
            nfcBottomSheet = NfcBottomSheet().also { it.show(supportFragmentManager, NfcBottomSheet.TAG) }
        } else {
            nfcBottomSheet = supportFragmentManager.findFragmentByTag(NfcBottomSheet.TAG) as? NfcBottomSheet
        }
    }

    override fun onResume() {
        super.onResume()
        when {
            !nfcReader.isNfcAvailable -> {
                Toast.makeText(this, nfcString("nfcUnsupported"), Toast.LENGTH_SHORT).show()
                closeSheet()
            }
            !nfcReader.isNfcEnabled -> showEnableNfcDialog()
            else -> nfcReader.enable()
        }
    }

    override fun onPause() {
        nfcReader.disable()
        super.onPause()
    }

    override fun onDestroy() {
        mainHandler.removeCallbacksAndMessages(null)
        toneGenerator?.release()
        enableNfcDialog?.dismiss()
        enableNfcDialog = null
        TapCardKit.NFCopened = false
        super.onDestroy()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        // Reader mode delivers tags via the callback; nothing to do with foreground-dispatch intents.
    }

    // region TapNfcReaderModeHelper.Listener

    override fun onCardDetected() {
        if (cardDelivered) return
        mainHandler.removeCallbacks(resetStatus)
        mainHandler.removeCallbacks(lostTimeout)
        // Re-discovery after a brief drop is silent: no second beep/haptic, keep the same status.
        if (!awaitingRediscovery) {
            window.decorView.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, BEEP_DURATION_MS)
        }
        awaitingRediscovery = false
        nfcBottomSheet?.showReading()
    }

    override fun onCardLost() {
        if (cardDelivered) return
        // Keep "hold it still" up while the stack re-polls; only complain if the card really left.
        awaitingRediscovery = true
        mainHandler.removeCallbacks(lostTimeout)
        mainHandler.postDelayed(lostTimeout, LOST_GRACE_MS)
    }

    override fun onReadProgress(commandsSent: Int, expectedCommands: Int) {
        if (cardDelivered) return
        nfcBottomSheet?.setReadProgress(commandsSent, expectedCommands)
    }

    override fun onCardRead(card: TapEmvCard) {
        if (cardDelivered) return
        cardDelivered = true
        nfcReader.disable()
        window.decorView.performHapticFeedback(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) HapticFeedbackConstants.CONFIRM
            else HapticFeedbackConstants.LONG_PRESS
        )
        nfcBottomSheet?.showSuccess()

        TapCardKit.fillCardNumber(
            cardNumber = card.cardNumber.orEmpty(),
            cardHolderName = card.holderFirstname.orEmpty(),
            cvv = "",
            expiryDate = formatExpiry(card)
        )
        TapCardKit.NFCopened = false
        // Let the user see the success state before the sheet slides away.
        mainHandler.postDelayed({ closeSheet() }, SUCCESS_LINGER_MS)
    }

    override fun onCardReadError(throwable: Throwable) {
        if (cardDelivered) return
        showReadError("scanFailed")
    }

    /** Reader mode stays armed, so the user just has to re-present the card. */
    private fun showReadError(messageKey: String) {
        awaitingRediscovery = false
        window.decorView.performHapticFeedback(REJECT_COMPAT)
        nfcBottomSheet?.showError(messageKey)
        mainHandler.removeCallbacks(resetStatus)
        mainHandler.postDelayed(resetStatus, ERROR_LINGER_MS)
    }

    // endregion

    /** Dismisses the sheet with its exit animation; the sheet's dismiss listener finishes the activity. */
    fun closeSheet() {
        val sheet = nfcBottomSheet
        if (sheet != null && sheet.isAdded) sheet.dismissAllowingStateLoss() else finish()
    }

    private fun showEnableNfcDialog() {
        if (enableNfcDialog?.isShowing == true) return
        enableNfcDialog = AlertDialog.Builder(this)
            .setTitle(nfcString("enableNFC"))
            .setMessage(nfcString("disabledNFC"))
            .setCancelable(false)
            .setPositiveButton(R.string.msg_ok) { dialog, _ ->
                dialog.dismiss()
                startActivity(Intent(Settings.ACTION_NFC_SETTINGS))
            }
            .setNegativeButton(R.string.msg_dismiss) { dialog, _ ->
                dialog.dismiss()
                closeSheet()
            }
            .show()
    }

    private fun nfcString(key: String): String =
        LocalizationManager.getValue(key, "NFC") as? String ?: key

    private fun formatExpiry(card: TapEmvCard): String {
        val date = card.expireDate ?: return ""
        return SimpleDateFormat("MM/yy", Locale.US).format(date)
    }

    private companion object {
        const val SUCCESS_LINGER_MS = 700L
        const val BEEP_VOLUME = 80
        const val BEEP_DURATION_MS = 120
        const val ERROR_LINGER_MS = 2_500L
        /** How long a dropped link may take to be re-discovered before we show an error. */
        const val LOST_GRACE_MS = 1_500L

        /** [HapticFeedbackConstants.REJECT] needs API 30; fall back to a plain tick below that. */
        val REJECT_COMPAT =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) HapticFeedbackConstants.REJECT
            else HapticFeedbackConstants.LONG_PRESS
    }
}
