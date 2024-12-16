package aoc2024.day_16

import utils.models.Position

data class MazeResult(
    val bestScore: Int,
    val bestPaths: Set<Position>
)
