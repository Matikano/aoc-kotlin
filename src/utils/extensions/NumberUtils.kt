package utils.extensions

import utils.GreatestCommonDivisor

fun Long.even(): Boolean = this % 2 == 0L

fun Int.even(): Boolean = this % 2 == 0

fun Int.odd(): Boolean = !even()

// Greatest common divisor
fun gcd(a: Long, b: Long): Long = GreatestCommonDivisor(a, b)

// Least common multiplicity
fun lcm(a: Long, b: Long): Long = a * b / gcd(a, b)

fun List<Long>.lcm(): Long = fold(head()) { acc, value ->
    lcm(acc, value)
}