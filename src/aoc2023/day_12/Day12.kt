package aoc2023.day_12

import utils.AocTask
import utils.extensions.numsInt
import kotlin.time.measureTime

object Day12: AocTask() {

    override fun executeTask() {
        measureTime {
            val records = testInput.lines().map { it.toRecord() }
            println("Sum of possible arrangements = ${records.sumOf { it.possibleArrangements }}")
            println("Sum of possible unfolded arrangements = ${records.sumOf { it.possibleUnfoldedArrangements }}")
        }.let { println("Test part took $it\n") }

        with(inputToList().map { it.toRecord() }) {
            measureTime {
                println("Sum of possible arrangements = ${sumOf { it.possibleArrangements }}")
            }.let { println("Part 1 took $it\n") }

            measureTime {
                println("Sum of possible unfolded arrangements = ${sumOf { it.possibleUnfoldedArrangements }}")
            }.let { println("Part 2 took $it\n") }
        }
    }

    private fun String.toRecord(): Record {
        val (row, arrangementChain) = split(' ')
        return Record(
            row = row,
            arrangement = arrangementChain.numsInt()
        )
    }

    fun String.possibleReplacements(): List<String> {
        val replacementChars = "#."
        return when {
            isEmpty() -> listOf("")
            else -> {
                val firstPart = if (first() == '?') replacementChars
                else first().toString()

                return firstPart.flatMap { first ->
                    drop(1).possibleReplacements().map { second ->
                        first.toString() + second
                    }
                }
            }
        }
    }
}