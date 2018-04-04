package simplecalculator

import simplecalculator.util.Operator
import simplecalculator.util.ExpressionUtil
import java.text.ParseException
import java.util.*

/**
 * Calculator Class
 * @param expression
 */
class Calculator(private val expression: String) {
    private var result = 0

    private val nums = Stack<Int>()
    private val ops = Stack<Operator>()
    private val util = ExpressionUtil.instance!!

    init {
        calculate()
    }

    @Throws(ParseException::class, ArithmeticException::class)
    private fun calculate() {
        var firstFlag = true
        var numFlag = false//Is previous char a number?
        var negativeFlag = false//Is next number negative?
        var lastOp = Operator.PLUS// last Useful Operator
        var index = -1

        nums.push(0)
        ops.push(Operator.PLUS)

        expression.chars().forEach {
            index++
            if (debugMode)
                println("[Read In] ${it.toChar()}")

            if (it.toChar() == ' ')//Ignore space
                return@forEach
            if (!util.isLegal(it))
                throw ParseException("illegal char '${it.toChar()}'", index)

            if (util.isNum(it)) {
                var num = util.toNum(it)
                if (negativeFlag) {
                    negativeFlag = false
                    num = -num
                }
                if (numFlag)
                    nums.push(nums.pop() * 10 + num)
                else
                    nums.push(num)

                firstFlag = false
                numFlag = true
            } else {
                val op = util.getOperator(it)!!

                if (firstFlag || (!numFlag && !util.isRightBracket(lastOp))) {
                    if (util.canBeginWith(op)) {
                        firstFlag = false
                        numFlag = false
                        if (op == Operator.MINUS) {
                            negativeFlag = !negativeFlag
                            return@forEach
                        }
                        if (op == Operator.PLUS)
                            return@forEach
                    } else {
                        throw ParseException("no left number for '${op.ch}'", index)
                    }
                }

                val preOp = ops.pop()
                if (util.isRightBracket(op)) {
                    if (!numFlag && !util.canEndWith(lastOp))
                        throw ParseException("no right number for '${lastOp.ch}'", index - 1)
                    if (preOp.priority != op.priority) {
                        ops.push(preOp)
                        try {
                            compute { it.priority != op.priority }
                        } catch (e: EmptyStackException) {
                            throw ParseException("no left bracket for '${op.ch}'",index)
                        }
                        ops.pop()
                    }
                    numFlag = false
                    lastOp = op
                    return@forEach
                }

                numFlag = false
                lastOp = op

                if (preOp.priority < op.priority || util.isLeftBracket(preOp))
                    ops.push(preOp)
                else {
                    ops.push(preOp)
                    compute { it.priority > op.priority && !util.isLeftBracket(it) }
                }
                ops.push(op)
            }
        }

        if (!numFlag && !util.canEndWith(lastOp))
            throw ParseException("no right number for '${lastOp.ch}'", expression.length - 1)

        compute { ops.isNotEmpty() }
        result = nums.pop()
    }

    @Throws(ParseException::class)
    private fun compute(condition: (Operator) -> (Boolean)) {
        val numsTemp = Stack<Int>()
        val opsTemp = Stack<Operator>()
        numsTemp.push(nums.pop())

        //Convert for * / ^
        var temp: Operator = ops.pop()
        while (condition(temp) && !util.canBeginWith(temp)) {
            opsTemp.push(temp)
            numsTemp.push(nums.pop())
            temp = ops.pop()
        }
        ops.push(temp)

        while (opsTemp.isNotEmpty()) {
            temp = opsTemp.pop()
            numsTemp.push(util.computeOperator(numsTemp.pop(), temp, numsTemp.pop()))
        }
        nums.push(numsTemp.pop())

        //Back forward compute
        temp = ops.pop()
        while (condition(temp)) {
            if (util.isLeftBracket(temp)){
                if(!fixRight)
                    throw ParseException("no right bracket for '${temp.ch}'", expression.indexOf(temp.ch))
                temp=ops.pop()
                continue
            }
            val right = nums.pop()
            val left = nums.pop()
            nums.push(util.computeOperator(left, temp, right))
            temp = ops.pop()
        }
        ops.push(temp)
    }

    override fun toString() = "$expression=$result"
    /**
     * get result of the expression
     */
    fun getResult() = result
}