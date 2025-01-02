package aoc2022.day_11

import aoc2022.day_11.Operation.Companion.toOperation
import utils.AocTask
import utils.extensions.numsInt
import utils.extensions.tail

object Day11: AocTask() {

    override fun executeTask() {

    }

    private fun List<Monkey>.processRound() {

    }

    private fun String.toMonkeys(): List<Monkey> = split("\n\n").map { it.toMonkey() }

    private fun String.toMonkey(): Monkey {
        val (items, operationCode, modulus, positiveTest, negativeTest) = lines().tail().toList()
        val (operationString, operand) = operationCode.split(" ").takeLast(2)

        return Monkey(
            items = items.numsInt().toMutableList(),
            operation = operationString.toOperation(),
            operand = operand.toIntOrNull(),
            modulus = modulus.numsInt().first(),
            positiveTestIndex = positiveTest.numsInt().first(),
            negativeTestIndex = negativeTest.numsInt().first()
        )
    }
}