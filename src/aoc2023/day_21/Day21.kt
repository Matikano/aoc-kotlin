package aoc2023.day_21

import utils.AocTask
import utils.models.Grid.Companion.toGrid
import kotlin.time.measureTime

object Day21: AocTask() {

    private const val STEPS_TO_TAKE = 64

    override fun executeTask() {
        measureTime {
            val testGarden = Garden(testInput.toGrid())
            println("Test garden")
            testGarden.grid.print()
            println()

            println("Number of unique reachable garden plots after 6 steps = ${testGarden.uniquePlotsAfterSteps(steps = 6)}")

        }.let { println("Test part took $it\n") }

        with(Garden(input.toGrid())) {
            measureTime {
                println("Number of unique reachable garden plots after $STEPS_TO_TAKE steps = ${uniquePlotsAfterSteps(steps = STEPS_TO_TAKE)}")
            }.let { println("Part 1 took $it\n") }

            measureTime {
                println("Extended grid plots = $extendedGridPlotsCount")
            }.let { println("Part 2 took $it\n") }
        }
    }
}