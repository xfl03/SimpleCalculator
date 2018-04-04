package simplecalculator

import simplecalculator.util.Operator
import simplecalculator.util.ExpressionUtil
import simplecalculator.util.Fraction
import java.text.ParseException
import java.util.*

/**
 * Calculator Class
 * @param expression
 */
class Calculator(private val expression: String) {
    private var result = Fraction(0)

    private val nums = Stack<Fraction>()
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
        var lastPoint=-1

        nums.push(Fraction(0))
        ops.push(Operator.PLUS)

        expression.chars().forEach {
            index++
            if (debugMode)
                println("[Read In] ${it.toChar()}")

            if (it.toChar() == ' ')//Ignore space
                return@forEach
            if (!util.isLegal(it))
                throw ParseException("illegal char '${it.toChar()}'", index)

            if(it.toChar()=='.'){
                if(lastPoint>=0)
                    throw ParseException("too more '.'",index)
                lastPoint=0
                if(!numFlag)
                    nums.push(Fraction(0))
                numFlag=true
            }else if (util.isNum(it)) {
                var num = util.toNum(it).toLong()
                if (negativeFlag) {
                    negativeFlag = false
                    num = -num
                }
                if (numFlag)
                    if(lastPoint>=0)
                        nums.push(nums.pop() .add(Fraction(num.toLong(),util.pow(10,lastPoint+1))))
                        else
                    nums.push(nums.pop() .multiply(Fraction(10)).add(Fraction(num)))
                else
                    nums.push(Fraction(num))

                firstFlag = false
                numFlag = true
            } else {
                lastPoint=-1
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
        val numsTemp = Stack<Fraction>()
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