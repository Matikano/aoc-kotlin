package aoc2021.day_21

import utils.abstractions.RecursiveMemo1
import utils.extensions.permutationsWithRepetition

typealias GameState = Pair<Player, Player>
typealias GameResult = Pair<Long, Long>

class DiracDiceGame(
    private val diracDice: IntRange,
    private val winningScore: Int,
    rollsPerTurn: Int
): RecursiveMemo1<GameState, GameResult>() {

    private val diceRollsFrequencyMap by lazy {
        diracDice.toList()
            .permutationsWithRepetition(rollsPerTurn)
            .groupingBy { it.sum() }
            .eachCount()
    }

    override fun MutableMap<GameState, GameResult>.recurse(
        players: GameState
    ): GameResult = getOrPut(players) {
        val (player1, player2) = players
        when {
            player1.score >= winningScore -> 1L to 0L
            player2.score >= winningScore -> 0L to 1L
            else -> {
                var player1Wins = 0L
                var player2Wins = 0L

                for ((rollSum, frequency) in diceRollsFrequencyMap) {
                    val nextPlayer = player1.copy()
                    nextPlayer.takeATurn(rollSum)

                    val nextWins = recurse(player2 to nextPlayer)

                    player1Wins += nextWins.second * frequency
                    player2Wins += nextWins.first * frequency
                }

                player1Wins to player2Wins
            }
        }
    }
}