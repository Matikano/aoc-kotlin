package aoc2022.day_06

import utils.AocTask
import kotlin.time.measureTime

object Day6: AocTask() {

    private const val MARKER_SIZE = 4
    private const val START_OF_MESSAGE_MARKER_SIZE = 14

    override fun executeTask() {
        measureTime {
            println("Index of marker = ${testInput.indexOfMarker()}")
        }.let { println("Test part took $it\n") }

        measureTime {
            println("Index of marker = ${input.indexOfMarker()}")
        }.let { println("Part 1 took $it\n") }


        measureTime {
            println("Index of marker = ${input.indexOfMarker(START_OF_MESSAGE_MARKER_SIZE)}")
        }.let { println("Part 2 took $it\n") }
    }

    private fun String.indexOfMarker(markerSize: Int = MARKER_SIZE): Int =
        windowed(markerSize).indexOfFirst { it.toSet().size == markerSize } + markerSize
}