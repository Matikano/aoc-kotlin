package aoc2022.day_22

data class Instruction(
    val steps: Int,
    val turn: Turn? = null
)

enum class Turn {
    RIGHT,
    LEFT;
}