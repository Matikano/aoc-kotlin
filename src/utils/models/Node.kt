package utils.models

typealias NodeState = Pair<Position, Direction>

data class Node(
    val position: Position,
    val direction: Direction,
    val cost: Int
) {
    val state: NodeState
        get() = position to direction
}