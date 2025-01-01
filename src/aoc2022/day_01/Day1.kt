package aoc2022.day_01

import utils.AocTask
import kotlin.time.measureTime

object Day1: AocTask() {

    override fun executeTask() {
        measureTime {
            val result = testInput.toCaloriesBlocks().maxOf { it.toCaloriesSum() }
            println("Test result = $result")
        }.let { println("Test part took $it\n") }

        measureTime {
            val result = input.trim().toCaloriesBlocks().maxOf { it.toCaloriesSum() }
            println("Test result = $result")
        }.let { println("Part 1 took $it\n") }

        measureTime {
            val result = input.trim().toCaloriesBlocks().map { it.toCaloriesSum() }.sortedDescending().take(3).sum()
            println("Test result = $result")
        }.let { println("Part 2 took $it\n") }
    }

    private fun String.toCaloriesBlocks(): List<String> = split("\n\n")
    private fun String.toCaloriesSum(): Int = lines().sumOf { it.toInt() }

}