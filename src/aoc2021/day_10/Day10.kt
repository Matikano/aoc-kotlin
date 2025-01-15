package aoc2021.day_10

import utils.AocTask
import kotlin.time.measureTime

object Day10: AocTask() {

    private val errorMap = mapOf(
        ')' to 3,
        ']' to 57,
        '}' to 1197,
        '>' to 25137
    )

    private val bracketsMap = mapOf(
        '(' to ')',
        '[' to ']',
        '{' to '}',
        '<' to '>'
    )

    private const val OPENING_BRACKETS = "([{<"
    private const val CLOSING_BRACKETS = ")]}>"

    override fun executeTask() {
        measureTime {
            val lines = testInput.lines()
            println("Sum of errors = ${lines.sumOf { it.errorScore }}")
            println("Middle completion score = ${lines.middleCompletionScore}")
        }.let { println("Test part took $it\n") }

        with(input.lines()) {
            measureTime {
                println("Sum of errors = ${sumOf { it.errorScore }}")
            }.let { println("Part 1 took $it\n") }

            measureTime {
                println("Middle completion score = $middleCompletionScore")
            }.let { println("Part 2 took $it\n") }
        }
    }

    private val List<String>.middleCompletionScore: Long
        get() = filter { it.errorScore == 0 }
            .map { it.completionString.completionScore }
            .sorted()
            .let { list -> list[list.size / 2] }

    private val String.errorScore: Int
        get() {
            val stack = mutableListOf<Char>()

            forEach { char ->
                when (char) {
                    in OPENING_BRACKETS -> stack.add(0, char)
                    in CLOSING_BRACKETS -> {
                        val expected = stack.removeFirst()
                        if (bracketsMap[expected] != char)
                            return errorMap[char]!!
                    }
                }
            }

            return 0
        }

    private val String.completionString: String
        get() {
            val stack = mutableListOf<Char>()

            forEach { char ->
                when (char) {
                    in OPENING_BRACKETS -> stack.add(0, char)
                    in CLOSING_BRACKETS -> stack.removeFirst()
                }
            }

            return stack.map(bracketsMap::get).joinToString("")
        }

    private val String.completionScore: Long
        get() = fold(0) { acc, char -> 5 * acc + CLOSING_BRACKETS.indexOf(char) + 1 }
}