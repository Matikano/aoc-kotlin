package aoc2023.day_21

import utils.extensions.even
import utils.models.Direction
import utils.models.Grid
import utils.models.Position
import java.math.BigInteger
import kotlin.math.pow

data class Garden(
    val grid: Grid<Char>
) {
    val size: Int
        get() =  grid.width

    private val startPosition: Position
        get() = grid.cells.first { it.value == START }.position

    init {
        assert(grid.width == grid.height) { "Garden is not a square!" }
        assert(startPosition.colIndex == startPosition.rowIndex && startPosition.colIndex == size / 2)
            { "Start position is not in the middle of Garden!" }
        assert(PART_2_STEPS % size == size / 2)
    }

    val extendedGridPlotsCount: BigInteger
        get() =
            oddGridsCount.toBigInteger() * oddGridPlots.toBigInteger() +
                    evenGridsCount.toBigInteger() * evenGridPlots.toBigInteger() +
                    topCornerPlots.toBigInteger() + rightCornerPlots.toBigInteger() +
                    bottomCornerPlots.toBigInteger() + leftCornerPlots.toBigInteger() +
                    (extendedGridWidth + 1).toBigInteger() *
                        (smallTopRightPlots + smallBottomRightPlots + smallBottomLeftPlots + smallTopLeftPlots).toBigInteger() +
                    extendedGridWidth.toBigInteger() * (bigTopRightPlots + bigBottomRightPlots + bigBottomLeftPlots + bigTopLeftPlots).toBigInteger()

    val extendedGridWidth: Int
        get() = PART_2_STEPS / size - 1

    val oddGridsCount: Long
        get() = (extendedGridWidth / 2 * 2.0 + 1).pow(2).toLong()

    val evenGridsCount: Long
        get() = ((extendedGridWidth + 1) / 2 * 2.0).pow(2).toLong()

    val oddGridPlots: Int by lazy {
        uniquePlotsAfterSteps(steps = size * 2 + 1)
    }

    val evenGridPlots: Int by lazy {
        uniquePlotsAfterSteps(steps = size * 2)
    }

    val topCornerPlots: Int by lazy {
        uniquePlotsAfterSteps(
            startPosition = Position(colIndex = size / 2, rowIndex = size - 1),
            steps = size - 1
        )
    }

    val bottomCornerPlots: Int by lazy {
        uniquePlotsAfterSteps(
            startPosition = Position(colIndex = size / 2, rowIndex = 0),
            steps = size - 1
        )
    }

    val leftCornerPlots: Int by lazy {
        uniquePlotsAfterSteps(
            startPosition = Position(colIndex = size - 1, rowIndex = size / 2),
            steps = size - 1
        )
    }

    val rightCornerPlots: Int by lazy {
        uniquePlotsAfterSteps(
            startPosition = Position(colIndex = 0, rowIndex = size / 2),
            steps = size - 1
        )
    }

    val smallTopRightPlots: Int by lazy {
        uniquePlotsAfterSteps(
            startPosition = Position(colIndex = 0, rowIndex = size - 1),
            steps =  size / 2 - 1
        )
    }

    val smallBottomRightPlots: Int by lazy {
        uniquePlotsAfterSteps(
            startPosition = Position(colIndex = 0, rowIndex = 0),
            steps =  size / 2 - 1
        )
    }

    val smallBottomLeftPlots: Int by lazy {
        uniquePlotsAfterSteps(
            startPosition = Position(colIndex = size - 1, rowIndex = 0),
            steps =  size / 2 - 1
        )
    }

    val smallTopLeftPlots: Int by lazy {
        uniquePlotsAfterSteps(
            startPosition = Position(colIndex = size - 1, rowIndex = size - 1),
            steps =  size / 2 - 1
        )
    }

    val bigTopRightPlots: Int by lazy {
        uniquePlotsAfterSteps(
            startPosition = Position(colIndex = 0, rowIndex = size - 1),
            steps =  (3 * size / 2) - 1
        )
    }

    val bigBottomRightPlots: Int by lazy {
        uniquePlotsAfterSteps(
            startPosition = Position(colIndex = 0, rowIndex = 0),
            steps =  (3 * size / 2) - 1
        )
    }

    val bigBottomLeftPlots: Int by lazy {
        uniquePlotsAfterSteps(
            startPosition = Position(colIndex = size - 1, rowIndex = 0),
            steps =  (3 * size / 2) - 1
        )
    }

    val bigTopLeftPlots: Int by lazy {
        uniquePlotsAfterSteps(
            startPosition = Position(colIndex = size - 1, rowIndex = size - 1),
            steps =  (3 * size / 2) - 1
        )
    }

    fun uniquePlotsAfterSteps(startPosition: Position = this.startPosition, steps: Int): Int {
        val startingPlot = Plot(startPosition, steps)
        val queue = ArrayDeque<Plot>().apply {
            add(startingPlot)
        }
        val reachedPositions = mutableSetOf<Position>()
        val seen = mutableSetOf(startingPlot.position)

        while (queue.isNotEmpty()) {
            val currentPlot = queue.removeFirst()

            if (currentPlot.remainingSteps.even())
                reachedPositions.add(currentPlot.position)

            if (currentPlot.remainingSteps == 0)
                continue

            val possibleNewPlots = Direction.validEntries
                .map { direction ->
                    currentPlot.position + direction
                }
                .filter { newPosition ->
                    grid.isInBounds(newPosition)
                            && grid[newPosition]!!.value != ROCK
                            && newPosition !in seen
                }
                .map { newPosition ->
                    Plot(
                        position = newPosition,
                        remainingSteps = currentPlot.remainingSteps - 1
                    )
                }

            possibleNewPlots.forEach { plot ->
                queue.add(plot)
                seen.add(plot.position)
            }
        }

        return reachedPositions.size
    }


    companion object {
        private const val START = 'S'
        private const val ROCK = '#'
        private const val REACHABLE_PLOT = 'O'
        private const val PART_2_STEPS = 26501365
    }

    data class Plot(
        val position: Position,
        val remainingSteps: Int
    )
}
