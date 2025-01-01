package utils.extensions

fun <T> Collection<T>.head(): T = first()

fun <T> Collection<T>.tail(): Collection<T> = drop(1)

operator fun <T> Collection<T>.contains(pair: Pair<T, T>): Boolean =
    pair.first in this || pair.second in this

operator fun <T> Collection<Pair<T, T>>.contains(other: T): Boolean =
    other in map { it.first } || other in map { it.second }

fun <K, V> MutableMap<K, MutableSet<V>>.initializeIfNotPresent(key: K) =
    if (key !in this)
        this[key] = mutableSetOf()
    else Unit

fun <T> MutableMap<T, MutableSet<T>>.safeAdd(pair: Pair<T, T>) {
    initializeIfNotPresent(pair.first).also { this[pair.first]!!.add(pair.second) }
    initializeIfNotPresent(pair.second).also { this[pair.second]!!.add(pair.first) }
}

fun List<Int>.minOrMaxInt(): Int = minOrNull() ?: Int.MAX_VALUE

fun <T> Collection<Pair<T, T>>.flattenPairsToSet(): Set<T> = flatMap { listOf(it.first, it.second) }.toSet()

fun <T> Pair<T, T>.reversed(): Pair<T, T> = Pair(first = second, second = first)

fun <T> Collection<Collection<T>>.mutualItems(): Set<T> =
    tail().fold(head().toSet()) { acc, next -> acc.intersect(next.toSet()) }
