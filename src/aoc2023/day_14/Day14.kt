package aoc2023.day_14

import utils.AocTask
import utils.models.Grid
import utils.models.Grid.Companion.toGrid
import utils.models.Position
import kotlin.time.measureTime

object Day14: AocTask() {

    private const val ROUND_ROCK = 'O'
    private const val CUBE_ROCK = '#'
    private const val EMPTY_SPACE = '.'
    private const val CYCLES_COUNT = 1000000000

    override fun executeTask() {
        with(input.toGrid()) {
            measureTime {
                val slided = slideRocksNorth()

                println("Total load of rounded rocks = ${slided.totalLoad()}")
            }.let { println("Part 1 took $it\n") }

            measureTime {
                val afterCycles = cycle(CYCLES_COUNT)

                println("Total load of rounded rocks after $CYCLES_COUNT cycles = ${afterCycles.totalLoad()}")
            }.let { println("Part 2 took $it\n") }
        }
    }

    private fun Grid<Char>.slideRocksNorth(): Grid<Char> {
        val startingRockPositions = roundRockPositions()
        val newRockPositions = startingRockPositions.map {
            it.positionAfterFallingNorth(startingRockPositions, cubeRockPositions())
        }
        val clearCells = cells.map { cell ->
            if (cell.position in startingRockPositions) cell.copy(value = EMPTY_SPACE)
            else cell
        }

        val newCells = clearCells.map { cell ->
            if (cell.position in newRockPositions) cell.copy(value = ROUND_ROCK)
            else cell
        }

        return Grid(newCells)
    }

    private fun Grid<Char>.cycle(cyclesCount: Int = 1): Grid<Char> {
        val cubeRocksPositions = cubeRockPositions()
        val startingPositions = roundRockPositions()
        var newRockPositions = startingPositions

        val uniqueCycles = mutableSetOf<Set<Position>>(startingPositions.toSet())
        val positionsStates = mutableListOf<Set<Position>>(startingPositions.toSet())
        var iteration = 0
        while (iteration < cyclesCount) {
            iteration++
            newRockPositions = newRockPositions.map {
                it.positionAfterFallingNorth(newRockPositions, cubeRocksPositions)
            }

            newRockPositions = newRockPositions.map {
                it.positionAfterFallingWest(newRockPositions, cubeRocksPositions)
            }

            newRockPositions = newRockPositions.map {
                it.positionAfterFallingSouth(newRockPositions, cubeRocksPositions, height)
            }

            newRockPositions = newRockPositions.map {
                it.positionAfterFallingEast(newRockPositions, cubeRocksPositions, width)
            }

            if (newRockPositions.toSet() in uniqueCycles)
                break

            uniqueCycles.add(newRockPositions.toSet())
            positionsStates.add(newRockPositions.toSet())
        }

        val indexOfFirstOccurrence = positionsStates.indexOf(newRockPositions.toSet())
        val indexOfDesiredState = (cyclesCount - indexOfFirstOccurrence) % (iteration - indexOfFirstOccurrence) + indexOfFirstOccurrence

        newRockPositions = positionsStates[indexOfDesiredState.toInt()].toList()

        val clearCells = cells.map { cell ->
            if (cell.position in startingPositions) cell.copy(value = EMPTY_SPACE)
            else cell
        }

        val newCells = clearCells.map { cell ->
            if (cell.position in newRockPositions) cell.copy(value = ROUND_ROCK)
            else cell
        }

        return Grid(newCells)
    }

    private fun Grid<Char>.totalLoad(): Int =
        roundRockPositions().sumOf { height - it.rowIndex }

    private fun Grid<Char>.roundRockPositions(): List<Position> =
        cells.filter { it.value == ROUND_ROCK }.map { it.position }

    private fun Grid<Char>.cubeRockPositions(): List<Position> =
        cells.filter { it.value == CUBE_ROCK }.map { it.position }

    private fun Position.positionAfterFallingNorth(
        currentPositions: List<Position>,
        cubeRocksPositions: List<Position>
    ): Position {
        var newRowIndex = cubeRocksPositions
            .findLast { it.colIndex == colIndex && it.rowIndex < rowIndex }?.rowIndex ?: -1
        newRowIndex += currentPositions.count { it.colIndex == colIndex && it.rowIndex in newRowIndex + 1..< rowIndex } + 1
        return Position(
            colIndex,
            newRowIndex,
        )
    }

    private fun Position.positionAfterFallingSouth(
        currentPositions: List<Position>,
        cubeRocksPositions: List<Position>,
        gridHeight: Int
    ): Position {
        var newRowIndex = cubeRocksPositions
            .find { it.colIndex == colIndex && it.rowIndex > rowIndex }?.rowIndex ?: gridHeight
        newRowIndex -= currentPositions.count { it.colIndex == colIndex && it.rowIndex in rowIndex + 1..< newRowIndex } + 1
        return Position(
            colIndex,
            newRowIndex,
        )
    }

    private fun Position.positionAfterFallingEast(
        currentPositions: List<Position>,
        cubeRocksPositions: List<Position>,
        gridWidth: Int
    ): Position {
        var newColIndex = cubeRocksPositions
            .find { it.rowIndex == rowIndex && it.colIndex > colIndex }?.colIndex ?: gridWidth
        newColIndex -= currentPositions.count { it.rowIndex == rowIndex && it.colIndex in colIndex + 1..< newColIndex } + 1
        return Position(
            newColIndex,
            rowIndex,
        )
    }

    private fun Position.positionAfterFallingWest(
        currentPositions: List<Position>,
        cubeRocksPositions: List<Position>
    ): Position {
        var newColIndex = cubeRocksPositions
            .findLast { it.rowIndex == rowIndex && it.colIndex < colIndex }?.colIndex ?: -1
        newColIndex += currentPositions.count { it.rowIndex == rowIndex && it.colIndex in newColIndex + 1..< colIndex } + 1
        return Position(
            newColIndex,
            rowIndex,
        )
    }
}