package aoc2023.day_20

import aoc2023.day_20.Network.Companion.OUTPUT_MODULE_LABEL
import utils.AocTask
import kotlin.time.measureTime

object Day20: AocTask() {

    private const val PUSH_COUNT = 1000

    override fun executeTask() {
        measureTime {
            val testNetwork = Network(testInput)
            println("Test Network:\n\n$testNetwork")
            println()
            testNetwork.pushButton(PUSH_COUNT)
            println("Pulse score after $PUSH_COUNT pushes = ${testNetwork.pulseScore}")
        }.let { println("Test part took $it\n") }

        with(Network(input.trim())) {
            // Part 1
            measureTime {
                println("Network:\n\n$this")
                println()

                pushButton(PUSH_COUNT)
                println("Pulse score after $PUSH_COUNT pushes = $pulseScore")
            }.let { println("Part 1 took $it\n") }

            // Part 2
            measureTime {
                reset()
                println("First low pulse will reach $OUTPUT_MODULE_LABEL after $pushCountForFirstLowPulseOnOutput pushes")
            }.let { println("Part 2 took $it\n") }
        }
    }
}