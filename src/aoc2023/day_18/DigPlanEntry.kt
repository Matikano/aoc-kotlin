package aoc2023.day_18

import utils.extensions.numsInt
import utils.extensions.numsLong
import utils.models.Direction

typealias Instruction = Pair<Direction, Int>

data class DigPlanEntry(
    val direction: Direction,
    val length: Int,
    val colorCode: String
) {
    val instruction: Instruction
        get() = direction to length

    val correctInstruction: Instruction
        get() {
            val hexadecimalCode = colorCode.substringAfter('#')
            val length = hexadecimalCode.dropLast(1).toInt(radix = 16)
            val direction = hexadecimalCode.last().digitToInt().toDirection()
            return direction to length
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
