package aoc2023.day_01

import utils.AocTask
import utils.extensions.numsInt
import java.util.Collections
import kotlin.time.measureTime

object Day1: AocTask {

    private val testInput = """two1nine
eightwothree
abcone2threexyz
xtwone3four
4nineeightseven2
zoneight234
7pqrstsixteen"""

    private val correctionTable = mapOf(
        "one" to 1,
        "two" to 2,
        "three" to 3,
        "four" to 4,
        "five" to 5,
        "six" to 6,
        "seven" to 7,
        "eight" to 8,
        "nine" to 9
    )

    override val fileName: String
        get() = "src/aoc2023/day_01/input.txt"

    override fun executeTask() {
        println("-------------------------------------")
        println("AoC 2023 Task ${this.javaClass.simpleName}")
        println()

        with(readFileToList().toListOfNumbers()) {
            measureTime {
                println("Calibration sum = ${calibrationSum()}")
            }.let { println("Part 1 took $it") }
        }

        with(readFileToList().toListOfNumbersCorrected()) {
            measureTime {
                println("Calibration sum of corrected list = ${sum()}")
            }.let { println("Part 2 took $it") }
        }
    }

    private fun List<String>.toListOfNumbers(): List<List<Int>> =
        map { it.filter { it.isDigit() }.map { it.digitToInt() } }

    private fun List<String>.toListOfNumbersCorrected(): List<Int> {
        var s = ""
        return map { line ->
            var first: Char? = null
            var last: Char? = null
            line.map { c ->
                if (c.isDigit()) {
                    if (first == null)
                        first = c
                    last = c
                } else {
                    s += c
                    correctionTable.forEach { (key, digit) ->
                        if (s.endsWith(key)) {
                            if (first == null) first = digit.toString().first()
                            last = digit.toString().first()
                        }
                    }
                }
            }
            "$first$last".toInt()
        }
    }


    private fun List<List<Int>>.calibrationSum(): Int =
        sumOf { "${it.first()}${it.last()}".toInt() }
}