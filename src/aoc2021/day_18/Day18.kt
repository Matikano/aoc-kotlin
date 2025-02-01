package aoc2021.day_18

import aoc2021.day_18.BinaryTree.Companion.toBinaryTree
import utils.AocTask
import utils.extensions.head
import utils.extensions.orderedPairs
import utils.extensions.tail
import kotlin.time.measureTime


object Day18: AocTask() {

    override fun executeTask() {
        measureTime {
            val trees = testInput.toBinaryTreeList()
            println("Magnitude after summing trees = ${trees.magnitudeAfterSum()}")
        }.let { println("Test part took $it\n") }


            measureTime {
                val trees = input.toBinaryTreeList()
                println("Magnitude after summing trees = ${trees.magnitudeAfterSum()}")
            }.let { println("Part 1 took $it\n") }

            measureTime {
                val trees = input.toBinaryTreeList()
                val maxMagnitude = trees.orderedPairs()
                    .maxOf {
                        // Need to create a copy in order to not manipulate the original tree
                        (it.first.copy() + it.second.copy()).magnitude
                    }
                println("Max magnitude among all pairs of trees = $maxMagnitude")
            }.let { println("Part 2 took $it\n") }

    }

    private fun String.toBinaryTreeList(): List<BinaryTree> = lines().mapNotNull { it.toBinaryTree() }

    private fun List<BinaryTree>.magnitudeAfterSum(): Int =
        tail().fold(head()) { acc, next ->
            acc + next
        }.magnitude
}