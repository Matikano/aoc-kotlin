package aoc2022.day_20

import utils.AocTask
import utils.extensions.numsLong
import kotlin.time.measureTime

object Day20: AocTask() {

    private const val DECRYPTION_KEY = 811589153L

    override fun executeTask() {
        measureTime {
            val linkedList = testInput.toLinkedList()
            val groveCoordinates = linkedList.groveCoordinates()
            println("Test grove coordinates = $groveCoordinates")
        }.let { println("Test part took $it\n") }

        measureTime {
            val linkedList = input.toLinkedList()
            println("Grove coordinates = ${linkedList.groveCoordinates()}")
        }.let { println("Part 1 took $it\n") }

        measureTime {
            val linkedList = input.toLinkedList(DECRYPTION_KEY)
            println("Grove coordinates = ${linkedList.groveCoordinates(10)}")
        }.let { println("Part 2 took $it\n") }
    }

    private fun List<LinkedListNode<Long>>.groveCoordinates(
        mixCount: Int = 1
    ): Long {
        lateinit var zero: LinkedListNode<Long>
        val modulus = size - 1L

        repeat(mixCount) {
            // Perform mixing
            for (node in this) {
                if (node.value == 0L) {
                    zero = node
                    continue
                }
                var temp = node
                if (node.value > 0) {
                    repeat((node.value % modulus).toInt()) {
                        temp = temp.next!!
                    }
                    if (node == temp)
                        continue
                    node.next?.previous = node.previous
                    node.previous?.next = node.next
                    temp.next?.previous = node
                    node.next = temp.next
                    temp.next = node
                    node.previous = temp
                } else {
                    repeat((-node.value % modulus).toInt()) {
                        temp = temp.previous!!
                    }
                    if (node == temp)
                        continue
                    node.previous!!.next = node.next
                    node.next?.previous = node.previous
                    temp.previous?.next = node
                    node.previous = temp.previous
                    temp.previous = node
                    node.next = temp
                }
            }
        }

        var total = 0L

        (1..3000).forEach {
            zero = zero.next!!
            if (it % 1000 == 0)
                total += zero.value
        }

        return total
    }

    private fun String.toLinkedList(factor: Long = 1L): List<LinkedListNode<Long>> = numsLong().map {
        LinkedListNode(value = it * factor)
    }.also { linkedList ->
        linkedList.forEachIndexed { index, node ->
            node.next = linkedList[(index + 1).mod(linkedList.size)]
            node.previous = linkedList[(index - 1).mod(linkedList.size)]
        }
    }
}