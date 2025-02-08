package aoc2021.day_22

import utils.extensions.length
import kotlin.math.max
import kotlin.math.min

data class Cuboid(
    val xRange: IntRange,
    val yRange: IntRange,
    val zRange: IntRange,
    val on: Boolean = true
) {
    fun intersects(other: Cuboid): Cuboid? {
        val xIntersection = max(xRange.first, other.xRange.first) ..min(xRange.last, other.xRange.last)
        val yIntersection = max(yRange.first, other.yRange.first) ..min(yRange.last, other.yRange.last)
        val zIntersection = max(zRange.first, other.zRange.first) ..min(zRange.last, other.zRange.last)

        return Cuboid(xIntersection, yIntersection, zIntersection, !on).takeIf { !xIntersection.isEmpty() && !yIntersection.isEmpty() && !zIntersection.isEmpty() }
    }

    val volume: Long
        get() = xRange.length.toLong() * yRange.length * zRange.length
}
