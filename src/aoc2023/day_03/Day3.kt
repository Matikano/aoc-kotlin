package aoc2023.day_03

import utils.AocTask
import utils.extensions.uniquePairs
import utils.models.Adjacent
import utils.models.Direction
import utils.models.Grid
import utils.models.Grid.Companion.toCharGrid
import utils.models.Position
import kotlin.time.measureTime


object Day3: AocTask() {

    override fun executeTask() {
        with(inputToString().toCharGrid()) {
            // Part 1
            measureTime {
                val numbersInGrid = toNumbersInGrid()
                val symbolPositions = symbolPositions()
                val validNumbersInGrid = numbersInGrid.filter {
                    it.surroundingPositions.any { position -> position in symbolPositions }
                }.map { it.value }

                println("Sum of valid numbers in grid = ${validNumbersInGrid.sum()}")
            }.let { println("Part 1 took $it\n") }

            // Part 2
            measureTime {
                val numbersInGrid = toNumbersInGrid()
                val starPositions = starPositions()
                val numbersToMultiply = numbersInGrid.uniquePairs()
                    .filter { (first, second) ->
                        starPositions.any { it in first.surroundingPositions && it in second.surroundingPositions }
                    }.map { it.first.value to it.second.value }

                val sumOfMultiplications = numbersToMultiply.fold(0) { acc, (first, second) ->
                    acc + first * second
                }

                println("Sum of multiplication of gear numbers in grid = $sumOfMultiplications")
            }.let { println("Part 2 took $it\n") }
        }
    }

    private fun Grid<Char>.symbolPositions(): List<Position> =
        cells.filter { !it.value.isDigit() && it.value != '.' }
            .map { it.position }

    private fun Grid<Char>.starPositions(): List<Position> =
        cells.filter { it.value == '*' }
            .map { it.position }

    private fun Grid<Char>.toNumbersInGrid(): List<NumberInGrid> {
        val numbers = mutableListOf<NumberInGrid>()
        var loopSkips = 0
        cells.forEach { cell ->
            if (loopSkips > 0) {
                loopSkips--
                return@forEach
            }
            var numberString = ""
            var numberPositions = mutableSetOf<Position>()
            if (!cell.value.isDigit())
                return@forEach

            numberString += cell.value
            numberPositions += cell.position
            var nextPosition = cell.position + Direction.RIGHT
            var nextCell = this[nextPosition]
            while (isInBounds(nextPosition) && nextCell?.value?.isDigit() == true) {
                numberString += nextCell.value
                numberPositions += nextCell.position

                nextPosition += Direction.RIGHT
                nextCell = this[nextPosition]
            }

            loopSkips = numberString.length - 1
            val numberSurroundingPositions = numberPositions.flatMap { position ->
                Adjacent.validEntries.map {
                    position + it
                }
            }.filter { isInBounds(it) }
                .toSet() - numberPositions

            numbers.add(
                NumberInGrid(
                    value = numberString.toInt(),
                    surroundingPositions = numberSurroundingPositions
                )
            )
        }

        return numbers
    }
}