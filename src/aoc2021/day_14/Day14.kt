package aoc2021.day_14

import utils.AocTask
import utils.abstractions.RecursiveMemo1
import utils.extensions.head
import utils.extensions.tail
import kotlin.time.measureTime

object Day14: AocTask() {

    override fun executeTask() {

        measureTime {
            val (polymer, template) = testInput.toPolymerAndTemplate()
            println("Polymer score after 10 steps = ${polymer.polymerScoreAfterSteps(10, template)}")
            println("Polymer score after 40 steps = ${polymer.polymerScoreAfterSteps(40, template)}")
        }.let { println("Test part took $it\n") }

        measureTime {
            val (polymer, template) = input.toPolymerAndTemplate()
            println("Polymer score after 10 steps = ${polymer.polymerScoreAfterSteps(10, template)}")
        }.let { println("Part 1 took $it\n") }

        measureTime {
            val (polymer, template) = input.toPolymerAndTemplate()
            println("Polymer score after 40 steps = ${polymer.polymerScoreAfterSteps(40, template)}")
        }.let { println("Part 2 took $it\n") }
    }

    private fun String.toPolymerAndTemplate(): Pair<String, Map<String, String>> {
        val (polymer, template) = split("\n\n")
        return polymer to template.toPolymerTemplate()
    }

    private fun String.toPolymerTemplate(): Map<String, String> = buildMap {
        lines().forEach { line ->
            val (key, char) = line.split(" -> ")
            put(key, char)
        }
    }

    private fun String.polymerToPairCount(): Map<String, Long> = mutableMapOf<String, Long>().apply {
        this@polymerToPairCount.windowed(2).forEach { pair ->
            this[pair] = getOrDefault(pair, 0L) + 1L
        }
    }

    private fun Map<String, Long>.mutate(template: Map<String, String>): Map<String, Long> {
        val newMap = toMutableMap()
        keys.forEach { pair ->
            val count = this[pair]!!
            val newPair1 = "${pair.first()}${template[pair]!!}"
            val newPair2 = "${template[pair]!!}${pair.last()}"
            newMap[newPair1] = newMap.getOrDefault(newPair1, 0) + count
            newMap[newPair2] = newMap.getOrDefault(newPair2, 0) + count
        }
        keys.forEach { pair ->
            newMap[pair] = newMap[pair]!! - this[pair]!!
        }
        return newMap
    }

    private fun Map<String, Long>.toCharCount(firstChar: Char, lastChar: Char): Map<Char, Long> = mutableMapOf<Char, Long>().apply {
        this[firstChar] = 1
        this[lastChar] = 1

        this@toCharCount.forEach { (pair, count) ->
            this[pair.first()] = getOrDefault(pair.first(), 0) + count
            this[pair.last()] = getOrDefault(pair.last(), 0) + count
        }

        keys.forEach { char ->
            this[char] = this[char]!! / 2
        }
    }

    private fun String.polymerScoreAfterSteps(steps: Int, template: Map<String, String>): Long {
        val polymerPairs = polymerToPairCount()
        val pairsCount = polymerPairs.grow(steps, template)
        val charCount = pairsCount.toCharCount(first(), last())
        return  charCount.values.max() - charCount.values.min()
    }

    private fun Map<String, Long>.grow(steps: Int, template: Map<String, String>): Map<String, Long> {
        var map = this
        repeat(steps) {
            map = map.mutate(template)
        }
        return map
    }
}