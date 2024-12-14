package utils.extensions

import utils.models.Position
import java.lang.IllegalStateException

fun <T> List<T>.middleElement(): T =
    if (size % 2 == 0) throw IllegalStateException("List size is even - no middle element")
    else this[size / 2]

fun <T> List<T>.swap(firstIndex: Int, secondIndex: Int): List<T> =
    toMutableList().apply {
        val temp = this[firstIndex]
        this[firstIndex] = this[secondIndex]
        this[secondIndex] = temp
    }.toList()

fun <T> List<T>.swapRange(firstRange: List<Int>, secondRange: List<Int>): List<T> =
    if (firstRange.size != secondRange.size) error("Ranges have different sizes")
    else toMutableList().apply {
        firstRange.indices.forEach {
            val firstIndex = firstRange[it]
            val secondIndex = secondRange[it]

            val temp = this[firstIndex]
            this[firstIndex] = this[secondIndex]
            this[secondIndex] = temp
        }
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

    (0 ..< size - 1).forEach { indexOfFirst ->
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

fun <T> List<T?>.emptySpaces(): List<IntRange> = buildList {
    var startIndex = 0
    var endIndex = -1
    var currentValue: T? = null
    this@emptySpaces.forEachIndexed { index, value ->
        if (currentValue == null) {
            if (value == null) {
                endIndex = index
            } else {
                if (startIndex <= endIndex)
                    add(startIndex .. endIndex)
            }
        } else {
            if (value == null) {
                startIndex = index
                endIndex = index
            }
        }
        currentValue = value
    }
}

fun <T> List<T?>.indexOfPreviousNotNull(startIndex: Int): Int {
    for (i in startIndex downTo    0) {
        if (this[i] != null) return i
    }
    return -1
}

fun <T> List<T?>.indexOfNextNull(startIndex: Int): Int {
    for (i in startIndex ..< size) {
        if (this[i] == null) return i
    }
    return -1
}

fun List<Position>.printPositions(
    gridBounds: Pair<Int, Int>,
    markerChar: Char = '#',
    emptyChar: Char = '.'
) {
    val (rows, cols) = gridBounds
    val grid = Array(rows) { CharArray(cols) { emptyChar } }

    forEach { position ->
        val (colIndex, rowIndex) = position
        if (rowIndex in 0 ..< rows && colIndex in 0 ..< cols) {
            grid[rowIndex][colIndex] = markerChar
        }
    }

    for (row in grid) {
        println(row.joinToString(""))
    }
}