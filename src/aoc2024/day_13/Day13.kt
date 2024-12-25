package aoc2024.day_13

import utils.AocTask
import utils.extensions.numsLong
import kotlin.time.measureTime

object Day13: AocTask() {

    private const val BUTTON_A_COST = 3L
    private const val BUTTON_B_COST = 1L
    private const val PART_2_PRIZE_CORRECTION = 10000000000000L

    override fun executeTask() {
        with(inputToString().toClawMachineList()) {
            measureTime {
                // Part 1
                println("Sum of tokens = ${sumOfTokens()}")
            }.let { println("Part 1 took $it\n") }

            measureTime {
                // Part 2
                val correctedList = map { it.copy(prize = it.prize + PART_2_PRIZE_CORRECTION) }
                println("Sum of tokens = ${correctedList.sumOfTokensPart2()}")
            }.let { println("Part 2 took $it\n") }
        }
    }

    private operator fun Pair<Long, Long>.plus(other: Long) =
        first + other to second + other

    private fun List<ClawMachine>.sumOfTokens(): Long =
        sumOf { it.cheapestCombination ?: 0L }

    private fun List<ClawMachine>.sumOfTokensPart2(): Long =
        sumOf { it.cheapestCombinationPart2 ?: 0L }

    private fun String.toClawMachineList(): List<ClawMachine> = buildList {
        var buttonA: Button? = null
        var buttonB: Button? = null

        for (line in split('\n')) {
            when {
                line.contains('A') -> {
                    val (dx, dy) = line.numsLong()
                    buttonA = Button(dx, dy, BUTTON_A_COST)
                }

                line.contains('B') -> {
                    val (dx, dy) = line.numsLong()
                    buttonB = Button(dx, dy, BUTTON_B_COST)
                }

                line.contains("Prize") -> {
                    val (prizeX, prizeY) = line.numsLong()
                    add(
                        ClawMachine(
                            buttonA = buttonA!!,
                            buttonB = buttonB!!,
                            prize = prizeX to prizeY
                        )
                    )
                }

                else -> Unit
            }
        }
    }
}