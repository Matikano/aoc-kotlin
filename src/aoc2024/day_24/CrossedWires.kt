package aoc2024.day_24

import utils.extensions.*
import kotlin.random.Random

data class CrossedWires(
    private val input: String
) {

    private val gates = mutableMapOf<String, Boolean>()
    private val instructions = mutableListOf<Instruction>()

    init {
        input.readData()
    }

    private val outputGates
        get() = instructions.map { it.outputGate }

    private fun reset() {
        gates.clear()
        instructions.clear()
        input.readData()
    }

    private fun resetWithSwaps(swaps: Set<Pair<String, String>>) = reset().also { swaps.forEach(::swapOutputs) }

    private operator fun Map<String, Boolean>.contains(pair: Pair<String, String>): Boolean =
        pair.first in this && pair.second in this

    val output: Long
        get() = decimalValueOfGatesStartingWith('z')

    private val xInput: Long
        get() = decimalValueOfGatesStartingWith('x')

    private val yInput: Long
        get() = decimalValueOfGatesStartingWith('y')

    val expectedOutput: Long
        get() = xInput + yInput

    private val wrongBitsIndices: List<Int>
        get() = indicesOfDifferentBits(output, expectedOutput)

    private val areInstructionsValid: Boolean
        get() = instructions.all { it.inputGates in gates }

    private fun decimalValueOfGatesStartingWith(firstChar: Char): Long =
        gates.keys
            .filter { it.startsWith(firstChar) }
            .sortedDescending()
            .map { gates[it]!!.toInt() }
            .joinToString("")
            .toLong(radix = 2)

    fun processInstructions() {
        var nextInstruction: Instruction
        val queue = mutableListOf(*instructions.toTypedArray())

        while (queue.isNotEmpty()) {
            nextInstruction = queue.first { it.inputGates in gates }
            queue.remove(nextInstruction)
            nextInstruction.process()
        }
    }

    private fun Instruction.process() {
        gates[outputGate] = operation.process(gates[inputGates.first]!!, gates[inputGates.second]!!)
    }

    private fun String.readData() =
        lines().forEach { line ->
            when {
                line.contains(GATE_SPLIT) -> line.storeGate()
                line.contains(ARROW) -> line.storeInstruction()
            }
        }

    private fun String.storeInstruction() {
        //   aaa    XOR        bbb     ->  z00
        val (first, operation, second, _,  output) = split(INPUT_SPLIT)

        instructions.add(
            Instruction(
                inputGates = first to second,
                operation = BoolOperation.valueOf(operation),
                outputGate = output
            )
        )
    }

    private fun String.storeGate() {
        val (key, value) = split(GATE_SPLIT)
        gates[key] = value.toInt() == 1
    }

    // This is not optimal nor always working, sometimes it can find a wrong answer
    fun getOutputsToSwap(): String {
        val swaps = mutableSetOf<Pair<String, String>>()
        val wrongSwaps = mutableSetOf<Pair<String, String>>()

        while (swaps.size < MAX_SWAPS) {
            var best: SwapScore? = null
            lateinit var outputsToSwap: Pair<String, String>
            for (firstOutput in outputGates) {
                for (secondOutput in outputGates) {
                    outputsToSwap = firstOutput to secondOutput

                    if (outputsToSwap.first == outputsToSwap.second ||
                        outputsToSwap in wrongSwaps)
                        continue

                    resetWithSwaps(swaps)
                    swapOutputs(outputsToSwap)

                    if (!areInstructionsValid) {
                        wrongSwaps.add(outputsToSwap)
                        continue
                    }

                    val testedWrongIndices =
                        testOnDifferentInputs(setOf(*swaps.toTypedArray()) + (outputsToSwap))

                    if (best == null || (best.wrongBitsIndices.minOrMaxInt() < testedWrongIndices.minOrMaxInt())) {
                        best = SwapScore(
                            wrongBitsIndices = testedWrongIndices,
                            swappedOutputs = outputsToSwap
                        )
                    }
                }
            }

            if (best != null)
                swaps.add(outputsToSwap)
            else {
                // If best was not found, then outputsToSwap are wrong
                swaps.remove(outputsToSwap)
                wrongSwaps.add(outputsToSwap)
                wrongSwaps.add(outputsToSwap.reversed())
            }
        }

        return swaps
            .flattenPairsToSet()
            .sorted()
            .joinToString(",")
            .also { outputChain ->
                resetWithSwaps(swaps)
                processInstructions()

                println("Expected output = $expectedOutput, in binary = ${expectedOutput.toBinaryString()}")
                println("Actual output = $output, in binary = ${output.toBinaryString()}")
                println("Difference = ${expectedOutput xor output}, in binary = ${(expectedOutput xor output).toBinaryString()}")
                println("Swaps = $swaps")
                println("Output chain (sorted gate names to swap) = $outputChain")
            }
    }

    private fun testOnDifferentInputs(swaps: Set<Pair<String, String>>): List<Int> =
        buildSet {
            repeat(10) {
                resetWithSwaps(swaps)
                randomizeInputs()
                processInstructions()
                addAll(wrongBitsIndices)
            }
        }.toList()

    private fun randomizeInputs() {
        gates.keys
            .filter { gate -> gate.startsWith("x") || gate.startsWith("y") }
            .forEach { gate -> gates[gate] = Random.nextBoolean() }
    }

    private fun swapOutputs(outputs: Pair<String, String>) {
        with(instructions) {
            val (firstOutput, secondOutput) = outputs
            val first = first { it.outputGate == firstOutput }
            val second = first { it.outputGate == secondOutput }

            val indexOfFirst = indexOf(first)
            val indexOfSecond = indexOf(second)

            val firstSwapped = first.copy(outputGate = secondOutput)
            val secondSwapped = second.copy(outputGate = firstOutput)

            this[indexOfFirst] = firstSwapped
            this[indexOfSecond] = secondSwapped
        }
    }

    companion object {
        private const val ARROW = "->"
        private const val INPUT_SPLIT = " "
        private const val GATE_SPLIT = ": "
        private const val MAX_SWAPS = 4
    }
}
