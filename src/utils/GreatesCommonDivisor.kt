package utils

import utils.abstractions.RecursiveMemo2

object GreatestCommonDivisor: RecursiveMemo2<Long, Long, Long>() {
    override fun MutableMap<Pair<Long, Long>, Long>.recurse(
        a: Long,
        b: Long
    ): Long = getOrPut(a to b) {
        if (b == 0L) a
        else recurse(b, a % b)
    }
}