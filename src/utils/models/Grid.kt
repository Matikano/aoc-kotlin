package utils.models

data class Grid<T>(
    val cells: List<GridCell<T>>
) {
    val width: Int
        get() = cells.maxOf { it.position.colIndex } + 1

    val height: Int
        get() = cells.maxOf { it.position.rowIndex } + 1

    operator fun get(position: Position): GridCell<T>? =
        cells.firstOrNull { it.position == position }
}
