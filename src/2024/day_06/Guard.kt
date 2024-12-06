package `2024`.day_06

data class Guard(
    var direction: Direction,
) {
    private val visitedPositions: MutableSet<Position> = mutableSetOf()
    private val encounteredObstacles: MutableList<Pair<Position, Direction>> = mutableListOf()

    val visitedPositionsCount: Int
        get() = visitedPositions.size

    val isInALoop: Boolean
        get() = encounteredObstacles.size > encounteredObstacles.distinct().size

    var position: Position = Position(0, 0)
        set(value)  {
            field = value
            visitedPositions.add(value)
        }

    fun moveToPosition(newPosition: Position) {
        position = newPosition
    }

    fun turn(obstaclePosition: Position) {
        encounteredObstacles.add(obstaclePosition to direction)
        direction = direction.nextDirection()
    }
}
