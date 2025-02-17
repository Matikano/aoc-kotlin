package utils.models

enum class Adjacent(val x: Int, val y: Int) {
    UP(0, -1),
    UP_LEFT(-1, -1),
    UP_RIGHT(1, -1),
    RIGHT(1, 0),
    DOWN(0, 1),
    DOWN_LEFT(-1, 1),
    DOWN_RIGHT(1, 1),
    LEFT(-1, 0),
    NONE(0, 0);

    companion object {
        val validEntries = Adjacent.entries.filterNot { it == NONE }
    }
}