package aoc2023.day_02

import utils.AocTask
import utils.extensions.numsInt
import kotlin.time.measureTime

object Day2: AocTask() {

    private const val MAX_RED = 12
    private const val MAX_GREEN = 13
    private const val MAX_BLUE = 14

    val cubeColors = listOf(
        "blue",
        "green",
        "red"
    )

    override fun executeTask() {
        with(inputToList().toGames()) {
            measureTime {
                val sumOfIds = filter { it.isValid() }.sumOf { it.id }
                println("Sum of valid games ids = $sumOfIds")
            }.let { println("Part 1 took $it") }

            measureTime {
                forEach {
                    println(it.cubes to it.power)
                }
                val sumOfPowers = sumOf { it.power }
                println("Sum of game powers = $sumOfPowers")
            }.let { println("Part 2 took $it") }
        }
    }

    private fun Game.isValid(): Boolean =
        cubes["green"]!!.max() <= MAX_GREEN &&
                cubes["red"]!!.max() <= MAX_RED &&
                cubes["blue"]!!.max() <= MAX_BLUE

    private val Game.power: Int
        get() = cubes.values
            .map { it.max() }
            .fold(1) { power, colorPower ->
                power * colorPower
            }

    private fun List<String>.toGames(): List<Game> =
        map { line ->
            Game(
                id = line.numsInt().first()
            ).apply {
                line.split(':')
                    .last()
                    .split(';')
                    .forEach { segment ->
                        segment.split(',').forEach { cubeCount ->
                            cubeColors.forEach { color ->
                                if (cubeCount.contains(color))
                                    cubes[color]!!.add(cubeCount.numsInt().first())
                            }
                        }
                    }
            }
        }
}