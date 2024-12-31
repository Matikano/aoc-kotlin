package aoc2023.day_24

import utils.AocTask
import utils.extensions.numsLong
import utils.extensions.uniquePairs
import java.math.BigInteger
import kotlin.time.measureTime

object Day24: AocTask() {

    private val testBoundMin = (7L).toBigInteger()
    private val testBoundMax = (27L).toBigInteger()
    private val boundMin = (200000000000000L).toBigInteger()
    private val boundMax = (400000000000000L).toBigInteger()

    override fun executeTask() {
        measureTime {
            val hailstones = testInput.toHailstoneList()
            println("Intersections for test data = ${hailstones.intersectionsCount(testBoundMin, testBoundMax)}")
        }.let { println("Test part took $it\n") }

        measureTime {
            val hailstones = input.trim().toHailstoneList()
            println("Intersections = ${hailstones.intersectionsCount(boundMin, boundMax)}")
        }.let { println("Test part took $it\n") }
    }

    private fun List<Hailstone>.intersectionsCount(min: BigInteger, max: BigInteger): Int =
        uniquePairs()
            .filterNot { it.first.xyParallel(it.second) }
            .count { (h1, h2) ->
                val (intersectionX, intersectionY) = h1.xyIntersectionCoordinates(h2)
                    intersectionX >= min && intersectionX <= max &&
                            intersectionY >= min && intersectionY <= max &&
                            h1.xyIntersectionInFuture(intersectionX to intersectionY) &&
                            h2.xyIntersectionInFuture(intersectionX to intersectionY)
            }

    private fun String.toHailstoneList(): List<Hailstone> = lines().map { it.toHailStone() }

    private fun String.toHailStone(): Hailstone {
        val (position, velocity) = split("@")
        val (x ,y, z)  = position.numsLong().map { it.toBigInteger() }
        val (vx ,vy, vz)  = velocity.numsLong().map { it.toBigInteger() }

        return Hailstone(x, y, z, vx, vy, vz)
    }

}