package aoc2021.day_04

import utils.AocTask
import utils.extensions.head
import utils.extensions.numsInt
import utils.extensions.tail
import utils.models.Grid.Companion.toIntGrid
import kotlin.time.measureTime

object Day4: AocTask() {

    override fun executeTask() {
        measureTime {
            val (revealed, boards) = testInput.toRevealedAndBingoBoards()
            println("First winning score of test data = ${boards.getFirstWinningScore(revealed)}")
            println("Last winning score of test data = ${boards.getLastWinningScore(revealed)}")
        }.let { println("Test part took $it\n") }

        measureTime {
            val (revealed, boards) = input.toRevealedAndBingoBoards()
            println("First winning score = ${boards.getFirstWinningScore(revealed)}")
        }.let { println("Part 1 took $it\n") }

        measureTime {
            val (revealed, boards) = input.toRevealedAndBingoBoards()
            println("Last winning score = ${boards.getLastWinningScore(revealed)}")
        }.let { println("Part 2 took $it\n") }
    }

    private fun List<BingoBoard>.getFirstWinningScore(reveals: List<Int>): Int {
        var revealedCount = 5
        var score = 0

        while (score == 0) {
            val revealed = reveals.take(revealedCount++).toSet()
            score = firstOrNull { it.isWinning(revealed) }?.getScore(revealed) ?: 0
        }

        return score
    }

    private fun List<BingoBoard>.getLastWinningScore(reveals: List<Int>): Int {
        var revealedCount = 5
        var revealed = reveals.take(revealedCount).toSet()

        while (count { !it.isWinning(revealed) } != 1)
            revealed = reveals.take(revealedCount++).toSet()

        val indexOfLastWinning = indexOfLast { !it.isWinning(revealed) }

        while (!this[indexOfLastWinning].isWinning(revealed))
            revealed = reveals.take(revealedCount++).toSet()


        return this[indexOfLastWinning].getScore(revealed)
    }

    private fun String.toRevealedAndBingoBoards(): Pair<List<Int>, List<BingoBoard>> =
        with(split("\n\n")) {
            val revealed = head().numsInt()
            val bingoBoards = tail().map { it.segmentToBingoBoard() }

            revealed to bingoBoards
        }

    private fun String.segmentToBingoBoard(): BingoBoard = BingoBoard(grid = toIntGrid())
}