package aoc2022.day_10

import utils.AocTask
import utils.extensions.numsInt
import kotlin.time.measureTime

object Day10: AocTask() {

    override fun executeTask() {
        measureTime {
            val computer = Computer()
            val instructions = testInput.toInstructions()
            instructions.forEach(computer::process)
            println("Signals = ${computer.signalStrengths}")
            println("Signals sum = ${computer.signalStrengthsSum}")
            println("CRT image:\n${computer.CRT}")
        }.let { println("Test part took $it\n") }

        measureTime {
            val computer = Computer()
            val instructions = input.toInstructions()
            instructions.forEach(computer::process)
            println("Signals = ${computer.signalStrengths}")
            println("Signals sum = ${computer.signalStrengthsSum}")
        }.let { println("Part 1 took $it\n") }

        measureTime {
            val computer = Computer()
            val instructions = input.toInstructions()
            instructions.forEach(computer::process)
            println("CRT image:\n${computer.CRT}")
        }.let { println("Part 2 took $it\n") }
    }

    private fun String.toInstructions() = lines().map { it.toInstruction() }

    private fun String.toInstruction(): Instruction =
        when {
            startsWith("noop") -> Instruction.Noop
            else -> Instruction.AddX(numsInt().first())
        }
}