package `2024`.day_06

data class Position(
    val colIndex: Int,
    val rowIndex: Int
) {
     fun nextPosition(direction: Direction): Position =
        Position(
            colIndex + direction.x,
            rowIndex + direction.y
        )
}
