package aoc2021.day_02

import utils.AocTask
import utils.models.Direction
import utils.models.Position
import kotlin.time.measureTime

object Day2: AocTask() {

    override fun executeTask() {
        measureTime {
            var position = Position.topLeftCorner()
            val instructions = testInput.toInstructions()
            instructions.forEach { (dir, value) ->
                repeat(value) {
                    position += dir
                }
            }
            println("Result = ${position.colIndex * position.rowIndex}")
        }.let { println("Test part took $it\n") }

        measureTime {
            var position = Position.topLeftCorner()
            val instructions = input.toInstructions()
            instructions.forEach { (dir, value) ->
                repeat(value) { position += dir }
            }
            println("Result = ${position.colIndex * position.rowIndex}")
        }.let { println("Part 1 took $it\n") }

        measureTime {
            var position = Position.topLeftCorner()
            val instructions = input.toInstructions()
            var aim = 0
            instructions.forEach { (dir, value) ->
                when (dir) {
                    Direction.UP -> aim -= value
                    Direction.DOWN -> aim += value
                    Direction.RIGHT -> {
                        position = position.copy(
                            colIndex = position.colIndex + value,
                            rowIndex = position.rowIndex + aim * value
                        )
                    }
                    else -> Unit
                }
            }
            println("Result = ${position.colIndex * position.rowIndex}")
        }.let { println("Part 2 took $it\n") }
    }

    private fun String.toInstructions(): List<Instruction> = lines().map { it.toInstruction() }

    private fun String.toInstruction(): Instruction {
        val (dir, value) = split(" ")
        return Instruction (
            direction = when (dir) {
                "forward" -> Direction.RIGHT
                "up" -> Direction.UP
                "down" -> Direction.DOWN
                else -> throw IllegalArgumentException("Unsupported value $this for Direction conversion")
            },
            value = value.toInt()
        )
    }
}