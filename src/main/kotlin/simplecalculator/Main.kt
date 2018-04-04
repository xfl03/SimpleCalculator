package simplecalculator

import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*

var debugMode = false
fun main(args: Array<String>) {
    if (args.contains("-d") || args.contains("--debug"))
        debugMode = true

    val br = BufferedReader(InputStreamReader(System.`in`))

    try {
        println("Input Expression: ")
        val cal = Calculator(br.readLine())
        println("Result: ")
        println(cal.getResult())
    } catch (e: Exception) {

        when (e) {
            is ArithmeticException -> {
                println("Math Error!")
                println("Caused by: ${e.localizedMessage}")
            }
            is EmptyStackException -> {
                println("Syntax Error!")
            }
            else -> {
                println("Unknown Error!")
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