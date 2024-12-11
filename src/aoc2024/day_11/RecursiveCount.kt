package aoc2024.day_11

import utils.abstractions.RecursiveMemo2
import utils.extensions.even

object RecursiveCount : RecursiveMemo2<Stone, Iterations, Long>() {
    private const val COPY_FACTOR = 2024

    override fun MutableMap<Pair<Stone, Iterations>, Long>.recurse(stone: Stone, iterations: Iterations): Long =
        getOrPut(stone to iterations) {
            when {
                iterations == 0 -> 1L
                stone == 0L -> recurse(1L, iterations - 1)
                stone.toString().length.even() -> stone.split().sumOf { recurse(it, iterations - 1) }
                else -> recurse(stone * COPY_FACTOR, iterations - 1)
            }
        }

    private fun Stone.split(): List<Stone> =
        with(toString()) {
            chunked(length / 2).map { it.toLong() }
        }
}