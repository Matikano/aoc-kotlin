package aoc2024.day_17

typealias Instruction = Pair<Int, Int>

data class Computer(
    private var registerA: Long,
    private var registerB: Long,
    private var registerC: Long,
) {

    private val output: MutableList<Int> = mutableListOf()

    fun findAForSelfReturn(
        program: List<Int>,
        target: List<Int> = program.map { it },
        answer: Long = 0L
    ): Long?  {
        if (target.isEmpty())
            return answer

        (0..< 8L).forEach { testValue ->
            val a = answer shl 3 or testValue
            var b = 0L
            var c = 0L
            var output: Int? = null
            var adv3 = false

            fun Long.toComboOperand(): Long =
                when (this) {
                    in 0..3 -> this
                    4L -> a
                    5L -> b
                    6L -> c
                    else -> throw IllegalArgumentException("Unsupported value of combo operand = $this")
                }

            for (pointer in 0..< program.size - 2 step 2) {
                val (operator, operand) = program[pointer] to program[pointer + 1].toLong()

                when (operator) {
                    0 -> {
                        assert(!adv3) { "Program has multiple ADVs" }
                        assert(operand == 3L) { "Program has ADV with operand other than 3" }
                        adv3 = true
                    }
                    1 -> b = b xor operand
                    2 -> b = operand.toComboOperand() % 8L
                    3 -> throw IllegalStateException("Program has JNZ inside loop body")
                    4 -> b = b xor c
                    5 -> {
                        assert(output == null) { "Program has multiple OUT" }
                        output = (operand.toComboOperand() % 8).toInt()
                    }
                    6 -> b = a shr operand.toComboOperand().toInt()
                    7 -> c = a shr operand.toComboOperand().toInt()
                }

                if (output == target.last()) {
                    return findAForSelfReturn(program = program, target = target.dropLast(1), a) ?: continue
                }
            }
        }
        return null
    }

    fun runProgram(input: List<Int>): String {
        output.clear()
        var pointer = 0

        while (pointer < input.size) {
            val instruction = input[pointer] to input[pointer + 1]
            pointer = instruction.process(pointer)
        }

        return output.joinToString(",")
    }

    private fun Instruction.process(pointer: Int): Int {
        val operand = second
        return when (first) {
            0 -> pointer + adv(operand)
            1 -> pointer + bxl(operand)
            2 -> pointer + bst(operand)
            3 -> jnz(operand, pointer)
            4 -> pointer + bxc(operand)
            5 -> pointer + out(operand)
            6 -> pointer + bdv(operand)
            7 -> pointer + cdv(operand)
            else -> throw IllegalArgumentException("Malformed program input! " +
                    "Combo operands of 7 and higher should not appear in the program!")
        }
    }

    private fun adv(operand: Int): Int {
        registerA = registerA shr operand.comboOperand()
        return POINTER_STEP
    }

    private fun bxl(operand: Int): Int {
        registerB = registerB xor operand.toLong()
        return POINTER_STEP
    }

    private fun bst(operand: Int): Int {
        registerB = operand.comboOperand() % 8L
        return POINTER_STEP
    }

    private fun jnz(operand: Int, pointer: Int): Int =
        if (registerA == 0L) pointer + POINTER_STEP
        else operand

    private fun bxc(operand: Int): Int {
        registerB = registerB xor registerC
        return POINTER_STEP
    }

    private fun out(operand: Int): Int {
        output += operand.comboOperand() % 8
        return POINTER_STEP
    }

    private fun bdv(operand: Int): Int {
        registerB = registerA shr operand.comboOperand()
        return POINTER_STEP
    }

    private fun cdv(operand: Int): Int {
        registerC = registerA shr operand.comboOperand()
        return POINTER_STEP
    }

    private fun Int.comboOperand(): Int =
        when (this) {
            in 0..3 -> this
            4 -> registerA.toInt()
            5 -> registerB.toInt()
            6 -> registerC.toInt()
            else -> throw IllegalArgumentException("Malformed program input! Combo operands of 7 and higher should not appear in the program!")
        }

    companion object {
        private const val POINTER_STEP = 2
    }
}
