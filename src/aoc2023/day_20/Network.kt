package aoc2023.day_20

import aoc2023.day_20.Pulse.Strength.*
import utils.extensions.lcm
import java.math.BigInteger

class Network(
    input: String
){
    private val pulseQueue: ArrayDeque<Pulse> = ArrayDeque(initialCapacity = 100)

    lateinit var modules: Map<String, Module>

    init {
        processInput(input)
    }

    private var lowPulseCount: Long = 0L
    private var highPulseCount: Long = 0L

    override fun toString(): String = modules.values.joinToString("\n")

    private val initialPulse: Pulse by lazy {
        Pulse(
            source = BUTTON_LABEL,
            destination = BROADCASTER_LABEL,
            strength = LOW
        )
    }

    fun reset() {
        pulseQueue.clear()
        lowPulseCount = 0
        highPulseCount = 0
    }

    val pulseScore: Long
        get() = lowPulseCount.toLong() * highPulseCount

    val pushCountForFirstLowPulseOnOutput: Long
        get() = findPushCountForFirstLowPulseOnOutputModule()

    private fun processInput(input: String) {
        val inputLines = input.lines()

        modules = inputLines.map { line ->
            val (moduleLabel, destinations) = line.split(MODULE_SEPARATOR)
            val outputs = destinations.split(MODULE_DESTINATIONS_SEPARATOR).toSet()
            when {
                moduleLabel.startsWith(BROADCASTER_LABEL) -> Module.Broadcaster(outputs)
                moduleLabel.startsWith(FLIP_FLOP_SYMBOL) -> Module.FlipFlop(moduleLabel.drop(1), outputs)
                moduleLabel.startsWith(CONJUNCTION_SYMBOL) -> Module.Conjunction(moduleLabel.drop(1), outputs)
                else -> throw IllegalArgumentException("Unsupported module label = $moduleLabel")
            }
        }.associate { it.label to it }

        val conjunctionInputs = mutableMapOf<String, MutableSet<String>>().withDefault { mutableSetOf() }

        modules.values
            .filter { it is Module.Conjunction }
            .map { it.label }
            .forEach { conjModuleLabel ->
                inputLines.forEach { line ->
                    val (moduleLabel, destinations) = line.split(MODULE_SEPARATOR)
                    val outputs = destinations.split(MODULE_DESTINATIONS_SEPARATOR).toSet()

                    if (conjModuleLabel in outputs) {
                        if (conjModuleLabel !in conjunctionInputs)
                            conjunctionInputs[conjModuleLabel] = mutableSetOf<String>()

                        conjunctionInputs[conjModuleLabel]!!.add(moduleLabel.drop(1))
                    }
                }
            }

        conjunctionInputs.forEach { (conjLabel, inputs) ->
            (modules[conjLabel] as? Module.Conjunction)?.initializeInputs(inputs)
                ?: throw IllegalArgumentException("Module $conjLabel is not of Conjunction type")
        }
    }


    fun pushButton(count: Int = 1) {
        repeat(count) {
            sendPulse(initialPulse)

            while (pulseQueue.isNotEmpty()) {
                val currentPulse = pulseQueue.removeFirst()
                modules[currentPulse.destination]?.processPulse(currentPulse, ::sendPulse)
            }
        }

        println("lowPulseCount = $lowPulseCount, highPulseCount = $highPulseCount")
    }

    private fun findPushCountForFirstLowPulseOnOutputModule(): Long {
        var buttonPushCount = 0

        val outputFeed = modules.values.firstOrNull { OUTPUT_MODULE_LABEL in it.outputs }
            ?.let { outputModule ->
                outputModule as? Module.Conjunction
                    ?: throw UnsupportedOperationException("'$OUTPUT_MODULE_LABEL' module feed is not of Conjunction Type")
            } ?: throw NoSuchElementException("Output module '$OUTPUT_MODULE_LABEL' not found in the network")

        val inputs = outputFeed.inputs.keys.toSet()
        val cycleLengths = mutableMapOf<String, Int>()
        val highPulseSeenCount = inputs.associate { it to 0 }.toMutableMap()

        while (true) {
            buttonPushCount++
            sendPulse(initialPulse)

            while (pulseQueue.isNotEmpty()) {
                val currentPulse = pulseQueue.removeFirst()
                modules[currentPulse.destination]?.processPulse(currentPulse, ::sendPulse)

                if (currentPulse.destination == outputFeed.label && currentPulse.strength == HIGH) {
                    if (currentPulse.source !in highPulseSeenCount)
                        highPulseSeenCount[currentPulse.source] = 0
                    highPulseSeenCount[currentPulse.source] = highPulseSeenCount[currentPulse.source]!! + 1

                    if (currentPulse.source !in cycleLengths)
                        cycleLengths[currentPulse.source] = buttonPushCount
                    else assert(
                        buttonPushCount == highPulseSeenCount[currentPulse.source]!! * cycleLengths[currentPulse.source]!!
                    ) { "Did not find cycles for '$OUTPUT_MODULE_LABEL' feed inputs" }
                }
            }

            if (highPulseSeenCount.values.all { it == 10 })
                break
        }

        println("Cycle lengths = $cycleLengths")

        return cycleLengths.values.toList().map { it.toLong() }.lcm()
    }

    fun sendPulse(pulse: Pulse) = pulseQueue.add(pulse)
        .also { if (pulse.strength == HIGH) highPulseCount++ else lowPulseCount++ }

    companion object {
        const val BUTTON_LABEL = "button"
        const val BROADCASTER_LABEL = "broadcaster"
        const val FLIP_FLOP_SYMBOL = "%"
        const val CONJUNCTION_SYMBOL = "&"
        const val OUTPUT_MODULE_LABEL = "rx"
        const val MODULE_SEPARATOR = " -> "
        const val MODULE_DESTINATIONS_SEPARATOR = ", "
    }
}