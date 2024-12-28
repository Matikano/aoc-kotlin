package aoc2023.day_13

import utils.AocTask
import utils.extensions.transpose
import kotlin.math.min
import kotlin.time.measureTime

object Day13: AocTask() {

    private const val HORIZONTAL_REFLECTION_MULTIPLIER = 100

    override fun executeTask() {

        val first = "..####."
        val second = "..###.#"

        println(first.difference(second))

        measureTime {
            val mirrors = testInput.toMirrors()
            println("Sum of mirror scores = ${mirrors.sumOf { it.mirrorScore() }}")
            println("Sum of single smudge mirror scores = ${mirrors.sumOf { it.smudgeMirrorScore() }}")
        }.let { println("Test part took $it\n") }

        // Part 1
        measureTime {
            val mirrors = input.toMirrors()
            println("Sum of mirror scores = ${mirrors.sumOf { it.mirrorScore() }}")
        }.let { println("Part 1 took $it\n") }

        // Part 2
        measureTime {
            val mirrors = input.toMirrors()
            println("Sum of single smudge mirror scores = ${mirrors.sumOf { it.smudgeMirrorScore() }}")
        }.let { println("Part 2 took $it\n") }
    }

    private fun String.toMirrors(): List<String> = split("\n\n")

    private fun String.indexOfHorizontalMirror(): Int {
        val lines = lines()

        for (index in 1..lines.size - 1) {
            val top = lines.take(index).reversed()
            val bottom = lines.drop(index)
            val trim = min(top.size, bottom.size)

            if (top.take(trim) == bottom.take(trim)) {
                return index
            }
        }
        return 0
    }

    private fun String.indexOfHorizontalMirrorWithSmudge(): Int {
        val lines = lines()

        for (index in 1..lines.size - 1) {
            val top = lines.take(index).reversed()
            val bottom = lines.drop(index)
            val trim = min(top.size, bottom.size)

            val sumOfDifferences = top.take(trim).zip(bottom.take(trim)) { a, b ->
                a.difference(b)
            }.sum()

            if (sumOfDifferences == 1) {
                return index
            }
        }
        return 0
    }

    private fun String.difference(other: String): Int =
        zip(other) { a, b -> if (a == b) 0 else 1 }.sum()

    private fun String.mirrorScore(): Int =
        indexOfHorizontalMirror() * HORIZONTAL_REFLECTION_MULTIPLIER + transpose().indexOfHorizontalMirror()

    private fun String.smudgeMirrorScore(): Int =
        indexOfHorizontalMirrorWithSmudge() * HORIZONTAL_REFLECTION_MULTIPLIER + transpose().indexOfHorizontalMirrorWithSmudge()
}