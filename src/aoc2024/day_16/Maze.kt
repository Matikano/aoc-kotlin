package aoc2024.day_16

import utils.extensions.printPositions
import utils.models.Direction
import utils.models.Grid
import utils.models.Position
import java.util.*
import kotlin.collections.ArrayDeque

data class Maze(
    val grid: Grid<Char>
) {
    private val startPosition: Position
        get() = grid.cells.first { it.value == START }.position

    private val endPosition: Position
        get() = grid.cells.first { it.value == END }.position

    fun solve(): MazeResult {
        val queue = PriorityQueue<Node> { a, b ->
            a.cost compareTo b.cost
        }
        var bestCost = Int.MAX_VALUE
        val costs = mutableMapOf<NodeSate, Int>().withDefault { Int.MAX_VALUE }
        val backtrack = mutableMapOf<NodeSate, MutableSet<NodeSate>>()
        val endStates = mutableSetOf<NodeSate>()
        val startingNode = Node(
            position = startPosition,
            direction = Direction.RIGHT,
            cost = 0
        )

        val seen = mutableSetOf(startingNode.state)

        queue.offer(startingNode)
        costs[startingNode.state] = 0

        while (queue.isNotEmpty()) {
            val current = queue.poll()
            if (current.cost > costs.getOrDefault(current.state, Int.MAX_VALUE))
                continue

            if (current.position == endPosition) {
                if (current.cost > bestCost)
                    break
                bestCost = current.cost
                endStates.add(current.state)
                val states = ArrayDeque(endStates)

                while (states.isNotEmpty()) {
                    val key = states.removeFirst()
                    for (last in backtrack.getOrDefault(key, emptySet())) {
                        if (last !in seen) {
                            seen.add(last)
                            states.add(last)
                        }
                    }
                }

                seen.add(current.state)
            }

            val newNodes = listOf(
                // Move forward in current direction
                current.copy(
                    position = current.position + current.direction,
                    cost = current.cost + 1
                ),
                // Make turns
                current.copy(
                    direction = current.direction.next(),
                    cost = current.cost + TURN_COST
                ),
                current.copy(
                    direction = current.direction.previous(),
                    cost = current.cost + TURN_COST
                )
            )

            newNodes.forEach { newNode ->
                val neighbor = grid[newNode.position]
                if (neighbor == null || neighbor.value == WALL)
                    return@forEach

                val lowestCost = costs[newNode.state] ?: Int.MAX_VALUE
                if (newNode.cost > lowestCost)
                    return@forEach
                if (newNode.cost < lowestCost) {
                    backtrack[newNode.state] = mutableSetOf()
                    costs[newNode.state] = newNode.cost
                }
                backtrack[newNode.state]?.add(current.state)
                queue.offer(newNode)
            }
        }

        return MazeResult(
            bestScore = bestCost,
            bestPaths = seen.map { it.first }.toSet()
        )
    }

    fun printMaze() {
        with(grid) {
            printPositions(
                gridBounds = width to height,
                firstList = cells.filter { it.value == WALL }.map { it.position },
                firstChar = WALL,
                secondList = listOf(startPosition),
                secondChar = START,
                thirdList = listOf(endPosition),
                thirdChar = END
            )
        }
    }

    companion object {
        const val WALL = '#'
        const val START = 'S'
        const val END = 'E'

        const val TURN_COST = 1000
    }
}
