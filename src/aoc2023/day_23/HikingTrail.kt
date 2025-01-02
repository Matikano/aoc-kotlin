package aoc2023.day_23

import utils.models.Direction
import utils.models.Grid
import utils.models.Position
import kotlin.math.max

data class HikingTrail(
    val grid: Grid<Char>
) {

    val startPosition: Position
        get() = Position(colIndex = 1, rowIndex = 0)

    val endPosition: Position
        get() = Position(colIndex = grid.width - 2, rowIndex = grid.height - 1)

    val graphPositions: MutableSet<Position> = mutableSetOf()
    val graph: MutableMap<Position, MutableMap<Position, Int>> = mutableMapOf()

    init {
        findGraphPositions()
        fillGraph()
    }

    private fun resetGraph() {
        graph.clear()
    }

    fun findGraphPositions() {
        graphPositions.add(startPosition)
        graphPositions.add(endPosition)

        grid.cells.forEach { cell ->
            if (cell.value == '#')
                return@forEach

            val neighbours = Direction.validEntries.map { dir ->
                cell.position + dir
            }.filter { newPosition ->
                grid.isInBounds(newPosition)
                        && grid[newPosition]!!.value != '#'
            }

            if (neighbours.size >= 3)
                graphPositions.add(cell.position)
        }
    }

    fun fillGraph(ignoreSlopes: Boolean = false) {
        graphPositions.forEach { startingPosition ->
            val stack = mutableListOf<Pair<Int, Position>>(0 to startingPosition)
            val seen = mutableSetOf<Position>(startingPosition)

            while (stack.isNotEmpty()) {
                val (cost, current) = stack.removeLast()
                val currentChar = grid[current]!!.value

                if (cost != 0 && current in graphPositions) {
                    if (startingPosition !in graph)
                        graph[startingPosition] = mutableMapOf()
                    graph[startingPosition]!![current] = cost
                    continue
                }

                val directions = if (ignoreSlopes) Direction.validEntries else currentChar.toDirections()

                val neighbours = directions.map { dir ->
                    current + dir
                }.filter { newPosition ->
                    grid.isInBounds(newPosition)
                            && grid[newPosition]!!.value != '#'
                            && newPosition !in seen
                }

                neighbours.forEach { nextPosition ->
                    stack.add(cost + 1 to nextPosition)
                    seen.add(nextPosition)
                }
            }
        }
    }

    fun findLongestTrailIgnoringSlopes(): Long {
        resetGraph()
        fillGraph(ignoreSlopes = true)
        return findLongestTrail()
    }

    fun findLongestTrail(): Long {
        val seen = mutableSetOf(startPosition)

        fun dfs(start: Position): Long {
            var maxTrailLength = Long.MIN_VALUE

            if (start == endPosition)
                return 0

            seen.add(start)
            for (connectedPoint in graph[start]!!.keys)
                if (connectedPoint !in seen)
                    maxTrailLength = max(maxTrailLength, dfs(connectedPoint) + graph[start]!![connectedPoint]!!)
            seen.remove(start)

            return maxTrailLength
        }

        return dfs(startPosition)
    }

    companion object {
        fun Char.toDirections(): List<Direction> =
            Direction.entries.find { it.symbol == this }
                ?.let { listOf(it) }
                ?: Direction.validEntries
    }
}
