package aoc2024.day_13


data class Button(
    val dx: Long,
    val dy: Long,
    val cost: Long
) {
    operator fun Pair<Long, Long>.plus(button: Button): Pair<Long, Long> =
        first + button.dx to second + button.dy
}
