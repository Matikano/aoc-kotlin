package aoc2021.day_05

import utils.AocTask
import utils.extensions.numsInt
import utils.models.Position
import kotlin.time.measureTime

object Day5: AocTask() {

    override fun executeTask() {
        measureTime {
            val positions = testInput.allPositions()
            println("Number of overlaps: ${positions.overlapCount()}")
        }.let { println("Test part took $it\n") }

        measureTime {
            val positions = input.allPositions()
            println("Number of overlaps: ${positions.overlapCount()}")
        }.let { println("Part 1 took $it\n") }

        measureTime {
            val positions = input.allPositions(false)
            println("Number of overlaps: ${positions.overlapCount()}")
        }.let { println("Part 2 took $it\n") }
    }

    private fun List<Position>.overlapCount(): Int = groupingBy { it }.eachCount().count { it.value > 1 }

    private fun String.allPositions(ignoreDiagonals: Boolean = true): List<Position> = lines().flatMap { it.toPositions(ignoreDiagonals) }

    private fun String.toPositions(ignoreDiagonals: Boolean = true): List<Position> {
        val (x1, y1, x2, y2) = numsInt()

        val xRange = if (x1 <= x2) x1..x2 else x1 downTo x2
        val yRange = if (y1 <= y2) y1..y2 else y1 downTo y2

        return if (x1 != x2 && y1 != y2) {
            if (ignoreDiagonals) emptyList()
            else xRange.zip(yRange).map { (x, y) -> Position(x, y) }
        } else xRange.flatMap { x ->
            yRange.map { y ->
                Position(colIndex = x, rowIndex = y)
            }
        }
    }
}