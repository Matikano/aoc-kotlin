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