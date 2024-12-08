package aoc2024.day_04

import aoc2024.AocTask

object Day4: AocTask {

    private const val TARGET_WORD = "XMAS"
    private const val TARGET_CROSS_WORD = "MAS"
    private val anyDirectons = listOf(
         0 to  1, // right
         0 to -1, // left
         1 to  0, // down
        -1 to  0, // up
         1 to  1, // down-right
        -1 to -1, // down-left
         1 to -1, // up-left
        -1 to  1 // up-right
    )
    override val fileName: String
        get() = "src/aoc2024/day_04/input.txt"

    override fun executeTask() {
        println("-------------------------------------")
        println("AoC 2024 Task ${this.javaClass.simpleName}")
        println()

        with(readToList()) {
            // Part 1
            println("Total occurrences of '$TARGET_WORD' = ${countAnyOccurrencesOfWord(TARGET_WORD)}")

            // Part 2
            println("Total cross occurrences of '$TARGET_CROSS_WORD' = ${countCrossOccurrencesOfWord(TARGET_CROSS_WORD)}")
        }
    }

    private fun readToList(): List<String> =
        mutableListOf<String>().apply {
            readFileByLines { add(it) }
        }

    // Part 1
    private fun Direction.isWordFound(
        x: Int,
        y: Int,
        list: List<String>,
        target: String
    ): Boolean {
        val rows = list.size
        val cols = list[0].length

        return target.indices.all { index ->
            val newX = x + first * index
            val newY = y + second * index

            newX in 0 until rows
                    && newY in 0 until cols
                    && list[newX][newY] == target[index]
        }
    }

    private fun List<String>.countAnyOccurrencesOfWord(target: String): Int {
        var count = 0
        val cols = get(0).length

        indices.forEach { xIndex ->
            (0 until cols).forEach { yIndex ->
                anyDirectons.forEach { direction ->
                    if (direction.isWordFound(xIndex, yIndex, this, target))
                        count++
                }
            }
        }

        return count
    }

    // Part 2
    private fun List<String>.isCrossedWordFound(
        x: Int,
        y: Int,
        target: String
    ): Boolean {
        val rows = size
        val cols = get(0).length

        val yWordEnd = y + target.length - 1

        val upDownWord = buildString {
            target.indices.forEach { index ->
                val newX = x + index
                val newY = y + index
                if (newX in 0 until rows && newY in 0 until  cols)
                    append(this@isCrossedWordFound[newX][newY])
                else
                    return false
            }
        }

        val downUpWord = buildString {
            target.indices.forEach { index ->
                val newX = x + index
                val newY = yWordEnd - index
                if (newX in 0 until rows && newY in 0 until  cols)
                    append(this@isCrossedWordFound[newX][newY])
                else
                    return false
            }
        }

        return upDownWord.reversedOrNormalEquals(target) and
                downUpWord.reversedOrNormalEquals(target)
    }

    private fun String.reversedOrNormalEquals(target: String): Boolean =
        this == target || reversed() == target

    private fun List<String>.countCrossOccurrencesOfWord(target: String): Int {
        var count = 0
        val cols = get(0).length

        indices.forEach { xIndex ->
            (0 until cols).forEach { yIndex ->
                if (isCrossedWordFound(xIndex, yIndex, target))
                    count++
            }
        }

        return count
    }

}

typealias Direction = Pair<Int, Int>