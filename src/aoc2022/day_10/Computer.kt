package aoc2022.day_10

class Computer {
    private var register: Int = 1
    private var cycle: Int = 0
        set(value) {
            CRT += if (cycle % CRT_ROW_SIZE in spritePosition) "#" else "."
            field = value
            if (value in signalCycles)
                signalStrengths.add(value * register)
            if (value % CRT_ROW_SIZE == 0) CRT += '\n'
        }

    val signalStrengths = mutableListOf<Int>()

    val signalStrengthsSum
        get() = signalStrengths.sum()

    var CRT: String = ""
        private set

    val spritePosition: IntRange
        get() = register - 1 .. register + 1

    fun process(instruction: Instruction) {
        repeat(instruction.cycles) { cycle++ }

        when (instruction) {
            is Instruction.Noop -> Unit
            is Instruction.AddX -> register += instruction.value
        }
    }

    companion object {
        const val SIGNAL_START_OFFSET = 20
        const val SIGNAL_STEP = 40
        const val SIGNALS_COUNT = 5

        const val CRT_ROW_SIZE = 40

        val signalCycles = listOf(SIGNAL_START_OFFSET) + buildList {
            repeat(SIGNALS_COUNT) { i ->
                add(SIGNAL_START_OFFSET + (i + 1) * SIGNAL_STEP)
            }
        }
    }
}
