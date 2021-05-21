import dev.kord.common.DiscordBitSet
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class BitSetTests {
    @Test
    @JsName("bContainsA")
    fun `b contains a`() {
        val a = DiscordBitSet(0b101)
        val b = DiscordBitSet(0b111)
        assertTrue(a in b)
    }

    @Test
    @JsName("eEqualsB")
    fun `a equals b`() {
        val a = DiscordBitSet(0b111, 0)
        val b = DiscordBitSet(0b111)
        assertEquals(a, b)
    }


    @Test
    @JsName("getABit")
    fun `get a bit`() {
        val a = DiscordBitSet(0b101, 0)
        assertFalse(a[1])
    }

    @JsName("getABitOutOfRange")
    @Test
    fun `get a bit out of range`() {
        val a = DiscordBitSet(0b101, 0)
        assertFalse(a[10000])
    }

    @JsName("addAndRemoveABit")
    @Test
    fun `add and remove a  bit`() {
        val a = DiscordBitSet(0b101, 0)
        a.add(DiscordBitSet(0b111))
        assertEquals(a.value, 0b111.toString())
        a.remove(DiscordBitSet(0b001))
        assertEquals(a.value, 0b110.toString())

    }

    @JsName("RemoveABit")
    @Test
    fun `remove a bit`() {
        val a = DiscordBitSet(0b101, 0)
        a.remove(DiscordBitSet(0b111))
        assertEquals(a.value, "0")

    }

}