package utils.models

enum class Direction(val x: Int, val y: Int) {
    UP(0, -1),
    RIGHT(1, 0),
    DOWN(0, 1),
    LEFT(-1, 0),
    NONE(0, 0);

    // Clockwise next direction
    fun next(): Direction =
        when (this) {
            UP -> RIGHT
            RIGHT -> DOWN
            DOWN -> LEFT
            LEFT -> UP
            NONE -> NONE
        }

    // Clockwise previous direction (counter-clockwise next)
    fun previous(): Direction =
        when (this) {
            UP -> LEFT
            LEFT -> DOWN
            DOWN -> RIGHT
            RIGHT -> UP
            NONE -> NONE
        }

    fun reversed(): Direction =
        when (this) {
            UP -> DOWN
            RIGHT -> LEFT
            DOWN -> UP
            LEFT -> RIGHT
            NONE -> NONE
        }

    val symobl: Char
        get() = when (this) {
            UP -> '^'
            RIGHT -> '>'
            DOWN -> 'v'
            LEFT -> '<'
            NONE -> 'A'
        }

    companion object {
        val validDirections = Direction.entries.filterNot { it == NONE }
    }
}