package aoc2024.day_16

import utils.AocTask
import utils.models.Grid
import utils.models.GridCell
import utils.models.Position
import kotlin.time.measureTime

object Day16: AocTask() {

    override fun executeTask() {
        with(inputToList().toMaze()) {
            measureTime {
                val mazeResult = solve()

                println("Best score = ${mazeResult.bestScore}")
                println("Number of best positions = ${mazeResult.bestPaths.size}")
            }.let { println("Part 1 and 2 took $it") }
        }
    }

    private fun List<String>.toMaze(): Maze =
        Maze(
            grid = Grid(
                cells = flatMapIndexed { rowIndex, row ->
                    row.mapIndexed { colIndex, char ->
                        GridCell(
                            position = Position(colIndex, rowIndex),
                            value = char
                        )
                    }
                }
            )
        )
}