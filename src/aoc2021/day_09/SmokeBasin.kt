package aoc2021.day_09

import utils.models.Grid
import utils.models.GridCell
import utils.models.Position

data class SmokeBasin(
    private val grid: Grid<Int>
) {
    private val lowPoints: Set<GridCell<Int>> by lazy {
        grid.cells.filter { cell ->
            cell.position.neighbours.filter {
                grid.isInBounds(it)
            }.all { grid[it]!!.value > cell.value }
        }.toSet()
    }

    private val basinSizes: Map<GridCell<Int>, Int>
        get() = lowPoints.associate { it to getBasinSize(it.position) }

    val sumOfRiskLevels: Int
        get() = lowPoints.sumOf { it.value + 1 }

    val productOfThreeLargestBasins: Long
        get() = basinSizes.values.sortedDescending()
            .take(3)
            .fold(1L) { acc, value -> acc * value }

    private fun getBasinSize(lowPoint: Position): Int {
        val startCell = lowPoint

        val queue = ArrayDeque<Position>(listOf(startCell))
        val seen = mutableSetOf<Position>(startCell)

        while (queue.isNotEmpty()) {
            val currentPosition = queue.removeFirst()
            val currentHeight = grid[currentPosition]!!.value

            currentPosition.neighbours.filter { neighbourPosition ->
                grid.isInBounds(neighbourPosition)
                        && grid[neighbourPosition]!!.value in currentHeight + 1 ..< 9
                        && neighbourPosition !in seen
            }.forEach { nextPosition ->
                queue.add(nextPosition)
                seen.add(nextPosition)
            }
        }

        return seen.size
    }
}