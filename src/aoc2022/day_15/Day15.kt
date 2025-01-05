package aoc2022.day_15

import utils.AocTask
import utils.extensions.numsInt
import utils.models.Position
import java.util.Comparator
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.time.measureTime

object Day15: AocTask() {

    private val DISTRESS_SIGNAL_BOUNDARY = 0..4000000
    private val TEST_DISTRESS_SIGNAL_BOUNDARY = 0..20
    private const val TUNING_FREQUENCY_COLUMN_FACTOR = 4000000L

    override fun executeTask() {
        measureTime {
            val sensors = testInput.toSensors()
            val rowIndex = 10
            val count = sensors.impossibleBeaconPositionsCountAtRow(rowIndex)
            println("Number of impossible beacon positions for row $rowIndex = $count")
            val distressSignalPosition = sensors.findDistressSignalPosition(TEST_DISTRESS_SIGNAL_BOUNDARY)
            println("Distress signal position = $distressSignalPosition, its tuning frequency = ${distressSignalPosition.tuningFrequency()}")
        }.let { println("Test part took $it\n") }

        measureTime {
            val sensors = input.toSensors()
            val rowIndex = 2000000
            val count = sensors.impossibleBeaconPositionsCountAtRow(rowIndex)
            println("Number of impossible beacon positions for row $rowIndex = $count")
        }.let { println("Part 1 took $it\n") }

        measureTime {
            val sensors = input.toSensors()
            val distressSignal = sensors.findDistressSignalPosition(DISTRESS_SIGNAL_BOUNDARY)
            println("Distress signal position = $distressSignal")
            println("Distress signal tuning frequency = ${distressSignal.tuningFrequency()}")
        }.let { println("Test part took $it\n") }
    }

    private fun Position.tuningFrequency(): Long =
        colIndex * TUNING_FREQUENCY_COLUMN_FACTOR + rowIndex

    private fun List<Sensor>.impossibleBeaconPositionsCountAtRow(
        rowIndex: Int,
        colRange: IntRange = minOf { it.closestBeacon.colIndex } .. maxOf { it.closestBeacon.colIndex }
    ): Int {
        val beacons = map { it.closestBeacon }
        var count = 0

        for (colIndex in colRange) {
            val position = Position(colIndex, rowIndex)
            if (position in beacons)
                continue
            if (any { it.position.distance(position) <= it.radius })
                count++
        }

        return count
    }

    private fun List<Sensor>.findDistressSignalPosition(boundary: IntRange): Position {

        boundary.forEach { rowIndex ->
            var intervals = mutableListOf<Pair<Int, Int>>()
            for (sensor in this) {
                val o = sensor.radius - (sensor.position.rowIndex - rowIndex).absoluteValue

                if (o < 0)
                    continue

                val lowColIndex = sensor.position.colIndex - o
                val highColIndex = sensor.position.colIndex + o

                intervals.add(lowColIndex to highColIndex)
            }

            intervals = intervals.sortedWith(PairComparator).toMutableList()

            val queue = mutableListOf<MutableList<Int>>()

            for ((lo, hi) in intervals) {
                if (queue.isEmpty()) {
                    queue.add(mutableListOf(lo, hi))
                    continue
                }

                val (_, qhi) = queue.last()

                if (lo > qhi + 1) {
                    queue.add(mutableListOf(lo, hi))
                    continue
                }

                queue[queue.size - 1][1] = max(qhi, hi)
            }

            var colIndex = 0

            for ((lo, hi) in queue) {
                if (colIndex < lo)
                    return Position(colIndex, rowIndex)
                colIndex = max(colIndex, hi + 1)
                if (colIndex > boundary.last)
                    break
            }
        }

        return Position(0, 0)
    }

    private fun String.toSensors(): List<Sensor> = lines().map { it.toSensor() }

    private fun String.toSensor(): Sensor {
        val (sensorColIndex, sensorRowIndex, beaconColIndex, beaconRowIndex) = numsInt()
        return Sensor(
            position = Position(sensorColIndex, sensorRowIndex),
            closestBeacon = Position(beaconColIndex, beaconRowIndex)
        )
    }

    object PairComparator: Comparator<Pair<Int, Int>> {
        override fun compare(
            o1: Pair<Int, Int>,
            o2: Pair<Int, Int>
        ): Int {
            val firstComparison = o1.first.compareTo(o2.first)
            return if (firstComparison != 0) firstComparison
            else  o1.second.compareTo(o2.second)
        }
    }
}