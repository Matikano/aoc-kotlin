package aoc2022.day_17

import utils.models.Position

sealed class Shape(
    var positions: Set<Position>,
    var fallen: Boolean = false
) {

    val height = positions.map { it.rowIndex }.toSet().size

    override fun toString(): String =
        buildString {
            for (y in positions.minOf { it.rowIndex }..positions.maxOf { it.rowIndex }) {
                for (x in positions.minOf { it.colIndex }..positions.maxOf { it.colIndex }) {
                    val position = Position(x, y)
                    append(if (position in positions) '#' else '.')
                }
                append('\n')
            }
        }

    class VerticalBar: Shape(positions = (0..< 4).map { Position(0, it) }.toSet())
    class HorizontalBar: Shape(positions = (0..< 4).map { Position(it, 0) }.toSet())
    class Cross: Shape(
        positions = (0..< 3).flatMap { setOf(Position(1, it), Position(it, 1)) }.toSet()
    )
    class L: Shape(
        positions = (0..< 3).flatMap { setOf(Position(2, it), Position(it, 2)) }.toSet()
    )
    class Box: Shape(
        positions = (0..< 2).flatMap { y ->
            (0..< 2).map { x ->
                Position(x, y)
            }
        }.toSet()
    )
}