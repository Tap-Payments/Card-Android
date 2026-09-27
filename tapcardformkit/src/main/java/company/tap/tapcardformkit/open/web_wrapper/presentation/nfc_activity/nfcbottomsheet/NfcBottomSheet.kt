package company.tap.tapcardformkit.open.web_wrapper.presentation.nfc_activity.nfcbottomsheet


import android.content.Context
import android.content.res.ColorStateList
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieCompositionFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tap.commondatamodels.TapLocal
import com.tap.commondatamodels.TapTheme
import company.tap.tapcardformkit.R
import company.tap.tapcardformkit.doAfterSpecificTime
import company.tap.tapcardformkit.open.web_wrapper.internal.TapBrandView
import company.tap.tapcardformkit.open.web_wrapper.internal.ThemeManager
import company.tap.tapcardformkit.open.web_wrapper.internal.ThemeManager.toColorIntOrDefault
import company.tap.tapcardformkit.open.web_wrapper.TapCardKit
import company.tap.taplocalizationkit.LocalizationManager


class NfcBottomSheet : BottomSheetDialogFragment() {

    private lateinit var  mShimmerViewContainer :LottieAnimationView
    private lateinit var statusCard: LinearLayout
    private lateinit var statusSpinner: ProgressBar
    private lateinit var statusIcon: ImageView
    private lateinit var statusText: TextView
    private lateinit var readProgress: ProgressBar
    private var statusTextColor = 0

    @Nullable
    override fun onCreateView(
        @NonNull inflater: LayoutInflater, @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.nfc_bottom_sheet, null)
        mShimmerViewContainer =  view.findViewById(R.id.shimmer_view)
        statusCard = view.findViewById(R.id.nfc_status_card)
        statusSpinner = view.findViewById(R.id.nfc_status_spinner)
        statusIcon = view.findViewById(R.id.nfc_status_icon)
        statusText = view.findViewById(R.id.nfc_status_text)
        readProgress = view.findViewById(R.id.nfc_progress)
        applyStatusTheme()
        loadLottie()
        return view
    }

    fun loadLottie() {
        val url = animationUrl() ?: return
        mShimmerViewContainer.setFailureListener { Log.w(TAG, "Failed to load NFC animation", it) }
        mShimmerViewContainer.setAnimationFromUrl(url)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tapBrandView = view.findViewById<TapBrandView>(R.id.tab_brand_view_nfc)

        // Let the sheet slide out; onDismiss finishes the host activity afterwards.
        tapBrandView.backButtonLinearLayout.setOnClickListener { dismissAllowingStateLoss() }
    }

    // region Scan status

    private fun applyStatusTheme() {
        statusCard.background.setTint(
            ThemeManager.getValue<String>("Nfc.topTextBackgroundColor").toColorIntOrDefault(DEFAULT_STATUS_BG)
        )
        statusTextColor =
            ThemeManager.getValue<String>("Nfc.topTextColor").toColorIntOrDefault(DEFAULT_STATUS_TEXT)
        statusText.setTextColor(statusTextColor)
        readProgress.progressTintList = ColorStateList.valueOf(statusTextColor)
        statusSpinner.indeterminateTintList = ColorStateList.valueOf(statusTextColor)
    }

    /** Card entered the field: tell the user to keep it there while we read. */
    fun showReading() {
        statusText.text = nfcString("cardDetected")
        statusSpinner.visibility = View.VISIBLE
        statusIcon.visibility = View.GONE
        readProgress.progress = INITIAL_PROGRESS
        readProgress.visibility = View.VISIBLE
        mShimmerViewContainer.pauseAnimation()
        mShimmerViewContainer.animate().alpha(DIMMED_ANIMATION_ALPHA).setDuration(150).start()
        reveal()
    }

    fun setReadProgress(commandsSent: Int, expectedCommands: Int) {
        val fraction = commandsSent.toFloat() / expectedCommands
        val target = (INITIAL_PROGRESS + fraction * (MAX_READ_PROGRESS - INITIAL_PROGRESS)).toInt()
        readProgress.progress = target.coerceIn(readProgress.progress, MAX_READ_PROGRESS)
    }

    fun showSuccess() {
        readProgress.progress = 100
        statusText.text = nfcString("scanSuccess")
        showIcon(R.drawable.ic_nfc_status_check, SUCCESS_COLOR)
        reveal()
    }

    /** @param messageKey NFC localization key: "cardMoved" for a lost link, "scanFailed" for a bad read. */
    fun showError(messageKey: String) {
        statusText.text = nfcString(messageKey)
        readProgress.visibility = View.GONE
        showIcon(R.drawable.ic_nfc_status_error, ERROR_COLOR)
        reveal()
    }

    /** Back to the "tap your card" state, e.g. after an error so the user can retry. */
    fun showIdle() {
        if (!::statusCard.isInitialized) return
        statusCard.animate().alpha(0f).setDuration(150)
            .withEndAction { statusCard.visibility = View.GONE }.start()
        mShimmerViewContainer.animate().alpha(1f).setDuration(150).start()
        mShimmerViewContainer.resumeAnimation()
    }

    private fun showIcon(drawableRes: Int, tint: Int) {
        statusSpinner.visibility = View.GONE
        statusIcon.setImageResource(drawableRes)
        statusIcon.imageTintList = ColorStateList.valueOf(tint)
        statusIcon.visibility = View.VISIBLE
    }

    private fun reveal() {
        if (statusCard.visibility == View.VISIBLE && statusCard.alpha == 1f) return
        statusCard.alpha = 0f
        statusCard.visibility = View.VISIBLE
        statusCard.animate().alpha(1f).setDuration(150).start()
    }

    private fun nfcString(key: String): String =
        LocalizationManager.getValue(key, "NFC") as? String ?: key

    // endregion

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        val host = activity ?: return
        // Give the window exit animation time to play before tearing the activity down.
        doAfterSpecificTime(time = 300) {
            if (!host.isFinishing && !host.isDestroyed) host.finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogFragment)
    }

    override fun getTheme(): Int = R.style.CustomBottomSheetDialogFragment

    companion object {
        const val TAG = "NfcBottomSheet"
        private const val INITIAL_PROGRESS = 10
        private const val MAX_READ_PROGRESS = 90
        private const val DIMMED_ANIMATION_ALPHA = 0.35f
        private const val DEFAULT_STATUS_BG = 0xFFE0DFE0.toInt()
        private const val DEFAULT_STATUS_TEXT = 0xFF4B4847.toInt()
        private const val SUCCESS_COLOR = 0xFF2E9E5B.toInt()
        private const val ERROR_COLOR = 0xFFD9534F.toInt()


        /** Lottie asset matching the current SDK language/theme, or null when neither is set yet. */
        fun animationUrl(): String? {
            val (language, theme) = TapCardKit.languageThemePair
            val lang = when (language) {
                TapLocal.en.name -> "en"
                TapLocal.ar.name -> "ar"
                else -> return null
            }
            val mode = when (theme) {
                TapTheme.light.name -> "light"
                TapTheme.dark.name -> "dark"
                else -> return null
            }
            return "https://tap-assets.b-cdn.net/card-sdk/nfc/nfc_${mode}_$lang.json"
        }

        /**
         * Warms Lottie's cache so the sheet opens with the animation already available
         * instead of showing an empty area while it downloads.
         */
        fun preloadAnimation(context: Context) {
            val url = animationUrl() ?: return
            LottieCompositionFactory.fromUrl(context.applicationContext, url)
        }
    }
}
