package aoc2024.day_25

import utils.AocTask
import utils.extensions.uniquePairs
import utils.models.Grid
import utils.models.Grid.Companion.transpose
import utils.models.GridCell
import utils.models.Position
import kotlin.time.measureTime

typealias Schematic = List<String>
typealias LockAndKeyHeights = Pair<List<Int>, List<Int>>

object Day25: AocTask() {

    private const val EMPTY = '.'
    private const val FILLED = '#'

    override fun executeTask() {
        // Test part
        measureTime {
            val (locks, keys) = testInput.toLocksAndKeys()
            val (lockHeights, keyHeights) = locks.map { it.toHeights() } to keys.map { it.toHeights() }
            // we need to first and last space sine they are baseline
            val maxHeight = locks.first().first().length - 2
            val allLockKeyPairs = uniquePairs(lockHeights, keyHeights)
            val uniqueFittingPairsCount = allLockKeyPairs.count { it.fits(maxHeight) }

            println("Fitting pairs count = $uniqueFittingPairsCount")
        }.let { println("Test part took $it\n") }

        // Part 1
        measureTime {
            val (locks, keys) = inputToString().toLocksAndKeys()
            val (lockHeights, keyHeights) = locks.map { it.toHeights() } to keys.map { it.toHeights() }
            // we need to first and last space sine they are baseline
            val maxHeight = locks.first().first().length - 2
            val allLockKeyPairs = uniquePairs(lockHeights, keyHeights)
            val uniqueFittingPairsCount = allLockKeyPairs.count { it.fits(maxHeight) }

            println("Fitting pairs count = $uniqueFittingPairsCount")
        }.let { println("Part 1 took $it\n") }

        // No Part 2 :c
    }

    private fun String.toLocksAndKeys(): Pair<List<Schematic>, List<Schematic>> {
        val locks = mutableListOf<Schematic>()
        val keys = mutableListOf<Schematic>()

        val segments = split("\n\n")

        segments.forEach { segment ->
            when {
                segment.startsWith(FILLED) -> locks.add(
                    segment.toGrid().transpose().toSchematic()
                )
                segment.startsWith(EMPTY) -> keys.add(
                    segment.toGrid().transpose().toSchematic()
                        .map { it.reversed() }
                )
            }
        }

        return locks to keys
    }

    private fun String.toGrid(): Grid<Char> =
        Grid(
            cells = lines().flatMapIndexed { rowIndex, row ->
                row.mapIndexed { colIndex, char ->
                    GridCell(
                        position = Position(colIndex, rowIndex),
                        value = char
                    )
                }
            }
        )

    private fun Grid<Char>.toSchematic(): Schematic =
        cells.windowed(width, width).map { row ->
            row.map { it.value }.joinToString("")
        }

    private fun Schematic.toHeights(): List<Int> =
        map { it.lastIndexOf(FILLED) }

    private fun LockAndKeyHeights.fits(maxHeight: Int): Boolean =
        first.zip(second) { a, b -> a + b }
            .all { heightsSum -> heightsSum <= maxHeight }
}