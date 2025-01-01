package aoc2022.day_07

import utils.AocTask
import utils.extensions.head
import utils.extensions.numsInt
import utils.extensions.tail
import kotlin.time.measureTime

object Day7: AocTask() {

    private const val THRESHOLD = 100000
    private const val DISK_MAX_SIZE = 70000000
    private const val UPDATE_SIZE = 30000000
    private const val ROOT_DIR = "/"
    private const val DIR = "dir"
    private const val COMMAND = "$"
    private const val CURRENT_DIRECTORY = "cd"
    private const val RETURN = ".."

    override fun executeTask() {
        measureTime {
            val fileSystem = testInput.toFileSystem()
            println(fileSystem)
            val sum = fileSystem.values.filter { it <= THRESHOLD }.sum()
            println("Sum of directories with size smaller than $THRESHOLD = $sum")
        }.let { println("Test part took $it") }

        measureTime {
            val fileSystem = input.toFileSystem()
            println(fileSystem)
            val sum = fileSystem.values.filter { it <= THRESHOLD }.sum()
            println("Sum of directories with size smaller than $THRESHOLD = $sum")
        }.let { println("Part 1 took $it") }

        measureTime {
            val fileSystem = input.toFileSystem()
            println(fileSystem)
            val totalSize = fileSystem[ROOT_DIR] ?: 0
            val requiredSpace = UPDATE_SIZE - (DISK_MAX_SIZE - totalSize)
            val result =  fileSystem.values.filter { it >= requiredSpace }.minOrNull() ?: 0
            println("Sum of directories with size smaller than $THRESHOLD = $result")
        }.let { println("Part 2 took $it\n") }


    }

    private fun String.toFileSystem(): Map<String, Int> {
        val path: MutableList<String> = mutableListOf()
        val dirs = mutableMapOf<String, Int>()

        lines().map { it.split(" ") }.forEach { line ->
            when (line.head()) {
                COMMAND -> {
                    when (line.tail().head()) {
                        CURRENT_DIRECTORY -> {
                            if (line.last() == RETURN) {
                                path.removeLast()
                            } else {
                                path.add(line.last())
                            }
                        }
                    }
                }
                else -> {
                    if (line.head() != DIR) {
                        for (i in 0 until path.size) {
                            val currentPath = path.subList(0, i + 1).joinToString("/")
                            dirs[currentPath] = dirs.getOrDefault(currentPath, 0) + line[0].toInt()
                        }
                    }
                }
            }
        }

        return dirs
    }
}