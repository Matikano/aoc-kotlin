package aoc2023.day_17

import utils.models.Direction
import utils.models.Grid
import utils.models.Position
import java.util.PriorityQueue

data class CrucibleMaze(
    val grid: Grid<Char>
) {

    private val startPosition: Position
        get() = Position(colIndex = 0, rowIndex = 0)

    private val endPosition: Position
        get ()= Position(colIndex = grid.width - 1, rowIndex = grid.height - 1)

    fun findLeastHeatLossPath(): Set<ConstrainedNode> {
        val startingNode = ConstrainedNode(
            position = startPosition,
            direction = Direction.NONE,
            cost = 0,
            sameDirectionCount = 1
        )
        val queue = PriorityQueue<ConstrainedNode> { a, b ->
            a.cost.compareTo(b.cost)
        }.apply { offer(startingNode) }

        val seenStates = mutableSetOf<ConstrainedNode.ConstrainedNodeState>()
        while (queue.isNotEmpty()) {
            val current = queue.poll()
            if (current.position == endPosition)
                return current.path

            if (current.state in seenStates)
                continue

            seenStates.add(current.state)

            val possibleNewNodes = Direction.validDirections
                .filter {
                    it != current.direction.reversed() &&
                    if (current.sameDirectionCount == 3)
                        it != current.direction
                    else true
                }
                .map { dir -> current.position + dir to dir }
                .filter { grid.isInBounds(it.first) }
                .map { (position, dir) ->
                    ConstrainedNode(
                        position = position,
                        direction = dir,
                        cost = current.cost + grid[position]!!.value.digitToInt(),
                        sameDirectionCount = if (dir == current.direction) {
                            current.sameDirectionCount + 1
                        } else 1,
                        fromNode = current
                    )
                }

            possibleNewNodes.forEach { newNode ->
                queue.offer(newNode)
            }
        }

        return emptySet()
    }

    fun findLeastHeatLossPathUltra(): Set<ConstrainedNode> {
        val startingNode = ConstrainedNode(
            position = startPosition,
            direction = Direction.NONE,
            cost = 0,
            sameDirectionCount = 1
        )
        val queue = PriorityQueue<ConstrainedNode> { a, b ->
            a.cost.compareTo(b.cost)
        }.apply { offer(startingNode) }

        val seenStates = mutableSetOf<ConstrainedNode.ConstrainedNodeState>()
        while (queue.isNotEmpty()) {
            val current = queue.poll()
            if (current.position == endPosition && current.sameDirectionCount >= 4)
                return current.path

            if (current.state in seenStates)
                continue

            seenStates.add(current.state)
            val possibleNewNodes = Direction.validDirections
                .filter {
                    it != current.direction.reversed() &&
                    if (current.sameDirectionCount == 10)
                        it != current.direction
                    else if (current.sameDirectionCount < 4 && current.direction != Direction.NONE) it == current.direction
                    else true
                }
                .map { dir -> current.position + dir to dir }
                .filter { grid.isInBounds(it.first) }
                .map { (position, dir) ->
                    ConstrainedNode(
                        position = position,
                        direction = dir,
                        cost = current.cost + grid[position]!!.value.digitToInt(),
                        sameDirectionCount = if (dir == current.direction) {
                            current.sameDirectionCount + 1
                        } else 1,
                        fromNode = current
                    )
                }

            possibleNewNodes.forEach { newNode ->
                queue.offer(newNode)
            }
        }

        return emptySet()
    }
}
