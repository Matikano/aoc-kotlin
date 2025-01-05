package aoc2022.day_14

import utils.models.Adjacent
import utils.models.Position

class Cave(
    private val rockPositions: Set<Position>,
    dropColIndex: Int,
) {

    val dropPosition: Position = Position(dropColIndex, 0)
    val blockedPositions = rockPositions.toMutableSet()

    val abyss: Int
        get() = rockPositions.maxOf { it.rowIndex } + 1

    fun fillCaveWithSand(): Int {
        var sandPosition = dropSand()

        while (sandPosition.rowIndex < abyss) {
            blockedPositions.add(sandPosition)
            sandPosition = dropSand()
        }

        return blockedPositions.size - rockPositions.size
    }

    fun fillFloorCaveWithSand(): Int {
        var sandPosition = dropSand()

        while (dropPosition !in blockedPositions) {
            blockedPositions.add(sandPosition)
            sandPosition = dropSand()
        }

        return blockedPositions.size - rockPositions.size
    }

    fun dropSand(): Position {
        var position = dropPosition

        while (true) {
            if (position.rowIndex == abyss)
                return position

            if (position + Adjacent.DOWN !in blockedPositions){
                position += Adjacent.DOWN
                continue
            }

            if (position + Adjacent.DOWN_LEFT !in blockedPositions){
                position += Adjacent.DOWN_LEFT
                continue
            }

            if (position + Adjacent.DOWN_RIGHT !in blockedPositions){
                position += Adjacent.DOWN_RIGHT
                continue
            }

            return position
        }
    }
}