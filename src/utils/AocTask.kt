package utils

import java.io.BufferedReader
import java.io.File

abstract class AocTask {

    val testInput: String by lazy {
        inputToString(testFileName)
    }

    val input: String by lazy {
        inputToString(inputFileName).trim()
    }

    private val inputFileName: String
        get() = "$ROOT_DIR/${this.javaClass.packageName.replace('.', '/')}/$INPUT_FILE_NAME"

    private val testFileName: String
        get() = "$ROOT_DIR/${this.javaClass.packageName.replace('.', '/')}/$TEST_INPUT_FILE_NAME"

    protected abstract fun executeTask()

    fun solve() {
        printHeader()
        executeTask()
    }

    fun inputToList(): List<String> = readFile(inputFileName).readLines()

    fun inputByLines(action: (String) -> Unit) = readFile(inputFileName).forEachLine(action)

    fun inputToString(fileName: String = inputFileName): String = readFile(fileName).readText()

    private fun readFile(fileName: String): BufferedReader = File(fileName).inputStream().bufferedReader()

    private fun printHeader() {
        println("-------------------------------------")
        println("AoC 2024 Task ${this.javaClass.simpleName}")
        println()
    }

    companion object {
        private const val ROOT_DIR = "src"
        private const val INPUT_FILE_NAME = "input.txt"
        private const val TEST_INPUT_FILE_NAME = "test_input.txt"
    }
}