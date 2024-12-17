package aoc2024.day_07

import utils.AocTask
import utils.extensions.*
import kotlin.time.measureTime

object Day7: AocTask {

    override val fileName: String
        get() = "src/aoc2024/day_07/input.txt"

    override fun executeTask() {
        println("-------------------------------------")
        println("AoC 2024 Task ${this.javaClass.simpleName}")
        println()

        with(readToList()) {
            // Part 1
            measureTime {
                println("Sum of valid equations = ${sumOfValid()}")
            }.let { println("Part one took $it") }

            // Part 2
            measureTime {
                println("Sum of valid equations with concatenation = ${sumOfValidWithConcat()}")
            }.let { println("Part two took $it") }
        }
    }

    private fun readToList(): List<Equation> =
        readFileToList().map { line ->
            val numbers = line.numsLong()
            Equation(
                expectedValue = numbers.head(),
                operands = numbers.tail().toList()
            )
        }

    private fun canObtain(target: Long, operands: List<Long>): Boolean =
        when {
            operands.size == 1 -> operands.head() == target
            else -> {
                val last = operands.last()
                val lastDropped = operands.dropLast(1)
                target % last == 0L && canObtain(target / last, lastDropped) ||
                        target > last && canObtain(target - last, lastDropped)
            }
        }

    private fun canObtainWithConcat(target: Long, operands: List<Long>): Boolean =
        when {
            operands.size == 1 -> operands.head() == target
            else -> {
                val last = operands.last()
                val lastDropped = operands.dropLast(1)
                val targetString = target.toString()
                val lastString = last.toString()
                (target % last == 0L && canObtainWithConcat(target / last, lastDropped)) ||
                        (target > last && canObtainWithConcat(target - last, lastDropped)) ||
                        (targetString.length > lastString.length && targetString.endsWith(last.toString()) &&
                            canObtainWithConcat(targetString.dropLast(lastString.length).toLong(), lastDropped))
            }
        }

    private val Equation.isValid: Boolean
        get() = canObtain(expectedValue, operands)

    private val Equation.isValidWithConcat: Boolean
        get() = canObtainWithConcat(expectedValue, operands)

    private fun List<Equation>.sumOfValid(): Long =
        filter { it.isValid }
            .sumOf { it.expectedValue }

    private fun List<Equation>.sumOfValidWithConcat(): Long =
        filter { it.isValidWithConcat }
            .sumOf { it.expectedValue }
}
