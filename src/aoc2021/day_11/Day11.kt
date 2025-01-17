package aoc2021.day_11

import utils.AocTask
import utils.models.Grid.Companion.toDigitGrid
import kotlin.time.measureTime

object Day11: AocTask() {

    override fun executeTask() {
        measureTime {
            val octopuses = testInput.toOctopuses()
            println("Flashes after 100 step = ${octopuses.makeSteps(100).sum()}")
            println("Steps for 1st synchronization = ${octopuses.findStepWhenAllFlash()}")
        }.let { println("Test part took $it\n") }

        measureTime {
            val octopuses = input.toOctopuses()
            println("Flashes after 100 step = ${octopuses.makeSteps(100).sum()}")
        }.let { println("Part 1 took $it\n") }

        measureTime {
            val octopuses = input.toOctopuses()
            println("Steps for 1st synchronization = ${octopuses.findStepWhenAllFlash()}")
        }.let { println("Part 2 took $it\n") }
    }

    private fun String.toOctopuses(): OctopusGrid = OctopusGrid(toDigitGrid())
}