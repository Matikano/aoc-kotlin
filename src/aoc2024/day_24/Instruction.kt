package aoc2024.day_24

data class Instruction(
    val inputGates: Pair<String, String>,
    val operation: BoolOperation,
    val outputGate: String
)
