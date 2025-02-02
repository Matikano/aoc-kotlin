package aoc2021.day_19

import aoc2021.day_19.Position3D.Companion.NUMBER_OF_3D_ROTATIONS
import utils.AocTask
import utils.extensions.numsInt
import utils.extensions.tail
import utils.extensions.uniquePairs
import kotlin.time.measureTime

object Day19: AocTask() {

    private const val OVERLAPPING_BEACONS_COUNT = 12

    override fun executeTask() {
        measureTime {
            val scanners = testInput.toListOfScanners()
            val (beaconPositions, scannerPositions) = scanners.scannerAndBeaconPositions()
            val maximumDistance = scannerPositions.toList().uniquePairs().maxOf { it.first.absoluteOrderedDistance(it.second).sum() }
            println("Total beacons: ${beaconPositions.size}")
            println("Maximum distance between scanners = $maximumDistance")
        }.let { println("Test part took $it\n") }

        measureTime {
            val scanners = input.toListOfScanners()
            val (beaconPositions, _) = scanners.scannerAndBeaconPositions()
            println("Total beacons: ${beaconPositions.size}")
        }.let { println("Part 1 took $it\n") }

        measureTime {
            val scanners = input.toListOfScanners()
            val (_, scannerPositions) = scanners.scannerAndBeaconPositions()
            val maximumDistance = scannerPositions.toList().uniquePairs().maxOf { it.first.absoluteOrderedDistance(it.second).sum() }
            println("Maximum distance between scanners = $maximumDistance")
        }.let { println("Part 2 took $it\n") }
    }

    private fun List<Scanner>.scannerAndBeaconPositions(): Pair<Set<Position3D>,Set<Position3D>> {
        val beaconPositions = mutableSetOf<Position3D>()
        val scannerPositions = mutableListOf<Position3D>(Position3D(0, 0, 0))
        val alignedScannersIndices = mutableSetOf(0)

        beaconPositions.addAll(first().positions)

        while (alignedScannersIndices.size < size) {
            for (index in 1 ..< size) {
                // Already aligned
                if (index in alignedScannersIndices)
                    continue

                val scannerPoints = get(index).positions
                for (rotation in 0..< NUMBER_OF_3D_ROTATIONS) {
                    val rotatedPoints = scannerPoints.map { it.rotate(rotation) }
                    for (refPoint in beaconPositions) {
                        for (rotatedPoint in rotatedPoints) {
                            val dx = refPoint.x - rotatedPoint.x
                            val dy = refPoint.y - rotatedPoint.y
                            val dz = refPoint.z - rotatedPoint.z

                            val translatedPoints = rotatedPoints.map { it.translate(dx, dy, dz) }
                            val matchingPoints = translatedPoints.intersect(beaconPositions)

                            if (matchingPoints.size >= OVERLAPPING_BEACONS_COUNT) {
                                beaconPositions.addAll(translatedPoints)
                                scannerPositions.add(Position3D(dx, dy, dz))
                                alignedScannersIndices.add(index)
                                // Scanner aligned, move to the next one
                                break
                            }
                        }
                        // Scanner aligned, move to the next one
                        if (index in alignedScannersIndices)
                            break
                    }
                    // Scanner aligned, move to the next one
                    if (index in alignedScannersIndices)
                        break
                }
            }
        }

        return beaconPositions to scannerPositions.toSet()
    }

    private fun String.toListOfScanners(): List<Scanner> = split("\n\n").mapIndexed { index, block ->
        block.toScanner(index)
    }

    private fun String.toScanner(index: Int): Scanner = lines().tail().map { line ->
        val (x, y, z) = line.numsInt()
        Position3D(x, y ,z)
    }.let { positions -> Scanner(index, positions) }
}