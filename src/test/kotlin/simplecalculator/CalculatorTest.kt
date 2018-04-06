package simplecalculator

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.text.ParseException

class CalculatorTest {
    @BeforeEach
    fun init() {
        fixRight = false
    }

    @Test
    @DisplayName("Parse: no right value")
    fun testCalculatorParse0() {
        try {
            Calculator("1+")
            assert(false)
        } catch (e: ParseException) {
            assertEquals(1, e.errorOffset)
        }
    }

    @Test
    @DisplayName("Parse: no left value")
    fun testCalculatorParse1() {
        try {
            Calculator("*1")
            assert(false)
        } catch (e: ParseException) {
            assertEquals(0, e.errorOffset)
        }
    }

    @Test
    @DisplayName("Parse: another no left value")
    fun testCalculatorParse2() {
        try {
            Calculator("1**1")
            assert(false)
        } catch (e: ParseException) {
            assertEquals(2, e.errorOffset)
        }
    }

    @Test
    @DisplayName("Parse: no left bracket")
    fun testCalculatorParse3() {
        try {
            Calculator("+1)")
            assert(false)
        } catch (e: ParseException) {
            assertEquals(2, e.errorOffset)
        }
    }

    @Test
    @DisplayName("Parse: no right bracket")
    fun testCalculatorParse4() {
        try {
            Calculator("1+(2+1")
            assert(false)
        } catch (e: ParseException) {
            assertEquals(2, e.errorOffset)
        }
    }

    @Test
    @DisplayName("Calculate: example1")
    fun testCalculator0() {
        assertEquals("9", Calculator("(1+9)*2/2-1").getResult().toString())
    }

    //12+68-49*(-85)*(-84)*(-94)+96-58-45+87*(-72)
    @Test
    @DisplayName("Calculate: example2")
    fun testCalculator2() {
        assertEquals("32880649", Calculator("12+68-49*(-85)*(-84)*(-94)+96-58-45+87*(-72)").getResult().toString())
    }


    @Test
    @DisplayName("Calculate: fix right bracket")
    fun testCalculator3() {
        fixRight = true
        assertEquals("5", Calculator("1+(2*2").getResult().toString())
    }
}