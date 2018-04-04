package simplecalculator

import java.io.BufferedReader
import java.io.InputStreamReader

fun main(args: Array<String>) {
    val br = BufferedReader(InputStreamReader(System.`in`))

    try {
        println("Input Expression：")
        val cal = Calculator(br.readLine())
        println(cal.getResult())
    } catch (e: Exception) {
        println("Illegal Expression!")
        //e.printStackTrace()
    }
}