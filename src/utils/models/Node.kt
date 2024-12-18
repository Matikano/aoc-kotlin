package utils.models

typealias NodeSate = Pair<Position, Direction>

data class Node(
    val position: Position,
    val direction: Direction,
    val cost: Int
) {
    val state: NodeSate
        get() = position to direction
}