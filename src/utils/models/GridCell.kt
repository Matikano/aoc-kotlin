package utils.models

data class GridCell<T>(
    val value: T,
    val position: Position
) {
    override fun toString(): String = "$position: $value"
}