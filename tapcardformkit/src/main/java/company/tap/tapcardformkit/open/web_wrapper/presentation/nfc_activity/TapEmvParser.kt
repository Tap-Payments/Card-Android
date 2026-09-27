package company.tap.tapcardformkit.open.web_wrapper.presentation.nfc_activity

import company.tap.nfcreader.internal.library.exception.CommunicationException
import company.tap.nfcreader.internal.library.iso7816emv.EmvTags
import company.tap.nfcreader.internal.library.model.EmvTransactionRecord
import company.tap.nfcreader.internal.library.parser.EmvParser
import company.tap.nfcreader.internal.library.parser.IProvider
import company.tap.nfcreader.internal.library.utils.TlvUtil
import company.tap.nfcreader.open.reader.TapEmvCard
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * [EmvParser] trimmed down to what a card form needs: PAN, expiry and cardholder name.
 *
 * The stock parser keeps talking to the card after it already has the PAN — it fetches the
 * transaction log (GET DATA log format + one READ RECORD per entry, often ten) and the PIN
 * try counter (another GET DATA). That is a dozen extra APDUs during which the user may lift
 * the card, so the read fails even though the number was already in hand. Both are skipped here.
 *
 * It also adds the fallback from EMV Book 3 that the stock parser lacks: when a record carries
 * no Track 2 Equivalent Data (tag 57) the PAN and expiry are taken from tags 5A and 5F24.
 */
class TapEmvParser(private val provider: RecordingProvider) : EmvParser(provider, true) {

    /** No transaction history: it is not shown anywhere and costs the most APDUs. */
    @Throws(CommunicationException::class)
    override fun extractLogEntry(logEntry: ByteArray?): List<EmvTransactionRecord> = emptyList()

    /** No PIN try counter either; one less round-trip. */
    @Throws(CommunicationException::class)
    override fun getLeftPinTry(): Int = UNKNOW

    @Throws(CommunicationException::class)
    override fun readEmvCard(): TapEmvCard? {
        val card = super.readEmvCard() ?: return null
        if (card.cardNumber.isNullOrBlank()) applyPanAndExpiryFallback(card)
        return card
    }

    /** Tag 57 was absent: look for 5A / 5F24 in every response the card gave us. */
    private fun applyPanAndExpiryFallback(card: TapEmvCard) {
        for (response in provider.responses) {
            val pan = TlvUtil.getValue(response, EmvTags.PAN) ?: continue
            card.cardNumber = decodeCompressedNumeric(pan)
            TlvUtil.getValue(response, EmvTags.APP_EXPIRATION_DATE)?.let { raw ->
                runCatching { card.expireDate = EXPIRY_FORMAT.parse(toHex(raw)) }
            }
            if (!card.cardNumber.isNullOrBlank()) return
        }
    }

    /** IProvider that also keeps every response so tags can be re-examined after the read. */
    interface RecordingProvider : IProvider {
        val responses: List<ByteArray>
    }

    private companion object {
        /** 5F24 is "n 6" YYMMDD, BCD encoded. */
        val EXPIRY_FORMAT = SimpleDateFormat("yyMMdd", Locale.US)

        /** "cn" data is left-aligned BCD padded with F nibbles. */
        fun decodeCompressedNumeric(bytes: ByteArray): String = toHex(bytes).trimEnd('F', 'f')

        fun toHex(bytes: ByteArray): String = bytes.joinToString("") { "%02X".format(it) }
    }
}
