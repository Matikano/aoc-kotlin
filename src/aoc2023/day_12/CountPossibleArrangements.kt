package aoc2023.day_12

import utils.abstractions.RecursiveMemo2
import utils.extensions.head
import utils.extensions.tail

class CountPossibleArrangements(
    private val blockChar: Char,
    private val emptySpaceChar: Char,
    private val wildCardChar: Char
): RecursiveMemo2<String, List<Int>, Long>() {

    override fun MutableMap<Pair<String, List<Int>>, Long>.recurse(
        row: String,
        arrangement: List<Int>
    ): Long = getOrPut(
        row to arrangement
    ) {
        when {
            row.isEmpty() -> if (arrangement.isEmpty()) 1L else 0L

            arrangement.isEmpty() -> if (blockChar in row) 0L else 1L

            else -> {
                var result = 0L
                val expectedBlockSize = arrangement.first()

                if (row.head() in "$emptySpaceChar$wildCardChar") {
                    result += recurse(row.tail(), arrangement)
                }

                if (row.head() in "$blockChar$wildCardChar" &&
                    expectedBlockSize <= row.length &&
                    emptySpaceChar !in row.take(expectedBlockSize) &&
                    (expectedBlockSize == row.length || row[expectedBlockSize] != blockChar))
                        result += recurse(row.drop(expectedBlockSize + 1), arrangement.tail().toList())

                result
            }
        }
    }
}