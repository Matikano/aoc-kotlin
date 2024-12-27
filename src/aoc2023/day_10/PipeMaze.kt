package aoc2023.day_10

import utils.models.Direction
import utils.models.Grid
import utils.models.Position

data class PipeMaze(
    var grid: Grid<Char>
) {

    val startPosition: Position by lazy {
        grid.cells.first { it.value == START_CHAR }.position
    }

    val loop: MutableSet<Position> = mutableSetOf<Position>()

    val longestDistanceFromStart: Int
        get() = loop.size / 2

    fun solve() {
        val queue = ArrayDeque<Position>().apply {
            add(startPosition)
        }
        val seen = mutableSetOf(startPosition)
        var startPipeChar = grid[startPosition]!!.value

        while (queue.isNotEmpty()) {
            val currentPosition = queue.removeFirst()
            val currentChar = grid[currentPosition]!!.value

            val nextPositions = currentChar
                .toValidDirections()
                .map { direction ->
                    currentPosition + direction
                }
                .filter { grid.isInBounds(it) }
                .filter { newPosition ->
                    val newPipe = grid[newPosition]!!.value
                    currentPosition in newPipe.toValidDirections().map { newPosition + it }
                            && newPosition !in seen
                }

            if (currentPosition == startPosition) {
                startPipeChar = nextPositions.map { it - startPosition }
                    .map { backwardsDirection ->
                        Direction.validDirections
                            .first { it.x == backwardsDirection.colIndex
                                    && it.y == backwardsDirection.rowIndex }
                }.toPipeChar()

                replaceStartChar(startPipeChar)
            }

            for (nextPosition in nextPositions) {
                seen.add(nextPosition)
                queue.add(nextPosition)
            }
        }

        loop.addAll(seen)
        cleanOfJunkPipes()
    }

    fun enclosedByLoopCount(): Int {
        val enclosedPositions = mutableSetOf<Position>()

        grid.forEachRow { row ->
            var within = false
            var up: Boolean? = null
            row.forEach { cell ->
                val char = cell.value
                when(char) {
                    VERTICAL_PIPE -> {
                        assert(up == null)
                        within = !within
                    }
                    HORIZONTAL_PIPE -> assert(up != null)
                    in "$NORTH_EAST_PIPE$SOUTH_EAST_PIPE" -> {
                        assert(up == null)
                        up = char == SOUTH_EAST_PIPE
                        within = !within
                    }
                    in "$SOUTH_WEST_PIPE$NORTH_WEST_PIPE" -> {
                        up?.let {
                            val pipeToCompareWith = if (it) NORTH_WEST_PIPE else SOUTH_WEST_PIPE
                            if (char != pipeToCompareWith)
                                within = !within
                        }
                        up = null
                    }
                    GROUND -> Unit
                    else -> throw IllegalArgumentException("Unexpected character (horizontal): $char")
                }

                if (within && cell.position !in loop)
                    enclosedPositions.add(cell.position)
            }
        }

        replaceEnclosedChars(enclosedPositions)

        return enclosedPositions.size
    }

    private fun replaceEnclosedChars(enclosedPositions: Set<Position>) {
        grid = grid.copy(
            cells = grid.cells.map { cell ->
                if (cell.position in enclosedPositions) cell.copy(value = ENCLOSED_CHAR)
                else cell
            }
        )
    }

    private fun replaceStartChar(pipeChar: Char) {
        grid = grid.copy(
            cells = grid.cells.map { cell ->
                if (cell.position == startPosition) cell.copy(value = pipeChar)
                else cell
            }
        )
    }

    private fun cleanOfJunkPipes() {
        grid = grid.copy(
            cells = grid.cells.map { cell ->
                if (cell.position !in loop) cell.copy(value = GROUND)
                else cell
            }
        )
    }

    companion object {
        private const val START_CHAR = 'S'
        private const val VERTICAL_PIPE = '|'
        private const val HORIZONTAL_PIPE = '-'
        private const val NORTH_EAST_PIPE = 'L'
        private const val NORTH_WEST_PIPE = 'J'
        private const val SOUTH_EAST_PIPE = 'F'
        private const val SOUTH_WEST_PIPE = '7'
        private const val GROUND = '.'
        private const val ENCLOSED_CHAR = 'I'
        private const val OUTSIDE_CHAR = 'O'

        fun Char.toValidDirections(): List<Direction> =
            when(this) {
                START_CHAR -> Direction.validDirections
                VERTICAL_PIPE -> listOf(Direction.UP, Direction.DOWN)
                HORIZONTAL_PIPE -> listOf(Direction.RIGHT, Direction.LEFT)
                NORTH_EAST_PIPE -> listOf(Direction.UP, Direction.RIGHT)
                NORTH_WEST_PIPE -> listOf(Direction.UP, Direction.LEFT)
                SOUTH_EAST_PIPE -> listOf(Direction.DOWN, Direction.RIGHT)
                SOUTH_WEST_PIPE -> listOf(Direction.DOWN, Direction.LEFT)
                else -> emptyList()
            }

        fun List<Direction>.toPipeChar(): Char =
            when {
                containsAll(listOf(Direction.UP, Direction.DOWN)) -> VERTICAL_PIPE
                containsAll(listOf(Direction.RIGHT, Direction.LEFT)) -> HORIZONTAL_PIPE
                containsAll(listOf(Direction.UP, Direction.RIGHT)) -> NORTH_EAST_PIPE
                containsAll(listOf(Direction.UP, Direction.LEFT)) -> NORTH_WEST_PIPE
                containsAll(listOf(Direction.DOWN, Direction.RIGHT)) -> SOUTH_EAST_PIPE
                containsAll(listOf(Direction.DOWN, Direction.LEFT)) -> SOUTH_WEST_PIPE
                else -> '.'
            }
    }
}


