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
        val sequencePairs = (Direction.NONE.symbol + sequence).zip(sequence)
        if (depth == 1)
            sequencePairs.sumOf { keypadLengths[it]!! }.toLong()
        else {
            sequencePairs.sumOf { symbolPair ->
                keypadSequences[symbolPair]!!.minOf { subsequence ->
                    recurse(subsequence.toSequence(), depth - 1)
                }
            }
        }
    }
}