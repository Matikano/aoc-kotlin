package aoc2024.day_10

import utils.AocTask
import utils.models.GridCell
import utils.models.Position

object Day10: AocTask() {

    override fun executeTask() {
        with(readToGrid()) {
            // Part 1
            println("Sum of all trail scores = $sumOfAllTrailScores")

            // Part 2
            println("Sum of all trail ratings = $sumOfAllTrailRatings")
        }
    }

    private fun readToGrid(): Grid =
        Grid(
            cells = inputToList().flatMapIndexed { rowIndex, row ->
                row.indices.map { colIndex ->
                    GridCell(
                        value = Digit.entries[row[colIndex].digitToInt()],
                        position = Position(colIndex, rowIndex)
                    )
                }
            }
        )
}