package aoc2022.day_17

import aoc2022.day_17.Shape.*
import utils.AocTask
import utils.extensions.findCycle
import utils.models.Direction
import utils.models.Direction.Companion.toDirection
import utils.models.Grid
import utils.models.GridCell
import utils.models.Position
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.time.measureTime

object Day17: AocTask() {

    private const val PART_2_BLOCKS = 1000000000000L
    private const val STARTING_ROW_OFFSET = 4
    private const val STARTING_COLUMN_OFFSET = 3

    private fun getShape(index: Int) =
        when (index % 5) {
            0 -> HorizontalBar()
            1 -> Cross()
            2 -> L()
            3 -> VerticalBar()
            4 -> Box()
            else -> error("")
        }

    private val bounds = 1 .. 7

    override fun executeTask() {
        measureTime {
            val directions = testInput.toDirections()
            val towerHeight = towerHeight(directions)
            println("Tower height after 2022 blocks dropped = $towerHeight")

            val finalHeight = towerHeight(directions, PART_2_BLOCKS)
            println("Tower height after $PART_2_BLOCKS blocks dropped = $finalHeight")
        }.let { println("Test part took $it\n") }

        measureTime {
            val directions = input.toDirections()
            val towerHeight = towerHeight(directions)
            println("Tower height after 2022 blocks dropped = $towerHeight")
        }.let { println("Part 1 took $it\n") }

        measureTime {
            val directions = input.toDirections()
            val towerHeight = towerHeight(directions, PART_2_BLOCKS)
            println("Final height after $PART_2_BLOCKS rock drops = $towerHeight")
        }.let { println("Part 2 took $it\n") }
    }

    private fun towerHeight(
        directions: List<Direction>,
        blocksToDrop: Long = 2022
    ): Long {
        var iteration = 0
        var fallenBlocks = 0
        var oldHeight = 0
        var heightDiffs = mutableListOf<Int>()
        var cycles: Pair<List<Int>, Int>? = null
        val blockedPositions: MutableSet<Position> = bounds.map { Position(it, 0) }.toMutableSet()

        while (fallenBlocks < blocksToDrop) {
            val shape = getShape(fallenBlocks)
            val startingRow = blockedPositions.minOf { it.rowIndex } - STARTING_ROW_OFFSET - shape.height + 1
            val startingOffset = Position(colIndex = STARTING_COLUMN_OFFSET, rowIndex = startingRow )
            shape.positions = shape.positions.map { it + startingOffset }.toSet()

            while (!shape.fallen) {
                val direction = directions[iteration++ % directions.size]
                val positionsAfterBlow = shape.positions.map { it + direction }

                if (positionsAfterBlow.all { it.colIndex in bounds && it !in blockedPositions })
                    shape.positions = positionsAfterBlow.toSet()

                val positionsAfterFall = shape.positions.map { it + Direction.DOWN }

                if (positionsAfterFall.any { it in blockedPositions }) {
                    shape.fallen = true
                    fallenBlocks++
                    oldHeight = blockedPositions.minOf { it.rowIndex }.absoluteValue
                    blockedPositions.addAll(shape.positions)
                    heightDiffs.add(blockedPositions.minOf { it.rowIndex }.absoluteValue - oldHeight)
                } else shape.positions = positionsAfterFall.toSet()
            }

            if (fallenBlocks > 4000)
                cycles = heightDiffs.findCycle(20)

            cycles?.let { (cycle, cycleStart) ->
                val cycles = (blocksToDrop - cycleStart) / cycle.size
                val cycleReminder = (blocksToDrop - cycleStart) % cycle.size
                return heightDiffs.take(cycleStart).sum() +
                        cycles * cycle.sum() +
                        cycle.take(cycleReminder.toInt()).sum()
            }
        }

        return blockedPositions.minOf { it.rowIndex }.absoluteValue.toLong()
    }

    private fun printTower(blockedPositions: Set<Position>, shapePositions: Set<Position>) {
        val maxRowIndex = blockedPositions.minOf { it.rowIndex }.absoluteValue
        val maxRow = max(maxRowIndex, shapePositions.minOf { it.rowIndex }.absoluteValue)
        val normalizedBlockedPositions = blockedPositions.map { Position(it.colIndex, it.rowIndex + maxRow)}
        val normalizedShapePosition = shapePositions.map { Position(it.colIndex, it.rowIndex + maxRow)}
        Grid(
            cells = (0..maxRow).flatMap { rowIndex ->
                (0..8).map { colIndex ->
                    val position = Position(colIndex, rowIndex)
                    GridCell(
                        position = position,
                        value = when {
                            position in listOf(Position(0, maxRow),
                                Position(8, maxRow)) -> '+'
                            position.colIndex in listOf(0, 8) -> '|'
                            position.rowIndex == maxRow -> '-'
                            position in normalizedBlockedPositions -> '#'
                            position in normalizedShapePosition -> '@'
                            else -> '.'
                        }
                    )
                }
            }
        ).print()
    }

    private fun String.toDirections(): List<Direction> = map { it.toDirection() }
}