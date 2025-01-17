package aoc2021.day_11

import utils.models.Grid
import utils.models.Position

data class OctopusGrid(
    var grid: Grid<Int>
) {

    fun makeSteps(count: Int): List<Int> {
        var flashes = mutableListOf<Int>()

        val queue = mutableListOf<Position>()
        var localGrid = grid.copy()

        repeat(count) {
            localGrid = localGrid.copy(
                cells = localGrid.cells.map { cell ->
                    cell.copy(value = cell.value + 1)
                }
            )

            val flashed = mutableSetOf<Position>()
            queue.addAll(localGrid.cells.filter { it.value > 9 }.map { it.position })

            while (queue.isNotEmpty()) {
                val position = queue.removeFirst()
                if (position in flashed)
                    continue
                else flashed.add(position)

                val adjacents = position.adjacents.filter { localGrid.isInBounds(it) }

                localGrid = localGrid.copy(
                    cells = localGrid.cells.map { cell ->
                        when (cell.position)  {
                            in adjacents -> {
                                if (cell.value >= 9)
                                    queue.add(cell.position)
                                cell.copy(value = cell.value + 1)
                            }
                            else -> cell
                        }
                    }
                )
            }

            localGrid = localGrid.copy(
                cells = localGrid.cells.map { cell ->
                    if (cell.value > 9) cell.copy(value = 0)
                    else cell
                }
            )

            flashes += flashed.size
        }

        return flashes
    }

    fun findStepWhenAllFlash(): Int {
        var step = 0
        var flashes = emptyList<Int>()

        while (flashes.firstOrNull { it == 100 } == null)
            flashes = makeSteps(++step)

        return step
    }
}
