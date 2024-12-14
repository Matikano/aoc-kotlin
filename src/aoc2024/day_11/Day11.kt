package aoc2024.day_11

import aoc2024.AocTask
import utils.extensions.numsInt
import utils.extensions.numsLong
import kotlin.time.measureTime

typealias Stone = Long
typealias Iterations = Int

object Day11: AocTask {

    private const val FILE_SPLITTING_DELIMITER = " "
    private const val PART_1_ITERATIONS = 25
    private const val PART_2_ITERATIONS = 75

    override val fileName: String
        get() = "src/aoc2024/day_11/input.txt"

    override fun executeTask() {
        println("-------------------------------------")
        println("AoC 2024 Task ${this.javaClass.simpleName}")
        println()

        with(readToList()) {
            measureTime {
                val sum = sumOf { RecursiveCount(it, PART_1_ITERATIONS) }
                println("Number of stones after $PART_1_ITERATIONS iterations = $sum")
            }.let { println("Part 1 took $it\n") }

            measureTime {
                val sum = sumOf { RecursiveCount(it, PART_2_ITERATIONS) }
                println("Number of stones after $PART_2_ITERATIONS iterations = $sum")
            }.let { println("Part 2 took $it\n") }
        }
    }

    private fun readToList(): List<Stone> =
        readFileToString().numsLong()
}