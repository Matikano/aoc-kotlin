package aoc2021.day_08

import utils.AocTask
import kotlin.time.measureTime

object Day8: AocTask() {

    override fun executeTask() {
        measureTime {
            val segments = testInput.toSevenSegmentList()
            println("Count of instances of 1, 4, 7 and 8 = ${segments.sumOf { it.countOfUniqueSizeDigits }}")
            println("Deciphered output sum = ${segments.sumOf { it.decipheredOutput }}")
        }.let { println("Test part took $it\n") }

        with(input.toSevenSegmentList()) {
            measureTime {
                println("Count of instances of 1, 4, 7 and 8 = ${sumOf { it.countOfUniqueSizeDigits }}")
            }.let { println("Part 1 took $it\n") }

            measureTime {
                println("Deciphered output sum = ${sumOf { it.decipheredOutput }}")
            }.let { println("Part 2 took $it\n") }
        }
    }

    private fun String.toSevenSegmentList(): List<SevenSegment> = lines().map { it.toSevenSegment() }
    private fun String.toSevenSegment(): SevenSegment {
        val (input, output) = split(" | ")
        return SevenSegment(input.split(" "), output.split(" "))
    }
}