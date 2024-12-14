package aoc2024.day_14

data class Velocity(
    val dx: Int,
    val dy: Int
) {
    operator fun times(scalar: Int): Velocity =
        Velocity(
            dx = dx * scalar,
            dy = dy * scalar
        )
}
