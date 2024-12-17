package aoc2024.day_17

import utils.AocTask
import utils.extensions.numsInt
import kotlin.time.measureTime

object Day17: AocTask {

    private val testInput: String = """
                Register A: 2024
                Register B: 0
                Register C: 0
                
                Program: 0,3,5,4,3,0
    """.trimIndent()

    override val fileName: String
        get() = "src/aoc2024/day_17/input.txt"

    override fun executeTask() {
        println("-------------------------------------")
        println("AoC 2024 Task ${this.javaClass.simpleName}")
        println()

        val (computer, program) = readFileToString().toComputer()

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