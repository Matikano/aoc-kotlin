package aoc2022.day_11

import aoc2022.day_11.Operation.Companion.toOperation
import utils.AocTask
import utils.extensions.lcm
import utils.extensions.numsInt
import utils.extensions.numsLong
import utils.extensions.tail
import kotlin.time.measureTime

object Day11: AocTask() {

    private const val ROUNDS = 20
    private const val PART_2_ROUNDS = 10000

    override fun executeTask() {
        measureTime {
            val monkeys = testInput.toMonkeys()
            monkeys.processGame(ROUNDS)
            println("Monkey business level = ${monkeys.monkeyBusinessLevel}")
            val monkeys2 = testInput.toMonkeys()
            monkeys2.processGame(PART_2_ROUNDS, keepWorryLevelLow = false)
            println(monkeys2.map { it.itemInspectionCount })
            println("Monkey business level part 2 = ${monkeys2.monkeyBusinessLevel}")
        }.let { println("Test part took $it\n") }

        measureTime {
            val monkeys = input.toMonkeys()
            monkeys.processGame(ROUNDS)
            println("Monkey business level = ${monkeys.monkeyBusinessLevel}")
        }.let { println("Part 1 took $it\n") }

        measureTime {
            val monkeys = input.toMonkeys()
            monkeys.processGame(PART_2_ROUNDS, false)
            println(monkeys.map { it.itemInspectionCount })
            println("Monkey business level = ${monkeys.monkeyBusinessLevel}")
        }.let { println("Part 2 took $it\n") }
    }

    private val List<Monkey>.monkeyBusinessLevel: Long
        get() = sortedByDescending { it.itemInspectionCount }
            .take(2)
            .fold(1L) { acc, monkey -> acc * monkey.itemInspectionCount }

    private fun List<Monkey>.processGame(rounds: Int, keepWorryLevelLow: Boolean = true) {
        val lcm = map { it.modulus.toLong() }.lcm()
        repeat(rounds) {
            forEach { monkey ->
                monkey.processRound(keepWorryLevelLow).forEach { (item, index) ->
                    this[index].addItem(item.mod(lcm.toLong()))
                }
            }
        }
    }

    private fun String.toMonkeys(): List<Monkey> =
        lines().windowed(6, 7)
            .map { it.joinToString("\n").toMonkey() }

    private fun String.toMonkey(): Monkey {
        val (items, operationCode, modulus, positiveTest, negativeTest) = lines().tail().toList()
        val (operationString, operand) = operationCode.split(" ").takeLast(2)

        return Monkey(
            items = items.numsLong().map { it.toLong() }.toMutableList(),
            operation = operationString.toOperation(),
            operand = operand.toLongOrNull()?.toLong(),
            modulus = modulus.numsInt().first(),
            positiveTestIndex = positiveTest.numsInt().first(),
            negativeTestIndex = negativeTest.numsInt().first()
        )
    }
}