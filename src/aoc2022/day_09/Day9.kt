package aoc2022.day_09

import utils.AocTask
import utils.models.Direction
import utils.models.Position
import kotlin.time.measureTime

object Day9: AocTask() {

    private const val PART_1_KNOTS = 2
    private const val PART_2_KNOTS = 10

    override fun executeTask() {
        measureTime {
            val rope = Rope(PART_1_KNOTS)
            val instructions = testInput.toInstructions()
            val tailPositions = rope.processInstructions(instructions)
            println("Unique tail positions = ${tailPositions.size}")
        }.let { println("Test part took $it\n") }

        measureTime {
            val rope = Rope(PART_1_KNOTS)
            val instructions = input.toInstructions()
            val tailPositions = rope.processInstructions(instructions)
            println("Unique tail positions = ${tailPositions.size}")
        }.let { println("Part 1 took $it\n") }

        measureTime {
            val rope = Rope(PART_2_KNOTS)
            val instructions = input.toInstructions()
            val tailPositions = rope.processInstructions(instructions)
            println("Unique tail positions = ${tailPositions.size}")
        }.let { println("Part 2 took $it\n") }
    }

    private fun Rope.processInstructions(instructions: List<Instruction>): Set<Position> {
        val queue = ArrayDeque<Instruction>(instructions)
        val tailPositions = mutableSetOf(tailPosition)

        while (queue.isNotEmpty()) {
            val instruction = queue.removeFirst()

            repeat(instruction.stepCount) {
                move(instruction.direction)
                tailPositions.add(tailPosition)
            }
        }

        return tailPositions
    }

    private fun String.toDirection(): Direction =
        when (this) {
            "U" -> Direction.UP
            "R" -> Direction.RIGHT
            "L" -> Direction.LEFT
            "D" -> Direction.DOWN
            else -> throw IllegalArgumentException("Unsupported Direction mapping for $this")
        }

    private fun String.toInstruction(): Instruction {
        val (dirChar, stepCount) = split(" ")
        return Instruction(
            direction = dirChar.toDirection(),
            stepCount = stepCount.toInt()
        )
    }

    private fun String.toInstructions(): List<Instruction> = lines().map { it.toInstruction() }
}