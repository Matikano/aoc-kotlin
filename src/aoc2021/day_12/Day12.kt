package aoc2021.day_12

import utils.AocTask
import utils.extensions.safeAdd
import kotlin.time.measureTime

object Day12: AocTask() {

    override fun executeTask() {
        measureTime {
            val caveSystem = testInput.toCaveSystem()
            println("Cave distinct paths = ${caveSystem.distinctPaths().size}")
            println("Cave distinct paths with 1 allowed double visit = ${caveSystem.distinctPaths(1).size}")
        }.let { println("Test part took $it\n") }

        measureTime {
            val caveSystem = input.toCaveSystem()
            println("Cave distinct paths = ${caveSystem.distinctPaths().size}")
        }.let { println("Part 1 took $it\n") }

        measureTime {
            val caveSystem = input.toCaveSystem()
            println("Cave distinct paths with 1 allowed double visit = ${caveSystem.distinctPaths(1).size}")
        }.let { println("Part 2 took $it\n") }
    }

    private fun String.toCaveSystem(): CaveSystem = CaveSystem(
        mutableMapOf<String, MutableSet<String>>().apply {
            lines().forEach { line ->
                val (cave, neighbour) = line.split('-')
                safeAdd(cave to neighbour)
            }
        }
    )
}