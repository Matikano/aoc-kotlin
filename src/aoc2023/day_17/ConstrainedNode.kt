package aoc2023.day_17

import utils.models.Position
import utils.models.Direction

data class ConstrainedNode(
    val position: Position,
    val direction: Direction,
    val cost: Int,
    val sameDirectionCount: Int = 0,
    val fromNode: ConstrainedNode? = null
) {
    val state: ConstrainedNodeState
        get() = ConstrainedNodeState(
            position = position,
            direction = direction,
            sameDirectionCount = sameDirectionCount,
            fromPosition = fromNode?.position
        )

    val path: Set<ConstrainedNode>
        get() = buildSet<ConstrainedNode> {
            add(this@ConstrainedNode)
            var backTrackNode = fromNode
            while (backTrackNode != null && backTrackNode.position != Position(0, 0)) {
                add(backTrackNode)
                backTrackNode = backTrackNode.fromNode
            }
        }

    data class ConstrainedNodeState(
        val position: Position,
        val direction: Direction,
        val sameDirectionCount: Int = 0,
        val fromPosition: Position? = null
    )
}
