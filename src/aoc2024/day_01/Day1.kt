package aoc2024.day_01

import utils.AocTask
import utils.extensions.numsInt
import kotlin.math.absoluteValue

object Day1: AocTask {
    override val fileName: String
        get() = "src/aoc2024/day_01/input.txt"

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

    private fun readToLists(): Pair<List<Int>, List<Int>> =
        readFileToList().map {
            val (first, second) = it.numsInt()
            first to second
        }.let { list ->
            list.map { it.first } to list.map { it.second }
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