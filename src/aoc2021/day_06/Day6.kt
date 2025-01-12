package aoc2021.day_06

import utils.AocTask
import utils.extensions.numsInt
import kotlin.time.measureTime

object Day6: AocTask() {

    private const val DAYS_TO_ELAPSE = 80
    private const val DAYS_TO_ELAPSE_2 = 256

    override fun executeTask() {
        measureTime {
            val lanternFishes = testInput.toLanternFishes()
            println("Fishes after $DAYS_TO_ELAPSE days = ${lanternFishes.lanternFishCountAfter(DAYS_TO_ELAPSE)}")
            println("Fishes after $DAYS_TO_ELAPSE_2 days = ${lanternFishes.lanternFishCountAfter(DAYS_TO_ELAPSE_2)}")
        }.let { println("Test part took $it\n") }

        measureTime {
            val lanternFishes = input.toLanternFishes()
            println("Fishes after $DAYS_TO_ELAPSE days = ${lanternFishes.lanternFishCountAfter(DAYS_TO_ELAPSE)}")
        }.let { println("Part 1 took $it\n") }

        measureTime {
            val lanternFishes = input.toLanternFishes()
            println("Fishes after $DAYS_TO_ELAPSE_2 days = ${lanternFishes.lanternFishCountAfter(DAYS_TO_ELAPSE_2)}")
        }.let { println("Part 2 took $it\n") }
    }

    private fun List<Int>.lanternFishCountAfter(days: Int) =
        sumOf { LanternFishCount(LanternFishCount.LanternFishState(days, it)) }

    private fun String.toLanternFishes(): List<Int> = numsInt()
}