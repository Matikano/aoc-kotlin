package aoc2023.day_07

import aoc2023.day_07.HandType.FULL
import aoc2023.day_07.HandType.HIGHEST_CARD
import aoc2023.day_07.HandType.PAIR
import aoc2023.day_07.HandType.TWO_PAIR
import utils.extensions.tail

enum class Card {
    J,
    TWO,
    THREE,
    FOUR,
    FIVE,
    SIX,
    SEVEN,
    EIGHT,
    NINE,
    TEN,
    Q,
    K,
    A;

    fun toSymbol(): Char =
        when (this) {
            A -> 'A'
            K -> 'K'
            Q -> 'Q'
            J -> 'J'
            TEN -> 'T'
            NINE -> '9'
            EIGHT -> '8'
            SEVEN -> '7'
            SIX -> '6'
            FIVE -> '5'
            FOUR -> '4'
            THREE -> '3'
            TWO -> '2'
        }

    companion object {

        fun List<Card>.possibleJokerReplacements(
            uniqueCards: String
        ): List<String> {
            val stringCards = asString()
            val cardsToReplaceWith = uniqueCards.takeIf { it.isNotEmpty() } ?: "A"
            return when {
                isEmpty() -> listOf("")
                else -> {
                    val firstPart = if (stringCards.first() == 'J') cardsToReplaceWith
                        else stringCards.first().toString()

                    return firstPart.flatMap { first ->
                        tail().toList().possibleJokerReplacements(cardsToReplaceWith).map { second ->
                            first.toString() + second
                        }
                    }
                }
            }
        }

        fun String.toCards(): List<Card> = map { it.toCard() }
        fun List<Card>.asString(): String = map { it.toSymbol() }.joinToString("")
        fun List<Card>.toHandType(): HandType = groupingBy { it }
            .eachCount()
            .values.let { cardCounts ->
                when {
                    5 in cardCounts -> HandType.FIVE
                    4 in cardCounts -> HandType.FOUR
                    3 in cardCounts -> {
                        if (2 in cardCounts) FULL
                        else HandType.THREE
                    }
                    cardCounts.count { it == 2 } == 2 -> TWO_PAIR
                    2 in cardCounts -> PAIR
                    else -> HIGHEST_CARD
                }
            }

        fun Char.toCard(): Card =
            when (this) {
                'A' -> A
                'K' -> K
                'Q' -> Q
                'J' -> J
                'T' -> TEN
                '9' -> NINE
                '8' -> EIGHT
                '7' -> SEVEN
                '6' -> SIX
                '5' -> FIVE
                '4' -> FOUR
                '3' -> THREE
                '2' -> TWO
                else -> throw IllegalArgumentException("Unsupported char for card mapping")
            }
    }
}