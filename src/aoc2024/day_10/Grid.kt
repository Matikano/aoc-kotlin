package aoc2024.day_10

import utils.models.Direction
import utils.models.GridCell
import utils.models.Position
import aoc2024.day_10.Digit.*
import utils.extensions.head

typealias Path = Set<GridCell<Digit>>

data class Grid(
    private val cells: List<GridCell<Digit>>
) {

    operator fun get(position: Position) =
        cells.firstOrNull { it.position == position }

    private val zeros: List<GridCell<Digit>>
        get() = cells.filter { it.value == ZERO }

    // Part 1
    private val allTrailsScores: List<Int>
        get() = zeros.map {
            it.generatePaths()
                .distinctBy { trail -> trail.first() to trail.last() }
                .size
        }

    val sumOfAllTrailScores: Int
        get() = allTrailsScores.sum()

    // Part 2
    private val allTrailsRatings: List<Int>
        get() = zeros.flatMap {
            it.generatePaths()
                .groupBy { trail -> trail.head() }
                .map { it.value.size }
        }

    val sumOfAllTrailRatings: Int
        get() = allTrailsRatings.sum()

    // Utils
    private fun GridCell<Digit>.getNextSteps(): Set<GridCell<Digit>> =
        Direction.validEntries.mapNotNull { direction ->
            this@Grid[this@getNextSteps.position + direction]
        }.filter {
            it.value.isNext(value)
        }.toSet()

    private fun Path.getPossibleNextPaths(): List<Path> =
       last()
           .getNextSteps()
           .map { this + it }

    private fun GridCell<Digit>.generatePaths(): List<Path> {
        var paths = listOf(setOf(this))

        repeat(Digit.entries.size - 1) {
            paths = paths.flatMap {
                it.getPossibleNextPaths()
            }
        }

        return paths
    }
}
