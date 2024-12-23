package aoc2024.day_05

import utils.AocTask
import utils.extensions.middleElement
import utils.extensions.numsInt
import utils.extensions.swap

typealias Rule = Pair<Int, Int>
typealias Page = List<Int>

object Day5: AocTask() {

    private const val RULE_SEPARATOR = '|'
    private const val PAGE_SEPARATOR = ','

    override fun executeTask() {
        println("-------------------------------------")
        println("AoC 2024 Task ${this.javaClass.simpleName}")
        println()

        val (orderRules, pages) = readFile()

        // Part 1
        val sumOfMiddleElementsOfValidPages = pages.filter { it.isInValidOrder(orderRules) }
            .sumOf { it.middleElement() }

        println("Sum of middle elements of valid pages = $sumOfMiddleElementsOfValidPages")

        // Part 2
        val sumOfMiddleElementsOfCorrectedPages = pages.filterNot { it.isInValidOrder(orderRules) }
            .map { it.correctList(orderRules) }
            .sumOf { it.middleElement() }

        println("Sum of middle elements of corrected pages = $sumOfMiddleElementsOfCorrectedPages")
    }

    private fun readFile(): Pair<List<Rule>, List<Page>> {
        val orderRules = mutableListOf<Pair<Int, Int>>()
        val pages = mutableListOf<List<Int>>()

        inputByLines { line ->
            if (line.contains(RULE_SEPARATOR)) {
                val (first, second) = line.numsInt()
                orderRules.add(first to second)
            } else if (line.contains(PAGE_SEPARATOR)) {
                pages.add(line.numsInt())
            }
        }

        return orderRules to pages
    }

    // Part 1
    private fun List<Int>.isInValidOrder(rules: List<Rule>): Boolean {
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
    private fun List<Int>.correctList(rules: List<Rule>): List<Int> {
        var list = this

        while (!list.isInValidOrder(rules))
            list = list.swapIncorrectness(rules)

        return list
    }

    private fun List<Int>.swapIncorrectness(rules: List<Rule>): List<Int> {
        rules.forEach {
            val indexOfFirst = indexOf(it.first)
            val indexOfSecond = indexOf(it.second)

            if (indexOfFirst != -1 && indexOfSecond != -1 && indexOfFirst > indexOfSecond)
                return swap(indexOfFirst, indexOfSecond)
        }

        return this
    }
}