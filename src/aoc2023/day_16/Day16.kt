package aoc2023.day_16

import utils.AocTask
import utils.models.Grid.Companion.toCharGrid
import kotlin.time.measureTime

object Day16: AocTask() {

    override fun executeTask() {
        measureTime {
            val testMaze = LavaMaze(testInput.toCharGrid())
            val energizedMap = testMaze.solve()
            println("Number of energized test positions = ${energizedMap.size}")

            println()
            println("Best energization = ${testMaze.bestEnergized}")
        }.let { println("Test part took $it\n") }

        measureTime {
            val maze = LavaMaze(input.toCharGrid())
            val energizedMap = maze.solve()
            println("Number of energized positions = ${energizedMap.size}")
        }.let { println("Part 1 took $it\n") }

        measureTime {
            val maze = LavaMaze(input.toCharGrid())
            println("Best energization = ${maze.bestEnergized}")
        }.let { println("Part 2 took $it\n") }
    }
}