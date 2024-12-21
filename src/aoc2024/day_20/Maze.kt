package aoc2024.day_20

import aoc2024.day_20.Day20.MIN_SAVE_TIME
import utils.models.*
import java.util.*

data class Maze(
    val grid: Grid<Char>
) {
    private val startPosition: Position
        get() = grid.cells.first { it.value == START }.position

    private val endPosition: Position
        get() = grid.cells.first { it.value == END }.position

    private val walls: List<Position>
        get() = grid.cells.filter { it.value == WALL }.map { it.position }

    private val distances = grid.cells.associate {
        it.position to if (it.position == startPosition) 0 else -1
    }.toMutableMap()

    private val reversedDistances = grid.cells.associate {
        it.position to if (it.position == endPosition) 0 else -1
    }.toMutableMap()

    fun cheatPositionsCount(
        minDifference: Int = MIN_SAVE_TIME,
        maxRadius: Int = MIN_CHEAT_SEARCH_RADIUS
    ): Int = distances.filter { it.value != -1 }.keys.flatMap { cheatStart ->
        reversedDistances.filter { it.value != -1 }.keys.map { cheatEnd ->
            cheatStart to cheatEnd
        }
    }.count { (cheatStart, cheatEnd) ->
        val difference = cheatEnd.difference(cheatStart)
        val optimal = distances[endPosition]!!

        difference <= maxRadius &&
                distances[cheatStart]!! + difference + reversedDistances[cheatEnd]!! <= optimal - minDifference
    }

    fun solveFromEnd() {
        solve(
            startPosition = endPosition,
            endPosition = startPosition,
            distances = reversedDistances
        )
    }

    fun solve(
        startPosition: Position = this.startPosition,
        endPosition: Position = this.endPosition,
        distances: MutableMap<Position, Int> = this.distances
    ): Int {
        val seen = mutableSetOf<Position>()
        val startingNode = Node(
            position = startPosition,
            direction = Direction.UP,
            cost = 0
        )
        val queue = PriorityQueue<Node> { a, b ->
            a.cost.compareTo(b.cost)
        }.apply { offer(startingNode) }

        while (queue.isNotEmpty()) {
            val current = queue.poll()

            if (current.position == endPosition)
                return distances[current.position] ?: Int.MAX_VALUE

            if (current.position in seen)
                continue

            seen.add(current.position)

            Direction.validDirections.forEach { direction ->
                val newPosition = current.position + direction
                if (!grid.isInBounds(newPosition) || grid[newPosition]!!.value == WALL || distances[newPosition] != -1)
                    return@forEach
                distances[newPosition] = distances[current.position]!! + 1
                queue.offer(
                    current.copy(
                        position = newPosition,
                        cost = current.cost + 1
                    )
                )
            }
        }

        return Int.MAX_VALUE
    }

    fun printDistances() {
        distances.values
            .windowed(size = grid.width, step = grid.width)
            .forEach { row ->
                println(
                    row.joinToString("\t") {
                        when (it) {
                            -1 -> WALL
                            0 -> START
                            else -> it.toString()
                        }.toString()
                    }
                )
            }
    }

    fun printReversedDistances() {
        reversedDistances.values
            .windowed(size = grid.width, step = grid.width)
            .forEach { row ->
                println(
                    row.joinToString("\t") {
                        when (it) {
                            -1 -> WALL
                            0 -> START
                            else -> it.toString()
                        }.toString()
                    }
                )
            }
    }

    companion object {
        const val START = 'S'
        const val END = 'E'
        const val WALL = '#'
        const val MIN_CHEAT_SEARCH_RADIUS = 2
    }
}
