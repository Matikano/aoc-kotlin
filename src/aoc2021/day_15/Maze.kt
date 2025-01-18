package aoc2021.day_15

import utils.models.Direction
import utils.models.Grid
import utils.models.Node
import utils.models.Position
import java.util.PriorityQueue

data class Maze(
    val grid: Grid<Int>
) {

    val endPosition: Position
        get() = grid.cells.last().position

    val scaledEndPosition
        get() = endPosition.copy(
            colIndex = endPosition.colIndex + (4 * grid.width),
            rowIndex = endPosition.rowIndex + (4 * grid.height)
        )

    fun getValue(position: Position): Int =
        (grid[position.copy(position.colIndex % grid.width, position.rowIndex % grid.height)]!!.value +
                position.colIndex / grid.width +
                position.rowIndex / grid.height - 1) % 9 + 1

    fun shortestPathRisk(endPosition: Position = this.endPosition): Int {
            val startPosition = Position.topLeftCorner()
            val startNode = Node(
                position = startPosition,
                direction = Direction.NONE
            )
            val seen = mutableSetOf<Position>()

            val queue = PriorityQueue<Node> { a, b ->
                a.cost.compareTo(b.cost)
            }.apply { offer(startNode) }

            val scale = if (endPosition == this.scaledEndPosition) 5 else 1
            val (rows, cols) = (grid.height * scale) to (grid.width * scale)

            while (queue.isNotEmpty()) {
                val current = queue.poll()

                if (current.position in seen)
                    continue
                else seen.add(current.position)

                if (current.position == endPosition)
                    return current.cost


                Direction.validEntries.map { dir ->
                    current.position + dir to dir
                }.filter { (pos, _) ->
                    pos.isValid(rows, cols)
                }.forEach { (pos, dir) ->
                    queue.offer(
                        Node(
                            position = pos,
                            direction = dir,
                            cost = current.cost + getValue(pos),
                            previousNode = current
                        )
                    )
                }
            }

            return Int.MAX_VALUE
        }

    private fun Position.isValid(rows: Int, cols: Int): Boolean =
        colIndex in 0 ..< cols && rowIndex in 0 ..< rows
}
