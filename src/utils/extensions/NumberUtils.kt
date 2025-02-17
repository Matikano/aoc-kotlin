package utils.extensions

import utils.abstractions.RecursiveMemo1
import utils.abstractions.RecursiveMemo2

fun Long.even(): Boolean = this % 2 == 0L
fun Int.even(): Boolean = this % 2 == 0
fun Int.odd(): Boolean = !even()

// Least Common Multiplicity
fun lcm(a: Long, b: Long): Long = a * b / GreatestCommonDivisor(a, b)
fun List<Long>.lcm(): Long = fold(head(), ::lcm)

object GreatestCommonDivisor: RecursiveMemo2<Long, Long, Long>() {
    override fun MutableMap<Pair<Long, Long>, Long>.recurse(
        a: Long,
        b: Long
    ): Long = getOrPut(a to b) {
        if (b == 0L) a
        else recurse(b, a % b)
    }
}

val Int.binaryNegation: Int get() = this xor binaryMask
val Int.binaryMask: Int get() = (takeHighestOneBit() shl 1) - 1

object NthSum: RecursiveMemo1<Long, Long>() {
    override fun MutableMap<Long, Long>.recurse(n: Long): Long = getOrPut(n) {
        when (n) {
            0L -> 0L
            else -> n + recurse(n - 1)
        }
    }
}
