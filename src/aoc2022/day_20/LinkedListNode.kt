package aoc2022.day_20

data class LinkedListNode<T>(
    val value: T,
    var next: LinkedListNode<T>? = null,
    var previous: LinkedListNode<T>? = null
) {
    override fun toString(): String = value.toString()
}
