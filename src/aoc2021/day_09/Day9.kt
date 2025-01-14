package aoc2021.day_09

import utils.AocTask
import utils.models.Grid.Companion.toDigitGrid
import kotlin.time.measureTime

object Day9: AocTask() {

    override fun executeTask() {
        measureTime {
            val smokeBasin = testInput.toSmokeBasin()
            println("Sum of low points risk levels = ${smokeBasin.sumOfRiskLevels}")
            println("Product of 3 largest basin sizes = ${smokeBasin.productOfThreeLargestBasins}")
        }.let { println("Test part took $it\n") }

        with(input.toSmokeBasin()) {
            measureTime {
                println("Sum of low points risk levels = $sumOfRiskLevels")
            }.let { println("Part 1 took $it\n") }

            measureTime {
                println("Product of 3 largest basin sizes = $productOfThreeLargestBasins")
            }.let { println("Part 2  took $it\n") }
        }
    }

    private fun String.toSmokeBasin(): SmokeBasin = SmokeBasin(toDigitGrid())
}