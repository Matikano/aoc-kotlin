package aoc2024.day_21

import utils.models.*

data class Robot(
    val grid: Grid<Char>
) {

    var currentPosition: Position =
        grid.cells.first { it.value == ACTION_SYMBOL }.position

    val currentSymbol: Char
        get() = grid[currentPosition]!!.value

    companion object {
        const val ACTION_SYMBOL = 'A'
        const val GAP_SYMBOL = '#'
    }
}
