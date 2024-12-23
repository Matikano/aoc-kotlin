package aoc2024.day_23

import utils.AocTask
import kotlin.time.measureTime


object Day23: AocTask {

    private const val INTERCONNECTION_SIZE = 3
    private const val COMPUTER_FIRST_CHAR = 't'

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



    override val fileName: String
        get() = "src/aoc2024/day_23/input.txt"

    override fun executeTask() {
        println("-------------------------------------")
        println("AoC 2024 Task ${this.javaClass.simpleName}")
        println()

        measureTime {
            val network = testInput.lines().toNetwork()
            val connectionTriplesWithTSize = network.countOfConnectionThatStartWithOfSize(
                firstChar = COMPUTER_FIRST_CHAR,
                size = INTERCONNECTION_SIZE
            )
            println("TEST: Number of interconnected triplets " +
                    "with computer that starts with $COMPUTER_FIRST_CHAR = $connectionTriplesWithTSize")
        }.let { println("Test part took $it") }

        lateinit var network: Network

        measureTime {
            network = readFileToList().toNetwork()
        }.let { println("Network initialization took $it") }

        measureTime {
            val connectionTriplesWithTSize = network.countOfConnectionThatStartWithOfSize(
                firstChar = COMPUTER_FIRST_CHAR,
                size = INTERCONNECTION_SIZE
            )
            println("Number of interconnections of size = $INTERCONNECTION_SIZE " +
                    "with computer that starts with $COMPUTER_FIRST_CHAR = $connectionTriplesWithTSize")
        }.let { println("Part 1 took $it") }

        measureTime {
            println("Password for part 2 = ${network.password}")
        }.let { println("Part 2 took $it") }
    }

    private fun List<String>.toNetwork(): Network =
        Network(
            connectedComputers = map { line ->
                val (first, second) = line.split('-')
                first to second
            }
        )
}