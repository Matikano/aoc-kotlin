package utils.models

import aoc2024.day_14.Velocity
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sign

data class Position(
    val colIndex: Int,
    val rowIndex: Int
) : Comparable<Position> {

    val adjacents: Set<Position>
        get() = Adjacent.validEntries.map { this + it }.toSet()

    val neighbours: Set<Position>
        get() = Direction.validEntries.map { this + it }.toSet()

    override fun toString(): String = "($colIndex,$rowIndex)"

    operator fun plus(direction: Direction): Position =
        Position(
            colIndex + direction.x,
            rowIndex + direction.y
        )

    operator fun plus(adjacent: Adjacent): Position =
        Position(
            colIndex + adjacent.x,
            rowIndex + adjacent.y
        )

    operator fun plus(other: Position): Position =
        Position(
            colIndex + other.colIndex,
            rowIndex + other.rowIndex
        )

    operator fun plus(other: Pair<Int, Int>): Position =
        Position(
            colIndex + other.first,
            rowIndex + other.second
        )

    operator fun plus(velocity: Velocity): Position =
        Position(
            colIndex + velocity.dx,
            rowIndex + velocity.dy
        )

    operator fun minus(other: Position): Position =
        Position(
            colIndex - other.colIndex,
            rowIndex - other.rowIndex
        )

    fun distance(other: Position): Int =
        (colIndex - other.colIndex).absoluteValue + (rowIndex - other.rowIndex).absoluteValue

    fun distanceRanges(other: Position): Pair<IntRange, IntRange> {
        val colRangeStart = min(colIndex, other.colIndex)
        val colRangeEnd = max(colIndex, other.colIndex)
        val rowRangeStart = min(rowIndex, other.rowIndex)
        val rowRangeEnd = max(rowIndex, other.rowIndex)

        return colRangeStart..colRangeEnd to rowRangeStart..rowRangeEnd
    }

    fun toAdjacent(): Adjacent =
        Adjacent.entries.first { it.x == colIndex.sign && it.y == rowIndex.sign }

    override operator fun compareTo(other: Position): Int =
        when {
            rowIndex < other.rowIndex -> -1
            rowIndex > other.rowIndex -> 1
            else -> colIndex.compareTo(other.colIndex)
        }

    fun plusX(scalar: Int): Position =
        Position (
            colIndex = colIndex + scalar,
            rowIndex = rowIndex
        )

    fun scaleX(factor: Int): Position =
        Position(
            colIndex = colIndex * factor,
            rowIndex = rowIndex
        )

    fun reversed(): Position =
        Position(
            colIndex = rowIndex,
            rowIndex = colIndex
        )

    companion object {
        operator fun Pair<Position, Position>.plus(direction: Direction): Pair<Position, Position> =
            first + direction to second + direction

        fun topLeftCorner(): Position = Position(0, 0)
    }
}
