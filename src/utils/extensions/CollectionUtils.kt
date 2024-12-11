package utils.extensions

fun <T> Collection<T>.head(): T = first()

fun <T> Collection<T>.tail(): Collection<T> = drop(1)