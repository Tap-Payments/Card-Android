package company.tap.tapcardformkit.open.web_wrapper.presentation.nfc_activity

import company.tap.nfcreader.internal.library.exception.CommunicationException
import company.tap.nfcreader.internal.library.iso7816emv.EmvTags
import company.tap.nfcreader.internal.library.iso7816emv.EmvTerminal
import company.tap.nfcreader.internal.library.iso7816emv.TagAndLength
import company.tap.nfcreader.internal.library.model.EmvTransactionRecord
import company.tap.nfcreader.internal.library.parser.EmvParser
import company.tap.nfcreader.internal.library.parser.IProvider
import company.tap.nfcreader.internal.library.utils.TlvUtil
import company.tap.nfcreader.open.reader.TapEmvCard
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Currency
import java.util.Date
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
 *
 * Two more changes target cards that only read some of the time:
 *
 * - **Terminal data.** The stock terminal describes itself as an offline-only reader in France
 *   making a zero-value EUR purchase. Issuers - Visa-style cards in particular - may refuse that
 *   profile or take the long offline path, which needs many more READ RECORDs and so more time
 *   for the card to slip away. [getGetProcessingOptions] presents an ordinary online-capable
 *   POS instead, in the merchant's own country and currency, for a small non-zero amount.
 *
 * - **Application priority.** On cards with more than one payment application (e.g. a local
 *   scheme co-badged with Visa or Mastercard) the stock parser tries them in directory order.
 *   [getAids] orders them by the issuer's Application Priority Indicator (tag 87) instead. The
 *   stock loop already moves on to the next application when one refuses GPO, so the
 *   preferred application is tried first and the others remain as fallbacks.
 */
class TapEmvParser(
    private val provider: RecordingProvider,
    private val terminal: TerminalProfile = TerminalProfile.DEFAULT
) : EmvParser(provider, true) {

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

    /**
     * Same AIDs the stock parser found, reordered by tag 87 (lower value = higher priority,
     * 0 or absent = no preference, kept after the ranked ones in directory order). Nothing is
     * added or dropped, so a card without priorities behaves exactly as before.
     */
    override fun getAids(data: ByteArray?): List<ByteArray> =
        orderByPriority(super.getAids(data).orEmpty(), data)

    /**
     * GET PROCESSING OPTIONS with our terminal profile. Tags the profile does not cover fall
     * back to the stock terminal values, so the card still gets an answer for every PDOL entry.
     * A null PDOL (the stock parser's retry) sends an empty command template, as before.
     */
    @Throws(CommunicationException::class)
    override fun getGetProcessingOptions(pdol: ByteArray?, provider: IProvider): ByteArray =
        provider.transceive(gpoCommand(pdol, terminal))

    /** Tag 57 was absent: look for 5A / 5F24 in every response the card gave us. */
    private fun applyPanAndExpiryFallback(card: TapEmvCard) {
        for (response in this.provider.responses) {
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

    /**
     * What this phone tells the card about the "terminal" it is talking to. Defaults to the
     * merchant's configured currency; the country follows that currency.
     */
    class TerminalProfile(private val countryCode: Int, private val currencyCode: Int) {

        fun valueFor(tl: TagAndLength): ByteArray {
            val tag = toHex(tl.tag.tagBytes)
            val len = tl.length
            return when (tag) {
                // Online-capable qVSDC reader: signature + online PIN, online cryptogram
                // required, consumer-device CVM supported. Cards answer GPO with Track 2
                // directly on this path instead of needing a long offline record read.
                "9F66" -> fitBinary(TTQ, len)
                "9F02" -> fitNumeric(AMOUNT, len)
                "9F03" -> fitNumeric(ByteArray(6), len)
                "9F1A" -> fitNumeric(bcd(countryCode, 2), len)
                "5F2A" -> fitNumeric(bcd(currencyCode, 2), len)
                "9A" -> fitNumeric(fromHex(DATE.format(Date())), len)
                "9F21" -> fitNumeric(fromHex(TIME.format(Date())), len)
                "9C" -> fitNumeric(byteArrayOf(0x00), len) // purchase
                else -> EmvTerminal.constructValue(tl)
            }
        }

        companion object {
            private val TTQ = byteArrayOf(0x26, 0x80.toByte(), 0x40, 0x00)
            /** 100 minor units: non-zero, far below any contactless limit. */
            private val AMOUNT = byteArrayOf(0x00, 0x00, 0x00, 0x00, 0x01, 0x00)
            private const val KUWAIT = 414
            private const val KWD = 414

            /** ISO 3166 numeric country for the currencies Tap merchants settle in. */
            private val COUNTRY_FOR_CURRENCY = mapOf(
                "KWD" to 414, "SAR" to 682, "AED" to 784, "BHD" to 48, "QAR" to 634,
                "OMR" to 512, "EGP" to 818, "JOD" to 400, "USD" to 840, "GBP" to 826
            )

            val DEFAULT = TerminalProfile(KUWAIT, KWD)

            fun forCurrency(alphaCode: String?): TerminalProfile {
                val code = alphaCode?.trim()?.uppercase(Locale.US).orEmpty()
                val currency = runCatching { Currency.getInstance(code).numericCode }.getOrNull()
                    ?.takeIf { it > 0 } ?: return DEFAULT
                return TerminalProfile(COUNTRY_FOR_CURRENCY[code] ?: KUWAIT, currency)
            }
        }
    }

    private class DirectoryEntry(val aid: ByteArray?, val priority: Int)

    companion object {
        /** GET PROCESSING OPTIONS APDU for [pdol] (null = card sent none) using [terminal]. */
        internal fun gpoCommand(pdol: ByteArray?, terminal: TerminalProfile): ByteArray {
            val requested: List<TagAndLength> = pdol?.let { TlvUtil.parseTagAndLength(it) }.orEmpty()
            val values = ByteArrayOutputStream()
            for (tl in requested) values.write(terminal.valueFor(tl))
            val data = values.toByteArray()
            val template = ByteArrayOutputStream().apply {
                write(0x83)
                write(berLength(data.size))
                write(data)
            }.toByteArray()
            return ByteArrayOutputStream().apply {
                write(byteArrayOf(0x80.toByte(), 0xA8.toByte(), 0x00, 0x00))
                write(template.size)
                write(template)
                write(0x00) // Le
            }.toByteArray()
        }

        /** [aids] reordered by the tag 87 priorities found in the PPSE response [data]. */
        internal fun orderByPriority(aids: List<ByteArray>, data: ByteArray?): List<ByteArray> {
            if (aids.size < 2 || data == null) return aids
            val priorities = HashMap<String, Int>()
            for (entry in directoryEntries(data)) {
                val aid = entry.aid ?: continue
                priorities[toHex(aid)] = entry.priority
            }
            if (priorities.values.none { it > 0 }) return aids
            return aids.sortedBy { aid -> priorities[toHex(aid)].takeIf { it != null && it > 0 } ?: NO_PRIORITY }
        }

        private const val NO_PRIORITY = Int.MAX_VALUE

        /** 5F24 is "n 6" YYMMDD, BCD encoded. */
        private val EXPIRY_FORMAT = SimpleDateFormat("yyMMdd", Locale.US)
        private val DATE = SimpleDateFormat("yyMMdd", Locale.US)
        private val TIME = SimpleDateFormat("HHmmss", Locale.US)

        /** "cn" data is left-aligned BCD padded with F nibbles. */
        private fun decodeCompressedNumeric(bytes: ByteArray): String = toHex(bytes).trimEnd('F', 'f')

        private fun toHex(bytes: ByteArray): String = bytes.joinToString("") { "%02X".format(it) }

        private fun fromHex(hex: String): ByteArray =
            ByteArray(hex.length / 2) { hex.substring(it * 2, it * 2 + 2).toInt(16).toByte() }

        /** Right-aligned BCD of [value] in [bytes] bytes, e.g. 414 -> 04 14. */
        private fun bcd(value: Int, bytes: Int): ByteArray = fromHex(value.toString().padStart(bytes * 2, '0'))

        /** Numeric fields are right-aligned: pad or drop on the left. */
        private fun fitNumeric(value: ByteArray, len: Int): ByteArray = when {
            value.size == len -> value
            value.size > len -> value.copyOfRange(value.size - len, value.size)
            else -> ByteArray(len - value.size) + value
        }

        /** Binary fields are left-aligned: pad or drop on the right. */
        private fun fitBinary(value: ByteArray, len: Int): ByteArray = value.copyOf(len)

        private fun berLength(length: Int): ByteArray = when {
            length < 0x80 -> byteArrayOf(length.toByte())
            length <= 0xFF -> byteArrayOf(0x81.toByte(), length.toByte())
            else -> byteArrayOf(0x82.toByte(), (length shr 8).toByte(), length.toByte())
        }

        /** Every Directory Entry (tag 61) in a PPSE response, with its AID (4F) and priority (87). */
        private fun directoryEntries(data: ByteArray): List<DirectoryEntry> {
            val out = mutableListOf<DirectoryEntry>()
            walk(data, 0, data.size) { tag, value ->
                if (tag == "61") {
                    var aid: ByteArray? = null
                    var priority = 0
                    walk(value, 0, value.size) { t, v ->
                        when (t) {
                            "4F" -> aid = v
                            "87" -> if (v.isNotEmpty()) priority = v[0].toInt() and 0x0F
                        }
                    }
                    out += DirectoryEntry(aid, priority)
                }
            }
            return out
        }

        /** Minimal BER-TLV walk; descends into constructed tags and stops at anything malformed. */
        private fun walk(data: ByteArray, from: Int, to: Int, visit: (String, ByteArray) -> Unit) {
            var i = from
            while (i < to) {
                val first = data[i].toInt() and 0xFF
                if (first == 0x00 || first == 0xFF) { i++; continue } // padding
                val tagStart = i++
                if (first and 0x1F == 0x1F) {
                    while (i < to && data[i].toInt() and 0x80 != 0) i++
                    i++
                }
                if (i >= to) return
                val tag = toHex(data.copyOfRange(tagStart, i))
                var len = data[i++].toInt() and 0xFF
                if (len and 0x80 != 0) {
                    val n = len and 0x7F
                    if (n == 0 || n > 2 || i + n > to) return
                    len = 0
                    repeat(n) { len = (len shl 8) or (data[i++].toInt() and 0xFF) }
                }
                if (i + len > to) return
                val value = data.copyOfRange(i, i + len)
                visit(tag, value)
                if (first and 0x20 != 0) walk(value, 0, value.size, visit)
                i += len
            }
        }
    }
}
