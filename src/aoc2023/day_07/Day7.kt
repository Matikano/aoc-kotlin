package aoc2023.day_07

import aoc2023.day_07.Card.Companion.toCard
import aoc2023.day_07.Card.Companion.toCards
import utils.AocTask
import kotlin.time.measureTime

object Day7: AocTask() {

    override fun executeTask() {
        measureTime {
            val handList = inputToList().toListOfHands()
            val sortedHandList = handList.sorted()

            println("Total winnings = ${sortedHandList.totalWinnings()}")
        }.let { println("Part 1 took $it") }

        measureTime {
            val handList = inputToList().toListOfHands()
            val sortedHandListConsideringJokers = handList.sorted()
            val totalWinnings = sortedHandListConsideringJokers.totalWinnings()

            println("Total winnings considering Jokers = $totalWinnings")
        }.let { println("Part 2 took $it") }
    }

    private fun List<String>.toListOfHands(): List<Hand> = map { it.toHand() }

    private fun List<Hand>.totalWinnings(): Long = foldIndexed(0) { index, acc, hand ->
        acc + hand.bid * (index + 1)
    }

    private fun String.toHand(): Hand {
        val (cardsString, bidString) = split(" ")

        return Hand(
            cards = cardsString.toCards(),
            bid = bidString.toInt()
        )
    }
}