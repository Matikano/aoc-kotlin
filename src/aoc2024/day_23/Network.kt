package aoc2024.day_23

import utils.extensions.safeAdd

typealias Connection = Pair<String, String>

data class Network(
    private val connectedComputers: List<Connection>
) {
    private val connections = mutableMapOf<String, MutableSet<String>>()
    private val interconnections = mutableSetOf<Set<String>>()

    init {
        connectedComputers.storeConnections()
        storeInterconnections()
    }

    val password: String
        get() = interconnections.maxOf { it.size }
            .let { biggestInterconnectionSize ->
                interconnections
                    .first { it.size == biggestInterconnectionSize }
                    .sorted()
                    .joinToString(",")
            }

    fun countOfConnectionThatStartWithOfSize(
        firstChar: Char,
        size: Int
    ): Int = findInterconnectionsOfSize(size)
        .count { interconnection ->
            interconnection.any { computer ->
                computer.startsWith(firstChar)
            }
        }

    private fun List<Connection>.storeConnections() =
        forEach(connections::safeAdd)

    private fun search(
        computer: String,
        requiredConnections: Set<String> = setOf(computer)
    ) {
        val key = requiredConnections.sorted().toSet()
        if (key in interconnections)
            return
        interconnections.add(key)

        connections[computer]!!.forEach { neighbour ->
            when {
                neighbour in requiredConnections ||
                    requiredConnections.any { neighbour !in connections[it]!! } -> return@forEach
                else -> search(neighbour, requiredConnections + neighbour)
            }
        }
    }

    private fun storeInterconnections() = connections.keys.forEach(::search)

    private fun findInterconnectionsOfSize(size: Int): Set<Set<String>> =
        interconnections.filter { it.size == size }.toSet()
}