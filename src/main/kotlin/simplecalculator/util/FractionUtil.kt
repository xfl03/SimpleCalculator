package simplecalculator.util

import simplecalculator.intOnly
import simplecalculator.mixedFraction

object FractionUtil {
    fun gcd(a: Long, b: Long): Long = if (b == 0L) a else gcd(b, a % b)

}

/**
 * Fraction Class
 *
 *  numerator
 * -----------
 * denominator
 */
data class Fraction(private var numerator: Long, private var denominator: Long = 1) {
    init {
        if (denominator == 0L)
            throw ArithmeticException("/ by zero")
        if (intOnly) {
            numerator /= denominator
            denominator = 1
        } else {
            val gcd = FractionUtil.gcd(numerator, denominator)
            numerator /= gcd
            denominator /= gcd
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

    override fun toString() = "$numerator" + if (denominator == 1L) "" else " / $denominator"

    /**
     * Format and print fraction
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
    fun print() {
        var a = if (mixedFraction) numerator / denominator else 0L
        var b = numerator - a * denominator
        var c = denominator
        val neg = b * c < 0
        if (neg) {
            a = -a
            b = Math.abs(b)
            c = Math.abs(c)
        }
        val astr = if (a == 0L) if (neg) "-" else "" else a.toString()
        val bstr = if (b == 0L) "" else b.toString()
        val cstr = c.toString()

        val sb0 = StringBuilder()
        val sb1 = StringBuilder(astr)
        val sb2 = StringBuilder()
        if (astr.isNotEmpty()) {
            sb1.append(' ')
            (0..astr.length).forEach {
                sb0.append(' ')
                sb2.append(' ')
            }
        }
        if (bstr.isNotEmpty()) {
            val d = cstr.length - bstr.length
            if (d >= 0) {
                (1..d / 2).forEach { sb0.append(' ') }
                sb0.append(bstr)
                (1..cstr.length).forEach { sb1.append('-') }
                sb2.append(cstr)
            } else {
                sb0.append(bstr)
                (1..bstr.length).forEach { sb1.append('-') }
                (d / 2..-1).forEach { sb2.append(' ') }
                sb2.append(cstr)
            }
        }

        if (b != 0L)
            println(sb0)
        if (a != 0L || c != 1L)
            println(sb1)
        if (c != 1L && b != 0L)
            println(sb2)
        if (a == 0L && b == 0L)
            println(0)
    }
}