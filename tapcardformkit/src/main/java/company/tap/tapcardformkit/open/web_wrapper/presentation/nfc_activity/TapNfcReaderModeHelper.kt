package company.tap.tapcardformkit.open.web_wrapper.presentation.nfc_activity

import android.app.Activity
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.IsoDep
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import company.tap.nfcreader.internal.library.exception.CommunicationException
import company.tap.nfcreader.open.reader.TapEmvCard
import java.io.IOException
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Reads EMV card data over NFC using [NfcAdapter.enableReaderMode].
 *
 * Reader mode talks to the tag directly on a binder thread the moment it is discovered:
 * no system NFC sound, no PendingIntent round-trip and no activity relaunch via
 * onNewIntent, which makes the scan feel instant and reliable on a single tap.
 *
 * The parsing itself is delegated to [TapEmvParser]; we only supply the [IsoDep] transport.
 */
class TapNfcReaderModeHelper(
    private val activity: Activity,
    private val listener: Listener
) {

    interface Listener {
        /** A card entered the field and the read is starting. Always delivered on the main thread. */
        fun onCardDetected()

        /**
         * One more APDU exchanged with the card. A full EMV read is roughly
         * [EXPECTED_COMMANDS] commands, so callers can map this to a progress bar.
         * Always delivered on the main thread.
         */
        fun onReadProgress(commandsSent: Int, expectedCommands: Int)

        /** A card was parsed successfully. Always delivered on the main thread. */
        fun onCardRead(card: TapEmvCard)

        /**
         * The RF link dropped before the read completed. Reader mode stays armed and the card
         * will be re-discovered as soon as it settles, so treat this as "keep holding", not as
         * a failure. Always delivered on the main thread.
         */
        fun onCardLost()

        /** The card could not be parsed. Always delivered on the main thread. */
        fun onCardReadError(throwable: Throwable)
    }

    private val nfcAdapter: NfcAdapter? = NfcAdapter.getDefaultAdapter(activity)
    private val mainHandler = Handler(Looper.getMainLooper())
    private val reading = AtomicBoolean(false)
    private var enabled = false

    val isNfcAvailable: Boolean get() = nfcAdapter != null
    val isNfcEnabled: Boolean get() = nfcAdapter?.isEnabled == true

    private val readerCallback = NfcAdapter.ReaderCallback { tag -> onTagDiscovered(tag) }

    fun enable() {
        val adapter = nfcAdapter ?: return
        if (enabled || !adapter.isEnabled) return
        val extras = Bundle().apply {
            putInt(NfcAdapter.EXTRA_READER_PRESENCE_CHECK_DELAY, PRESENCE_CHECK_DELAY_MS)
        }
        adapter.enableReaderMode(activity, readerCallback, READER_FLAGS, extras)
        enabled = true
    }

    fun disable() {
        if (!enabled) return
        enabled = false
        runCatching { nfcAdapter?.disableReaderMode(activity) }
            .onFailure { Log.w(TAG, "disableReaderMode failed", it) }
    }

    private fun onTagDiscovered(tag: Tag) {
        // Ignore taps that arrive while a previous read is still in flight.
        if (!reading.compareAndSet(false, true)) {
            Log.d(TAG, "Tag discovered while a read is in flight, ignoring")
            return
        }
        Log.d(TAG, "Tag discovered: techs=${tag.techList.joinToString()}")
        val startedAt = SystemClock.elapsedRealtime()
        mainHandler.post { if (enabled) listener.onCardDetected() }

        val isoDep = IsoDep.get(tag)
        if (isoDep == null) {
            reading.set(false)
            deliverError(IOException("Tag does not support IsoDep"))
            return
        }

        val provider = IsoDepProvider(isoDep) { sent ->
            mainHandler.post { if (enabled) listener.onReadProgress(sent, EXPECTED_COMMANDS) }
        }
        try {
            isoDep.connect()
            isoDep.timeout = ISO_DEP_TIMEOUT_MS
            val card = TapEmvParser(provider).readEmvCard()
            // Never log card data, not even masked: this runs in a released SDK.
            Log.d(
                TAG,
                "Read finished in ${SystemClock.elapsedRealtime() - startedAt}ms " +
                    "after ${provider.responses.size} APDUs"
            )
            if (card?.cardNumber.isNullOrBlank()) {
                deliverError(IOException("Could not read card data"))
            } else {
                deliverSuccess(card!!)
            }
        } catch (e: IOException) {
            // The RF link dropped mid-read (card still moving, edge of the antenna). Do NOT try
            // to reconnect here: IsoDep.connect() on a lost tag blocks ~1s per call and, while
            // this callback is running, the NFC stack cannot poll. Releasing the tag right away
            // lets the stack re-discover the card in ~100-300ms and call us again.
            Log.d(TAG, "Link lost after ${SystemClock.elapsedRealtime() - startedAt}ms: ${e.message}")
            deliverLost()
        } catch (t: Throwable) {
            Log.w(TAG, "Read failed after ${SystemClock.elapsedRealtime() - startedAt}ms", t)
            deliverError(t)
        } finally {
            runCatching { isoDep.close() }
            reading.set(false)
        }
    }

    private fun deliverLost() {
        mainHandler.post { if (enabled) listener.onCardLost() }
    }

    private fun deliverSuccess(card: TapEmvCard) {
        mainHandler.post { if (enabled) listener.onCardRead(card) }
    }

    private fun deliverError(throwable: Throwable) {
        mainHandler.post { if (enabled) listener.onCardReadError(throwable) }
    }

    /**
     * Bridges [IsoDep] to the parser so it can drive the APDU exchange,
     * reporting each successful command so the UI can show read progress.
     */
    private class IsoDepProvider(
        private val isoDep: IsoDep,
        private val onCommandSent: (Int) -> Unit
    ) : TapEmvParser.RecordingProvider {
        override val responses = mutableListOf<ByteArray>()

        @Throws(CommunicationException::class)
        override fun transceive(command: ByteArray): ByteArray {
            return try {
                isoDep.transceive(command).also {
                    responses += it
                    onCommandSent(responses.size)
                }
            } catch (e: IOException) {
                throw CommunicationException(e.message)
            }
        }
    }

    companion object {
        /** Typical APDU count for a trimmed contactless EMV read: PPSE, SELECT AID, GPO, 2-4 READ RECORDs. */
        const val EXPECTED_COMMANDS = 6

        private const val TAG = "TapNfcReaderMode"
        /** Short presence check so polling restarts quickly after a card is lost. */
        private const val PRESENCE_CHECK_DELAY_MS = 100
        private const val ISO_DEP_TIMEOUT_MS = 5_000
        private const val READER_FLAGS =
            NfcAdapter.FLAG_READER_NFC_A or
                NfcAdapter.FLAG_READER_NFC_B or
                NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK or
                NfcAdapter.FLAG_READER_NO_PLATFORM_SOUNDS
    }
}
