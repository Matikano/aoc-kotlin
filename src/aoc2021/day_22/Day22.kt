package aoc2021.day_22

import utils.AocTask
import utils.extensions.numsInt
import kotlin.time.measureTime

object Day22: AocTask() {

    private const val INITIALIZATION_PROCEDURE_THRESHOLD = 50

    override fun executeTask() {
        measureTime {
            val cuboids = testInput.toCuboids()
            val afterIntersections = cuboids.afterIntersections()
            val sum = afterIntersections.filter {
                it.xRange.first >= -INITIALIZATION_PROCEDURE_THRESHOLD && it.xRange.last <= INITIALIZATION_PROCEDURE_THRESHOLD &&
                it.yRange.first >= -INITIALIZATION_PROCEDURE_THRESHOLD && it.yRange.last <= INITIALIZATION_PROCEDURE_THRESHOLD &&
                it.zRange.first >= -INITIALIZATION_PROCEDURE_THRESHOLD && it.zRange.last <= INITIALIZATION_PROCEDURE_THRESHOLD
            }.sumOfOnCubes()
            println("Test part sum of cubes = $sum")
        }.let { println("Test part took $it\n") }

        measureTime {
            val cuboids = input.toCuboids()
            val afterIntersections = cuboids.afterIntersections()
            val sum = afterIntersections.filter {
                it.xRange.first >= -INITIALIZATION_PROCEDURE_THRESHOLD && it.xRange.last <= INITIALIZATION_PROCEDURE_THRESHOLD &&
                        it.yRange.first >= -INITIALIZATION_PROCEDURE_THRESHOLD && it.yRange.last <= INITIALIZATION_PROCEDURE_THRESHOLD &&
                        it.zRange.first >= -INITIALIZATION_PROCEDURE_THRESHOLD && it.zRange.last <= INITIALIZATION_PROCEDURE_THRESHOLD
            }.sumOfOnCubes()

            println("Sum of cubes = $sum")
        }.let { println("Part 1 took $it\n") }

        measureTime {
            val cuboids = input.toCuboids()
            val afterIntersections = cuboids.afterIntersections()
            val sum = afterIntersections.sumOfOnCubes()

            println("Sum of cubes = $sum")
        }.let { println("Part 2 took $it\n") }
    }

    private fun List<Cuboid>.sumOfOnCubes(): Long = sumOf { if (it.on) it.volume else -it.volume}

    private fun List<Cuboid>.afterIntersections(): List<Cuboid> {
        val cuboids = mutableListOf<Cuboid>()

        for (instruction in this) {
            val newCuboids = mutableListOf<Cuboid>()
            for (existingCuboid in cuboids) {
                val intersection = existingCuboid.intersects(instruction)
                intersection?.let { newCuboids.add(it) }
            }
            cuboids.addAll(newCuboids)
            if (instruction.on) cuboids.add(instruction)
        }

        return cuboids
    }

    private fun String.toCuboids(): List<Cuboid> = lines().map { it.toCuboid() }

    private fun String.toCuboid(): Cuboid {
        val on = split(" ").first().let { it == "on" }
        val (xRange, yRange, zRange) =  numsInt().windowed(2, 2).map { it.first() .. it.last() }

        return Cuboid(
            xRange = xRange,
            yRange = yRange,
            zRange = zRange,
            on
        )
    }
}