package utils.extensions

fun String.nums(): List<Int> =
    Regex("-?\\d+").findAll(this)
        .map { it.value.toInt() }
        .toList()