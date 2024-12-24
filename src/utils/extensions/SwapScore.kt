package utils.extensions

data class SwapScore(
    val wrongBitsIndices: List<Int>,
    val swappedOutputs: Pair<String, String>
)
