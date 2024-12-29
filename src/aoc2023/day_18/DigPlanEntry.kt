package aoc2023.day_18

import utils.extensions.numsInt
import utils.extensions.numsLong
import utils.models.Direction

typealias Instruction = Pair<Direction, Int>

data class DigPlanEntry(
    val direction: Direction,
    val length: Int,
    private val colorCode: String
) {
    val instruction: Instruction
        get() = direction to length

    val correctInstruction: Instruction
        get() {
            val number = colorCode.substringAfter('#').toInt()
            return (number % 10).toDirection() to number / 10
        }

    private fun Int.toDirection(): Direction =
        when (this) {
            3 -> Direction.UP
            2 -> Direction.LEFT
            1 -> Direction.DOWN
            0 -> Direction.RIGHT
            else -> throw IllegalArgumentException("Unsupported integer $this for Direction mapping")
        }

}
