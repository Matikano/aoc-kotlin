package aoc2022.day_16

import aoc2022.day_16.ValveDfs.SimulationState
import utils.abstractions.RecursiveMemo1
import kotlin.math.max

class ValveDfs(
    val distances: Map<String, Map<String, Int>>,
    val flows: Map<String, Int>,
    val nonEmptyIndices: Map<String, Int>
): RecursiveMemo1<SimulationState, Int>() {
    override fun MutableMap<SimulationState, Int>.recurse(data: SimulationState): Int = getOrPut(data) {
        var maxPressureReleased = 0
        val (time, valve, bitmask) = data

        for ((neighbour, distance) in distances[valve]!!) {
            val bit = 1 shl nonEmptyIndices[neighbour]!!
            if (bitmask and bit > 0)
                continue
            val remainingTime = time - distance - 1
            if (remainingTime <= 0)
                continue
            val state = SimulationState(remainingTime, neighbour, bitmask or bit)
            maxPressureReleased = max(maxPressureReleased, recurse(state) + flows[neighbour]!! * remainingTime)
        }

        maxPressureReleased
    }

    data class SimulationState(
        val time: Int,
        val valve: String,
        val bitmask: Int
    )
}
