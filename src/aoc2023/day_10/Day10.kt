package aoc2023.day_10

import utils.AocTask
import utils.models.Grid.Companion.toGrid
import kotlin.time.measureTime

object Day10: AocTask() {

    override fun executeTask() {
        with(PipeMaze(testInput.toGrid())) {
            solve()
            grid.print()
            val numberOfEnclosedPositions = enclosedByLoopCount()
            println("Number of enclosed positions = $numberOfEnclosedPositions")
            grid.print()
        }

        with (PipeMaze(inputToString().toGrid())) {
            measureTime {
                solve()
                println("Distance to furthest pipe from the start = $longestDistanceFromStart")
            }.let { println("Part one took $it\n") }

            measureTime {
                println("Number of enclosed positions = ${enclosedByLoopCount()}")
            }
        }
    }
}