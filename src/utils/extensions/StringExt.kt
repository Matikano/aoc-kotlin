package utils.extensions

fun String.numsInt(): List<Int> =
    Regex("-?\\d+").findAll(this)
        .map { it.value.toInt() }
        .toList()

fun String.numsLong(): List<Long> =
    Regex("-?\\d+").findAll(this)
        .map { it.value.toLong() }
        .toList()

fun String.findCharChainLengths(char: Char): List<Int> =
    Regex("$char+").findAll(this).map { it.value.length }.toList()

fun String.tail(): String = drop(1)
fun String.head(): Char = first()

fun List<String>.transpose(): List<String> {
    val transposedGrid = mutableListOf<String>()
    val rows = size
    val cols = first().length

    for (colIndex in 0 until cols) {
        var row = ""
        for (rowIndex in 0 until rows) {
            row += this[rowIndex][colIndex]
        }
        transposedGrid.add(row)
    }

    return transposedGrid
}

fun String.transpose(): String = lines().transpose().joinToString("\n")