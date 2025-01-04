package aoc2022.day_12

import utils.models.Direction
import utils.models.Grid
import utils.models.Position

data class HillMaze(
    var grid: Grid<Char>
) {

    val startPosition: Position = Position.topLeftCorner()

    val endPosition: Position = grid.cells.find { it.value == HILL_SUMMIT_SYMBOL }?.position
        ?: error("End symbol not found in the grid")

    init {
        grid = grid.copy(
            cells = grid.cells.map { cell ->
                when (cell.value) {
                    START_SYMBOL -> cell.copy(value = 'a')
                    else -> cell
                }
            }
        )
    }

    fun findShortestPath(
        startPosition: Position = this.startPosition,
        endChar: Char = HILL_SUMMIT_SYMBOL,
        nodesConnected: (Char, Char) -> Boolean = ::canStepUp
    ): Int {
        val queue = ArrayDeque<Pair<Int, Position>>().apply { add(0 to startPosition) }
        val seen = mutableSetOf<Position>(startPosition)

        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            val currentValue = grid[current.second]!!.value

            val newPositions = Direction.validEntries.map { dir ->
                current.second + dir
            }

            for (position in newPositions) {
                if (!grid.isInBounds(position) || position in seen)
                    continue

                val nextValue = grid[position]!!.value
                if (!nodesConnected(currentValue, nextValue))
                    continue
                if (nextValue == endChar)
                    return current.first + 1

                seen.add(position)
                queue.add(current.first + 1 to position)
            }
        }

        return 0
    }

    companion object {
        private const val START_SYMBOL = 'S'
        private const val HILL_SUMMIT_SYMBOL = 'E'
        fun canStepDown(current: Char, other: Char): Boolean =
            when {
                current.toString() == HILL_SUMMIT_SYMBOL.toString() -> other.code <= 'z'.code
                else -> current.code - other.code <= 1
            }

        fun canStepUp(current: Char, other: Char): Boolean =
            when {
                other.toString() == HILL_SUMMIT_SYMBOL.toString() -> current.toString() == "z"
                else -> other.code - current.code <= 1
            }
    }
}
