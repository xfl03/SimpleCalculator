package simplecalculator

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.text.ParseException

var debugMode = false
var fixRight = false
var mixedFraction = false
var intOnly = false
fun main(args: Array<String>) {
    if (args.contains("-d") || args.contains("--debug"))
        debugMode = true
    if (args.contains("-f") || args.contains("--fix"))
        fixRight = true
    if (args.contains("-m") || args.contains("--mixed"))
        mixedFraction = true
    if (args.contains("-i") || args.contains("--int"))
        intOnly = true

    val br = BufferedReader(InputStreamReader(System.`in`))

    var expression = ""
    try {
        println("Input Expression: ")
        expression = br.readLine()
        val cal = Calculator(expression)
        println("Result: ")
        cal.getResult().print()
    } catch (e: Exception) {
        when (e) {
            is ParseException -> {
                println("Syntax Error!")
                println("Caused by: ${e.localizedMessage}")
                println(expression)
                val sb0 = StringBuilder()
                (1..e.errorOffset).forEach { sb0.append(' ') }
                sb0.append('^')
                print(sb0)
            }
            is ArithmeticException -> {
                println("Math Error!")
                print("Caused by: ${e.localizedMessage}")
            }
            is IOException -> {
                println("IO Exception!")
                print("Caused by: ${e.localizedMessage}")
            }
            else -> {
                println("Unknown Exception!")
                println("Exception: ${e.javaClass}")
                print("Caused by: ${e.localizedMessage}")
            }
        }
        if (debugMode) {
            println()
            println("[Error] Stack Trace:")
            Thread.sleep(100)//Avoid async output
            e.printStackTrace()
        }
    }
}