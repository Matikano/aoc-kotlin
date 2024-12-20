package aoc2024.day_19

import utils.AocTask
import kotlin.time.measureTime

object Day19: AocTask {

    private val testInput = """
        r, wr, b, g, bwu, rb, gb, br

        brwrr
        bggr
        gbbr
        rrbgbr
        ubwu
        bwurrg
        brgr
        bbrgwb
    """.trimIndent()


    override val fileName: String
        get() = "src/aoc2024/day_19/input.txt"

    override fun executeTask() {
        println("-------------------------------------")
        println("AoC 2024 Task ${this.javaClass.simpleName}")
        println()

        val (testPatterns, testDesigns) = testInput.readToData()

        measureTime {
            val validDesignsCount = testDesigns.count { it.isPossibleDesign(testPatterns) }
            val possibleArrangementCount = testDesigns.sumOf { PossibleDesignArrangementsCount(testPatterns).invoke(it) }

            println("Number of valid test input designs = $validDesignsCount")
            println("Sum of possible test arrangement count = $possibleArrangementCount")
        }.let { println("Test part took $it\n") }


        val (patterns, designs) = readFileToString().readToData()

        measureTime {
            val validDesignsCount = designs.count { it.isPossibleDesign(patterns) }
            println("Number of valid designs = $validDesignsCount")
        }.let { println("Part 1 took $it\n") }

        measureTime {
            val possibleArrangementCount = designs.sumOf {  PossibleDesignArrangementsCount(patterns).invoke(it) }
            println("Sum of possible arrangement count = $possibleArrangementCount")
        }.let { println("Part 2 took $it\n") }
    }

    private fun String.readToData(): Pair<List<String>, List<String>> {
        val (patternsChain, designsSegment) = split("\n\n")

        val patterns = patternsChain.split(", ").map { it.trim() }
        val designs = designsSegment.lines().map { it.trim() }

        return patterns to designs
    }

    private fun String.isPossibleDesign(patterns: List<String>): Boolean =
        if (isEmpty()) true
        else patterns.any { pattern ->
            startsWith(pattern) && substringAfter(pattern).isPossibleDesign(patterns)
        }
}