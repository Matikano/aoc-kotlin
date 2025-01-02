package aoc2024.day_24

data class SwapScore(
    val wrongBitsIndices: List<Int>,
    val swappedOutputs: Pair<String, String>
)