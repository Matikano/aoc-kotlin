package aoc2022.day_12

import utils.AocTask
import utils.models.Grid.Companion.toCharGrid
import kotlin.time.measureTime

object Day12: AocTask() {

    override fun executeTask() {
        measureTime {
            val hillMaze = HillMaze(testInput.toCharGrid())
            hillMaze.grid.print()
            val shortestPath = hillMaze.findShortestPath()
            println("Shortest path length = $shortestPath")
        }.let { println("Test part took $it\n") }

        measureTime {
            val hillMaze = HillMaze(input.toCharGrid())
            val shortestPath = hillMaze.findShortestPath()
            println("Shortest path length = $shortestPath")
        }.let { println("Part 1 took $it\n") }

        measureTime {
            val hillMaze = HillMaze(input.toCharGrid())
            val shortestPath = hillMaze.findShortestPath(hillMaze.endPosition, 'a', HillMaze::canStepDown)
            println("Shortest path length = $shortestPath")
        }.let { println("Part 2 took $it\n") }
    }
}