package utils.models

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

    operator fun minus(other: Position): Position =
        Position(
            colIndex - other.colIndex,
            rowIndex - other.rowIndex
        )

    operator fun compareTo(other: Position): Int =
        when {
            rowIndex < other.rowIndex -> -1
            rowIndex > other.rowIndex -> 1
            else -> colIndex.compareTo(other.colIndex)
        }
}
