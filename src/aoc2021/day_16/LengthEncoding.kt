package aoc2021.day_16

enum class LengthEncoding {
    BITS,
    SUB_PACKETS;

    companion object {
        fun Int.toLengthEncoding(): LengthEncoding = entries.first { it.ordinal == this }
    }
}