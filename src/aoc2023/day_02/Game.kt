package aoc2023.day_02

import aoc2023.day_02.Day2.cubeColors

data class Game(
    val id: Int,
    val cubes: Map<String, MutableList<Int>> = cubeColors
        .associateWith<String, MutableList<Int>> { mutableListOf() }
        .withDefault { mutableListOf(0) }
)
