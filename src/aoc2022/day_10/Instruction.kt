package aoc2022.day_10

sealed class Instruction(val cycles: Int) {
    object Noop: Instruction(1)
    data class AddX(val value: Int): Instruction(2)
}