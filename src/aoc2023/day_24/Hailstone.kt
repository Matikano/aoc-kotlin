package aoc2023.day_24

import java.math.BigInteger

data class Hailstone(
    val x: BigInteger,
    val y: BigInteger,
    val z: BigInteger,
    val vx: BigInteger,
    val vy: BigInteger,
    val vz: BigInteger
) {
    // as for 'ax + by = c' standard form
    val a: BigInteger
        get() = vy

    val b: BigInteger
        get() = -vx

    val c: BigInteger
        get() = x * vy - y * vx

    fun xyParallel(other: Hailstone): Boolean =
        a * other.b == other.a * b

    fun xyIntersectionCoordinates(other: Hailstone): Pair<BigInteger, BigInteger> =
        Pair(
            first = (other.b * c - b * other.c).divide(a * other.b - other.a * b),
            second = (a * other.c - other.a * c).divide(a * other.b - other.a * b)
        )

    fun xyIntersectionInFuture(intersection: Pair<BigInteger, BigInteger>): Boolean {
        val xInFuture = (intersection.first - x) * vx >= BigInteger.ZERO
        val yInFuture = (intersection.second - y) * vy >= BigInteger.ZERO
        return xInFuture && yInFuture
    }
}
