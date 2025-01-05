package aoc2022.day_14

import utils.AocTask
import utils.extensions.isValid
import utils.extensions.numsInt
import utils.extensions.subListsWithOneDroppedElement
import utils.models.Grid
import utils.models.GridCell
import utils.models.Position
import kotlin.math.max
import kotlin.math.min
import kotlin.time.measureTime

object Day14: AocTask() {

    private const val SAND_DROP_COLUMN = 500

    override fun executeTask() {
        measureTime {
            val (edgePointsNormalized, dropIndex) = testInput.toAllEdgePositions().normalize()
            println("Drop index = $dropIndex")
            println(edgePointsNormalized)
            val cave = Cave(
                rockPositions = edgePointsNormalized,
                dropColIndex = dropIndex
            )
            println("Cave drop position = ${cave.dropPosition}")
            val restedSand = cave.fillCaveWithSand()
            println("Number of rested sand = $restedSand")

            val caveWithFloor = testInput.toAllEdgePositions().normalizeWithFloor().let { (edges, dropIndex) ->
                Cave(
                    rockPositions = edges,
                    dropColIndex = dropIndex
                )
            }
            val restedSandFloor = caveWithFloor.fillFloorCaveWithSand()
            println("Number of rested sand with floor Cave = $restedSandFloor")
        }.let { println("Test part took $it\n") }

        measureTime {
            val (edgePointsNormalized, dropIndex) = input.toAllEdgePositions().normalize()
            val cave = Cave(
                rockPositions = edgePointsNormalized,
                dropColIndex = dropIndex
            )
            val restedSand = cave.fillCaveWithSand()
            println("Number of rested sand = $restedSand")
        }.let { println("Part 1 took $it\n") }

        measureTime {
            val caveWithFloor = input.toAllEdgePositions().normalizeWithFloor().let { (edges, dropIndex) ->
                Cave(
                    rockPositions = edges.toSet(),
                    dropColIndex = dropIndex
                )
            }
            val restedSandFloor = caveWithFloor.fillFloorCaveWithSand()
            println("Number of rested sand with floor Cave = $restedSandFloor")
        }.let { println("Part 2 took $it\n") }
    }

    private fun List<Position>.rockPositionsToGrid(): Grid<Char> {
        val maxRowIndex = maxOf { it.rowIndex }
        val maxColIndex = maxOf { it.colIndex }

        return Grid(
            cells = (0..maxRowIndex).flatMap { rowIndex ->
                (0..maxColIndex).map { colIndex ->
                    val position = Position(colIndex = colIndex, rowIndex = rowIndex)
                    GridCell(
                        position = position,
                        value = if (position in this) '#' else '.'
                    )
                }
            }
        )
    }

    private fun Set<Position>.normalize(): Pair<Set<Position>, Int> =
        minOf { it.colIndex }.let { minColIndex ->
            map { position ->
                position.copy(
                    colIndex = position.colIndex - minColIndex,
                    rowIndex = position.rowIndex
                )
            }.toSet() to SAND_DROP_COLUMN - minColIndex
        }

    private fun Set<Position>.normalizeWithFloor(): Pair<Set<Position>, Int> {
        val floorRowIndex = maxOf { it.rowIndex } + 2
        val minColIndex = SAND_DROP_COLUMN - floorRowIndex - 1
        val maxColIndex = SAND_DROP_COLUMN + floorRowIndex + 1

        val floorPositions = (minColIndex..maxColIndex).map { floorColIndex ->
            Position(floorColIndex, floorRowIndex)
        }

        return (floorPositions + this).map { position ->
            position.copy(
                colIndex = position.colIndex - minColIndex,
                rowIndex = position.rowIndex
            )
        }.toSet() to SAND_DROP_COLUMN - minColIndex
    }

    private fun String.toAllEdgePositions(): Set<Position> =
        lines().flatMap { it.toCorners().toEdgePositions() }.toSet()

    private fun String.toCorners(): List<Position> =
        numsInt()
            .windowed(2, 2)
            .map { (colIndex, rowIndex) ->
                Position(colIndex, rowIndex)
            }

    private fun List<Position>.toEdgePositions() =
        windowed(2, 1).flatMap { (first, second) -> first.edgePoints(second, maxOf { it.rowIndex } + 2) }

    private fun Position.edgePoints(otherCorner: Position, maxRowIndex: Int): Set<Position> =
        when {
            colIndex == otherCorner.colIndex ->
                (min(rowIndex, otherCorner.rowIndex)..max(rowIndex, otherCorner.rowIndex)).map { edgeRowIndex ->
                    Position(colIndex, edgeRowIndex)
                }

            rowIndex == otherCorner.rowIndex ->
                (min(colIndex, otherCorner.colIndex)..max(colIndex, otherCorner.colIndex)).map { edgeColIndex ->
                    Position(edgeColIndex, rowIndex)
                }

            else -> throw IllegalStateException("Corners do not lay on the same column or row")
        }.toSet()
}