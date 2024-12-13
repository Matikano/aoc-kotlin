package aoc2024.day_13

import aoc2024.AocTask
import kotlin.time.measureTime

object Day13: AocTask {

    private const val BUTTON_A_COST = 3L
    private const val BUTTON_B_COST = 1L
    private const val PART_2_PRIZE_CORRECTION = 10000000000000L

    private const val COORDINATES_SEPARATOR = ", "
    private const val LABEL_SEPARATOR = ": "
    private const val BUTTON_VALUE_SEPARATOR = '+'
    private const val PRIZE_VALUE_SEPARATOR = '='

    override val fileName: String
        get() = "src/aoc2024/day_13/input.txt"

    override fun executeTask() {
        println("-------------------------------------")
        println("AoC 2024 Task ${this.javaClass.simpleName}")
        println()

        with(readFileToString().toClawMachineList()) {
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
                    val (dx, dy) = line.toDxDy()
                    buttonA = Button(dx, dy, BUTTON_A_COST)
                }

                line.contains('B') -> {
                    val (dx, dy) = line.toDxDy()
                    buttonB = Button(dx, dy, BUTTON_B_COST)
                }

                line.contains("Prize") -> {
                    add(
                        ClawMachine(
                            buttonA = buttonA!!,
                            buttonB = buttonB!!,
                            prize = line.toPrize()
                        )
                    )
                }

                line.isEmpty() -> Unit
            }
        }
    }

    private fun String.toPrize(): Pair<Long, Long> =
        substringAfter(LABEL_SEPARATOR)
            .split(COORDINATES_SEPARATOR)
            .map { it.substringAfter(PRIZE_VALUE_SEPARATOR).toLong() }
            .let { it.first() to it.last() }

    private fun String.toDxDy(): Pair<Long, Long> =
        substringAfter(LABEL_SEPARATOR)
            .split(COORDINATES_SEPARATOR)
            .map { it.substringAfter(BUTTON_VALUE_SEPARATOR).toLong() }
            .let { it.first() to it.last() }
}