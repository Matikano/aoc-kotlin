package aoc2024.day_16

import utils.models.Direction
import utils.models.Position

typealias NodeSate = Pair<Position, Direction>
data class Node(
    val position: Position,
    val direction: Direction,
    val cost: Int
) {
    val state: NodeSate
        get() = position to direction
}
