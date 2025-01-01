package aoc2022.day_02

import aoc2022.day_02.RockPaperScissors.Companion.toRPS
import utils.AocTask
import kotlin.time.measureTime

object Day2: AocTask() {

    override fun executeTask() {
        measureTime {
            val matches = testInput.toListOfMatches()
            val result = matches.sumOf { it.matchScore() }
            println("Sum of test matches scores = $result")
        }.let { println("Test part took $it\n") }

        measureTime {
            val matches = input.toListOfMatches()
            val result = matches.sumOf { it.matchScore() }
            println("Sum of matches scores = $result")
        }.let { println("Part 1 took $it\n") }

        measureTime {
            val matches = input.toListOfMatchesPart2()
            val result = matches.sumOf { it.matchScore() }
            println("Sum of matches scores = $result")
        }.let { println("Part 2 took $it\n") }
    }

    private fun String.toListOfMatches(): List<Pair<RockPaperScissors, RockPaperScissors>> = lines().map { it.toRPSPair() }
    private fun String.toListOfMatchesPart2(): List<Pair<RockPaperScissors, RockPaperScissors>> = lines().map { it.toRPSPairPart2() }

    private fun String.toRPSPair(): Pair<RockPaperScissors, RockPaperScissors> {
        val (first, second) = split(' ').map { it.toRPS() }
        return first to second
    }

    private fun String.toRPSPairPart2(): Pair<RockPaperScissors, RockPaperScissors> {
        val (first, second) = split(' ')
        val firstRPS = first.toRPS()
        return firstRPS to RockPaperScissors.findCorrectRPS(firstRPS, second)
    }

    private fun Pair<RockPaperScissors, RockPaperScissors>.matchScore(): Int = second.matchScore(first)

}