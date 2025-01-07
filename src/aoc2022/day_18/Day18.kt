package aoc2022.day_18

import utils.AocTask
import utils.extensions.numsInt
import kotlin.time.measureTime

object Day18: AocTask() {

    private const val CUBE_FACES = 6

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
        get() = (1 .. maxOf { it.x }).flatMap { x ->
                    (1 .. maxOf { it.y}).flatMap { y ->
                        (1 .. maxOf { it.z }).mapNotNull { z ->
                           val cube = Cube(x, y, z)
                           if (isTrapped(cube) && cube !in this) cube
                           else null
                        }
                    }
                }

    val List<Cube>.surfaceArea: Int
        get() = sumOf { cube ->
            CUBE_FACES - count { other -> cube.isAdjacent(other) }
        }

    val List<Cube>.exteriorSurfaceArea: Int
        get() = surfaceArea - interiorEmptyCubes.surfaceArea
}