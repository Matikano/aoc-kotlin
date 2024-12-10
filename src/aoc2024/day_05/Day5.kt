package aoc2024.day_05

import aoc2024.AocTask
import utils.middleElement
import utils.swap

object Day5: AocTask {

    private const val RULE_SEPARATOR = '|'
    private const val PAGE_SEPARATOR = ','

    override val fileName: String
        get() = "src/aoc2024/day_05/input.txt"

    override fun executeTask() {
        println("-------------------------------------")
        println("AoC 2024 Task ${this.javaClass.simpleName}")
        println()

        val (orderRules, pages) = readFile()

        // Part 1
        val sumOfMiddleElementsOfValidPages =
            pages
                .filter { it.isInValidOrder(orderRules) }
                .sumOf { it.middleElement() }

        println("Sum of middle elements of valid pages = $sumOfMiddleElementsOfValidPages")

        // Part 2
        val sumOfMiddleElementsOfCorrectedPages =
            pages
                .filterNot { it.isInValidOrder(orderRules) }
                .map { it.correctList(orderRules) }
                .sumOf { it.middleElement() }

        println("Sum of middle elements of corrected pages = $sumOfMiddleElementsOfCorrectedPages")
    }

    private fun readFile(): Pair<List<Pair<Int, Int>>, List<List<Int>>> {
        val orderRules = mutableListOf<Pair<Int, Int>>()
        val pages = mutableListOf<List<Int>>()

        readFileByLines { line ->
            if (line.contains(RULE_SEPARATOR))
                line.split(RULE_SEPARATOR)
                    .map { it.toInt() }
                    .let { orderRules.add(it.first() to it.last()) }
            else if (line.contains(PAGE_SEPARATOR)) {
                line.split(PAGE_SEPARATOR)
                    .map { it.toInt() }
                    .let { pages.add(it) }
            }
        }

        return orderRules to pages
    }

    // Part 1
    private fun List<Int>.isInValidOrder(rules: List<Pair<Int, Int>>): Boolean {
        forEachIndexed { index, value ->
            val shouldBeBefore = rules
                .filter { it.first == value }
                .map { it.second }

            val shouldBeAfter = rules
                .filter { it.second == value }
                .map { it.first }

            val beforeValues = subList(0, index)
            val afterValues = subList(index + 1, size)

            if (
                !beforeValues.all {
                    shouldBeAfter.contains(it) || !shouldBeBefore.contains(it)
                } or
                !afterValues.all {
                    shouldBeBefore.contains(it) || !shouldBeAfter.contains(it)
                }
            ) return false
        }
        return true
    }

    // Part 2
    private fun List<Int>.correctList(rules: List<Pair<Int, Int>>): List<Int> {
        var list = this

        while (!list.isInValidOrder(rules))
            list = list.swapIncorrectness(rules)

        return list
    }

    private fun List<Int>.swapIncorrectness(rules: List<Pair<Int, Int>>): List<Int> {
        rules.forEach {
            val indexOfFirst = indexOf(it.first)
            val indexOfSecond = indexOf(it.second)

            if (indexOfFirst != -1 && indexOfSecond != -1 && indexOfFirst > indexOfSecond)
                return swap(indexOfFirst, indexOfSecond)
        }

        return this
    }
}