package aoc2023.day_10

import utils.AocTask
import utils.models.Grid.Companion.toCharGrid
import kotlin.time.measureTime

object Day10: AocTask() {

    override fun executeTask() {
        // Test part
        with(PipeMaze(testInput.toCharGrid())) {
            solve()
            val numberOfEnclosedPositions = enclosedByLoopCount()
            println("Number of enclosed positions = $numberOfEnclosedPositions")
        }

        with (PipeMaze(inputToString().toCharGrid())) {
            // Part 1
            measureTime {
                solve()
                println("Distance to furthest pipe from the start = $longestDistanceFromStart")
            }.let { println("Part one took $it\n") }

            // Part 2
            measureTime {
                println("Number of enclosed positions = ${enclosedByLoopCount()}")
            }.let { println("Part 2 took $it\n") }
        }
    }
}