package aoc2024.day_18

import aoc2024.day_18.Maze.Companion.EMPTY_SPACE
import aoc2024.day_18.Maze.Companion.WALL
import utils.AocTask
import utils.extensions.numsInt
import utils.models.Grid
import utils.models.GridCell
import utils.models.Position
import kotlin.time.measureTime

object Day18: AocTask {

    private const val GRID_WIDTH = 71
    private const val GRID_HEIGHT = 71
    const val BYTE_COUNT = 1024

    private val testInput = """
        5,4
        4,2
        4,5
        3,0
        2,1
        6,3
        2,4
        1,5
        0,6
        3,3
        2,6
        5,1
        1,2
        5,5
        2,5
        6,5
        1,4
        0,4
        6,4
        1,1
        6,1
        1,0
        0,5
        1,6
        2,0
    """.trimIndent()

    override val fileName: String
        get() = "src/aoc2024/day_18/input.txt"

    override fun executeTask() {
        println("-------------------------------------")
        println("AoC 2024 Task ${this.javaClass.simpleName}")
        println()

        val testCount = 12
        val testWidth = 7
        val testHeight = 7

        with(testInput.lines().readToMaze(testWidth, testHeight)) {
            printMaze(testCount)
            val bestScore = solveFor(testCount)
            println("Best score for test data = $bestScore")
        }

        with(readFileToList().readToMaze()) {
            measureTime {
                printMaze(BYTE_COUNT)
                val bestScore = solveFor(BYTE_COUNT)

                println("Best score for Part 1 = $bestScore")
            }.let { println("Part 1 took $it") }

            measureTime {
                val blockingByte = findBlockingByte()

                println("Blocking bye = $blockingByte")
            }.let { println("Part 1 took $it") }
        }
    }

    private fun List<String>.readToMaze(width: Int = GRID_WIDTH, height: Int = GRID_HEIGHT): Maze {
        val walls = map {
            val (colIndex, rowIndex) = it.numsInt()
            Position(colIndex, rowIndex)
        }

        return Maze(
            grid = Grid(
                cells = (0..< width).flatMap { colIndex ->
                    (0 ..< height).map { rowIndex ->
                        val position = Position(colIndex, rowIndex)
                        GridCell(
                            position = position,
                            value = if (position in walls) WALL else EMPTY_SPACE
                        )
                    }
                }
            ),
            walls = walls
        )
    }
}