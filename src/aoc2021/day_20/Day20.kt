package aoc2021.day_20

import utils.AocTask
import utils.models.Grid
import utils.models.Grid.Companion.toCharGrid
import utils.models.GridCell
import utils.models.Position
import kotlin.time.measureTime

object Day20: AocTask() {

    private const val PART_1_EXPANSION_COUNT = 2
    private const val PART_2_EXPANSION_COUNT = 50

    private const val LIGHT_PIXEL = '#'
    private const val DARK_PIXEL = '.'

    override fun executeTask() {
        measureTime {
            val (algorithm, image) = testInput.toAlgorithmAndImageGrid()
            val expandedImage = image.expandAndApplyAlgorithm(PART_1_EXPANSION_COUNT, algorithm)

            println("After $PART_1_EXPANSION_COUNT expansions:")
            expandedImage.print()

            val countOfLightPixels = expandedImage.cells.count { it.value == LIGHT_PIXEL }
            println("Lit pixels after $PART_1_EXPANSION_COUNT expansions = $countOfLightPixels")
        }.let { println("Test part took $it\n") }

        measureTime {
            val (algorithm, image) = input.toAlgorithmAndImageGrid()
            val expandedImage = image.expandAndApplyAlgorithm(PART_1_EXPANSION_COUNT, algorithm)

            val countOfLightPixels = expandedImage.cells.count { it.value == LIGHT_PIXEL }
            println("Lit pixels after $PART_1_EXPANSION_COUNT expansions = $countOfLightPixels")
        }.let { println("Part 1 took $it\n") }

        measureTime {
            val (algorithm, image) = input.toAlgorithmAndImageGrid()
            val expandedImage = image.expandAndApplyAlgorithm(PART_2_EXPANSION_COUNT, algorithm)

            val countOfLightPixels = expandedImage.cells.count { it.value == LIGHT_PIXEL }
            println("Lit pixels after $PART_2_EXPANSION_COUNT expansions = $countOfLightPixels")
        }.let { println("Part 2 took $it\n") }
    }

    private fun Grid<Char>.expandAndApplyAlgorithm(
        count: Int,
        imageAlgorithm: String
    ): Grid<Char> {
        var grid = this
        repeat(count) { iteration ->
            grid = grid.expand(iteration, imageAlgorithm)
            grid = grid.copy(
                cells = grid.cells.map { cell ->
                    cell.copy(
                        value = imageAlgorithm[grid.binaryMask(cell.position, iteration, imageAlgorithm)]
                    )
                }
            )
        }
        return grid
    }

    private fun Grid<Char>.binaryMask(
        position: Position,
        iteration: Int,
        imageAlgorithm: String
    ): Int =
        (position.adjacents + position)
            .sorted()
            .joinToString("") {
                (get(it)?.value?.toBinaryInt() ?: if (iteration % 2 == 0) 0 else imageAlgorithm.first().toBinaryInt()).toString()
            }.toInt(radix = 2)

    private fun String.toAlgorithmAndImageGrid(): Pair<String, Grid<Char>> {
        val (algorithmString, imageString) = trimIndent().split("\n\n")
        return algorithmString.replace("\n", "") to imageString.toCharGrid()
    }

    private fun Grid<Char>.expand(
        iteration: Int,
        imageAlgorithm: String
    ): Grid<Char> = copy(cells.expand(iteration, imageAlgorithm))

    private fun List<GridCell<Char>>.expand(
        iteration: Int,
        imageAlgorithm: String
    ): List<GridCell<Char>> {
        val paddingValue = if (iteration % 2 == 0) DARK_PIXEL else imageAlgorithm.first()
        val maxRow = maxOf { it.position.rowIndex }
        val maxCol = maxOf { it.position.colIndex }

        val paddingPositions = (0 .. maxRow +  2).flatMap { rowIndex ->
            setOf(
                Position(0, rowIndex),
                Position(maxCol + 2, rowIndex)
            )
        }.toSet() + (0 .. maxCol + 2).flatMap { colIndex ->
            setOf(
                Position(colIndex, 0),
                Position(colIndex, maxRow + 2)
            )
        }.toSet()

        val expandedLightPositions = filter { it.value == '#' }
            .map {
                it.position.copy(
                    it.position.colIndex + 1,
                    it.position.rowIndex + 1
                )
        }

        return (0 .. maxCol + 2).flatMap { colIndex ->
            (0 .. maxRow + 2).map { rowIndex ->
                val position = Position(colIndex, rowIndex)
                GridCell(
                    position = position,
                    value = when (position) {
                        in paddingPositions -> paddingValue
                        in expandedLightPositions -> LIGHT_PIXEL
                        else -> DARK_PIXEL
                    }
                )
            }
        }
    }

    private fun Char.toBinaryInt(): Int = when (this) {
        LIGHT_PIXEL -> 1
        DARK_PIXEL -> 0
        else -> throw IllegalArgumentException("Invalid char value = $this for binaryInt mapping")
    }
}