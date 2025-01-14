package aoc2022.day_23

import utils.AocTask
import utils.extensions.printPositions
import utils.models.Adjacent
import utils.models.Direction
import utils.models.Position
import kotlin.time.measureTime

object Day23: AocTask() {

    val moves = mutableListOf(
        Direction.UP,
        Direction.DOWN,
        Direction.LEFT,
        Direction.RIGHT
    )

    override fun executeTask() {
        measureTime {
            var elves = testInput.toElfPositions()
            val groundTiles = elves.groundTilesCountAfterMoves()
            println("There is $groundTiles empty ground tiles after 10 moves")
        }.let { println("Test part took $it\n") }

        measureTime {
            var elves = input.toElfPositions()
            val groundTiles = elves.groundTilesCountAfterMoves()
            println("There is $groundTiles empty ground tiles after 10 moves")
        }.let { println("Part 1 took $it\n") }

        measureTime {
            var elves = input.toElfPositions()
            val roundWithoutChanges = elves.findRoundWithoutChanges()
            println("First round when elves does not move = $roundWithoutChanges")
        }.let { println("Part 2 took $it\n") }
    }

    private fun Set<Position>.findRoundWithoutChanges(): Int {
        var elves = this
        var iteration = 1

        while (true) {
            val newElves = elves.moveElves()
            if (newElves == elves)
                return iteration
            elves = newElves
            iteration++
        }
    }

    private fun Set<Position>.groundTilesCountAfterMoves(moves: Int = 10): Int {
        var elves = this

        repeat(moves) {
            elves = elves.moveElves()
        }

        val rows = elves.maxOf { it.rowIndex } - elves.minOf { it.rowIndex } + 1
        val cols = elves.maxOf { it.colIndex } - elves.minOf { it.colIndex } + 1

        elves.toList().printPositions()

        return rows * cols - elves.size
    }

    private fun Set<Position>.moveElves(): Set<Position> {
        val list = toMutableList()
        val elvesToMove = list.mapIndexedNotNull { index, elf ->
            if (elf.adjacents.any { it in list }) index to elf
            else null
        }

        val newPositions = mutableSetOf<Pair<Int, Position>>()
        val overlaps = mutableSetOf<Int>()

        elvesToMove.forEach { (index, elf) ->
            val declared = newPositions.map { it.second }
            for (direction in moves) {
                if (direction.toAdjacents().all { elf + it !in this }) {
                    val newPosition = elf + direction
                    if (newPosition in declared){
                        overlaps.add(index)
                        overlaps.addAll(newPositions.filter { it.second == newPosition }.map { it.first })
                    } else {
                        newPositions.add(index to newPosition)
                    }
                    return@forEach
                }
            }
        }

        newPositions.removeAll { it.first in overlaps }
        newPositions.forEach { (index, position) ->
            list[index] = position
        }

        moves.add(moves.removeFirst())

        return list.toSet()
    }

    private fun String.toElfPositions(): Set<Position> = lines()
        .flatMapIndexed { rowIndex, row ->
            row.mapIndexedNotNull { colIndex, c ->
                if (c == '#') Position(colIndex, rowIndex)
                else null
            }
        }.toSet()

    private fun Direction.toAdjacents(): List<Adjacent> =
        when (this) {
            Direction.UP -> listOf(Adjacent.UP_LEFT, Adjacent.UP, Adjacent.UP_RIGHT)
            Direction.RIGHT -> listOf(Adjacent.UP_RIGHT, Adjacent.RIGHT, Adjacent.DOWN_RIGHT)
            Direction.DOWN -> listOf(Adjacent.DOWN_LEFT, Adjacent.DOWN, Adjacent.DOWN_RIGHT)
            Direction.LEFT -> listOf(Adjacent.UP_LEFT, Adjacent.LEFT, Adjacent.DOWN_LEFT)
            Direction.NONE -> emptyList()
        }
}