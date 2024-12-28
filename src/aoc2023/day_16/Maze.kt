package aoc2023.day_16

import utils.models.Direction
import utils.models.Grid
import utils.models.Node
import utils.models.NodeState
import utils.models.Position

data class Maze(
    val grid: Grid<Char>
) {
    val startingNodeState: NodeState = Position(0, 0) to Direction.RIGHT

    val possibleStartingStates: List<NodeState> by lazy {
        buildList {
            grid.cells.forEach {
                val position = it.position
                if (position.rowIndex in 0..< grid.width && position.colIndex == 0)
                    add(position to Direction.RIGHT)
                if (position.rowIndex in 0..< grid.width && position.colIndex == grid.width - 1)
                    add(position to Direction.LEFT)
                if (position.colIndex in 0..< grid.height && position.rowIndex == 0)
                    add(position to Direction.DOWN)
                if (position.colIndex in 0..< grid.height && position.rowIndex == grid.height - 1)
                    add(position to Direction.UP)
            }
        }
    }

    val startingStatesToScores: Map<NodeState, Int> by lazy {
        possibleStartingStates.associate { start ->
            start to solve(start).size
        }
    }

    val bestEnergized: Int by lazy {
        startingStatesToScores.values.max()
    }

    fun solve(startState: NodeState = startingNodeState): Set<Position> {
        val startingNode = Node(
            startState.first,
            startState.second,
            0
        )
        val queue = ArrayDeque<Node>().apply {
            add(startingNode)
        }
        val energized = mutableSetOf<Position>(startingNode.position)
        val seenStates = mutableSetOf<NodeState>()

        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()

            if (current.position !in energized)
                energized.add(current.position)

            if (current.state !in seenStates)
                seenStates.add(current.state)

            val field = grid[current.position]!!.value
            val directions = field.nextDirections(current.direction)
            val possibleNodeStates = directions.map { dir ->
                current.position + dir to dir
            }.filter { (pos, _ ) -> grid.isInBounds(pos) }
                .filter { possibleNewState -> possibleNewState !in seenStates }

            possibleNodeStates.forEach { newNodeState ->
                queue.add(
                    Node(
                        position = newNodeState.first,
                        direction = newNodeState.second,
                        cost = 0
                    )
                )
            }
        }

        return energized.toSet()
    }

    companion object {
        private const val EMPTY = '.'
        private const val SLASH_MIRROR = '/'
        private const val BACKSLASH_MIRROR = '\\'
        private const val HORIZONTAL_SPLITTER = '-'
        private const val VERTICAL_SPLITTER = '|'

        private fun Char.nextDirections(approachingDirection: Direction): List<Direction> = buildList {
            when(this@nextDirections) {
                EMPTY -> add(approachingDirection)
                SLASH_MIRROR -> when (approachingDirection) {
                    Direction.UP -> Direction.RIGHT
                    Direction.RIGHT -> Direction.UP
                    Direction.DOWN -> Direction.LEFT
                    Direction.LEFT -> Direction.DOWN
                    Direction.NONE -> Direction.NONE
                }.let { add(it) }
                BACKSLASH_MIRROR -> when (approachingDirection) {
                    Direction.UP -> Direction.LEFT
                    Direction.RIGHT -> Direction.DOWN
                    Direction.DOWN -> Direction.RIGHT
                    Direction.LEFT -> Direction.UP
                    Direction.NONE -> Direction.NONE
                }.let { add(it) }
                HORIZONTAL_SPLITTER -> when (approachingDirection) {
                    Direction.UP,
                    Direction.DOWN -> listOf(Direction.RIGHT, Direction.LEFT)
                    else -> listOf(approachingDirection)
                }.let { addAll(it) }
                VERTICAL_SPLITTER -> when (approachingDirection) {
                    Direction.RIGHT,
                    Direction.LEFT -> listOf(Direction.UP, Direction.DOWN)
                    else -> listOf(approachingDirection)
                }.let { addAll(it) }
            }
        }
    }
}
