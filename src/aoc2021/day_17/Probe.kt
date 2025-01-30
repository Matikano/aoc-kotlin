package aoc2021.day_17

import aoc2024.day_14.Velocity
import utils.models.Position
import kotlin.math.sign

data class Probe(
    val startVelocity: Velocity
) {
    private var velocity = startVelocity

    private val startPosition = Position.topLeftCorner()

    fun getPositionsForValidTrajectories(targetArea: TargetArea): Set<Position> {
        val positions = mutableSetOf<Position>(startPosition)

        val xBound = targetArea.xRange.endInclusive
        val yBound = targetArea.yRange.start

        var nextPosition = startPosition

        while (nextPosition.colIndex <= xBound && nextPosition.rowIndex >= yBound) {
            nextPosition += velocity
            positions.add(nextPosition)

            velocity = velocity.drag()
        }

        return positions
    }


    private fun Velocity.drag(): Velocity =
        copy(
            dx = dx - sign(dx.toFloat()).toInt(),
            dy = dy - 1
        )
}
