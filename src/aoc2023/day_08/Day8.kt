package aoc2023.day_08

import utils.AocTask
import utils.extensions.tail
import kotlin.time.measureTime

typealias Connection = Pair<String, Pair<String, String>>

object Day8: AocTask() {

    override fun executeTask() {

        measureTime {
            val testNetwork = testInput.lines().toNetwork()
            println("Steps to solve test network with cycle = ${testNetwork.solve()}")
        }.let { println("Test part took $it") }

        measureTime {
            val network = inputToList().toNetwork()
            println("Steps to solve network = ${network.solve()}")
        }.let { println("Part 1 $it\n") }

        measureTime {
            val network = inputToList().toNetwork()
            println("Steps to solve network by ghosts = ${network.solveByGhosts()}")
        }.let { println("Part 2 $it\n") }
    }

    private fun List<String>.toNetwork(): Network {
        val instructions = first()
        val connections = tail().tail().toList()

        return Network(
            instructions = instructions,
            connections = connections.mapConnections()
        )
    }

    private fun List<String>.mapConnections(): Map<String, Pair<String, String>> =
        map { it.toConnection() }.associate { it.first to it.second }

    private fun String.toConnection(): Connection {
        val (sourceNode, destinations) = split(" = ")
        val (firstDestination, secondDestination) = destinations.substringAfter('(').substringBefore(')').split(", ")
        return sourceNode to (firstDestination to secondDestination)
    }
}