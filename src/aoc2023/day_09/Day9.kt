package aoc2023.day_09

import utils.AocTask
import utils.extensions.numsInt
import utils.extensions.tail
import kotlin.time.measureTime

object Day9: AocTask() {

    override fun executeTask() {
        with(inputToList().map { it.numsInt() }) {
            // Part 1
            measureTime {
                println("Sum of predicted next values of all sequences = ${sumOf { it.extrapolate() }}")
            }.let { println("Part 1 took $it\n") }

            // Part 2
            measureTime {
                println("Sum of predicted previous values of all sequences = ${sumOf { it.extrapolateBackwards() }}")
            }.let { println("Part 2 took $it\n") }
        }
    }

    private fun List<Int>.extrapolate(): Int =
        when {
            all { it == 0 } -> 0
            else -> {
                val deltas = zip(tail()){ a, b -> b - a }
                val difference = deltas.extrapolate()
                last() + difference
            }
        }

    private fun List<Int>.extrapolateBackwards(): Int =
        when {
            all { it == 0 } -> 0
            else -> {
                val deltas = zip(tail()){ a, b -> b - a }
                val difference = deltas.extrapolateBackwards()
                first() - difference
            }
        }
}