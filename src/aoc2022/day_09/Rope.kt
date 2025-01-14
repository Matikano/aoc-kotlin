package aoc2022.day_09

import utils.models.Direction
import utils.models.Position

class Rope(knotsCount: Int) {

    init {
       if (knotsCount < 2) throw IllegalStateException("Rope cannot have less than 2 knots" )
    }

    private var knots: MutableList<Position> = (0..< knotsCount).map { Position(0, 0) }.toMutableList()

    val tailPosition: Position
        get() = knots.last()

    private fun move(direction: Direction) {
        knots[0] += direction
        moveRope()
    }

    fun processInstructions(instructions: List<Instruction>): Set<Position> {
        val queue = ArrayDeque<Instruction>(instructions)
        val tailPositions = mutableSetOf(tailPosition)

        while (queue.isNotEmpty()) {
            with(queue.removeFirst()) {
                repeat(stepCount) {
                    move(direction)
                    tailPositions.add(tailPosition)
                }
            }
        }

        return tailPositions
    }

    private fun moveRope() {
        for (index in 1 ..< knots.size) {
            val head = knots[index - 1]
            val knot = knots[index]
            if (knot !in head.adjacents)
                knots[index] += (head - knot).toAdjacent()
        }
    }
}
