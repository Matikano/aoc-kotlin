package aoc2024.day_08

import utils.AocTask
import kotlin.time.measureTime

object Day8: AocTask() {

    override fun executeTask() {
        measureTime {
            // Part 1
            with(readToGrid()) {
                putFirstAntinodes()
                println("Sum of antinodes = $antinodeCount")
            }
        }.let { println("Part 1 took $it \n") }

        measureTime {
            // Part 2
            with(readToGrid()) {
                puAllAntinodes()
                println("Sum of antinodes = $antinodeCount")
            }
        }.let { println("Part 2 took $it\n") }

    }

    private fun readToGrid(): Grid =
        Grid(rows = inputToList().toMutableList())
}