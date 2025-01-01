package utils.extensions

import kotlin.math.max
import kotlin.math.min

val IntRange.length: Int
    get() = endInclusive - start + 1

val IntRange.isValid: Boolean
    get() = !isEmpty()

infix fun IntRange.overlaps(other: IntRange): Boolean =
        min(last, other.last) >= max(first, other.first)

operator fun IntRange.contains(other: IntRange): Boolean =
    other.start in this && other.last in this