@file:Suppress("UNCHECKED_CAST")

package aoc2022.day_13

import utils.AocTask
import java.util.Comparator
import kotlin.time.measureTime

object Day13: AocTask() {

    private val two = "[[2]]".parseToNestedLists()
    private val six = "[[6]]".parseToNestedLists()

    override fun executeTask() {
        measureTime {
            val pairs = testInput.pairsOfNestedLists()
            println("Sum of ordered pairs indices = ${pairs.orderedIndicesSum()}")
        }.let { println("Test part took $it\n") }

        measureTime {
            val pairs = input.pairsOfNestedLists()
            println("Sum of ordered pairs indices = ${pairs.orderedIndicesSum()}")
        }.let { println("Test part took $it\n") }

        measureTime {
            val pairs = (input.toNestedLists() + listOf(two) + listOf(six)).sortedWith(NestedListComparator)
            println("Product of indices of divider packages = ${(pairs.indexOf(two) + 1) * (pairs.indexOf(six) + 1)}")
        }.let { println("Part 2 took $it\n") }
    }

    private fun List<Pair<List<Any>, List<Any>>>.orderedIndicesSum(): Int =
        mapIndexed { index, (first, second) ->
            if (first.compareTo(second) < 1)
                index + 1
            else 0
        }.sum()

    private fun String.toNestedLists(): List<List<Any>> =
        lines().mapNotNull { line ->
            if (line.isEmpty()) null
            else line.parseToNestedLists()
        }

    private fun String.pairsOfNestedLists(): List<Pair<List<Any>, List<Any>>> =
        lines().windowed(2, 3).map { (first, second) ->
            first.parseToNestedLists() to second.parseToNestedLists()
        }

    private fun String.parseToNestedLists(): List<Any> {
        val stack = mutableListOf<MutableList<Any>>()
        var currentList = mutableListOf<Any>()

        var index = 0
        while (index < length) {
            when (this[index]) {
                '[' -> {
                    stack.add(currentList)
                    currentList = mutableListOf()
                }
                ']' -> {
                    if (stack.isNotEmpty()) {
                        val parentList = stack.removeLast()
                        parentList.add(currentList)
                        currentList = parentList
                    } else {
                        return currentList
                    }
                }
                in '0'..'9' -> {
                    val numStart = index
                    while (index < length && this[index].isDigit()) {
                        index++
                    }
                    currentList.add(substring(numStart, index).toInt())
                    index-- // Decrement i to account for the extra increment in the loop
                }
            }
            index++
        }

        return currentList.first() as? List<Any> ?: error("Invalid list type")
    }


    object NestedListComparator: Comparator<Any> {
        override fun compare(o1: Any, o2: Any): Int = o1.compareTo(o2)
    }

    fun Any.compareTo(other: Any): Int {
        return if (this is Int) {
            when (other) {
                is Int -> this - other
                is List<*> -> {
                    if (other.isEmpty()) 1
                    else listOf(this).compareTo(other)
                }
                else -> throw IllegalStateException("Unsupported type")
            }
        } else if (this is List<*>) {
            if (other is Int) compareTo(listOf(other))
            else if (other is List<*>) {
                zip(other).forEach { (a, b) ->
                    val comparison = a!!.compareTo(b!!)
                    if (comparison != 0)
                        return comparison
                }
                size - other.size
            } else throw IllegalStateException("Unsupported type")
        } else throw IllegalStateException("Unsupported type")
    }
}