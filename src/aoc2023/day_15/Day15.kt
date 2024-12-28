package aoc2023.day_15

import utils.AocTask
import kotlin.time.measureTime

object Day15: AocTask() {

    private const val MODULO_FACTOR = 256
    private const val MULTIPLICATION_FACTOR = 17

    override fun executeTask() {


        measureTime {
            val boxes: MutableMap<Int, MutableList<Lens>> =
                (0..MODULO_FACTOR).associate { it to mutableListOf<Lens>() }.toMutableMap()
            val testSequence = testInput.split(",")
            println("Sum of test hashes = ${testSequence.sumOf { it.hash() }}")
            testSequence.forEach { it.processOperation(boxes) }

            val focusingPower = boxes.focusingPower()
            println("Test lenses focusing power = $focusingPower")
        }


        with(input.trim().split(",")) {
            val boxes: MutableMap<Int, MutableList<Lens>> =
                (0..MODULO_FACTOR).associate { it to mutableListOf<Lens>() }.toMutableMap()

            measureTime {
                println("Sum of hashes = ${sumOf { it.hash() }}")
            }.let { println("Part 1 took $it\n") }

            measureTime {
                forEach { it.processOperation(boxes) }
                boxes.filter { it.value.isNotEmpty() }.forEach { println(it) }
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

    private fun String.processOperation(boxes: MutableMap<Int, MutableList<Lens>>) {
        when {
            contains('=') -> {
                val (label, focalLength) = split('=')
                val boxIndex = label.hash()
                val box = boxes.getOrDefault(boxIndex, mutableListOf())
                val lens = Lens(label, focalLength.toInt())
                val replacementIndex = box.indexOfFirst { it.label == label }
                if (replacementIndex != -1)
                    box[replacementIndex] = lens
                else box.add(lens)
            }

            contains('-') -> {
                val (label, _ ) = split('-')
                val boxIndex = label.hash()
                val box = boxes.getOrDefault(boxIndex, mutableListOf())
                val removalIndex = box.indexOfFirst { it.label == label }
                if (removalIndex != -1)
                    box.removeAt(removalIndex)
            }
        }
    }

    private fun Char.hashStep(currentValue: Int): Int {
        var value = currentValue
        value += code
        value *= MULTIPLICATION_FACTOR
        value = value % MODULO_FACTOR
        return value
    }

    private fun String.hash(): Int = fold(0) { acc, char -> char.hashStep(acc) }
}