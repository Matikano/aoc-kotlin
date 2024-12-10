package aoc2024.day_07

import aoc2024.AocTask
import utils.head
import utils.repeatingPermutations
import utils.tail
import kotlin.time.measureTime

object Day7: AocTask {

    private const val EQUATION_SEPARATOR = ": "
    private const val OPERANDS_SEPARATOR = " "

    override val fileName: String
        get() = "src/aoc2024/day_07/input.txt"

    override fun executeTask() {
        println("-------------------------------------")
        println("AoC 2024 Task ${this.javaClass.simpleName}")
        println()

        val basicOperators = Operator.entries - Operator.CONCATENATE
        val extendedOperators = Operator.entries
        with(readToList()) {
            // Part 1
            measureTime {
                println("Sum of valid equations = ${sumOfValid(basicOperators)}")
            }.let { println("Part one took $it") }

            // Part 2
            measureTime {
                println("Sum of valid equations with concatenation = ${sumOfValid(extendedOperators)}")
            }.let { println("Part two took $it") }
        }
    }

    private fun readToList(): List<Equation> =
        mutableListOf<Equation>().apply {
            readFileByLines { line ->
               val (testValue, operandsChain) = line.split(EQUATION_SEPARATOR)
                add(
                    Equation(
                        expectedValue = testValue.toLong(),
                        operands = operandsChain.split(OPERANDS_SEPARATOR).map { it.toLong() }
                    )
                )
            }
        }

    private fun Equation.isValid(operators: List<Operator>): Boolean =
        operators
            .repeatingPermutations(operands.size - 1)
            .any { evaluateOperators(it) }

    private fun Equation.evaluateOperators(operators: List<Operator>): Boolean =
        with(operands) {
            tail().foldIndexed(
                initial = head()
            ) { index, acc, value ->
                if (acc > expectedValue)
                    return false
                operators[index].calcualte(acc, value)
            } == expectedValue
        }

    private fun List<Equation>.sumOfValid(operators: List<Operator>): Long =
        filter { it.isValid(operators) }
            .sumOf { it.expectedValue }
}
