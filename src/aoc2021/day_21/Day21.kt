package aoc2021.day_21

import utils.AocTask
import utils.extensions.numsInt
import kotlin.time.measureTime

object Day21: AocTask() {

    val DIRAC_DICE = 1..3
    const val ROLLS_PER_TURN = 3
    const val WINNING_SCORE = 21

    override fun executeTask() {
        measureTime {
            val players = testInput.toPlayers()
            val finalScore = players.playGame()
            println("Final game score = $finalScore")
        }.let { println("Test part took $it\n") }

        measureTime {
            val players = input.toPlayers()
            val finalScore = players.playGame()
            println("Final game score = $finalScore")
        }.let { println("Part 1 took $it\n") }

        measureTime {
            val players = input.toPlayers()
            val playerWins = DiracDiceGame(DIRAC_DICE, WINNING_SCORE, ROLLS_PER_TURN)(players)
            val maxWins = maxOf(playerWins.first, playerWins.second)
            println("Maximum wins = $maxWins")
        }.let { println("Part 2 took $it\n") }
    }

    fun Pair<Player, Player>.playGame(): Int {
        var turn = 0
        val allRolls = (1..100).toList()

        while (!first.isWinning && !second.isWinning) {
            val turnRollsSum = listOf(
                allRolls[(turn * ROLLS_PER_TURN) % allRolls.size],
                allRolls[(turn * ROLLS_PER_TURN + 1) % allRolls.size],
                allRolls[(turn * ROLLS_PER_TURN + 2) % allRolls.size],
            ).sum()
            if (turn % 2 == 0) first.takeATurn(turnRollsSum)
            else second.takeATurn(turnRollsSum)
            turn++
        }

        val losingScore = if (first.isWinning) second.score else first.score
        return losingScore * turn * ROLLS_PER_TURN
    }

    private fun String.toPlayers(): Pair<Player, Player> {
        val (first, second) = lines().map { it.toPlayer() }
        return first to second
    }

    private fun String.toPlayer(): Player = Player(numsInt().last())
}