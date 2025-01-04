package aoc2023.day_11

import utils.AocTask
import utils.extensions.uniquePairs
import utils.models.Grid
import utils.models.Grid.Companion.toGrid
import utils.models.Position
import kotlin.time.measureTime

object Day11: AocTask() {

    private const val EMPTY_SPACE_CHAR = '.'
    private const val GALAXY_CHAR = '#'
    private const val PART_1_EXPANSION_FACTOR = 2L
    private const val PART_2_EXPANSION_FACTOR = 1000000L

    override fun executeTask() {

        measureTime {
            val grid = testInput.toGrid()
            val minimumDistancesBetweenGalaxies = grid.minimumDistancesBetweenGalaxies(PART_1_EXPANSION_FACTOR)
            println("Sum of minimal distances between all galaxy pairs = ${minimumDistancesBetweenGalaxies.sum()}")
        }.let { println("Test part took $it\n") }

        with(input.toGrid()) {
            measureTime {
                val minimumDistancesBetweenGalaxies = minimumDistancesBetweenGalaxies(PART_1_EXPANSION_FACTOR)
                println("Sum of minimal distances between all galaxy pairs = ${minimumDistancesBetweenGalaxies.sum()}")
            }.let { println("Part 1 took $it\n") }

            measureTime {
                val minimumDistancesBetweenGalaxies = minimumDistancesBetweenGalaxies(PART_2_EXPANSION_FACTOR)
                println("Sum of minimal distances between all galaxy pairs = ${minimumDistancesBetweenGalaxies.sum()}")
            }.let { println("Part 2 took $it\n") }
        }
    }

    private fun Grid<Char>.emptyRowIndices(): List<Int> = buildList {
        forEachRowIndexed { index, row ->
            if (row.map { it.value }.joinToString("").all { it == EMPTY_SPACE_CHAR })
                add(index)
        }
    }

    private fun Grid<Char>.emptyColumnIndices(): List<Int> = buildList {
        transpose().forEachRowIndexed { index, row ->
            if (row.map { it.value }.joinToString("").all { it == EMPTY_SPACE_CHAR })
                add(index)
        }
    }

    private fun Grid<Char>.distanceRanges(): List<Pair<IntRange, IntRange>> =
        galaxyPositions().uniquePairs().map { it.first.distanceRanges(it.second) }

    private fun Grid<Char>.minimumDistancesBetweenGalaxies(expansionFactor: Long = 2L): List<Long> {
        val emptyColumns = emptyColumnIndices()
        val emptyRows = emptyRowIndices()

        return distanceRanges().map { (colRange, rowRange) ->
            val expandedColsCount = emptyColumns.count { it in colRange }
            val expandedRowsCount = emptyRows.count { it in rowRange }

            (colRange.endInclusive - colRange.start).toLong() + expandedColsCount * (expansionFactor - 1) +
                    (rowRange.endInclusive - rowRange.start).toLong() + expandedRowsCount * (expansionFactor - 1)
        }
    }

    private fun Grid<Char>.galaxyPositions(): List<Position> =
        cells.filter { it.value == GALAXY_CHAR }.map { it.position }
}