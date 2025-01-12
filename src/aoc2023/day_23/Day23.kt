package aoc2023.day_23

import utils.AocTask
import utils.models.Grid.Companion.toCharGrid
import kotlin.time.measureTime

object Day23: AocTask() {

    override fun executeTask() {
        measureTime {
            val hikingTrail = HikingTrail(testInput.toCharGrid())

            println("Start to end = ${hikingTrail.startPosition to hikingTrail.endPosition}")
            println("Graph positions = ${hikingTrail.graphPositions}")
            println("Graph = ${hikingTrail.graph}")

            hikingTrail.grid.copy(
                cells = hikingTrail.grid.cells.map { cell ->
                    if (cell.position in hikingTrail.graphPositions) cell.copy(value = 'X')
                    else cell
                }
            ).print()

            println("Longest trail length = ${hikingTrail.findLongestTrail()}")
        }.let { println("Test part took $it\n") }

        measureTime {
            val hikingTrail = HikingTrail(input.toCharGrid())
            println("Longest trail length = ${hikingTrail.findLongestTrail()}")
        }.let { println("Part 1 took $it\n") }

        measureTime {
            val hikingTrail = HikingTrail(input.toCharGrid())
            println("Longest trail length ignoring slopes = ${hikingTrail.findLongestTrailIgnoringSlopes()}")
        }.let { println("Part 2 took $it\n") }
    }
}