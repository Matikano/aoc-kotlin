package aoc2024.day_10

import aoc2024.AocTask
import utils.models.GridCell
import utils.models.Position

object Day10: AocTask {

    override val fileName: String
        get() = "src/aoc2024/day_10/input.txt"

    override fun executeTask() {
        println("-------------------------------------")
        println("AoC 2024 Task ${this.javaClass.simpleName}")
        println()

        with(readToGrid()) {
            // Part 1
            println("Sum of all trail scores = $sumOfAllTrailScores")

            // Part 2
            println("Sum of all trail ratings = $sumOfAllTrailRatings")
        }
    }

    private fun readToGrid(): Grid =
        Grid(
            cells = buildList {
                val digits = Digit.entries
                readFileToList().forEachIndexed { rowIndex, row ->
                    row.indices.forEach { colIndex ->
                        add(
                            GridCell(
                                value = digits[row[colIndex].digitToInt()],
                                position = Position(colIndex, rowIndex)
                            )
                        )
                    }
                }
            }
        )
}