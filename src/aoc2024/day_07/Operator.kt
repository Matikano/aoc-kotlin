package aoc2024.day_07

enum class Operator {
    ADD,
    MULTIPLY,
    CONCATENATE;

    fun calcualte(a: Long, b: Long): Long =
        when (this) {
            ADD -> a + b
            MULTIPLY -> a * b
            CONCATENATE -> "$a$b".toLong()
        }
}