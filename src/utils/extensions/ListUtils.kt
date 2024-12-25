package utils.extensions

import utils.models.Position
import kotlin.reflect.KFunction

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

fun <T> List<T>.subListsWithOneDroppedElement(): List<List<T>> =
    indices.map {
       filterIndexed { index, _ -> index != it }
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

fun <T> uniquePairs(list1: List<T>, list2: List<T>): List<Pair<T, T>> = buildList {
    if (list1.isEmpty() || list2.isEmpty()) return emptyList()

   list1.forEach { firstListItem ->
        list2.forEach { secondListItem ->
            add(firstListItem to secondListItem)
        }
    }
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

fun printPositions(
    gridBounds: Pair<Int, Int>,
    firstList: List<Position>,
    firstChar: Char,
    secondList: List<Position>,
    secondChar: Char,
    thirdList: List<Position>,
    thirdChar: Char,
    emptyChar: Char = '.'
) {
    val (rows, cols) = gridBounds
    val grid = Array(rows) { CharArray(cols) { emptyChar } }

    fun List<Position>.markPositions(markerChar: Char) {
        forEach { position ->
            val (colIndex, rowIndex) = position
            if (rowIndex in 0 ..< rows && colIndex in 0 ..< cols) {
                grid[rowIndex][colIndex] = markerChar
            }
        }
    }

    firstList.markPositions(firstChar)
    secondList.markPositions(secondChar)
    thirdList.markPositions(thirdChar)

    for (row in grid) {
        println(row.joinToString(""))
    }
}

fun printScaledPositions(
    gridBounds: Pair<Int, Int>,
    firstList: List<Position>,
    firstChars: List<Char>,
    secondList: List<Position>,
    secondChars: List<Char>,
    thirdList: List<Position>,
    thirdChars: List<Char>,
    emptyChar: Char = '.',
    scale: Int = 2
) {
    val (rows, cols) = gridBounds
    val grid = Array(rows) { CharArray(cols * scale) { emptyChar } }

    val scaledCols = cols * scale

    fun List<Position>.markPositions(markerChars: List<Char>) {
        forEach { position ->
            val (colIndex, rowIndex) = position
            if (rowIndex in 0 ..< rows && colIndex in 0 ..< scaledCols) {
                markerChars.forEachIndexed { index, markerChar ->
                    grid[rowIndex][colIndex + index] = markerChar
                }
            }
        }
    }

    firstList.markPositions(firstChars)
    secondList.markPositions(secondChars)
    thirdList.markPositions(thirdChars)

    for (row in grid) {
        println(row.joinToString(""))
    }
}

fun <T> List<T>.binarySearchWithCostFunction(startLeft: Int = 0, costFunction: (T) -> Int): T? {
    var left = startLeft
    var right = size - 1

    while (left < right) {
        val mid = (left + right) / 2
        val value = this[mid]

        if (costFunction(value) == Int.MAX_VALUE) {
            right = mid
        } else left = mid + 1
    }

    return if (left < size && costFunction(this[left]) == Int.MAX_VALUE)
        this[left]
    else null
}

/**
 * Create the cartesian product of any number of sets of any size. Useful for parameterized tests
 * to generate a large parameter space with little code. Note that any type information is lost, as
 * the returned set contains list of any combination of types in the input set.
 *
 *
 * @param sets Sets.
 */
fun cartesianProduct(a: List<*>, vararg sets: List<*>): List<List<*>> =
    listOf(a).plus(sets).fold(listOf(listOf<Any?>())) { acc, set ->
            acc.flatMap { list -> set.map { element -> list + element } }
        }

/**
 * Transform elements of a cartesian product.
 */
fun <T> List<List<*>>.map(transform: KFunction<T>) = map { transform.call(*it.toTypedArray()) }