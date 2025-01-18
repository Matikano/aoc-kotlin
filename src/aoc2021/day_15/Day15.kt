package aoc2021.day_15

import utils.AocTask
import utils.models.Grid.Companion.toDigitGrid
import kotlin.time.measureTime

object Day15: AocTask() {

    override fun executeTask() {
        measureTime {
            val maze = testInput.toMaze()
            println("Maze end position = ${maze.endPosition}")
            println("Maze shortest path risk level = ${maze.shortestPathRisk()}")
            println("Scaled aze shortest path risk level = ${maze.shortestPathRisk(maze.scaledEndPosition)}")
        }.let { println("Test part took $it\n") }

        measureTime {
            val maze = input.toMaze()
            println("Maze end position = ${maze.endPosition}")
            println("Maze shortest path risk level = ${maze.shortestPathRisk()}")
        }.let { println("Part 1 took $it\n") }

        measureTime {
            val maze = input.toMaze()
            println("Scaled maze shortest path risk level = ${maze.shortestPathRisk(maze.scaledEndPosition)}")
        }.let { println("Part 2 took $it\n") }
    }

    private fun String.toMaze() = Maze(toDigitGrid())
}