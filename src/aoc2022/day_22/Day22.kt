package aoc2022.day_22

import utils.AocTask
import utils.models.Grid.Companion.toCharGrid
import kotlin.time.measureTime

object Day22: AocTask() {

    override fun executeTask() {
       measureTime {
           val (monkeyMap, instructions) = testInput.toMonkeyMapAndInstructions()
           println("Password for test data = ${monkeyMap.processInstructions(instructions)}")
       }.let { println("Test part took $it\n") }

        measureTime {
            val (monkeyMap, instructions) = input.toMonkeyMapAndInstructions()
            println("Password for test data = ${monkeyMap.processInstructions(instructions)}")
        }.let { println("Part 1 took $it\n") }

        measureTime {
            val (monkeyMap, instructions) = input.toMonkeyMapAndInstructions()
            println("Password for test data = ${monkeyMap.processInstructions(instructions, folded = true)}")
        }.let { println("Part 1 took $it\n") }
    }

    private fun String.toMonkeyMapAndInstructions(): Pair<MonkeyMap, List<Instruction>> {
        val (grid, instructionsChain) = split("\n\n")
        val instructions = Regex("(\\d+)([RL])?").findAll(instructionsChain)
            .map {
                val match = it.value
                Instruction(
                    steps = if (match.last().isDigit()) {
                        match.toInt()
                    } else match.dropLast(1).toInt(),
                    turn = when (match.last()) {
                        'R' -> Turn.RIGHT
                        'L' -> Turn.LEFT
                        else -> null
                    }
                )
            }.toList()

        return MonkeyMap(grid.toCharGrid()) to instructions
    }
}