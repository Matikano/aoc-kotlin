package aoc2023.day_05

import utils.AocTask
import utils.extensions.head
import utils.extensions.numsLong
import utils.extensions.tail
import kotlin.math.max
import kotlin.math.min
import kotlin.time.measureTime


object Day5: AocTask() {

    override fun executeTask() {
        // Test part
        measureTime {
            val (seeds, blocks) = testInput.toSeedsAndBlocks()
            val seedsToLocations = mapSeedsToLocations(seeds, blocks)

            println("Seeds to locations map = $seedsToLocations")

            val (seedRanges, _) = testInput.toSeedRangesAndBlocks()
            println("Seed ranges = $seedRanges")
            val locationRanges = seedRanges.getLocationRanges(blocks)
            println("Location ranges = $locationRanges")
            val closestLocation = locationRanges.minOf { it.start }
            println("Closes test location for seed ranges = $closestLocation")
        }.let { println("Test part took $it\n") }

        // Part 1
        measureTime {
            val (seeds, blocks) = inputToString().toSeedsAndBlocks()
            val seedsToLocations = mapSeedsToLocations(seeds, blocks)
            val closestLocation = seedsToLocations.values.min()

            println("Closest location = $closestLocation")
        }.let { println("Part 1 took = $it\n") }

        // Part 2
        measureTime {
            val (seedRanges, blocks) = inputToString().toSeedRangesAndBlocks()
            val locationRanges = seedRanges.getLocationRanges(blocks)
            val closestLocation = locationRanges.minOf { it.start }
            println("Closes location for seed ranges = $closestLocation")
        }.let { println("Part 2 took = $it\n") }
    }

    private fun String.toSeedsAndBlocks(): Pair<List<Long>, List<String>> {
        val segments = split("\n\n")
        return segments.head().numsLong() to segments.tail().toList()
    }

    private fun String.toSeedRangesAndBlocks(): Pair<List<LongRange>, List<String>> {
        val segments = split("\n\n")
        val seedRanges = segments.head()
            .numsLong()
            .windowed(size = 2, step = 2)
            .map { (start, length) -> start..start+length }

        return seedRanges to segments.tail().toList()
    }

    private fun String.blockToRanges(): List<List<Long>> =
        lines().map { it.numsLong() }.filter { it.isNotEmpty() }

    private fun mapSeedsToLocations(seeds: List<Long>, blocks: List<String>): Map<Long, Long> {
        var sources = seeds

        blocks.forEachIndexed { index, block ->
            val newSources = mutableListOf<Long>()
            sources.forEach { source ->
                var found = false
                block.blockToRanges().forEach { (destinationStart, sourceStart, rangeLength) ->
                    if (found)
                        return@forEach
                    if (source in sourceStart..sourceStart + rangeLength) {
                        newSources.add(source - sourceStart + destinationStart)
                        found = true
                    }
                }
                if (!found)
                    newSources.add(source)
            }
            sources = newSources
        }

        return seeds.zip(sources).associate { it.first to it.second }
    }

    private fun List<LongRange>.getLocationRanges(
        blocks: List<String>
    ): List<LongRange> {
        var ranges = mutableListOf(*this.toTypedArray())

        blocks.forEachIndexed { index, block ->
            val newRanges = mutableListOf<LongRange>()
            while (ranges.isNotEmpty()) {
                val range: LongRange = ranges.removeAt(0)
                val (rangeStart, rangeEnd) = range.start to range.endInclusive
                var found = false
                block.blockToRanges().forEach { (destinationStart, sourceStart, rangeLength) ->
                    val overlapStart = max(rangeStart, sourceStart)
                    val overlapEnd = min(rangeEnd, sourceStart + rangeLength)
                    if (overlapStart < overlapEnd) {
                        found = true
                        val newRangeStart = overlapStart - sourceStart + destinationStart
                        val newRangeEnd = overlapEnd - sourceStart + destinationStart
                        newRanges.add(newRangeStart ..< newRangeEnd)
                        if (overlapStart > rangeStart)
                            ranges.add(rangeStart..< overlapStart)
                        if (overlapEnd < rangeEnd)
                            ranges.add(overlapEnd..< rangeEnd)
                        return@forEach
                    }
                }
                if (!found) newRanges.add(range)
            }
            ranges = newRanges
        }

        return ranges
    }
}