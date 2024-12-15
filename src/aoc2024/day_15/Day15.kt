package aoc2024.day_15

import aoc2024.AocTask
import utils.extensions.contains
import utils.extensions.printPositions
import utils.extensions.printScaledPositions
import utils.models.Direction
import utils.models.Position
import utils.models.Position.Companion.plus
import kotlin.time.measureTime

object Day15: AocTask {

    private const val SCALE_FACTOR = 2

    private const val UP_CHAR = '^'
    private const val RIGHT_CHAR = '>'
    private const val DOWN_CHAR = 'v'
    private const val LEFT_CHAR = '<'

    private const val BOX_CHAR = 'O'
    private const val WALL_CHAR = '#'
    private const val ROBOT_CHAR = '@'

    private val SCALED_BOX_CHARS = listOf('[', ']')
    private val SCALED_WALL_CHARS = listOf('#', '#')
    private val SCALED_ROBOT_CHARS = listOf('@', '.')

    private var walls = mutableSetOf<Position>()
    private var boxes = mutableSetOf<Position>()
    private lateinit var robot: Position

    private var scaledWalls = mutableSetOf<Pair<Position, Position>>()
    private var scaledBoxes = mutableSetOf<Pair<Position, Position>>()
    private var scaledRobot = Position(0, 0) to Position(1, 0)

    private lateinit var directions: List<Direction>
    private lateinit var bounds: Pair<Int, Int>

    override val fileName: String
        get() = "src/aoc2024/day_15/input.txt"

    override fun executeTask() {
        println("-------------------------------------")
        println("AoC 2024 Task ${this.javaClass.simpleName}")
        println()

        measureTime {
            readFileToString().loadInput()
            predictRobotMovement()

            val sumOfGPSCoordinates = boxes.toList().sumOfGPSCoordinates()
            println("Sum of boxes GPS coordinates = $sumOfGPSCoordinates")
        }.let { println("Part 1 took $it") }

        measureTime {
            readFileToString().loadInput()
            scaleInput()
            predictRobotMovementScaled()

            val sumOfGPSCoordinates = scaledBoxes.map { it.first }.toList().sumOfGPSCoordinates()
            println("Sum of boxes GPS coordinates = $sumOfGPSCoordinates")
        }.let { println("Part 2 took $it") }
    }

    private fun String.loadInput() {
        boxes.clear()
        walls.clear()
        robot = Position(0, 0)

        val (grid, directionChain) = split("\n\n")
        val gridRows = grid.split('\n')

        bounds = gridRows.first().length to gridRows.size
        directions = directionChain.mapNotNull { it.toDirection() }

        gridRows.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { colIndex, char ->
                val position = Position(colIndex = colIndex, rowIndex = rowIndex)
                when (char) {
                    WALL_CHAR -> walls.add(position)
                    BOX_CHAR -> boxes.add(position)
                    ROBOT_CHAR -> robot = position
                    else -> Unit
                }
            }
        }
    }

    private fun Char.toDirection(): Direction? =
        when (this) {
            UP_CHAR -> Direction.UP
            RIGHT_CHAR -> Direction.RIGHT
            DOWN_CHAR -> Direction.DOWN
            LEFT_CHAR -> Direction.LEFT
            else -> null
        }

    private fun List<Position>.sumOfGPSCoordinates(): Int =
        sumOf { it.gpsCoordinateSum() }

    private fun Position.gpsCoordinateSum(): Int =
        rowIndex * 100 + colIndex

    private fun printGrid() = printPositions(
        gridBounds = bounds,
        firstList = walls.toList(),
        firstChar = WALL_CHAR,
        secondList = boxes.toList(),
        secondChar = BOX_CHAR,
        thirdList = listOf(robot),
        thirdChar = ROBOT_CHAR
    )

    private fun printScaledGrid() = printScaledPositions(
        gridBounds = bounds,
        firstList = scaledWalls.map { it.first },
        firstChars = SCALED_WALL_CHARS,
        secondList = scaledBoxes.map { it.first },
        secondChars = SCALED_BOX_CHARS,
        thirdList = listOf(scaledRobot).map { it.first },
        thirdChars = SCALED_ROBOT_CHARS
    )

    private fun scaleInput() {
        scaledBoxes = boxes.map { it.scaleX(SCALE_FACTOR) to it.scaleX(SCALE_FACTOR).plusX(1) }.toMutableSet()
        scaledWalls = walls.map { it.scaleX(SCALE_FACTOR) to it.scaleX(SCALE_FACTOR).plusX(1) }.toMutableSet()
        scaledRobot = robot.scaleX(SCALE_FACTOR) to robot.scaleX(SCALE_FACTOR).plusX(1)
    }

    private fun predictRobotMovement() {
        directions.forEach { direction ->
            var nextPosition = robot + direction
            val boxPositionsToMove = mutableSetOf<Position>()
            while (true) when {
                nextPosition in boxes -> {
                    boxPositionsToMove.add(nextPosition)
                    nextPosition += direction
                }
                (nextPosition !in walls && nextPosition !in boxes) ||
                        nextPosition in walls -> break
            }

            if (nextPosition !in walls) {
                boxes.removeAll(boxPositionsToMove)
                boxes.addAll(boxPositionsToMove.map { it + direction })
                robot += direction
            }
        }
    }

    private fun predictRobotMovementScaled() {
        directions.forEach { direction ->
            val boxPositionsToMove = mutableSetOf(scaledRobot.first)
            var iteration = 0
            var hitWall = false
            while (iteration < boxPositionsToMove.size) {
                val position = boxPositionsToMove.toList()[iteration]
                val nextPosition = position + direction
                if (nextPosition in scaledBoxes) {
                    boxPositionsToMove.add(nextPosition)
                    if (nextPosition in scaledBoxes.map { it.first } )
                        boxPositionsToMove.add(nextPosition.plusX(1))
                    if (nextPosition in scaledBoxes.map { it.second } )
                        boxPositionsToMove.add(nextPosition.plusX(-1))
                } else if (nextPosition in scaledWalls) {
                    hitWall = true
                    break
                }
                iteration++
            }

            if (hitWall) return@forEach

            scaledBoxes.apply {
                val boxesToMove = filter { it in boxPositionsToMove }.toSet()
                removeAll(boxesToMove)
                addAll(boxesToMove.map { it + direction })
            }
            scaledRobot += direction
        }
    }
}