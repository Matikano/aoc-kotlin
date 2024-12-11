package aoc2024.day_11

import utils.abstractions.RecursiveMemo2
import utils.extensions.even

object RecursiveCount : RecursiveMemo2<Stone, Iterations, Long>() {
    private const val COPY_FACTOR = 2024

    override val recurse: (Stone, Iterations) -> Long = { stone, iterations ->
        when {
            iterations == 0 -> 1L
            stone == 0L -> invoke(1L, iterations - 1)
            stone.toString().length.even() -> stone.split().sumOf { invoke(it, iterations - 1) }
            else -> invoke(stone * COPY_FACTOR, iterations - 1)
        }
    }

    private fun Stone.split(): List<Stone> = buildList {
        with(this@split.toString()) {
            val midIndex = indices.last / 2

            add(substring(indices.first..midIndex).toLong())
            add(substring(midIndex + 1..indices.last).toLong())
        }
    }
}