package aoc2024.day_19

import utils.AocTask
import utils.extensions.tail
import kotlin.time.measureTime

object Day19: AocTask() {

    override fun executeTask() {
        println("-------------------------------------")
        println("AoC 2024 Task ${this.javaClass.simpleName}")
        println()

        val (testPatterns, testDesigns) = testInput.lines().readToData()

        measureTime {
            val validDesignsCount = testDesigns.count { it.isPossible(testPatterns) }
            val possibleArrangementCount = testDesigns.sumOf { it.possibleDesignArrangements(testPatterns) }

            println("Number of valid test input designs = $validDesignsCount")
            println("Sum of possible test arrangement count = $possibleArrangementCount")
        }.let { println("Test part took $it\n") }


        val (patterns, designs) = inputToList().readToData()

        measureTime {
            val validDesignsCount = designs.count { it.isPossible(patterns) }
            println("Number of valid designs = $validDesignsCount")
        }.let { println("Part 1 took $it\n") }

        measureTime {
            val possibleArrangementCount = designs.sumOf {  it.possibleDesignArrangements(patterns) }
            println("Sum of possible arrangement count = $possibleArrangementCount")
        }.let { println("Part 2 took $it\n") }
    }

    private fun List<String>.readToData(): Pair<List<String>, List<String>> {
        val (patternsChain, designsSegment) = first() to tail().tail().toList()

        val patterns = patternsChain.split(", ").map { it.trim() }
        val designs = designsSegment.map { it.trim() }

        return patterns to designs
    }

    private fun String.possibleDesignArrangements(patterns: List<String>): Long =
        PossibleDesignArrangementsCount(patterns)(this)

    private fun String.isPossible(patterns: List<String>): Boolean =
        CheckDesignPossibility(patterns)(this)
}