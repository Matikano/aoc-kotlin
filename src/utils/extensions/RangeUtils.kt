package utils.extensions

import kotlin.math.max
import kotlin.math.min

val IntRange.length: Int
    get() = endInclusive - start + 1

val IntRange.isValid: Boolean
    get() = !isEmpty()

fun IntRange.overlaps(other: IntRange): Boolean =
        min(last, other.last) >= max(first, other.first)