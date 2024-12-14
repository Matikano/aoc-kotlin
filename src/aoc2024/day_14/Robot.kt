package aoc2024.day_14

import utils.models.Position

data class Robot(
    var position: Position,
    val velocity: Velocity,
    private val bounds: Bounds
) {

    operator fun Position.plus(velocity: Velocity): Position =
        Position(
            colIndex = (colIndex + velocity.dx + bounds.width) % (bounds.width),
            rowIndex =  (rowIndex + velocity.dy + bounds.height) % (bounds.height)
        )

    fun move() {
        position += velocity
    }
}
