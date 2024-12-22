package aoc2024.day_21

import utils.abstractions.RecursiveMemo2
import utils.models.Direction
import utils.models.Direction.Companion.toSequence

class ComputeLengthAtDepth(
    private val keypadLengths: Map<Pair<Char, Char>, Int>,
    private val keypadSequences: Sequences
): RecursiveMemo2<String, Int, Long>() {
    override fun MutableMap<Pair<String, Int>, Long>.recurse(
        sequence: String,
        depth: Int
    ): Long = getOrPut(sequence to depth) {
        (Direction.NONE.symbol + sequence)
            .zip(sequence)
            .sumOf { symbolPair ->
                if (depth == 1) keypadLengths[symbolPair]!!.toLong()
                else keypadSequences[symbolPair]!!.minOf { subsequence ->
                    recurse(subsequence.toSequence(), depth - 1)
                }
            }
    }
}