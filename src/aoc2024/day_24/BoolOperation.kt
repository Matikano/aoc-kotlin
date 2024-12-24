package aoc2024.day_24

enum class BoolOperation {
    OR,
    XOR,
    AND;

    fun process(a: Boolean, b: Boolean): Boolean =
        when (this) {
            OR -> a or b
            XOR -> a xor b
            AND -> a and b
        }
}