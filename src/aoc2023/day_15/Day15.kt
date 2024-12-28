package aoc2023.day_15

import utils.AocTask
import kotlin.time.measureTime

object Day15: AocTask() {

    private const val MODULO_FACTOR = 256
    private const val MULTIPLICATION_FACTOR = 17

    override fun executeTask() {

        measureTime {
            val boxes: MutableMap<Int, MutableList<Lens>> =
                (0..< MODULO_FACTOR).associate { it to mutableListOf<Lens>() }.toMutableMap()
            val testSequence = testInput.split(",")
            println("Sum of test hashes = ${testSequence.sumOf { it.hash() }}")
            testSequence.forEach { boxes.processOperation(it) }

            val focusingPower = boxes.focusingPower()
            println("Test lenses focusing power = $focusingPower")
        }.let { println("Test part took $it\n") }

        with(input.trim().split(",")) {
            val boxes: MutableMap<Int, MutableList<Lens>> =
                (0..< MODULO_FACTOR).associate { it to mutableListOf<Lens>() }.toMutableMap()

            measureTime {
                println("Sum of hashes = ${sumOf { it.hash() }}")
            }.let { println("Part 1 took $it\n") }

            measureTime {
                forEach { boxes.processOperation(it) }
                val focusingPower = boxes.focusingPower()
                println("Focusing power = $focusingPower")
            }.let { println("Part 2 took $it\n") }
        }
    }

    private fun Map<Int, MutableList<Lens>>.focusingPower(): Int =
        filter { it.value.isNotEmpty() }
            .map { (boxIndex, lenses) ->
                lenses.mapIndexed { lensIndex, lens ->
                    (lensIndex + 1) * lens.focalLength * (boxIndex + 1)
                }.sum()
            }.sum()

    private fun MutableMap<Int, MutableList<Lens>>.processOperation(operation: String) {
        when {
            operation.contains('=') -> {
                val (label, focalLength) = operation.split('=')
                val boxIndex = label.hash()
                val box = getOrDefault(boxIndex, mutableListOf())
                val lens = Lens(label, focalLength.toInt())
                val replacementIndex = box.indexOfFirst { it.label == label }
                if (replacementIndex != -1)
                    box[replacementIndex] = lens
                else box.add(lens)
            }

            operation.contains('-') -> {
                val (label, _ ) = operation.split('-')
                val boxIndex = label.hash()
                val box = getOrDefault(boxIndex, mutableListOf())
                val removalIndex = box.indexOfFirst { it.label == label }
                if (removalIndex != -1)
                    box.removeAt(removalIndex)
            }
        }
    }

    private fun Char.hashStep(currentValue: Int): Int =
        ((currentValue + code) * MULTIPLICATION_FACTOR) % MODULO_FACTOR

    private fun String.hash(): Int = fold(0) { acc, char -> char.hashStep(acc) }
}