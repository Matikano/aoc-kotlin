package aoc2021.day_19

import utils.extensions.uniquePairs
import utils.models.Position
import kotlin.Triple

data class Scanner(
    val index: Int,
    val positions: List<Position3D>
) {
    override fun toString(): String = positions.joinToString("\n") + "\n"

    val distancesBetweenBeaconPairs: List<BeaconDistance>
        get() = positions.uniquePairs().map {
            BeaconDistance(
                indexOfFirst = positions.indexOf(it.first),
                indexOfSecond = positions.indexOf(it.second),
                distance = it.first.absoluteOrderedDistance(it.second)
            )
        }

    fun overlappingIndices(otherScanners: List<Scanner>): Set<Int> {
        val scanners = otherScanners.minus(this)
        val indices = mutableSetOf<Int>()

        scanners.forEach { other ->
            if (overlaps(other))
                indices.addAll(overlappingBeaconsIndices(other))
        }

        return indices
    }

    fun overlappingBeaconsIndicesPairs(other: Scanner) =
        distancesBetweenBeaconPairs
            .mapNotNull { firstDistance ->
                val sameBeacons = other.distancesBetweenBeaconPairs.filter { it.distance == firstDistance.distance }
                assert(sameBeacons.size == 1) { "Found multiple pairs of beacons with same distance between them" }
                sameBeacons.first()?.let { otherDistance ->
                    Pair(
                        Triple(index, firstDistance.indexOfFirst, firstDistance.indexOfSecond),
                        Triple(other.index, otherDistance.indexOfFirst, otherDistance.indexOfSecond),
                    )
                }
            }.toSet()

    fun overlappingBeaconsIndices(other: Scanner): List<Int> =
        distancesBetweenBeaconPairs
            .filter { it.distance in other.distancesBetweenBeaconPairs.map { it.distance } }
        .flatMap { it.indices }
        .distinct()

    fun overlappingBeacons(other: Scanner): List<Pair<Int, Position3D>> =
        overlappingBeaconsIndices(other)
            .let { overlappingBeaconIndices ->
                positions.mapIndexedNotNull { index, pos ->
                    (index to pos).takeIf { index in overlappingBeaconIndices }
                }
            }

    fun overlapCount(other: Scanner): Int =
        overlappingBeacons(other).count()

    fun overlaps(other: Scanner): Boolean =
        overlapCount(other) >= 12

    data class BeaconDistance(
        val indexOfFirst: Int,
        val indexOfSecond: Int,
        val distance: List<Int>
    ) {
        override fun toString(): String = "($indexOfFirst,$indexOfSecond)-$distance"
        val indices = listOf(indexOfFirst, indexOfSecond)
    }
}

