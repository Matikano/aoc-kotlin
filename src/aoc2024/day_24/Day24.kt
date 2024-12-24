package aoc2024.day_24

import utils.AocTask
import utils.extensions.toBinaryString
import kotlin.time.measureTime

object Day24: AocTask() {

    override fun executeTask() {
        println("-------------------------------------")
        println("AoC 2024 Task ${this.javaClass.simpleName}")
        println()

        measureTime {
            val wires = CrossedWires(testInput)
            wires.processInstructions()
            println("Decimal value of output from Z wires = ${wires.output}")
        }.let { println("Test part took $it") }

        measureTime {
            with(CrossedWires(inputToString())) {
                processInstructions()
                println("Decimal value of output from Z wires = $output")
            }
        }.let { println("Part 1 took $it") }

        measureTime {
            with(CrossedWires(inputToString())) {
                processInstructions()
                println("Expected output = $expectedOutput, in binary = ${expectedOutput.toBinaryString()}")
                println("Actual output = $output, in binary ${output.toBinaryString()}")
                println("Difference = ${output xor expectedOutput} in binary = ${(expectedOutput xor output).toBinaryString()}")
                println("Potential output = ${getOutputsToSwap()}")
            }
        }.let { println("Part 2 took $it") }
    }
}