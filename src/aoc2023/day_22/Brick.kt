package aoc2023.day_22

import utils.extensions.overlaps

data class Brick(
    val xRange: IntRange,
    val yRange: IntRange,
    val zRange: IntRange,
) {
    fun overlaps(other: Brick): Boolean =
        xRange.overlaps(other.xRange) && yRange.overlaps(other.yRange)

    fun supports(other: Brick): Boolean =
        this.overlaps(other) && zRange.last + 1 == other.zRange.first
}
