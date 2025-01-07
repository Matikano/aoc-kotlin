package aoc2022.day_18

import utils.AocTask
import utils.extensions.numsInt
import kotlin.math.max
import kotlin.time.measureTime

object Day18: AocTask() {

    override fun executeTask() {
        measureTime {
            val cubes = testInput.toCubes()
            println("Test surface area = ${cubes.surfaceArea}")
            println("Test exterior trapped cubes = ${cubes.exteriorSurfaceArea}")
        }.let { println("Test part took $it\n") }

        measureTime {
            val cubes = input.toCubes()
            println("Surface area = ${cubes.surfaceArea}")
        }.let { println("Part 1 took $it\n") }

        measureTime {
            val cubes = input.toCubes()
            println("Exterior surface area = ${cubes.exteriorSurfaceArea}")
        }.let { println("Part 2 took $it\n") }
    }

    private fun String.toCubes(): List<Cube> = lines().map { it.toCube() }
    private fun String.toCube(): Cube = numsInt().let { (x, y, z) -> Cube(x, y, z) }

    fun List<Cube>.isTrapped(cube: Cube): Boolean =
        any { it.x == cube.x && it.y == cube.y && it.z > cube.z } &&
            any { it.x == cube.x && it.y == cube.y && it.z < cube.z } &&
            any { it.x == cube.x && it.y > cube.y && it.z == cube.z } &&
            any { it.x == cube.x && it.y < cube.y && it.z == cube.z } &&
            any { it.x > cube.x && it.y == cube.y && it.z == cube.z } &&
            any { it.x < cube.x && it.y == cube.y && it.z == cube.z }

    val List<Cube>.interiorEmptyCubes: List<Cube>
        get() {
            var trapped = mutableListOf<Cube>()

            for (x in 1..maxOf { it.x }) {
                for (y in 1..maxOf { it.y} ){
                    for (z in 1..maxOf { it.z }) {
                        val cube = Cube(x, y, z)
                        if (isTrapped(cube) && cube !in this)
                           trapped.add(cube)
                    }
                }
            }

            return trapped
        }

    val List<Cube>.surfaceArea: Int
        get() = sumOf { cube ->
            6 - count { other -> cube.isAdjacent(other) }
        }

    val List<Cube>.exteriorSurfaceArea: Int
        get() = surfaceArea - interiorEmptyCubes.surfaceArea
}