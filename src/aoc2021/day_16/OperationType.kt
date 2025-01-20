package aoc2021.day_16


enum class OperationType {
    SUM,
    PRODUCT,
    MIN,
    MAX,
    NONE,
    BIGGER,
    LESS,
    EQUAL;

    companion object {
        fun Int.toOperationType(): OperationType = entries.first { this == it.ordinal }
    }
}