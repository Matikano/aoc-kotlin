package aoc2022.day_15

import utils.models.Position

data class Sensor(
    val position: Position,
    val closestBeacon: Position
) {
    val radius: Int = position.distance(closestBeacon)
}
