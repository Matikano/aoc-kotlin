package utils

import java.io.File

interface AocTask {
    val fileName: String
    fun executeTask()

    fun readFileToList(): List<String> =
        File(fileName)
            .inputStream()
            .bufferedReader()
            .readLines()

    fun readFileByLines(action: (String) -> Unit) {
        File(fileName)
            .inputStream()
            .bufferedReader()
            .forEachLine { line ->
                action(line)
            }
    }

    fun readFileToString(): String =
        File(fileName)
            .inputStream()
            .bufferedReader()
            .readText()
}