package aoc2021.day_04

import utils.models.Grid

data class BingoBoard(
    val grid: Grid<Int>
) {

    val rows: List<Set<Int>> = grid.cells
        .groupBy { it.position.rowIndex }
        .map { it.value.map { it.value }.toSet() }

    val cols: List<Set<Int>> = grid.cells
        .groupBy { it.position.colIndex }
        .map { it.value.map { it.value }.toSet() }

    fun isWinning(revealed: Set<Int>): Boolean =
        rows.any { revealed.containsAll(it) }
                || cols.any { revealed.containsAll(it) }

    fun getScore(revealed: Set<Int>): Int =
        if (isWinning(revealed))
            revealed.last() * grid.cells.map { it.value }.filter { it !in revealed }.sum()
        else 0
}
