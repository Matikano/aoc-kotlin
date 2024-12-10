package aoc2024.day_10

enum class Digit(val value: Int) {
    ZERO(0),
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6),
    SEVEN(7),
    EIGHT(8),
    NINE(9);

    private fun nextDigit(): Digit =
        (ordinal + 1).coerceAtMost(entries.size).let { entries[it] }

    fun isNext(other: Digit): Boolean =
        if (other == NINE) false
        else other.nextDigit() == this
}