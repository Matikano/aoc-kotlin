package day_01

import readInput

const val TEST_VALUE = 24000

fun main() {

    fun findMaxCalories(input: String): Int {
        print(input.split("\n\n"))

        val elves = input
            .split("\n\n")
            .map { elf ->
                elf.lines()
                    .map {
                        it.toInt()
                    }
            }

        return elves.maxOf { it.sum() }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    println(findMaxCalories(testInput))
    check(findMaxCalories(testInput) == TEST_VALUE)
}
