package company.tap.tapcardformkit.open.web_wrapper.presentation.nfc_activity

import org.junit.Assert.assertEquals
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TapEmvParserTest {

    private fun hex(s: String) = s.replace(" ", "").chunked(2).map { it.toInt(16).toByte() }.toByteArray()
    private fun hex(b: ByteArray) = b.joinToString("") { "%02X".format(it) }

    @Test
    fun `GPO carries an online-capable profile in the merchant's currency`() {
        // A typical Visa PDOL: TTQ, amount, other amount, country, TVR, currency, date, type, UN.
        val pdol = hex("9F66 04 9F02 06 9F03 06 9F1A 02 95 05 5F2A 02 9A 03 9C 01 9F37 04")
        val apdu = TapEmvParser.gpoCommand(pdol, TapEmvParser.TerminalProfile.forCurrency("SAR"))
        val data = apdu.copyOfRange(7, apdu.size - 1) // past 80 A8 00 00 Lc 83 L, before Le

        assertEquals("80A80000", hex(apdu.copyOfRange(0, 4)))
        assertEquals(33, data.size)                       // 4+6+6+2+5+2+3+1+4
        assertEquals(apdu[4].toInt(), data.size + 2)      // Lc covers 83 L + data
        assertEquals(0x83, apdu[5].toInt() and 0xFF)
        assertEquals(data.size, apdu[6].toInt())
        assertEquals(0x00, apdu.last().toInt())           // Le

        assertEquals("26804000", hex(data.copyOfRange(0, 4)))       // TTQ
        assertEquals("000000000100", hex(data.copyOfRange(4, 10)))  // amount 1.00
        assertEquals("000000000000", hex(data.copyOfRange(10, 16))) // other amount
        assertEquals("0682", hex(data.copyOfRange(16, 18)))         // Saudi Arabia
        assertEquals("0682", hex(data.copyOfRange(23, 25)))         // SAR
        assertEquals(SimpleDateFormat("yyMMdd", Locale.US).format(Date()), hex(data.copyOfRange(25, 28)))
        assertEquals("00", hex(data.copyOfRange(28, 29)))           // purchase
    }

    @Test
    fun `GPO without a PDOL sends an empty command template`() {
        assertEquals("80A8000002830000", hex(TapEmvParser.gpoCommand(null, TapEmvParser.TerminalProfile.DEFAULT)))
    }

    @Test
    fun `fields are fitted to the length the card asks for`() {
        // Card asks for a 3-byte amount and a 2-byte TTQ.
        val data = TapEmvParser.gpoCommand(hex("9F02 03 9F66 02"), TapEmvParser.TerminalProfile.DEFAULT)
            .let { it.copyOfRange(7, it.size - 1) }
        assertEquals("0001002680", hex(data)) // amount keeps its rightmost digits, TTQ its leftmost bytes
    }

    @Test
    fun `unknown currency falls back to Kuwait and KWD`() {
        val apdu = TapEmvParser.gpoCommand(hex("9F1A 02 5F2A 02"), TapEmvParser.TerminalProfile.forCurrency("XYZ"))
        assertEquals("04140414", hex(apdu.copyOfRange(7, apdu.size - 1)))
    }

    @Test
    fun `co-badged card is tried in the issuer's priority order`() {
        // PPSE: first entry is a local scheme with priority 2, second is Visa with priority 1.
        val local = "A0000002281010"
        val visa = "A0000000031010"
        val ppse = hex(
            "6F 31 84 0E 325041592E5359532E4444463031 A5 1F BF0C 1C" +
                " 61 0C 4F 07 $local 87 01 02" +
                " 61 0C 4F 07 $visa 87 01 01" +
                " 90 00"
        )
        val order = TapEmvParser.orderByPriority(listOf(hex(local), hex(visa)), ppse).map { hex(it) }
        assertEquals(listOf(visa, local), order)
    }

    @Test
    fun `card without priorities keeps directory order`() {
        val a = "A0000002281010"
        val b = "A0000000031010"
        val ppse = hex("6F 2B 84 0E 325041592E5359532E4444463031 A5 19 BF0C 16 61 09 4F 07 $a 61 09 4F 07 $b 90 00")
        assertEquals(listOf(a, b), TapEmvParser.orderByPriority(listOf(hex(a), hex(b)), ppse).map { hex(it) })
    }
}
