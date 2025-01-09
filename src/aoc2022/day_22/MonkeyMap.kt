package aoc2022.day_22

import utils.models.Direction
import utils.models.Grid
import utils.models.Node
import utils.models.NodeState
import utils.models.Position

data class MonkeyMap(
    val grid: Grid<Char>
) {

    private val validPositions: Set<Position>
        get() = grid.cells.filter { it.value == '.' }.map { it.position }.toSet()

    private val invalidPositions:  Set<Position>
        get() = grid.cells.filter { it.value == ' ' }.map { it.position }.toSet()

    private val rocks: Set<Position>
        get() = grid.cells.filter { it.value == '#' }.map { it.position }.toSet()

    private val startPosition: Position
        get() = validPositions.minBy { it.rowIndex }

    private val validWithRocks: Set<Position> by lazy {
       (validPositions + rocks).sorted().toSet()
    }

    fun processInstructions(
        instructions: List<Instruction>,
        folded: Boolean = false
    ): Int {
        var currentState = Node(
            position = startPosition,
            direction = Direction.RIGHT,
        ).state

        val queue = instructions.toMutableList()

        while (queue.isNotEmpty())
            currentState = processInstruction(
                instruction = queue.removeFirst(),
                currentState = currentState,
                folded = folded
            )

        return currentState.toPassword()
    }

    fun processInstruction(
        instruction: Instruction,
        currentState: NodeState,
        folded: Boolean
    ): NodeState {
        var (position, direction) = currentState

        repeat(instruction.steps) {
            val (newPosition, newDirection) = position.step(direction, folded)
            if (newPosition == position)
                return position to direction.makeATurn(instruction.turn)
            position = newPosition
            direction = newDirection
        }

        return position to direction.makeATurn(instruction.turn)
    }

    private fun isInBounds(position: Position, direction: Direction): Boolean =
        when (direction) {
            Direction.UP -> position.rowIndex >= validWithRocks.filter { it.colIndex == position.colIndex }.minOf { it.rowIndex }
            Direction.DOWN -> position.rowIndex <= validWithRocks.filter { it.colIndex == position.colIndex }.maxOf { it.rowIndex }
            Direction.RIGHT -> position.colIndex <= validWithRocks.filter { it.rowIndex == position.rowIndex }.maxOf { it.colIndex }
            Direction.LEFT ->  position.colIndex >= validWithRocks.filter { it.rowIndex == position.rowIndex }.minOf { it.colIndex }
            Direction.NONE -> throw IllegalStateException("Unexpected direction NONE for password value")
        }

    private fun Position.step(
        direction: Direction,
        fold: Boolean = false
    ): NodeState {
        val nextPosition = this + direction

        return when {
            nextPosition in invalidPositions || !isInBounds(nextPosition, direction) ->
                if (fold) nextPosition.folded(direction)
                else nextPosition.wrapped(direction)
            else -> nextPosition to direction
        }.let { if (it.first in rocks) (this to direction) else it }
    }

    private fun Position.folded(direction: Direction): NodeState =
        when (direction) {
            Direction.UP -> {
                when (colIndex) {
                    in 0..49 -> {
                        Position(
                            colIndex = 50,
                            rowIndex = colIndex + 50
                        ) to Direction.RIGHT
                    }
                    in 50..99 -> {
                        Position(
                            colIndex = 0,
                            rowIndex = colIndex + 100
                        ) to Direction.RIGHT
                    }
                    in 100..149 -> {
                        Position(
                            colIndex = colIndex - 100,
                            rowIndex = 199
                        ) to Direction.UP
                    }
                    else -> throw IllegalArgumentException()
                }
            }
            Direction.DOWN ->
                when (colIndex) {
                    in 0..49 -> {
                        Position(
                            colIndex = colIndex + 100,
                            rowIndex = 0
                        ) to Direction.DOWN
                    }
                    in 50..99 -> {
                        Position(
                            colIndex = 49,
                            rowIndex = colIndex + 100
                        ) to Direction.LEFT
                    }
                    in 100..149 -> {
                        Position(
                            colIndex = 99,
                            rowIndex = colIndex - 50
                        ) to Direction.LEFT
                    }
                    else -> throw IllegalArgumentException()
                }
            Direction.RIGHT ->
                when (rowIndex) {
                    in 0..49 -> {
                        Position(
                            colIndex = 99,
                            rowIndex = 149 - rowIndex
                        ) to Direction.LEFT
                    }
                    in 50..99 -> {
                        Position(
                            colIndex = rowIndex + 50,
                            rowIndex = 49
                        ) to Direction.UP
                    }
                    in 100..149 -> {
                        Position(
                            colIndex = 149,
                            rowIndex = 149 - rowIndex
                        ) to Direction.LEFT
                    }
                    in 150..199 -> {
                        Position(
                            colIndex = rowIndex - 100,
                            rowIndex = 149
                        ) to Direction.UP
                    }
                    else -> throw IllegalArgumentException()
                }
            Direction.LEFT ->
                when (rowIndex) {
                    in 0..49 -> {
                        Position(
                            colIndex = 0,
                            rowIndex = 149 - rowIndex
                        ) to Direction.RIGHT
                    }
                    in 50..99 -> {
                        Position(
                            colIndex = rowIndex - 50,
                            rowIndex = 100
                        ) to Direction.DOWN
                    }
                    in 100..149 -> {
                        Position(
                            colIndex = 50,
                            rowIndex = 149 - rowIndex
                        ) to Direction.RIGHT
                    }
                    in 150..199 -> {
                        Position(
                            colIndex = rowIndex - 100,
                            rowIndex = 0
                        ) to Direction.DOWN
                    }
                    else -> throw IllegalArgumentException()
                }
            else -> throw IllegalStateException("Unexpected direction NONE for movement")
        }

    private fun Position.wrapped(direction: Direction): NodeState =
        when (direction) {
            Direction.UP -> validWithRocks.last { it.colIndex == colIndex }
            Direction.DOWN -> validWithRocks.first { it.colIndex == colIndex }
            Direction.RIGHT -> validWithRocks.first { it.rowIndex == rowIndex }
            Direction.LEFT -> validWithRocks.last { it.rowIndex == rowIndex }
            else -> throw IllegalStateException("Unexpected direction NONE for movement")
        } to direction

    private fun NodeState.toPassword(): Int = 1000 * (first.rowIndex + 1) + 4 * (first.colIndex + 1) + second.toPasswordValue()

    private fun Direction.toPasswordValue(): Int =
        when (this) {
            Direction.RIGHT -> 0
            Direction.DOWN -> 1
            Direction.LEFT -> 2
            Direction.UP -> 3
            Direction.NONE -> throw IllegalStateException("Unexpected direction NONE for password value")
        }

    private fun Direction.makeATurn(turn: Turn?): Direction =
        when (turn) {
            Turn.RIGHT -> next()
            Turn.LEFT -> previous()
            else -> this
        }
}
