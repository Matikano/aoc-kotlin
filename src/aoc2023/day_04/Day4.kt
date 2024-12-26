package aoc2023.day_04

import utils.AocTask
import utils.extensions.numsInt
import kotlin.time.measureTime

object Day4: AocTask() {

    private const val CARD_NAME_SPLIT_CHAR = ':'
    private const val LISTS_SPLIT_CHAR = '|'

    override fun executeTask() {

        val testCards = testInput.lines().toListOfCards()
        val scoreSum = testCards.sumOf { it.score }
        println("Test cards scores sum = $scoreSum")

        with(inputToList().toListOfCards()) {
            measureTime {
                val scoreSum = sumOf { it.score }
                println("Cards scores sum = $scoreSum")
            }.let { print("Part 1 took $it\n") }

            measureTime {
                println("Number of cards after copying them = ${countOfCardsAfterCopying()}")
            }.let { println("Part 2 took $it\n") }
        }
    }

    private fun List<Card>.countOfCardsAfterCopying(): Int  {
        val copiesMap = associate { it.id to 1 }.toMutableMap()

        forEachIndexed { index, card ->
            for (nextIndex in index + 1 .. index + card.winningNumbersCount) {
                val cardToCopy = this[nextIndex]
                copiesMap[cardToCopy.id] = copiesMap[cardToCopy.id]!! + copiesMap[card.id]!!
            }
        }

        return copiesMap.values.sum()
    }

    private fun List<String>.toListOfCards(): List<Card> =
        mapIndexed { index, line -> line.toCard(index + 1) }

    private fun String.toCard(id: Int): Card {
        val (winning, actual) = substringAfter(CARD_NAME_SPLIT_CHAR).split(LISTS_SPLIT_CHAR)
        return Card(
            id = id,
            winningNumbers = winning.numsInt(),
            actualNumbers = actual.numsInt()
        )
    }
}