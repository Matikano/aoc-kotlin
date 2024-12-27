package utils.models

data class Grid<T>(
    val cells: List<GridCell<T>>
) {
    val width: Int
        get() = cells.maxOf { it.position.colIndex } + 1

    val height: Int
        get() = cells.maxOf { it.position.rowIndex } + 1

    fun isInBounds(position: Position): Boolean =
        with(position) {
            colIndex in 0 ..< width &&
                rowIndex in 0 ..< height
        }

    operator fun get(position: Position): GridCell<T>? =
        cells.firstOrNull { it.position == position }

    fun positionOf(value: T): Position =
        cells.first { it.value == value }.position

    fun print() = forEachRow { row ->
        println(row.joinToString("\t") { it.value.toString() })
    }

    inline fun forEachRow(action: (List<GridCell<T>>) -> Unit) =
        cells.windowed(
            size = width,
            step = width
        ).forEach(action)

    companion object {
        fun <T> Grid<T>.transpose(): Grid<T> = copy(
            cells = cells.map { cell ->
                cell.copy(
                    position = cell.position.reversed()
                )
            }.sortedBy { it.position }
        )

        fun String.toGrid(): Grid<Char> =
            Grid(
                cells = lines().flatMapIndexed { rowIndex, row ->
                    row.mapIndexed { colIndex, char ->
                        GridCell(
                            position = Position(colIndex, rowIndex),
                            value = char
                        )
                    }
                }
            )
    }
}