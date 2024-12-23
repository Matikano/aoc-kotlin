package aoc2024.day_23

import utils.AocTask
import utils.extensions.initializeIfNotPresent
import kotlin.time.measureTime

typealias Connection = Pair<String, String>

object Day23: AocTask {

    private const val INITIAL_TUPLE_SIZE = 3

    private val testInput = """
        kh-tc
        qp-kh
        de-cg
        ka-co
        yn-aq
        qp-ub
        cg-tb
        vc-aq
        tb-ka
        wh-tc
        yn-cg
        kh-ub
        ta-co
        de-co
        tc-td
        tb-wq
        wh-td
        ta-ka
        td-qp
        aq-cg
        wq-ub
        ub-vc
        de-ta
        wq-aq
        wq-vc
        wh-yn
        ka-de
        kh-ta
        co-tc
        wh-qp
        tb-vc
        td-yn
    """.trimIndent()

    private val connections = mutableMapOf<String, MutableSet<String>>()
    private val interconnections = mutableSetOf<Set<String>>()

    override val fileName: String
        get() = "src/aoc2024/day_23/input.txt"

    override fun executeTask() {
        println("-------------------------------------")
        println("AoC 2024 Task ${this.javaClass.simpleName}")
        println()

        measureTime {
            testInput.lines().toConnections().populateConnectionMaps()
            val connectionTriplesWithTSize = countOfConnectionThatStartWithOfSize('t')
            println("TEST: Number of interconnected triplets with computer that starts with 't' = $connectionTriplesWithTSize")
        }.let { println("Test part took $it") }

        measureTime {
            readFileToList().toConnections().populateConnectionMaps()
            val connectionTriplesWithTSize = countOfConnectionThatStartWithOfSize('t')
            println("Number of interconnected triplets with computer that starts with 't' = $connectionTriplesWithTSize")
        }.let { println("Part 1 took $it") }

        measureTime {
            println("Password for part 2 = ${getPassword()}")
        }.let { println("Part 2 took $it") }
    }

    private fun getPassword(): String {
        val biggestInterconnectionSize = interconnections.maxOf { it.size }
        return interconnections
            .first { it.size == biggestInterconnectionSize }
            .sorted()
            .joinToString(",")
    }

    private fun search(computer: String, requiredConnections: Set<String>) {
        val key = requiredConnections.sorted().toSet()
        if (key in interconnections) return
        interconnections.add(key)

        connections[computer]!!.forEach { neighbour ->
            if (neighbour in requiredConnections) return@forEach
            if (!(requiredConnections.all { query -> neighbour in connections[query]!! })) return@forEach
            search(neighbour, requiredConnections.plus(setOf(neighbour)))
        }
    }

    private fun countOfConnectionThatStartWithOfSize(firstChar: Char, size: Int = INITIAL_TUPLE_SIZE): Int =
        findInterconnectionOfSize(INITIAL_TUPLE_SIZE).count { interconnection ->
            interconnection.any { computer ->  computer.startsWith(firstChar) }
        }

    private fun findInterconnectionOfSize(size: Int = INITIAL_TUPLE_SIZE): Set<Set<String>> =
        interconnections.filter { it.size == size }.toSet()

    private fun findAllInterconnections() =
        connections.keys.forEach { computer ->
            computer.searchForInterconnections()
        }

    private fun String.searchForInterconnections() =
        search(this, setOf(this))

    private fun List<Connection>.populateConnectionMaps() =
        forEach { (first, second) ->
            connections.initializeIfNotPresent(first)
            connections.initializeIfNotPresent(second)
            connections[first]!!.add(second)
            connections[second]!!.add(first)
        }.also { findAllInterconnections() }

    private fun List<String>.toConnections(): List<Connection> =
        map { line ->
            val (first, second) = line.split('-').sorted().distinct()
            first to second
        }.also {
            connections.clear()
            interconnections.clear()
        }
}