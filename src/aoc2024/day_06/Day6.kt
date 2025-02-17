package aoc2024.day_06

import utils.AocTask
import utils.models.Direction
import utils.models.Position
import kotlinx.coroutines.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.time.measureTime


object Day6: AocTask() {

    private const val OBSTACLE = '#'
    private const val CHARACTER = '^'

    override fun executeTask() {
        val grid = readToGrid()
        val guard = grid.findGuardAndObstacles()

        // Part 1
        guard.startPatrol(grid)
        println("Guard visited ${guard.visitedPositionsCount} distinct positions!")
        println()

        // Part 2
        val visitedPositions = guard.visitedPositions
        measureTime {
            val loopingObstacles = AtomicInteger(0)
            runBlocking(Dispatchers.Default) {
                visitedPositions.forEach { position ->
                    launch {
                        grid.findGuardAndObstacles().apply {
                            startPatrol(grid, position)
                        }.also { if (it.isInALoop) loopingObstacles.incrementAndGet() }
                    }
                }
            }
            println("There are $loopingObstacles obstructions that would make Guard loop")
        }.let {
            println("Part 2 with coroutine and path optimization took $it")
            println()
        }
    }

    private fun readToGrid(): Grid = Grid(data = inputToList())

    // Part 1
    private fun Grid.findGuardAndObstacles(): Guard {
        data.forEachIndexed { rowIndex, row ->
            row.indices.forEach { colIndex ->
                val currentPosition = Position(colIndex, rowIndex)
                if (row[colIndex] == CHARACTER) {
                    return Guard(
                        direction = Direction.UP
                    ).apply {
                        position = currentPosition
                    }
                }
            }
        }

        error("Guard was not found in input data!")
    }

    private fun Guard.startPatrol(
        grid: Grid,
        newObstacle: Position? = null
    ) {
        while (true) {
            val nextPosition = position + direction

            if (!grid.isInsideGrid(nextPosition))
                break

            if (grid.getValue(nextPosition) == OBSTACLE || nextPosition == newObstacle) {
                turn(nextPosition)
                if (isInALoop)
                    break
            } else moveToPosition(nextPosition)
        }
    }
}
