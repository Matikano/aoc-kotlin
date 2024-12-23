package aoc2024.day_17

import utils.AocTask
import utils.extensions.numsInt
import kotlin.time.measureTime

object Day17: AocTask() {

    override fun executeTask() {
        println("-------------------------------------")
        println("AoC 2024 Task ${this.javaClass.simpleName}")
        println()

        val (computer, program) = inputToString().toComputer()

        measureTime {
            val output = computer.runProgram(program)

            println("Part 1 computer output = $output")
        }.let { println("Part 1 took $it") }

        measureTime {
            val registerA = computer.findAForSelfReturn(program)

            println("Part 2 registerA value for returning input = $registerA")
        }.let { println("Part 2 took $it") }
    }

    private fun String.toComputer(): Pair<Computer, List<Int>> {
        val numbers = numsInt()
        val (a, b, c) = numbers.take(3).map { it.toLong() }
        val programInput = numbers.drop(3)

        val computer = Computer(
            registerA = a,
            registerB = b,
            registerC = c,
        )

        return computer to programInput
    }

}