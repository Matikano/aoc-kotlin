package aoc2022.day_09

import utils.models.Direction
import utils.models.Position

class Rope(
    knotsCount: Int
) {
    init {
       if (knotsCount < 2)
           throw IllegalStateException("Rope cannot have less than 2 knots" )
    }

    private var knots: MutableList<Position> = (0..< knotsCount).map { Position(0, 0) }.toMutableList()

    val tailPosition: Position
        get() = knots.last()

    fun move(direction: Direction) {
        knots[0] += direction
        moveRope()
    }

    private fun moveRope() {
        for (index in 1 ..< knots.size) {
            val head = knots[index - 1]
            val knot = knots[index]
            if (knot !in head.adjacents) {
                val difference = head - knot
                val displacement = Position(
                    colIndex = difference.colIndex.coerceAtLeast(-1).coerceAtMost(1),
                    rowIndex = difference.rowIndex.coerceAtLeast(-1).coerceAtMost(1)
                )
                knots[index] += displacement
            }
        }
    }
}
