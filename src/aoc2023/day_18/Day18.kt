package aoc2023.day_18

import utils.AocTask
import utils.extensions.calculateArea
import utils.models.Direction
import utils.models.Position
import kotlin.time.measureTime

typealias DigPlan = List<DigPlanEntry>

object Day18: AocTask() {

    override fun executeTask() {
        measureTime {
            val digPlan = testInput.toDigPlan()
            val cornerPositions = digPlan.toCornerPositions()
            println("Count of by area formula = ${calculateArea(cornerPositions, digPlan.boundaryLength())}")

            val correctCornerPositions = digPlan.toCorrectCornerPositions()
            println("Count of filled correct positions = ${calculateArea(correctCornerPositions, digPlan.correctBoundaryLength())}")
        }.let { println("Test part took $it\n") }

        measureTime {
            val digPlan = input.toDigPlan()
            val corners = digPlan.toCornerPositions()
            println("Count of filled positions = ${calculateArea(corners, digPlan.boundaryLength())}")
        }.let { println("Part 1 took $it\n") }

        measureTime {
            val digPlan = input.toDigPlan()
            val cornerPositions = digPlan.toCorrectCornerPositions()

            println("Count of filled correct positions = ${calculateArea(cornerPositions, digPlan.correctBoundaryLength())}")
        }.let { println("Part 2 took $it\n") }
    }

    private fun DigPlan.toCornerPositions(): List<Position> = buildList {
        var currentPosition = Position(0, 0)
        add(currentPosition)
        this@toCornerPositions.forEach { entry ->
            val (direction, length) = entry.instruction
            repeat(length) {
                currentPosition += direction
            }
            add(currentPosition)
        }
    }

    private fun DigPlan.boundaryLength(): Long = sumOf { it.length } + 1L

    private fun DigPlan.correctBoundaryLength(): Long = sumOf { it.correctInstruction.second.toLong() } + 1

    private fun DigPlan.toCorrectCornerPositions(): List<Position> = buildList {
        var currentPosition = Position(0, 0)
        add(currentPosition)
        this@toCorrectCornerPositions.forEach { entry ->
            val (direction, length) = entry.correctInstruction
            repeat(length) {
                currentPosition += direction
            }
            add(currentPosition)
        }
    }

    private fun String.toDigPlan(): List<DigPlanEntry> = trim().lines().map { it.toDigPlanEntry() }

    private fun String.toDigPlanEntry(): DigPlanEntry =
        split(" ").let { (dir, length, colorCode) ->
            DigPlanEntry(
                direction = dir.toDirection(),
                length = length.toInt(),
                colorCode = colorCode.drop(1).dropLast(1)
            )
        }

    private fun String.toDirection(): Direction =
        when (this) {
            "U" -> Direction.UP
            "D" -> Direction.DOWN
            "L" -> Direction.LEFT
            "R" -> Direction.RIGHT
            else -> throw IllegalArgumentException("Unsupported string $this for Direction mapping")
        }
}