package aoc2024.day_14

import utils.models.Position

data class Robot(
    var position: Position,
    val velocity: Velocity,
    private val bounds: Bounds
) {

    operator fun Position.plus(velocity: Velocity): Position =
        Position(
            colIndex = (colIndex + velocity.dx).mod(bounds.width),
            rowIndex =  (rowIndex + velocity.dy).mod(bounds.height)
        )

    fun move(times: Int = 1) {
        position += velocity * times
    }
}
