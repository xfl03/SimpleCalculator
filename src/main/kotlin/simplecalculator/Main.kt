package simplecalculator

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.text.ParseException
import java.util.*

var debugMode = false
fun main(args: Array<String>) {
    if (args.contains("-d") || args.contains("--debug"))
        debugMode = true

    val br = BufferedReader(InputStreamReader(System.`in`))

    var expression = ""
    try {
        println("Input Expression: ")
        expression = br.readLine()
        val cal = Calculator(expression)
        println("Result: ")
        println(cal.getResult())
    } catch (e: Exception) {

        when (e) {
            is ParseException -> {
                println("Syntax Error!")
                println("Caused by: ${e.localizedMessage}")
                println(expression)
                val sb0 = StringBuilder()
                (1..e.errorOffset).forEach { sb0.append(' ') }
                sb0.append('^')
                println(sb0)
            }
            is ArithmeticException -> {
                println("Math Error!")
                println("Caused by: ${e.localizedMessage}")
            }
            is IOException -> {
                println("IO Exception!")
                println("Caused by: ${e.localizedMessage}")
            }
            else -> {
                println("Unknown Exception!")
                println("Exception: ${e.javaClass}")
                println("Caused by: ${e.localizedMessage}")
            }
        }
        if (debugMode) {
            println("[Error] Stack Trace:")
            Thread.sleep(100)//Avoid async output
            e.printStackTrace()
        }
    }
}