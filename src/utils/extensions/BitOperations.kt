package utils.extensions

fun indicesOfDifferentBits(first: Long, second: Long): List<Int> =
    (first xor second)
        .toString(2)
        .reversed()
        .mapIndexedNotNull { index, c ->
            if (c == '1') index
            else null
        }

fun Long.toBinaryString(): String = toString(radix = 2)