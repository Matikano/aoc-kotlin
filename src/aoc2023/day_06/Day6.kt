package aoc2023.day_06

import utils.AocTask
import utils.extensions.numsLong
import kotlin.time.measureTime

object Day6: AocTask() {

    override fun executeTask() {

        // Test part
        measureTime {
            val races = testInput.toRaces()
            val errorMargin = races.errorMargin()
            println("Error margin for test data = $errorMargin")

            val race = testInput.toRace()
            println("Single race = $race")
        }.let { println("Test part took $it\n") }

        // Part 1
        measureTime {
            val races = inputToString().toRaces()
            val errorMargin = races.errorMargin()
            println("Error margin = $errorMargin")
        }.let { println("Part 1 took $it\n") }

        // Part 2
        measureTime {
            val race = inputToString().toRace()
            println("Possible new records for a single race = ${race.possibleNewRecords}")
        }.let { println("Part 2 took $it\n") }
    }

    private fun String.toRace(): Race {
        val (timesLine, distancesLine) = lines()

        val time = timesLine.numsLong().joinToString("").toLong()
        val distance = distancesLine.numsLong().joinToString("").toLong()

        return Race(time = time, recordDistance = distance)
    }

    private fun String.toRaces(): List<Race> {
        val (timesLine, distancesLine) = lines()

        return timesLine.numsLong().zip(distancesLine.numsLong()) { time, distance ->
            Race(time = time, recordDistance = distance)
        }
    }

    private fun List<Race>.errorMargin(): Int =
        fold(1) { acc, race -> acc * race.possibleNewRecords }
}