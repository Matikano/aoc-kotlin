package aoc2022.day_18

import kotlin.math.absoluteValue

data class Cube(
    val x: Int,
    val y: Int,
    val z: Int
) {
    fun distance(other: Cube): Int =
        (other.x - x).absoluteValue + (other.y - y).absoluteValue + (other.z - z).absoluteValue

    fun isAdjacent(other: Cube): Boolean = distance(other) == 1
}