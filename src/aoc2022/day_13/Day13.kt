package aoc2022.day_13

import utils.AocTask
import java.util.Comparator
import kotlin.time.measureTime

typealias NestedListsPair = Pair<List<Any>, List<Any>>

object Day13 : AocTask() {

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
            println(
                "Product of indices of divider packages = ${
                    input.toNestedLists().productOfDividerPackageIndices()
                }"
            )
        }.let { println("Part 2 took $it\n") }

        measureTime {
            println(
                "Product of indices of divider packages iteratevly = ${
                    input.toNestedLists().productOfDividerPackageIndicesIter()
                }"
            )
        }.let { println("Part 2 took $it\n") }
    }

    private fun List<List<Any>>.productOfDividerPackageIndicesIter(): Int {
        // We index elements starting from 1, and we know that [[6]] > [[2]],
        // so it is always going to be after two in sorted list
        var i2 = 1
        var i6 = 2

        forEach { element ->
            if (element.compareTo(two) < 0) {
                i2++
                i6++
            } else if (element.compareTo(six) < 0) {
                i6++
            }
        }

        return i2 * i6
    }

    private fun List<List<Any>>.productOfDividerPackageIndices(): Int =
        (this + listOf(two, six))
            .sortedWith(NestedListComparator)
            .let { sorted ->
                (sorted.indexOf(two) + 1) * (sorted.indexOf(six) + 1)
            }

    private fun List<NestedListsPair>.orderedIndicesSum(): Int =
        mapIndexed { index, (first, second) ->
            if (first.compareTo(second) < 1) index + 1
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

    @Suppress("UNCHECKED_CAST")
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


    object NestedListComparator : Comparator<Any> {
        override fun compare(o1: Any, o2: Any): Int = o1.compareTo(o2)
    }

    fun Any.compareTo(other: Any): Int =
        when (this) {
            is Int -> {
                when (other) {
                    is Int -> this - other
                    is List<*> -> {
                        if (other.isEmpty()) 1
                        else listOf(this).compareTo(other)
                    }

                    else -> throw IllegalStateException("Unsupported type")
                }
            }

            is List<*> -> {
                when (other) {
                    is Int -> compareTo(listOf(other))
                    is List<*> -> {
                        zip(other).forEach { (a, b) ->
                            val comparison = a!!.compareTo(b!!)
                            if (comparison != 0)
                                return comparison
                        }
                        size - other.size
                    }

                    else -> throw IllegalStateException("Unsupported type")
                }
            }

            else -> throw IllegalStateException("Unsupported type")
        }
}
