package aoc2024

import java.lang.IllegalStateException

fun <T> List<T>.head(): T = first()

fun <T> List<T>.tail(): List<T> = drop(1)

fun <T> List<T>.middleElement(): T =
    if (size % 2 == 0)
        throw IllegalStateException("List size is even - no middle element")
    else
        get(size / 2)

fun <T> List<T>.swap(firstIndex: Int, secondIndex: Int): List<T> =
    toMutableList().apply {
        val temp = get(firstIndex)
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

    for(indexOfFirst in 0 until size - 1)
        for (indexOfSecond in indexOfFirst + 1 until size)
            list += get(indexOfFirst) to get(indexOfSecond)

    return list.toList()
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

        indices.forEach { i ->
            backtrack(current + this@backtrack[i],)
        }
    }

    backtrack(emptyList())

    return permutations
}