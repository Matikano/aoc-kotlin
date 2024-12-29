package aoc2023.day_18

import utils.AocTask
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
            val grid = edgePositions.toGrid()
            println("Edges grid")
            grid.print()
            val gridFilled = grid.fillInside()
            println("Filled grid")
            gridFilled.print()

            println("Count of filled positions = ${gridFilled.filledCount()}")
        }.let { println("Test part took $it\n") }

        measureTime {
            val digPlan = input.toDigPlan()
            val edgePositions = digPlan.toEdgePositions()
            val grid = edgePositions.toGrid()
            println("Grid bounds = " +
                    "rows: ${grid.cells.minOf { it.position.colIndex } to grid.cells.maxOf { it.position.colIndex }}" +
                    "cols : ${grid.cells.minOf { it.position.rowIndex } to grid.cells.maxOf { it.position.rowIndex }}")
            val gridFilled = grid.fillInside()
            println("Count of filled positions = ${gridFilled.filledCount()}")
        }.let { println("Part 1 took $it\n") }
    }

    private fun Grid<Char>.fillInside(): Grid<Char> {
        val edgePositions = cells.filter { it.value == '#' }.map { it.position }
        val startPosition = Position(0, 0)
        val seen = mutableSetOf(startPosition)
        val queue = ArrayDeque(setOf(startPosition))

        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            val possibleNewPositions = Direction.validDirections.map { dir ->
                current + dir
            }.filter { isInBounds(it) && it !in edgePositions && it !in seen }

            possibleNewPositions.forEach { newPosition ->
                seen.add(newPosition)
                queue.add(newPosition)
            }
        }

        val filledPositions = cells.map { it.position }.toSet() - seen

        return copy(
            cells = cells.map { cell ->
                if (cell.position in filledPositions) cell.copy(value = '#')
                else cell
            }
        )
    }

    private fun Grid<Char>.filledCount() = cells.count { it.value == '#' }

    private fun List<Position>.toGrid(): Grid<Char> {
        val maxRowIndex = maxOf { it.rowIndex }
        val minRowIndex = minOf { it.rowIndex }
        val maxColIndex = maxOf { it.colIndex }
        val minColIndex = minOf { it.colIndex }
        val colNormalization = minColIndex.absoluteValue
        val rowNormalization = minRowIndex.absoluteValue
        return Grid(
            cells = (minRowIndex - 1..maxRowIndex + 1).flatMap { rowIndex ->
                (minColIndex - 1..maxColIndex + 1).map { colIndex ->
                    val position = Position(colIndex, rowIndex)
                    GridCell(
                        position = position + (colNormalization + 1 to rowNormalization + 1),
                        value = if (position in this) '#' else '.'
                    )
                }
            }
        )
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

    private fun String.toDigPlan(): List<DigPlanEntry> = trim().lines().map { it.toDigPlanEntry() }

    private fun String.toDigPlanEntry(): DigPlanEntry =
        split(" ").let { (dir, length, colorCode) ->
            DigPlanEntry(
                direction = dir.toDirection(),
                length = length.toInt(),
                colorCode = colorCode.drop(2).dropLast(1)
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