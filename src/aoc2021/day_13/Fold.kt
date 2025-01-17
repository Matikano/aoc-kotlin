package aoc2021.day_13

import utils.models.Position
import kotlin.math.absoluteValue

sealed class Fold(val index: Int) {

    class Vertical(index: Int): Fold(index)
    class Horizontal(index: Int): Fold(index)

    fun fold(positions: Set<Position>): Set<Position> =
        when (this) {
            is Vertical -> positions.map { pos ->
                if (pos.colIndex < index) pos
                else pos.copy(colIndex = index - (index - pos.colIndex).absoluteValue)
            }

            is Horizontal -> positions.map { pos ->
                if (pos.rowIndex < index) pos
                else pos.copy(rowIndex = index - (index - pos.rowIndex).absoluteValue)
            }
        }.toSet()

    companion object {
        fun String.toFold(): Fold {
            val (type, value) = split(" ").last().split("=")
            return when(type) {
                "x" -> Vertical(value.toInt())
                "y" -> Horizontal(value.toInt())
                else -> throw IllegalArgumentException("Unexpected type of fold for $type")
            }
        }
    }
}