package aoc2022.day_08

import utils.AocTask
import utils.models.Grid
import utils.models.GridCell
import utils.models.Position
import kotlin.time.measureTime

object Day8: AocTask() {

    override fun executeTask() {
        measureTime {
            val grid = testInput.toIntGrid()
            println("Test result = ${grid.visibleTreesCount()}")
            println("Best test tree score = ${grid.bestTreeScore()}")
        }.let { println("Test part took $it\n") }

        measureTime {
            val grid = input.toIntGrid()
            println("Visible trees count = ${grid.visibleTreesCount()}")
        }.let { println("Part 1 took $it\n") }

        measureTime {
            val grid = input.toIntGrid()
            println("Best tree score = ${grid.bestTreeScore()}")
        }.let { println("Part 2 took $it\n") }
    }

    private fun Grid<Int>.bestTreeScore(): Int =
        cells.maxOf { cell ->
            val (currentColIndex, currentRowIndex) = cell.position
            val rowTreesBefore = cells.filter { it.position.rowIndex == currentRowIndex
                    && it.position.colIndex < currentColIndex }
            val rowTreesAfter = cells.filter { it.position.rowIndex == currentRowIndex
                    && it.position.colIndex > currentColIndex }
            val colTreesBefore = cells.filter { it.position.colIndex == currentColIndex
                    && it.position.rowIndex < currentRowIndex }
            val colTreesAfter = cells.filter { it.position.colIndex == currentColIndex
                    && it.position.rowIndex > currentRowIndex }

            val visibleRowBefore = currentColIndex - (rowTreesBefore.lastOrNull { it.value >= cell.value }
                ?.position?.colIndex ?: 0)

            val visibleRowAfter = (rowTreesAfter.firstOrNull { it.value >= cell.value }
                ?.position?.colIndex
                    ?: cells.maxOf { it.position.colIndex }) - currentColIndex

            val visibleColBefore = currentRowIndex - (colTreesBefore.lastOrNull { it.value >= cell.value }
                ?.position?.rowIndex ?: 0)

            val visibleColAfter = (colTreesAfter.firstOrNull { it.value >= cell.value }
                ?.position?.rowIndex
                ?: cells.maxOf { it.position.rowIndex }) - currentRowIndex

            visibleRowBefore * visibleRowAfter * visibleColBefore * visibleColAfter
        }

    private fun Grid<Int>.visibleTreesCount(): Int =
        cells.count { cell ->
            val rowTreesBefore = cells.filter { it.position.rowIndex == cell.position.rowIndex
                    && it.position.colIndex < cell.position.colIndex }
            val rowTreesAfter = cells.filter { it.position.rowIndex == cell.position.rowIndex
                    && it.position.colIndex > cell.position.colIndex}
            val colTreesBefore = cells.filter { it.position.colIndex == cell.position.colIndex
                    && it.position.rowIndex < cell.position.rowIndex }
            val colTreesAfter = cells.filter { it.position.colIndex == cell.position.colIndex
                    && it.position.rowIndex > cell.position.rowIndex }

            rowTreesBefore.all { it.value < cell.value } ||
                    rowTreesAfter.all { it.value < cell.value } ||
                    colTreesBefore.all { it.value < cell.value } ||
                    colTreesAfter.all { it.value < cell.value }
        }

    private fun String.toIntGrid(): Grid<Int> =
        Grid(
            cells = lines().flatMapIndexed { rowIndex, row ->
                row.mapIndexed { colIndex, char ->
                    GridCell(
                        position = Position(colIndex, rowIndex),
                        value = char.digitToInt()
                    )
                }
            }
        )
}