package aoc2022.day_24

import utils.extensions.lcm
import utils.models.Direction
import utils.models.Direction.Companion.toDirection
import utils.models.Grid
import utils.models.Node
import utils.models.Position
import java.util.PriorityQueue

data class BlizzardBasin(
    private var grid: Grid<Char>
) {
    private val startPosition = Position(0, -1)
    private val endPosition
        get() = Position(grid.width - 1, grid.height)
    private val targets = listOf(endPosition, startPosition)

    val blizzards = grid.cells
        .filter { it.value in Direction.validEntries.map { it.symbol } }
        .groupBy { it.value.toDirection() }
        .mapValues { it.value.map { it.position } }

    fun findPathToEnd(stages: Int = 0): Int {
        val startNode = Node(
            position = startPosition,
            direction = Direction.DOWN,
            cost = 0
        ) to 0

        val queue = PriorityQueue<Pair<Node, Int>> { a, b ->
            a.first.cost.compareTo(b.first.cost)
        }.apply { offer(startNode) }

        val seen = mutableSetOf<SeenState>()
        val lcm = lcm(grid.width.toLong(), grid.height.toLong()).toInt()

        while (queue.isNotEmpty()) {
            val (current, stage) = queue.poll()
            val time = current.cost + 1
            var nStage = stage

            Direction.entries.map { direction ->
                current.position + direction
            }.filter {  (grid.isInBounds(it) || it in targets) }
                .forEach { nextPosition ->
                    val targetPosition = targets[stage % targets.size]

                    if (nextPosition == targetPosition) {
                        if (stage == stages)
                            return time
                        else nStage++
                    }

                    var blizzardCollision = false
                    if (nextPosition !in targets) {
                        for (dir in Direction.validEntries) {
                            val testPosition = Position(
                                colIndex = (nextPosition.colIndex - dir.x * time).mod(grid.width),
                                rowIndex = (nextPosition.rowIndex - dir.y * time).mod(grid.height)
                            )
                            if (testPosition in blizzards[dir]!!) {
                                blizzardCollision = true
                                break
                            }
                        }
                    }

                    if (!blizzardCollision) {
                        val state = SeenState(
                            position = nextPosition,
                            time = time % lcm,
                            stage = nStage
                        )
                        if (state in seen)
                            return@forEach
                        seen.add(state)
                        queue.offer(
                            Node(
                                position = nextPosition,
                                direction = Direction.NONE,
                                cost = time,
                                previousNode = current
                            ) to nStage
                        )
                    }
                }
        }

        return Int.MIN_VALUE
    }

    data class SeenState(
        val position: Position,
        val time: Int,
        val stage: Int
    )
}
