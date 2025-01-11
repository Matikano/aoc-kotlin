package aoc2022.day_24

import utils.AocTask
import utils.models.Grid
import utils.models.GridCell
import utils.models.Position
import kotlin.time.measureTime

object Day24: AocTask() {

    override fun executeTask() {
        measureTime {
            val blizzardBasin = testInput.toBlizzardBasin()
            println("Shortest time to get to the end = ${blizzardBasin.findPathToEnd()}")
        }.let { println("Test part took $it\n") }

        measureTime {
            val blizzardBasin = input.toBlizzardBasin()
            println("Shortest time to get to the end = ${blizzardBasin.findPathToEnd()}")
        }.let { println("Part 1 took $it\n") }

        measureTime {
            val blizzardBasin = input.toBlizzardBasin()
            println("Shortest time to get to the end = ${blizzardBasin.findPathToEnd(stages = 2)}")
        }.let { println("Part 2 took $it\n") }
    }

    private fun String.toBlizzardBasin(): BlizzardBasin =
        BlizzardBasin(
            grid = Grid(
                cells =
                    lines().drop(1).dropLast(1).flatMapIndexed { rowIndex, row ->
                        row.drop(1).dropLast(1).mapIndexed { colIndex, char ->
                            GridCell(
                                position = Position(colIndex, rowIndex),
                                value = char
                            )
                        }
                    }
            )
        )
}