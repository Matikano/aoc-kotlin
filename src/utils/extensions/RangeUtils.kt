package utils.extensions

val IntRange.length: Int
    get() = endInclusive - start + 1

val IntRange.isValid: Boolean
    get() = !isEmpty()