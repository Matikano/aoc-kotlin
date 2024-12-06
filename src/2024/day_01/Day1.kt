package `2024`.day_01

import `2024`.AocTask
import kotlin.math.absoluteValue

object Day1: AocTask {
    private const val VALUE_SEPARATOR = "   "
    override val fileName: String
        get() = "src/2024/day_01/input.txt"

    override fun executeTask() {
        println("-------------------------------------")
        println("AoC 2024 Task ${this.javaClass.simpleName}")
        println()

        val (firstColumn, secondColumn) = readToLists()

        // Part 1
        println("Sum of differences = ${firstColumn.calculateSumOfSortedDifferences(secondColumn)}")

        // Part 2
        println("Similarity score = ${firstColumn.calculateSimilarityScore(secondColumn)}")
    }

    private fun readToLists(): Pair<List<Int>, List<Int>> {
        val firstList = mutableListOf<Int>()
        val secondList = mutableListOf<Int>()

        readFileByLines { line ->
            val (first, second) = line.split(VALUE_SEPARATOR)

            firstList.add(first.toInt())
            secondList.add(second.toInt())
        }

        return firstList to secondList
    }

    // Part 1
    private fun List<Int>.calculateSumOfSortedDifferences(
        list: List<Int>
    ): Int = sorted().zip(list.sorted()) { first, second ->
        (first - second).absoluteValue
    }.sum()

    // Part 2
    private fun List<Int>.calculateSimilarityScore(
        list: List<Int>
    ): Int = sumOf { first ->
        first * list.count { it == first }
    }
}