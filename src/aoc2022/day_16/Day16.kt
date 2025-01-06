package aoc2022.day_16

import utils.AocTask
import utils.extensions.head
import utils.extensions.numsInt
import utils.extensions.tail
import kotlin.time.measureTime

object Day16: AocTask() {

    override fun executeTask() {
        measureTime {
            val valveGraph = testInput.toValvesAndTunnelsMaps()

            println("Flows: ${valveGraph.flows}")
            println("Connections: ${valveGraph.connections}")
            println("Flattened graphs = ${valveGraph.distances}")
            println("Best pressure release = ${valveGraph.getBestPressureRelease()}")
            println("Best pressure release with elephant= ${valveGraph.getBestPressureReleaseWithAssistant()}")
        }.let { println("Test part took $it\n") }

        measureTime {
            val valveGraph = input.toValvesAndTunnelsMaps()
            println("Best pressure release = ${valveGraph.getBestPressureRelease()}")
        }.let { println("Part 1 took $it\n") }

        measureTime {
            val valveGraph = input.toValvesAndTunnelsMaps()
            println("Best pressure release with elephant= ${valveGraph.getBestPressureReleaseWithAssistant()}")
        }.let { println("Part 2 took $it\n") }
    }

    private fun String.toValvesAndTunnelsMaps(): ValveGraph {
        val valveFlows = mutableMapOf<String, Int>()
        val valveConnections = mutableMapOf<String, Set<String>>()

        lines().forEach { line ->
            val valve = line.split(" ").tail().head()
            val flow = line.numsInt().first()
            val connectedValves = line.split(", ").let { listOf(it.first().split(" ").last()) + it.tail() }.toSet()

            valveFlows[valve] = flow
            valveConnections[valve] = connectedValves
        }

        return ValveGraph(flows = valveFlows, connections = valveConnections)
    }
}