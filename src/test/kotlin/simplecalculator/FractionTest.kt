package simplecalculator

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import simplecalculator.util.Fraction

class FractionTest {
    @BeforeEach
    fun init() {
        intOnly = false
        mixedFraction = false
    }

    @Test
    @DisplayName("Create: / by zero")
    fun testCreateFraction0() {
        assertThrows<ArithmeticException> { Fraction(1, 0) }
    }

    @Test
    @DisplayName("Create: reduction")
    fun testCreateFraction1() {
        assertEquals("1 / 2", Fraction(2, 4).toString())
    }

    @Test
    @DisplayName("Create: positive integer")
    fun testCreateFraction2() {
        assertEquals("2", Fraction(2, 1).toString())
    }

    @Test
    @DisplayName("Create: format negative")
    fun testCreateFraction3() {
        assertEquals("-1 / 2", Fraction(1, -2).toString())
    }

    @Test
    @DisplayName("Create: format negative integer")
    fun testCreateFraction4() {
        assertEquals("-2", Fraction(2, -1).toString())
    }

    @Test
    @DisplayName("Create: / to integer")
    fun testCreateFraction5() {
        intOnly = true
        assertEquals("0", Fraction(1, 2).toString())
    }

    @Test
    @DisplayName("Calculate: add")
    fun testCalculateFraction0() {
        assertEquals("1",
                Fraction(-1, 2).add(Fraction(3, 2)).toString())
    }

    @Test
    @DisplayName("Calculate: decline")
    fun testCalculateFraction1() {
        assertEquals("-2",
                Fraction(-1, 2).decline(Fraction(3, 2)).toString())
    }

    @Test
    @DisplayName("Calculate: multiply")
    fun testCalculateFraction2() {
        assertEquals("-3 / 4",
                Fraction(-1, 2).multiply(Fraction(3, 2)).toString())
    }

    @Test
    @DisplayName("Calculate: divide")
    fun testCalculateFraction3() {
        assertEquals("-1 / 3",
                Fraction(-1, 2).divide(Fraction(3, 2)).toString())
    }

    @Test
    @DisplayName("Format: mixed fraction")
    fun testPrintFraction0() {
        mixedFraction = true
        /*
            1
        -1 ---
            2
         */
        assertEquals("    1\r\n-1 ---\r\n    2",
                Fraction(-3, 2).format())
    }

    @Test
    @DisplayName("Format: improper fraction")
    fun testPrintFraction1() {
        /*
           333
        - -----
            2
         */
        assertEquals("   333\r\n- -----\r\n    2",
                Fraction(-333, 2).format())
    }

    @Test
    @DisplayName("Format: negative integer")
    fun testPrintFraction2() {
        /*
        -233
         */
        assertEquals("-233 ",
                Fraction(233, -1).format())
    }
}
