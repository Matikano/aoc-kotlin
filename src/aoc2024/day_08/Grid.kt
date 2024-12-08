package aoc2024.day_08

import aoc2024.Position
import aoc2024.uniquePairs

class Grid(
    private var rows: MutableList<String>,
) {

    private val height: Int
        get() = rows.size

    private val width: Int
        get() = rows.first().length

    val cells: Set<GridCell>
        get() = rows.flatMapIndexed { rowIndex, row ->
            row.mapIndexed { colIndex, char ->
                GridCell(
                    char,
                    Position(colIndex, rowIndex)
                )
            }
        }.toSet()

    val emptySpaces: Set<Position>
        get() = cells.filter { it.value == EMPTY_SPACE_CHAR }
            .map { it.position }
            .toSet()

    val occupiedSpaces: Set<GridCell>
        get() = cells.filter { it.value != EMPTY_SPACE_CHAR }
            .toSet()

    val antinodes = mutableSetOf<Position>()

    val antinodeCount: Int
        get() = antinodes.size

    private fun fitsIn(position: Position): Boolean =
        position in emptySpaces || position in occupiedSpaces.map { it.position }

    fun putAntinode(position: Position) {
        if (fitsIn(position)) {
            val (colIndex, rowIndex) = position
            if (position in emptySpaces)
                rows[rowIndex] = rows[rowIndex].replaceCharAt(colIndex, ANTINODE_CHAR)
            antinodes += position
        }
    }

    private fun String.replaceCharAt(index: Int, char: Char): String =
        buildString {
            append(this@replaceCharAt)
            setCharAt(index, char)
        }

    override fun toString(): String =
        buildString {
            rows.forEach {
                append(it)
                append('\n')
            }
        }

    fun putFirstAntinodes() {
        occupiedSpaces
            .groupBy { it.value }
            .flatMap {
                it.value.map { it.position }
                    .uniquePairs()
                    .flatMap { it.getFirstAntinodePositions() }

            }
            .filter { fitsIn(it) }
            .forEach { putAntinode(it) }
    }

    fun puAllAntinodes() {
        occupiedSpaces
            .groupBy { it.value }
            .flatMap {
                it.value.map { it.position }
                    .uniquePairs()
                    .flatMap { it.getAllAntinodePositions() }

            }
            .filter { fitsIn(it) }
            .forEach { putAntinode(it) }
    }

    // Part 1
    fun Pair<Position, Position>.getFirstAntinodePositions(): List<Position> {
        val distance: Position = second - first
        return listOf(
            first - distance,
            second + distance
        )
    }

    // Part 2
    fun Pair<Position, Position>.getAllAntinodePositions(): List<Position> {
        val distance: Position = second - first
        val positions = mutableListOf(first, second)

        var nextPosition = second + distance
        while (
            nextPosition.colIndex in 0 until width
            && nextPosition.rowIndex in 0 until height
        ) {
            positions += nextPosition
            nextPosition += distance
        }
        nextPosition = first - distance
        while (
            nextPosition.colIndex in 0 until width
            && nextPosition.rowIndex in 0 until height
        ) {
            positions += nextPosition
            nextPosition -= distance
        }

        return positions
    }

    companion object {
        const val EMPTY_SPACE_CHAR = '.'
        const val ANTINODE_CHAR = '#'
    }
}
