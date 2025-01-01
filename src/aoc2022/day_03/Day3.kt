package aoc2022.day_03

import utils.AocTask
import utils.extensions.mutualItems
import kotlin.time.measureTime

typealias Compartments = Pair<String, String>
typealias Group = Triple<String, String, String>

object Day3: AocTask() {

    private const val CHARACTERS = "abcdefghijklmnopqrstuvwxyz"
    private const val GROUP_SIZE = 3

    private val priorities: Map<Char, Int> = "$CHARACTERS${CHARACTERS.uppercase()}"
        .mapIndexed { index, char ->
            char to index + 1
        }.toMap()

    override fun executeTask() {
        measureTime {
            val backpacks = testInput.toBackpackCompartmentsList()
            println("Sum of priorities = ${backpacks.sumOf { it.mutualItemPriority() }}")

            val backpackGroups = testInput.toBackpackGroups()
            println("Sum group label priorities = ${backpackGroups.sumOf { it.mutualItemPriority() }}")
        }.let { println("Test part took $it\n") }

        measureTime {
            val backpacks = input.toBackpackCompartmentsList()
            println("Sum of priorities = ${backpacks.sumOf { it.mutualItemPriority() }}")
        }.let { println("Part 1 took $it\n") }

        measureTime {
            val backpackGroups = input.toBackpackGroups()
            println("Sum of priorities = ${backpackGroups.sumOf { it.mutualItemPriority() }}")
        }.let { println("Part 2 took $it\n") }
    }

    private fun String.toBackpackCompartmentsList(): List<Compartments> = lines().map { it.toBackpackCompartments() }

    private fun String.toBackpackCompartments(): Compartments {
        val (first, second) = windowed(size = length / 2, step = length / 2)
        return first to second
    }

    private fun String.toBackpackGroups(): List<Group> =
        lines().windowed(
            size = GROUP_SIZE,
            step = GROUP_SIZE
        ) { (a, b, c) ->
            Triple(a, b, c)
        }

    private fun Compartments.mutualItemPriority(): Int =
        with(toList().map { it.toList() }.mutualItems()) {
            assert(size == 1) { "Compared compartments have more than 1 mutual items!" }
            priorities[first()] ?: 0
        }

    private fun Group.mutualItemPriority(): Int =
        with(toList().map { it.toList() }.mutualItems()) {
            assert(size == 1) { "Compared groups have more than 1 mutual items!" }
            priorities[first()] ?: 0
        }
}