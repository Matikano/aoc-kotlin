package aoc2023.day_20

import aoc2023.day_20.Network.Companion.BROADCASTER_LABEL
import aoc2023.day_20.Network.Companion.CONJUNCTION_SYMBOL
import aoc2023.day_20.Network.Companion.FLIP_FLOP_SYMBOL
import aoc2023.day_20.Pulse.Strength.HIGH
import aoc2023.day_20.Pulse.Strength.LOW

sealed class Module(
    val label: String,
    val outputs: Set<String>
) {

    override fun toString(): String = "$prefix$label -> ${outputs.joinToString(", ")}"
    abstract val prefix: String
    abstract fun processPulse(pulse: Pulse, sendPulse: (Pulse) -> Unit)

    class FlipFlop(
        label: String,
        outputs: Set<String>
    ): Module(label, outputs) {
        private var on: Boolean = false
        override val prefix: String = FLIP_FLOP_SYMBOL

        override fun processPulse(pulse: Pulse, sendPulse: (Pulse) -> Unit) {
            if (pulse.strength == LOW) {
                on = !on
                outputs.forEach { destination ->
                    sendPulse(
                        Pulse(
                            source = label,
                            destination = destination,
                            strength = if (on) HIGH else LOW
                        )
                    )
                }
            }
        }
    }

    class Conjunction(
        label: String,
        outputs: Set<String>
    ): Module(label, outputs) {
        override val prefix: String = CONJUNCTION_SYMBOL
        internal val inputs = mutableMapOf<String, Pulse.Strength>()

        internal fun initializeInputs(inputLabels: Set<String>) {
            inputLabels.forEach { label ->
                inputs[label] = LOW
            }
        }

        override fun processPulse(pulse: Pulse, sendPulse: (Pulse) -> Unit) {
            val source = pulse.source
            inputs[source] = pulse.strength

            outputs.forEach { destination ->
                sendPulse(
                    Pulse(
                        source = label,
                        destination = destination,
                        strength = if (inputs.values.all { it == HIGH }) {
                            LOW
                        } else HIGH
                    )
                )
            }
        }
    }

    class Broadcaster(
        outputs: Set<String>
    ): Module(BROADCASTER_LABEL, outputs) {
        override val prefix: String = ""
        override fun processPulse(pulse: Pulse, sendPulse: (Pulse) -> Unit) {
            outputs.forEach { destination ->
                sendPulse(
                    Pulse(
                        source = label,
                        destination = destination,
                        strength = LOW
                    )
                )
            }
        }
    }
}