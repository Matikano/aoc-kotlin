package aoc2022.day_16

import aoc2022.day_16.ValveDfs.SimulationState
import kotlin.math.max

data class ValveGraph(
    val flows: Map<String, Int>,
    val connections: Map<String, Set<String>>
) {
    var distances: Map<String, Map<String, Int>>
        private set

    private val nonEmpty: MutableList<String> = mutableListOf()

    private val nonEmptyIndices: Map<String, Int>
        get() = nonEmpty.mapIndexed { index, entry -> entry to index  }.toMap()

    init {
        distances = flattenGraph()
    }

    private val valveDfs = ValveDfs(distances, flows, nonEmptyIndices)

    private fun flattenGraph(): Map<String, Map<String, Int>> {
        val distances = mutableMapOf<String, MutableMap<String, Int>>()

        flows.forEach { (valve, flow) ->
            val seen = mutableSetOf<String>()

            // Skip empty valves
            if (valve != STARTING_VALVE && flow == 0)
                return@forEach

            nonEmpty.add(valve)

            distances[valve] = mapOf(valve to 0, STARTING_VALVE to 0).toMutableMap()
            seen.add(valve)

            val queue = ArrayDeque<Pair<Int, String>>().apply {
                add(0 to valve)
            }

            // Find all nonempty valve connections and store distances to them
            while (queue.isNotEmpty()) {
                val (distance, position) = queue.removeFirst()

                connections[position]!!.forEach { neighbour ->
                    if (neighbour in seen)
                        return@forEach
                    seen.add(neighbour)
                    if (flows[neighbour]!! != 0) {
                        distances[valve]!![neighbour] = distance + 1
                    }
                    queue.add(distance + 1 to neighbour)
                }
            }

            // Remove distances to the starting valve and itself
            distances[valve]!!.remove(valve)
            if (valve != STARTING_VALVE)
                distances[valve]!!.remove(STARTING_VALVE)
        }

        return distances
    }

    fun getBestPressureRelease(): Int = valveDfs(SIMULATION_START_STATE)

    fun getBestPressureReleaseWithAssistant(): Int {
        val allOpenBitmask = (1 shl nonEmptyIndices.size) - 1
        var maxPressure = 0

        (0 .. allOpenBitmask / 2).forEach { bitmask ->
            maxPressure = max(maxPressure,
                valveDfs(SimulationState(SIMULATION_TIME - 4, STARTING_VALVE, bitmask)) +
                valveDfs(SimulationState(SIMULATION_TIME - 4, STARTING_VALVE, allOpenBitmask xor bitmask))
            )
        }

        return maxPressure
    }

    companion object {
        private const val STARTING_VALVE = "AA"
        private const val SIMULATION_TIME = 30
        private val SIMULATION_START_STATE = ValveDfs.SimulationState(SIMULATION_TIME, STARTING_VALVE, 0)
    }
}

