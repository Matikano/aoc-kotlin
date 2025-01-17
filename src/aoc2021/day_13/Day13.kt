package aoc2021.day_13

import aoc2021.day_13.Fold.Companion.toFold
import utils.AocTask
import utils.extensions.numsInt
import utils.extensions.printPositions
import utils.models.Position
import kotlin.time.measureTime

object Day13: AocTask() {

    override fun executeTask() {
        measureTime {
            var (dots, folds) = testInput.toDotsAndFolds()
            folds.forEach { dots = it.fold(dots) }
            println("Number of dots after all folds = ${dots.size}")
        }.let { println("Test part took $it\n") }

        measureTime {
            var (dots, folds) = input.toDotsAndFolds()
            dots = folds.first().fold(dots)
            println("Number of dots after 1 fold = ${dots.size}")
        }.let { println("Part 1 took $it\n") }

        measureTime {
            var (dots, folds) = input.toDotsAndFolds()
            folds.forEach { dots = it.fold(dots) }
            dots.toList().printPositions()
        }.let { println("Part 2 took $it\n") }
    }

    private fun String.toDotsAndFolds(): Pair<Set<Position>, List<Fold>> {
        val (dotsSegment, foldsSegment) = split("\n\n")
        return dotsSegment.toDots() to foldsSegment.toFolds()
    }

    private fun String.toFolds(): List<Fold> = lines().map { it.toFold() }
    private fun String.toDots(): Set<Position> =
        lines().map { line ->
            val (colIndex, rowIndex) = line.numsInt()
            Position(colIndex = colIndex, rowIndex = rowIndex)
        }.toSet()
}