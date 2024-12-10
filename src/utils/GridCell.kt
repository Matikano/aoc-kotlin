package utils

data class GridCell<T>(
    val value: T,
    val position: Position
) {
    override fun toString(): String =
        "GridCell: $value - (${position.colIndex}, ${position.rowIndex})"
}