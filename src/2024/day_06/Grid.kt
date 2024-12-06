package `2024`.day_06

data class Grid(
    val data: List<String>
) {
    val rows: Int
        get() = data.size

    val cols: Int
        get() = if (data.isNotEmpty()) data[0].length else 0

    fun isInsideGrid(position: Position): Boolean =
        position.colIndex in 0 until cols &&
            position.rowIndex in 0 until rows

    fun getValue(position: Position): Char =
        data[position.rowIndex][position.colIndex]

    override fun toString(): String =
        buildString {
            data.forEach {
                append(it)
                append("\n")
            }
        }
}