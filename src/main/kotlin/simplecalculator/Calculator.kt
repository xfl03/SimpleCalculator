package simplecalculator

import simplecalculator.util.Operator
import simplecalculator.util.ExpressionUtil
import java.util.*

/**
 * Calculator Class
 * @param expression
 */
class Calculator(val expression: String) {
    private var result = 0

    init {
        calculate()
    }

    private fun calculate() {
        val nums = Stack<Int>()
        val ops = Stack<Operator>()
        val util = ExpressionUtil.instance!!
        var flag = false//Is previous char a number?

        nums.push(0)
        ops.push(Operator.PLUS)

        expression.chars().forEach {
            //println(it.toChar())
            if(it.toChar()==' ')
                return@forEach

            if (util.isNum(it)) {
                val num = util.toNum(it)
                if (flag)
                    nums.push(nums.pop() * 10 + num)
                else
                    nums.push(num)

                flag = true
            } else {
                flag = false
                val op = util.getOperator(it)!!

                var preOp = ops.pop()
                if (util.isRightBracket(op)) {
                    val leftOp = util.getPreOperator(op)
                    while (preOp != leftOp) {
                        nums.push(util.computeOperator(nums.pop(), preOp, nums.pop()))
                        preOp = ops.pop()
                    }
                    return@forEach
                }

                if (preOp.priority < op.priority || util.isLeftBracket(preOp))
                    ops.push(preOp)
                else {
                    while (preOp.priority > op.priority && !util.isLeftBracket(preOp)) {
                        nums.push(util.computeOperator(nums.pop(), preOp, nums.pop()))
                        preOp = ops.pop()
                    }
                    ops.push(preOp)
                }
                ops.push(op)
            }
        }

        while (ops.isNotEmpty()&&nums.size>=2)
            nums.push(util.computeOperator(nums.pop(), ops.pop(), nums.pop()))
        result = nums.pop()
    }

    override fun toString() = "$expression=$result"
    /**
     * get result of the expression
     */
    fun getResult() = result
}