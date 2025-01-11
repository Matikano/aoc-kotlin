package aoc2021.day_01

import utils.AocTask
import utils.extensions.numsInt
import utils.extensions.tail
import kotlin.time.measureTime

object Day1: AocTask() {

    override fun executeTask() {
        measureTime {
            val numbers = testInput.toIntList()
            println("Count of increasing numbers = ${numbers.increasingCount()}")
            println("Count of increasing windows = ${numbers.increasingWindowedCount()}")
        }.let { println("Test part took $it\n") }

        measureTime {
            val numbers = input.toIntList()
            println("Count of increasing numbers = ${numbers.increasingCount()}")
        }.let { println("Part 1 took $it\n") }

        measureTime {
            val numbers = input.toIntList()
            println("Count of increasing numbers = ${numbers.increasingCount()}")
            println("Count of increasing windows = ${numbers.increasingWindowedCount()}")
        }.let { println("Part 2 took $it\n") }
    }

    private fun List<Int>.increasingWindowedCount(windowSize: Int = 3): Int =
        windowed(size = windowSize).map { it.sum() }.increasingCount()

    private fun List<Int>.increasingCount(): Int =
        zip(tail()) { a, b -> b - a }
            .count { it > 0 }

    private fun String.toIntList(): List<Int> = numsInt()

}