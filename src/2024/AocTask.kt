package `2024`

import java.io.File

interface AocTask {
    val fileName: String
    fun executeTask()
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