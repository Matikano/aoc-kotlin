package aoc2024.day_22

import utils.AocTask
import utils.extensions.numsLong
import kotlin.time.measureTime

object Day22: AocTask() {

    private const val PRICE_CHANGE_SEQUENCE_SIZE = 4
    private const val SECRET_NUMBERS_COUNT = 2000
    private const val PRUNE_VALUE: Long = 0xFFFFFF // 24 ones in binary is 0xFFFFFF in hexadecimal
    private const val STEP_1_SHIFT = 6
    private const val STEP_2_SHIFT = 5
    private const val STEP_3_SHIFT = 11

    private val sequenceToTotal: MutableMap<List<Long>, Long> = mutableMapOf()

    override fun executeTask() {
        println("-------------------------------------")
        println("AoC 2024 Task ${this.javaClass.simpleName}")
        println()

        measureTime {
            testInput.numsLong().sumOf {
                it.getNthSecretNumber(SECRET_NUMBERS_COUNT)
            }.let { sumOfTestValues ->
                println("Sum of test values of 2000th secret numbers = $sumOfTestValues")
            }
        }.let { println("Test of Part 1 took $it") }

        measureTime {
            inputToString().numsLong().sumOf {
                it.getNthSecretNumber(SECRET_NUMBERS_COUNT)
            }.let { sum ->
                println("Sum of part 1 values of 2000th secret numbers = $sum")
            }
        }.let { println("Part 1 took $it") }

        measureTime {
            val initialSecretNumbers = inputToString().numsLong()
            val prices = initialSecretNumbers.map { it.toListOfSecretNumbers(SECRET_NUMBERS_COUNT).toPrices() }
            prices.forEach { it.getBananasForEachSequence() }

            val maximumBananas = sequenceToTotal.values.max()
            println("Maximum bananas to be bought = $maximumBananas")
        }.let { println("Part 2 took $it") }
    }

    private fun List<Long>.getBananasForEachSequence() {
        val seen = mutableSetOf<List<Long>>()
        windowed(PRICE_CHANGE_SEQUENCE_SIZE + 1).forEach { (a, b, c, d, e) ->
            val sequence = listOf(b - a, c - b, d - c, e - d)
            if (sequence in seen)
                return@forEach
            seen.add(sequence)
            if (sequence !in sequenceToTotal)
                sequenceToTotal[sequence] = 0
            sequenceToTotal[sequence] = sequenceToTotal[sequence]!! + e
        }
    }

    private fun Long.newSecret(): Long = firstStep().secondStep().thirdStep()

    private fun Long.getNthSecretNumber(index: Int): Long = toListOfSecretNumbers(index).last()

    private fun Long.toListOfSecretNumbers(count: Int): List<Long> = buildList {
        var secret = this@toListOfSecretNumbers
        add(secret)
        repeat(count) {
            secret = secret.newSecret()
            add(secret)
        }
    }

    private fun Long.mix(other: Long): Long = this xor other

    private fun Long.prune(): Long = this and PRUNE_VALUE

    private fun Long.firstStep(): Long = mix(this shl STEP_1_SHIFT).prune()

    private fun Long.secondStep(): Long = mix(this shr STEP_2_SHIFT).prune()

    private fun Long.thirdStep(): Long = mix(this shl STEP_3_SHIFT).prune()

    private fun Long.lastDigit(): Long = this % 10

    private fun List<Long>.toPrices(): List<Long> = map { it.lastDigit() }
}