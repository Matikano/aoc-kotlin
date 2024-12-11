package aoc2024.day_11

import aoc2024.AocTask
import utils.even
import kotlin.time.measureTime

typealias Stone = Long
typealias Iterations = Int

object Day11: AocTask {

    private const val FILE_SPLITTING_DELIMETER = " "
    private const val PART_1_ITERATIONS = 25
    private const val PART_2_ITERATIONS = 75
    private const val COPY_FACTOR = 2024

    private val cache = mutableMapOf<Pair<Stone, Iterations>, Long>()

    override val fileName: String
        get() = "src/aoc2024/day_11/input.txt"

    override fun executeTask() {
        println("-------------------------------------")
        println("AoC 2024 Task ${this.javaClass.simpleName}")
        println()

        with(readToList()) {
            measureTime {
                val sum = sumOf { count(it, PART_1_ITERATIONS) }
                println("Number of stones after $PART_1_ITERATIONS iterations = $sum")
            }.let { println("Part 1 took $it\n") }

            measureTime {
                val sum = sumOf { count(it, PART_2_ITERATIONS) }
                println("Number of stones after $PART_2_ITERATIONS iterations = $sum")
            }.let { println("Part 2 took $it\n") }
        }
    }

    private fun readToList(): List<Stone> =
        readFileToString()
            .trim()
            .split(FILE_SPLITTING_DELIMETER)
            .filter { it.isNotEmpty() }
            .map { it.toLong() }

    private fun count(stone: Stone, iterations: Iterations): Long =
        cache.getOrPut(stone to iterations) {
            when {
                iterations == 0 -> 1L
                stone == 0L -> count(1, iterations - 1)
                stone.toString().length.even() -> stone.split().sumOf { count(it, iterations - 1) }
                else -> count(stone * COPY_FACTOR, iterations - 1)
            }
        }

    private fun Stone.split(): List<Stone> = buildList {
        with(this@split.toString()) {
            val midIndex = indices.last / 2

            add(substring(indices.first..midIndex).toLong())
            add(substring(midIndex + 1..indices.last).toLong())
        }
    }
}