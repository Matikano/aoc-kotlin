package aoc2024.day_12

import aoc2024.AocTask
import utils.models.GridCell
import utils.models.Position
import kotlin.time.measureTime

object Day12: AocTask {
    override val fileName: String
        get() = "src/aoc2024/day_12/input.txt"

    override fun executeTask() {
        println("-------------------------------------")
        println("AoC 2024 Task ${this.javaClass.simpleName}")
        println()

        with(readToGarden()) {
            findAllPlants()

            // Part 1
            measureTime {
                println("Part 1 total price of the garden = $totalPrice")
            }.let { println("Part 1 took $it") }

            measureTime {
                println("Part 2 discount price of the garden = $discountPrice")
            }.let { println("Part 2 took $it") }
        }
    }

    private fun readToGarden(): Garden =
        Garden(
            fields = readFileToList().flatMapIndexed {  rowIndex, row ->
                row.indices.map { colIndex ->
                    GridCell(
                        value = row[colIndex],
                        position = Position(colIndex, rowIndex)
                    )
                }
            }
        )
}