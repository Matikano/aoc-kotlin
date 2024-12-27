package aoc2023.day_12

data class Record(
    val row: String,
    val arrangement: List<Int>
) {
    val arrangementsCounter by lazy {
        CountPossibleArrangements(BLOCK_CHAR, EMPTY_SPACE_CHAR, WILD_CARD_CHAR)
    }

    val possibleArrangements: Long by lazy {
        arrangementsCounter(row, arrangement)
    }

    val possibleUnfoldedArrangements: Long by lazy {
        arrangementsCounter(unfoldedRow, unfoldedArrangements)
    }

    private val unfoldedRow: String by lazy {
        buildList {
            repeat(UNFOLD_FACTOR) { add(row) }
        }.joinToString(WILD_CARD_CHAR.toString())
    }

    private val unfoldedArrangements: List<Int> by lazy {
        buildList {
            repeat(UNFOLD_FACTOR) { addAll(arrangement) }
        }
    }

    companion object {
        const val UNFOLD_FACTOR = 5
        const val BLOCK_CHAR = '#'
        const val EMPTY_SPACE_CHAR = '.'
        const val WILD_CARD_CHAR = '?'
    }
}
