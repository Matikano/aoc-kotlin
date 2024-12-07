package `2024`.day_07

import `2024`.AocTask
import kotlin.system.measureTimeMillis

object Day7: AocTask {

    const val EQUATION_SEPARATOR = ": "
    const val OPERANDS_SEPARATOR = " "

    override val fileName: String
        get() = "src/2024/day_07/input.txt"

    override fun executeTask() {
        println("-------------------------------------")
        println("AoC 2024 Task ${this.javaClass.simpleName}")
        println()

        val basicOperators = Operator.values().toList() - Operator.CONCATENATE
        val extendedOperators = Operator.values().toList()
        with(readToList()) {
            // Part 1
            measureTimeMillis {
                println("Sum of valid equations = ${sumOfValid(basicOperators)}")
            }.let { println("Part one took $it millis") }

            // Part 2
            measureTimeMillis {
                println("Sum of valid equations with concatenation = ${sumOfValid(extendedOperators)}")
            }.let { println("Part two took $it millis") }
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

    private fun <T> List<T>.generatePermutations(length: Int): List<List<T>> {
        if (length == 0) return listOf(emptyList())
        if (isEmpty()) return emptyList()

        val permutations = mutableListOf<List<T>>()

        fun List<T>.backtrack(current: List<T>) {
            if (current.size == length) {
                permutations.add(current)
                return
            }

            indices.forEach { i ->
                backtrack(current + this@backtrack[i],)
            }
        }

        backtrack(emptyList())

        return permutations
    }

    private fun Equation.isValid(operators: List<Operator>): Boolean =
        operators
            .generatePermutations(operands.size - 1)
            .any { evaluateOperators(it) }

    private fun Equation.evaluateOperators(operators: List<Operator>): Boolean {
        val firstOperand = operands.first()
        val preparedOperands = operands.drop(1)

        return preparedOperands.foldIndexed(
            initial = firstOperand
        ) { index, acc, value ->
            operators[index].calcualte(acc, value)
        } == expectedValue
    }

    private fun List<Equation>.sumOfValid(operators: List<Operator>): Long =
        filter { it.isValid(operators) }
            .sumOf { it.expectedValue }
}