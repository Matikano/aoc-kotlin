package aoc2022.day_17

import aoc2022.day_17.Shape.*
import utils.AocTask
import utils.extensions.checkAllCycles
import utils.extensions.findRepeatingCycle
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
            println("directions = ${directions.size}")
            println("Tower height after 2022 blocks dropped = $towerHeight")
            val (cycleStart, cycle) = towerCycles(directions)!!
            val cycleLength = cycle.size
            println("Cycle start = ${cycleStart}, cycleLength = $cycleLength, cycle = $cycle")

            val towerHeightBeforeCycle = towerHeight(directions, cycleStart.toLong())
            val cycleHeightIncrement = cycle.sum()
            val cycles = (PART_2_BLOCKS - cycleStart) / cycleLength
            val cycleReminder = (PART_2_BLOCKS - cycleStart) % cycleLength
            println("Cycle height increment = $cycleHeightIncrement, cycle reminder = $cycleReminder, heightBeforeCycle = $towerHeightBeforeCycle")
            val finalHeight = cycles * cycleHeightIncrement + towerHeightBeforeCycle + cycle.take(cycleReminder.toInt()).sum()
            println("Final Height = $finalHeight")
        }.let { println("Test part took $it\n") }

//        measureTime {
//            val directions = input.toDirections()
//            val towerHeight = towerHeight(directions)
//            println("directions = ${directions.size}")
//            println("Tower height after 2022 blocks dropped = $towerHeight")
//        }.let { println("Part 1 took $it\n") }

        measureTime {
            val directions = input.toDirections()
            val towerHeight = towerHeight(directions, 5000)
            println("directions = ${directions.size}")
            println("Tower height after 2022 blocks dropped = $towerHeight")
            val (cycleStart, cycleLength) = towerCycles(directions)!!
            println("Cycle start = $cycleStart, cycle length = $cycleLength")
        }.let { println("Part 2 took $it\n") }
    }

    private fun towerCycles(directions: List<Direction>): Pair<Int, List<Int>>? {
        var iteration = 0
        var fallenBlocks = 0
        var oldHeight = 0
        val heightDiffs = mutableListOf<Int>()
        val blockedPositions: MutableSet<Position> = bounds.map { Position(it, 0) }.toMutableSet()

        while (fallenBlocks < 5000) {
            val shape = getShape(fallenBlocks)
            val startingRow = blockedPositions.minOf { it.rowIndex } - STARTING_ROW_OFFSET - shape.height + 1
            val startingOffset = Position(colIndex = 3, rowIndex = startingRow )
            shape.positions = shape.positions.map { it + startingOffset }.toSet()

            while (!shape.fallen) {
                val direction = directions[iteration++ % directions.size]
                val positionsAfterBlow = shape.positions.map { it + direction }

                if (positionsAfterBlow.all { it.colIndex in bounds && it !in blockedPositions })
                    shape.positions = positionsAfterBlow.toSet()

                val positionsAfterFall = shape.positions.map { it + Direction.DOWN }

                if (positionsAfterFall.any { it in blockedPositions }) {
                    fallenBlocks++
                    oldHeight = blockedPositions.minOf { it.rowIndex }.absoluteValue
                    blockedPositions.addAll(shape.positions)
                    shape.fallen = true
                    heightDiffs.add(blockedPositions.minOf { it.rowIndex }.absoluteValue - oldHeight)
                } else shape.positions = positionsAfterFall.toSet()
            }
        }

        return checkAllCycles(heightDiffs)
    }

    private fun towerHeight(
        directions: List<Direction>,
        blocksToDrop: Long = 2022
    ): Int {
        var iteration = 0
        var fallenBlocks = 0
        var oldHeight = 0
        var heightDiffs = mutableListOf<Int>()
        val blockedPositions: MutableSet<Position> = bounds.map { Position(it, 0) }.toMutableSet()

        while (fallenBlocks < blocksToDrop) {
            val shape = getShape(fallenBlocks)
            val startingRow = blockedPositions.minOf { it.rowIndex } - STARTING_ROW_OFFSET - shape.height + 1
            val startingOffset = Position(colIndex = 3, rowIndex = startingRow )
            shape.positions = shape.positions.map { it + startingOffset }.toSet()

            while (!shape.fallen) {
                val direction = directions[iteration++ % directions.size]
                val positionsAfterBlow = shape.positions.map { it + direction }

                if (positionsAfterBlow.all { it.colIndex in bounds && it !in blockedPositions })
                    shape.positions = positionsAfterBlow.toSet()

                val positionsAfterFall = shape.positions.map { it + Direction.DOWN }

                if (positionsAfterFall.any { it in blockedPositions }) {
                    fallenBlocks++
                    oldHeight = blockedPositions.minOf { it.rowIndex }.absoluteValue
                    blockedPositions.addAll(shape.positions)
                    shape.fallen = true
                    heightDiffs.add(blockedPositions.minOf { it.rowIndex }.absoluteValue - oldHeight)
                } else shape.positions = positionsAfterFall.toSet()
            }
        }
        return blockedPositions.minOf { it.rowIndex }.absoluteValue
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