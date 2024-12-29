package aoc2023.day_18

import aoc2023.day_18.Day18.toEdgePositions
import utils.AocTask
import utils.extensions.calculateArea
import utils.models.Direction
import utils.models.Grid
import utils.models.GridCell
import utils.models.Position
import kotlin.math.absoluteValue
import kotlin.time.measureTime

object Day18: AocTask() {

    override fun executeTask() {
        measureTime {
            val digPlan = testInput.toDigPlan()
            val edgePositions = digPlan.toEdgePositions()
            val cornerPositions = digPlan.toCornerPositions()

            val areaCalculatedFilledCount = calculateArea(cornerPositions, edgePositions.size.toLong())
            println("Count of by area formula = $areaCalculatedFilledCount")

            val correctEdgePositions = digPlan.toCorrectEdgePositions()
            val correctCornerPositions = digPlan.toCorrectCornerPositions()
            val correctedArea = calculateArea(correctCornerPositions, correctEdgePositions.size.toLong())
            println("Count of filled correct positions = $correctedArea")
        }.let { println("Test part took $it\n") }

        measureTime {
            val digPlan = input.toDigPlan()
            val corners = digPlan.toCornerPositions()
            val edgePositions = digPlan.toEdgePositions()
            println("Count of filled positions = ${calculateArea(corners, edgePositions.size.toLong())}")
        }.let { println("Part 1 took $it\n") }

        measureTime {
            val digPlan = input.toDigPlan()
            val edgePositions = digPlan.toCorrectEdgePositions()
            val cornerPositions = digPlan.toCorrectCornerPositions()

            val areaCalculatedFilledCount = calculateArea(cornerPositions, edgePositions.size.toLong())

            println("Count of filled correct positions = $areaCalculatedFilledCount")
        }.let { println("Part 2 took $it\n") }
    }

    private fun List<DigPlanEntry>.toEdgePositions(): List<Position> = buildList {
        var currentPosition = Position(0, 0)
        add(currentPosition)
        this@toEdgePositions.forEach { entry ->
            val (direction, length) = entry.instruction
            repeat(length) {
                currentPosition += direction
                add(currentPosition)
            }
        }
    }

    private fun List<DigPlanEntry>.toCornerPositions(): List<Position> = buildList {
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

    private fun List<DigPlanEntry>.toCorrectEdgePositions(): List<Position> = buildList {
        var currentPosition = Position(0, 0)
        add(currentPosition)
        this@toCorrectEdgePositions.forEach { entry ->
            val (direction, length) = entry.correctInstruction
            repeat(length) {
                currentPosition += direction
                add(currentPosition)
            }
        }
    }

    private fun List<DigPlanEntry>.toCorrectCornerPositions(): List<Position> = buildList {
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