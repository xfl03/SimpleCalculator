package simplecalculator.util

import simplecalculator.intOnly
import simplecalculator.mixedFraction
import simplecalculator.util.FractionUtil.fill
import simplecalculator.util.FractionUtil.gcd

object FractionUtil {
    fun gcd(a: Long, b: Long): Long = if (b == 0L) a else gcd(b, a % b)
    fun fill(sb: StringBuilder, num: Int, ch: Char = ' ') {
        (0 until num).forEach { sb.append(ch) }
    }
}

/**
 * Fraction Class
 *
 *   numerator
 * -------------
 *  denominator
 */
data class Fraction(private var numerator: Long, private var denominator: Long = 1) {
    init {
        if (denominator == 0L)
            throw ArithmeticException("/ by zero")

        if (intOnly) {
            numerator /= denominator
            denominator = 1
        } else {
            //reduction
            val gcd = gcd(numerator, denominator)
            numerator /= gcd
            denominator /= gcd

            if (denominator < 0) {
                numerator = -numerator
                denominator = -denominator
            }
        }
    }

    /**
     * this + f
     */
    fun add(f: Fraction) = Fraction(
            numerator * f.denominator + f.numerator * denominator,
            f.denominator * denominator
    )

    /**
     * this - f
     */
    fun decline(f: Fraction) = Fraction(
            numerator * f.denominator - f.numerator * denominator,
            f.denominator * denominator
    )

    /**
     * this * f
     */
    fun multiply(f: Fraction) = Fraction(
            f.numerator * numerator,
            f.denominator * denominator
    )

    /**
     * this / f
     */
    fun divide(f: Fraction) = Fraction(
            f.denominator * numerator,
            f.numerator * denominator
    )

    override fun toString() = "$numerator" + (if (denominator == 1L) "" else " / $denominator")

    /**
     * Format fraction
     *
     * if mixedFraction:
     *   b
     * a -
     *   c
     * else:
     * b
     * -
     * c
     */
    fun format(): String {
        val line0 = StringBuilder()
        val line1 = StringBuilder()
        val line2 = StringBuilder()

        formatInteger(line0, line1, line2)
        formatFraction(line0, line1, line2)

        val ret = StringBuilder()
        if (line0.isNotBlank())
            ret.append(line0).append("\r\n")
        ret.append(line1).append("\r\n")
        if (line2.isNotBlank())
            ret.append(line2).append("\r\n")
        return ret.substring(0, ret.length - 2)
    }

    private fun formatInteger(line0: StringBuilder, line1: StringBuilder, line2: StringBuilder) {
        val a = if (mixedFraction || denominator == 1L) numerator / denominator else 0

        var aStr = (if (numerator < 0) "-" else "") + if (a != 0L) Math.abs(a) else ""
        if (aStr.isNotEmpty()) {
            aStr += " "
        }

        fill(line0, aStr.length)
        line1.append(aStr)
        fill(line2, aStr.length)
    }

    private fun formatFraction(line0: StringBuilder, line1: StringBuilder, line2: StringBuilder) {
        val bStr = Math.abs(if (mixedFraction || denominator == 1L) numerator % denominator else numerator).toString()
        if (bStr == "0")//No need to print fraction
            return
        val cStr = denominator.toString()

        val d = (Math.abs(bStr.length - cStr.length) + 1) / 2
        fill(if (bStr.length < cStr.length) line0 else line2, d)

        line0.append(' ').append(bStr)
        fill(line1, Math.max(bStr.length, cStr.length) + 2, '-')
        line2.append(' ').append(cStr)
    }

    /**
     * Beauty print for fraction
     */
    fun print() {
        print(format())
    }
}