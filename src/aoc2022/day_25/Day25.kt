package aoc2022.day_25

import utils.AocTask
import kotlin.math.pow
import kotlin.time.measureTime

object Day25: AocTask() {

    override fun executeTask() {
        measureTime {
            val snafuNumbers = testInput.toListOfSnafuNumbers()
            println("SNAFU number representing sum = ${snafuNumbers.decimalSum().toSnafuNumber()}")
        }.let { println("Test part took $it\n") }

        measureTime {
            val snafuNumbers = input.toListOfSnafuNumbers()
            println("SNAFU number representing sum = ${snafuNumbers.decimalSum().toSnafuNumber()}")
        }.let { println("Part 1 took $it\n") }
    }

    fun Long.toSnafuNumber(): String {
        if (this == 0L) return "0"

        val digits = arrayOf("=", "-", "0", "1", "2")
        val base = 5
        var number = this
        val result = StringBuilder()

        while (number != 0L) {
            var remainder = number % base

            if (remainder < -2) {
                remainder += base
                number -= base
            } else if (remainder > 2) {
                remainder -= base
                number += base
            }

            result.append(digits[(remainder + 2).toInt()])
            number /= base
        }

        return result.reverse().toString()
    }

    private fun List<List<Int>>.decimalSum(): Long = sumOf {
        it.snafuToDecimal()
    }

    private fun List<Int>.snafuToDecimal(): Long = reversed().foldIndexed(0L) { index, acc, value ->
        acc + value * (5.0).pow(index).toLong()
    }

    private fun String.asSnafuList(): List<Int> =
        map { char ->
            when(char) {
                in "012" -> char.digitToInt()
                '-' -> -1
                '=' -> -2
                else -> throw IllegalArgumentException("Unsupported char $char for SNAFU number digit")
            }
        }

    private fun String.toListOfSnafuNumbers(): List<List<Int>> = lines().map { it.asSnafuList() }
}
