package aoc2024.day_08

import aoc2024.AocTask
import kotlin.time.measureTime

object Day8: AocTask {
    override val fileName: String
        get() = "src/aoc2024/day_08/input.txt"

    override fun executeTask() {
        println("-------------------------------------")
        println("AoC 2024 Task ${this.javaClass.simpleName}")
        println()


        measureTime {
            // Part 1
            with(readToGrid()) {
                println("Part 1")
                putFirstAntinodes()
                println()
                println("Sum of antinodes = $antinodeCount")
            }
        }.let { println("Part 1 took $it \n") }

        measureTime {
            // Part 2
            with(readToGrid()) {
                println("Part 2")
                puAllAntinodes()
                println("Sum of antinodes = $antinodeCount")
            }
        }.let { println("Part 2 took $it\n") }

    }

    private fun readToGrid(): Grid =
        Grid(rows = readFileToList().toMutableList())
}