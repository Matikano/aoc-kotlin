package aoc2023.day_03

import utils.models.Position

data class NumberInGrid(
    val value: Int,
    val surroundingPositions: Set<Position>
)
