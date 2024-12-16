package aoc2024.day_03

import utils.AocTask
import utils.extensions.numsInt


object Day3: AocTask {

    private const val MUL_REGEX = """mul\(\d{1,3},\d{1,3}\)"""
    private const val DO_REGEX = """do\(\)"""
    private const val DONT_REGEX = """don't\(\)"""
    override val fileName: String
        get() = "src/aoc2024/day_03/input.txt"

    override fun executeTask() {
        println("-------------------------------------")
        println("AoC 2024 Task ${this.javaClass.simpleName}")
        println()

        // Part 1
        println("Calculated multiplications = ${readToList().calculateMultiplications()}")

        // Part 2
        println("Calculated filtered multiplications = ${readToListFiltered().calculateMultiplications()}")
    }

    private fun readToList(): List<String> =
        MUL_REGEX.toRegex()
            .findAll(readFileToString())
            .map { matchResult ->
                matchResult.value
            }.toList()

    private fun readToListFiltered(): List<String> = buildList {
        val regex = "$MUL_REGEX|$DO_REGEX|$DONT_REGEX".toRegex()
        var enabled = true

        regex.findAll(readFileToString()).forEach { matchResult ->
            when (matchResult.value) {
                "do()" -> enabled = true
                "don't()" -> enabled = false
                else -> if (enabled) add(matchResult.value)
            }
        }
    }

    private fun List<String>.calculateMultiplications(): Int =
        sumOf { it.multiplyExpression() }

    private fun String.multiplyExpression(): Int =
        numsInt().fold(1) { acc, value -> acc * value }
}