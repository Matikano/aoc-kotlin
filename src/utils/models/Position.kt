package utils.models

import kotlin.math.absoluteValue

data class Position(
    val colIndex: Int,
    val rowIndex: Int
) {
     operator fun plus(direction: Direction): Position =
        Position(
            colIndex + direction.x,
            rowIndex + direction.y
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

    operator fun minus(other: Position): Position =
        Position(
            colIndex - other.colIndex,
            rowIndex - other.rowIndex
        )

    fun difference(other: Position): Int =
        (colIndex - other.colIndex).absoluteValue + (rowIndex - other.rowIndex).absoluteValue

    operator fun compareTo(other: Position): Int =
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

    companion object {
        operator fun Pair<Position, Position>.plus(direction: Direction): Pair<Position, Position> =
            first + direction to second + direction
    }
}
