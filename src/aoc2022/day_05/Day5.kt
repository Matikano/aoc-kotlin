package aoc2022.day_05

import utils.AocTask
import utils.extensions.numsInt
import utils.extensions.transpose
import kotlin.time.measureTime

typealias Stacks = MutableList<String>

object Day5: AocTask() {

    override fun executeTask() {
        measureTime {
            val (stacks, instructions) = testInput.toStacksAndInstructions()
            println("Stacks:\n$stacks")

            instructions.forEach { stacks.processInstruction(it) }

            println("Stacks after instructions = $stacks")
            println("Message = ${stacks.endCode()}")
        }.let { println("Test part took $it\n") }

        measureTime {
            val (stacks, instructions) = input.toStacksAndInstructions()
            println("Stacks:\n$stacks")

            instructions.forEach { stacks.processInstruction(it) }

            println("Stacks after instructions = $stacks")
            println("Message = ${stacks.endCode()}")
        }.let { println("Part 1 took $it\n") }

        measureTime {
            val (stacks, instructions) = input.toStacksAndInstructions()
            println("Stacks:\n$stacks")

            instructions.forEach { stacks.processInstruction(it, keepOrder = true) }

            println("Stacks after instructions = $stacks")
            println("Message = ${stacks.endCode()}")
        }.let { println("Part 2 took $it\n") }
    }

    private fun String.toStacksAndInstructions(): Pair<Stacks, List<Instruction>> {
        val (stacksBlock, instructionsBlock) = split("\n\n")
        return stacksBlock.toStacks() to instructionsBlock.toInstructions()
    }

    private fun String.toStacks(): MutableList<String> =
        transpose()
            .lines()
            .filter { it.any { it.isDigit() } }
            .map { it.trim().dropLast(1).reversed() }
            .toMutableList()

    private fun String.toInstructions(): List<Instruction> = lines().map { it.toInstruction() }

    private fun String.toInstruction(): Instruction {
        val (count, from, to) = numsInt()
        return Instruction(
            count = count,
            fromIndex = from - 1,
            toIndex = to - 1
        )
    }

    private fun Stacks.processInstruction(
        instruction: Instruction,
        keepOrder: Boolean = false
    ) {
        this[instruction.toIndex] += this[instruction.fromIndex]
            .takeLast(instruction.count)
            .let { if (keepOrder) it else it.reversed() }
        this[instruction.fromIndex] = this[instruction.fromIndex].dropLast(instruction.count)
    }

    private fun Stacks.endCode(): String = map { it.last() }.joinToString("")
}