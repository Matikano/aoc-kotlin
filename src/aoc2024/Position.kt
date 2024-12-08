package aoc2024

import aoc2024.day_06.Direction

data class Position(
    val colIndex: Int,
    val rowIndex: Int
) {
     operator fun plus(direction: Direction): Position =
        Position(
            colIndex + direction.x,
            rowIndex + direction.y
        )

    operator fun minus(other: Position): Position =
        Position(
            colIndex - other.colIndex,
            rowIndex - other.rowIndex
        )

    operator fun plus(other: Position): Position =
        Position(
            other.colIndex + colIndex,
            other.rowIndex + rowIndex
        )
}
