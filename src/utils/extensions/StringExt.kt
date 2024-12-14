package utils.extensions

fun String.numsInt(): List<Int> =
    Regex("-?\\d+").findAll(this)
        .map { it.value.toInt() }
        .toList()

fun String.numsLong(): List<Long> =
    Regex("-?\\d+").findAll(this)
        .map { it.value.toLong() }
        .toList()