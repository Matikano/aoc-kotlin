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