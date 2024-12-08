package aoc2024

import java.lang.IllegalStateException

fun <T> List<T>.head(): T = first()

fun <T> List<T>.tail(): List<T> = drop(1)

fun <T> List<T>.middleElement(): T =
    if (size % 2 == 0) throw IllegalStateException("List size is even - no middle element")
    else this[size / 2]

fun <T> List<T>.swap(firstIndex: Int, secondIndex: Int): List<T> =
    toMutableList().apply {
        val temp = this[firstIndex]
        this[firstIndex] = this[secondIndex]
        this[secondIndex] = temp
    }.toList()

fun <T> List<T>.subListsWithOneDroppedElement(): List<List<T>> = buildList {
    indices.forEach {
        add(
           this@subListsWithOneDroppedElement.filterIndexed { index, _ -> index != it }
        )
    }
}

fun <T> List<T>.uniquePairs(): List<Pair<T, T>> {
    if (size < 2) return emptyList()

    val list = mutableListOf<Pair<T, T>>()

    (0 until size - 1).forEach { indexOfFirst ->
        (indexOfFirst + 1 until size).forEach { indexOfSecond ->
            list.add(this@uniquePairs[indexOfFirst] to this@uniquePairs[indexOfSecond])
        }
    }

    return list
}

fun <T> List<T>.repeatingPermutations(length: Int): List<List<T>> {
    if (length == 0) return listOf(emptyList())
    if (isEmpty()) return emptyList()

    val permutations = mutableListOf<List<T>>()

    fun List<T>.backtrack(current: List<T>) {
        if (current.size == length) {
            permutations.add(current)
            return
        }

        indices.forEach { index ->
            backtrack(current + this@backtrack[index],)
        }
    }

    backtrack(emptyList())

    return permutations
}