package aoc2023.day_07

import aoc2023.day_07.Card.Companion.asString
import aoc2023.day_07.Card.Companion.possibleJokerReplacements
import aoc2023.day_07.Card.Companion.toCards
import aoc2023.day_07.Card.Companion.toHandType

data class Hand(
    val cards: List<Card>,
    val bid: Int
): Comparable<Hand> {

    val type: HandType
        get() = cards.toHandType()

    override fun toString(): String = "${cards.map { it.toSymbol() }.joinToString("")}: $bid"

    override fun compareTo(other: Hand): Int {
        // Part 1
        // val typeComparison = type.compareTo(other.type)

        // Part 2
        val typeComparison = bestJokerReplacementType.compareTo(other.bestJokerReplacementType)
        return if (typeComparison == 0) {
            var cardComparison = 0
            for ((current, other) in cards.zip(other.cards)) {
                cardComparison = current.compareTo(other)
                if (cardComparison != 0)
                    break
            }
            cardComparison
        } else typeComparison
    }

    val bestJokerReplacement: Hand
        get() = cards.possibleJokerReplacements(cards.filter { it != Card.J }.distinct().asString())
            .maxOfOrNull {
                Hand(cards = it.toCards(), bid)
            } ?: this

    val bestJokerReplacementType: HandType
        get() = bestJokerReplacement.type
}
