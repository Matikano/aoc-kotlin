package aoc2022.day_24

import utils.models.Direction
import utils.models.Position

data class Blizzard(
    val direction: Direction,
    var position: Position
) {
    override fun toString(): String = "${direction.symbol}:$position"

    fun move(
        times: Int,
        bounds: Pair<Int, Int>
    ) {
        val (cols, rows) = bounds
        position = position.copy(
            colIndex = (position.colIndex + times * direction.x).mod(cols),
            rowIndex = (position.rowIndex + times * direction.y).mod(rows)
        )
    }
}
