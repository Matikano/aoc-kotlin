package aoc2024.day_18

import aoc2024.day_16.Maze
import aoc2024.day_16.Maze.Companion
import aoc2024.day_18.Day18.BYTE_COUNT
import utils.extensions.binarySearchWithCostFunction
import utils.extensions.printPositions
import utils.models.Direction
import utils.models.Grid
import utils.models.Node
import utils.models.Position
import java.util.*
import kotlin.collections.ArrayDeque

data class Maze(
    val grid: Grid<Char>,
    val walls: List<Position>
) {
    private val startPosition: Position
        get() = Position(0, 0)

    private val endPosition: Position
        get() = Position(grid.width - 1, grid.height - 1)

    private fun Position.isValid(walls: List<Position>): Boolean =
        grid.isInBounds(this) && this !in walls

    fun findBlockingByte(): Position? =
        walls.binarySearchWithCostFunction(
            startLeft = BYTE_COUNT
        ) { position ->
            solveFor(walls.indexOf(position) + 1)
        }

    fun solveFor(byteCount: Int): Int {
        val walls = walls.take(byteCount)

        val queue = PriorityQueue<Node> { a, b ->
            a.cost.compareTo(b.cost)
        }

        var bestCost = Int.MAX_VALUE
        val costs = mutableMapOf<Position, Int>()
        val startingNode = Node(
            position = startPosition,
            direction = Direction.DOWN,
            cost = 0
        )

        queue.offer(startingNode)
        costs[startingNode.position] = startingNode.cost

        while (queue.isNotEmpty()) {
            val current = queue.poll()
            if (current.cost > costs.getOrDefault(current.position, Int.MAX_VALUE))
                continue

            if (current.position == endPosition) {
                if (current.cost > bestCost)
                    break
                bestCost = current.cost
            }

            val newNodes = listOf(
                // Move forward in current direction
                current.copy(
                    position = current.position + current.direction,
                    cost = current.cost + 1
                ),
                // Make turns
                current.copy(
                    position = current.position + current.direction.next(),
                    direction = current.direction.next(),
                    cost = current.cost + 1
                ),
                current.copy(
                    position = current.position + current.direction.previous(),
                    direction = current.direction.previous(),
                    cost = current.cost + 1
                )
            ).filter { it.position.isValid(walls) }

            newNodes.forEach { newNode ->
                val lowestCost = costs[newNode.position] ?: Int.MAX_VALUE
                if (newNode.cost > lowestCost)
                    return@forEach

                if (newNode.cost < lowestCost) {
                    costs[newNode.position] = newNode.cost
                    queue.offer(newNode)
                }
            }
        }

        return bestCost
    }

    fun printMaze(
        byteCount: Int,
        pathPositions: List<Position> = emptyList(),
        pathChar: Char = 'O'
    ) {
        with(grid) {
            printPositions(
                gridBounds = width to height,
                firstList = walls.take(byteCount),
                firstChar = WALL,
                secondList = pathPositions,
                secondChar = pathChar,
                thirdList = emptyList(),
                thirdChar = ' '
            )
        }
    }

    companion object {
        const val WALL = '#'
        const val EMPTY_SPACE = '.'
    }
}
