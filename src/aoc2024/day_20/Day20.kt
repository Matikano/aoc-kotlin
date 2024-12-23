package aoc2024.day_20

import utils.AocTask
import utils.models.Grid
import utils.models.GridCell
import utils.models.Position
import kotlin.time.measureTime

object Day20: AocTask() {

    const val MIN_SAVE_TIME = 100
    private const val MAX_CHEATS = 20

    override fun executeTask() {
        println("-------------------------------------")
        println("AoC 2024 Task ${this.javaClass.simpleName}")
        println()

        with(testInput.lines().toMaze()) {
            measureTime {
                val standardRaceTime = solve()
                println("Test race standard race time = $standardRaceTime")
                solveFromEnd()

                val cheatPositionsCount = cheatPositionsCount(minDifference = 2)
                println("Count of cheat paths = $cheatPositionsCount")
            }
        }

        with(inputToList().toMaze()) {
            measureTime {
                val standardRaceTime = solve()
                solveFromEnd()
                println("Standard race time = $standardRaceTime")

                val cheatScoresCount = cheatPositionsCount()
                println("Number of cheats that would save at least $MIN_SAVE_TIME = $cheatScoresCount")
            }.let { println("Part 1 took $it") }


            measureTime {
                val cheatScoresCount = cheatPositionsCount(maxRadius = MAX_CHEATS)
                println("Number of cheats that would save at least $MIN_SAVE_TIME and maximum of $MAX_CHEATS cheats = $cheatScoresCount")
            }.let { println("Part 2 took $it") }
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