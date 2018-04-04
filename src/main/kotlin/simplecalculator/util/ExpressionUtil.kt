package simplecalculator.util

import simplecalculator.debugMode
import simplecalculator.intOnly
import java.util.*

/**
 * Tool Class for Expression ( Number & Operator )
 */
class ExpressionUtil {
    private val operatorMap = HashMap<Char, Operator>()
    private val computeAlgorithms = HashMap<Operator, (Fraction, Fraction) -> Fraction>()

    init {
        Operator.values().forEach { operatorMap[it.ch] = it }

        computeAlgorithms[Operator.PLUS] = { a, b -> a.add(b) }
        computeAlgorithms[Operator.MINUS] = { a, b -> a.decline(b) }
        computeAlgorithms[Operator.TIMES] = { a, b -> a.multiply(b) }
        computeAlgorithms[Operator.DIVIDE] = { a, b -> a.divide(b) }
        //computeAlgorithms[Operator.POW] = { a, b -> pow(a, b) }
    }

    /**
     * Check is the Char legal
     */
    fun isLegal(ch: Int) = isNum(ch) || getOperator(ch) != null || (ch.toChar() == '.' && !intOnly)

    /**
     * is the Char in '0'-'9'
     */
    fun isNum(ch: Int) = ch.toChar() in '0'..'9'

    /**
     * Convert number Char to Int
     */
    fun toNum(ch: Int) = ch - '0'.toInt()

    /**
     * Convert operator Char to Operator enum
     */
    fun getOperator(op: Int) = operatorMap[op.toChar()]

    /**
     * Check if expression can begin with the operator
     * Allowed: + - ( [ {
     */
    fun canBeginWith(operator: Operator) =
            operator == Operator.PLUS || operator == Operator.MINUS || isLeftBracket(operator)

    /**
     * Check if expression can end with the operator
     * Allowed: ) ] }
     */
    fun canEndWith(operator: Operator) =
            isRightBracket(operator)

    /**
     * is the operator a left bracket
     * @return
     * ( [ { - true
     * other - false
     */
    fun isLeftBracket(operator: Operator) =
            operator == Operator.LEFT_S_BRACKET || operator == Operator.LEFT_M_BRACKET || operator == Operator.LEFT_B_BRACKET

    /**
     * is the operator a right bracket
     * @return
     * ) ] } - true
     * other - false
     */
    fun isRightBracket(operator: Operator) =
            operator == Operator.RIGHT_S_BRACKET || operator == Operator.RIGHT_M_BRACKET || operator == Operator.RIGHT_B_BRACKET

    /**
     * compute "left operator right"
     * Example: 1+2
     */
    fun computeOperator(left: Fraction, operator: Operator, right: Fraction): Fraction {
        val t = computeAlgorithms[operator]!!.invoke(left, right)
        if (debugMode)
            println("[Compute] $left ${operator.ch} $right = $t")
        return t
    }


    fun pow(a: Int, b: Int): Long {
        var ret = 1L
        (1..b).forEach {
            ret *= a
        }
        return ret
    }


    companion object {
        /**
         * Singleton for ExpressUtil
         */
        var instance: ExpressionUtil? = null
            get() {
                if (field == null) {
                    synchronized(this) {
                        if (field == null)
                            field = ExpressionUtil()
                    }
                }
                return field
            }
            private set
    }
}

/**
 * enum class for Operator
 * + - * / ^ ( ) [ ] { }
 */
enum class Operator(val ch: Char, val priority: Short) {
    PLUS('+', 0), MINUS('-', 1),
    TIMES('*', 2), DIVIDE('/', 2),
    //POW('^', 3),
    LEFT_S_BRACKET('(', 10),
    RIGHT_S_BRACKET(')', 10),
    LEFT_M_BRACKET('[', 11), RIGHT_M_BRACKET(']', 11),
    LEFT_B_BRACKET('{', 12), RIGHT_B_BRACKET('}', 12)
}