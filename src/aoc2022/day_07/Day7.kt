package aoc2022.day_07

import utils.AocTask
import utils.extensions.numsInt
import utils.extensions.tail
import kotlin.time.measureTime

object Day7: AocTask() {

    private const val THRESHOLD = 100000L

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
            val totalSize = fileSystem["/"] ?: 0
            val requiredSpace = 30000000 - (70000000 - totalSize)
            val result =  fileSystem.values.filter { it >= requiredSpace }.minOrNull() ?: 0
            println("Sum of directories with size smaller than $THRESHOLD = $result")
        }.let { println("Part 2 took $it\n") }


    }

    private fun String.toFileSystem(): Map<String, Int> {
        val path: MutableList<String> = mutableListOf()
        val dirs = mutableMapOf<String, Int>()

        lines().map { it.split(" ") }.forEach { l ->
            when (l[0]) {
                "$" -> {
                    when (l[1]) {
                        "cd" -> {
                            if (l[2] == "..") {
                                path.removeLast()
                            } else {
                                path.add(l[2])
                            }
                        }
                    }
                }
                else -> {
                    if (l[0] != "dir") {
                        for (i in 0 until path.size) {
                            val currentPath = path.subList(0, i + 1).joinToString("/")
                            dirs[currentPath] = dirs.getOrDefault(currentPath, 0) + l[0].toInt()
                        }
                    }
                }
            }
        }

        return dirs
    }
}