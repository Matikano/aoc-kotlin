package aoc2021.day_19

import kotlin.math.abs

data class Position3D(
    val x: Int,
    val y: Int,
    val z: Int
) {
    override fun toString(): String = "($x,$y,$z)"

    fun rotate(rotation: Int): Position3D {
        return when (rotation) {
            0 -> this
            1 -> Position3D(x, -z, y)
            2 -> Position3D(x, -y, -z)
            3 -> Position3D(x, z, -y)
            4 -> Position3D(-x, -y, z)
            5 -> Position3D(-x, z, y)
            6 -> Position3D(-x, y, -z)
            7 -> Position3D(-x, -z, -y)
            8 -> Position3D(y, x, -z)
            9 -> Position3D(y, z, x)
            10 -> Position3D(y, -x, z)
            11 -> Position3D(y, -z, -x)
            12 -> Position3D(-y, -x, -z)
            13 -> Position3D(-y, z, -x)
            14 -> Position3D(-y, x, z)
            15 -> Position3D(-y, -z, x)
            16 -> Position3D(z, x, y)
            17 -> Position3D(z, -y, x)
            18 -> Position3D(z, -x, -y)
            19 -> Position3D(z, y, -x)
            20 -> Position3D(-z, -x, y)
            21 -> Position3D(-z, y, x)
            22 -> Position3D(-z, x, -y)
            23 -> Position3D(-z, -y, -x)
            else -> throw IllegalArgumentException("Invalid rotation")
        }
    }

    fun translate(dx: Int, dy: Int, dz: Int): Position3D = Position3D(x + dx, y + dy, z + dz)

    fun absoluteOrderedDistance(other: Position3D): List<Int> =
        listOf(
            abs(other.x - x),
            abs(other.y - y),
            abs(other.z - z),
        ).sorted()

    companion object {
        const val NUMBER_OF_3D_ROTATIONS = 24
    }
}
