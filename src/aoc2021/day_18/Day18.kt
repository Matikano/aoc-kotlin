package aoc2021.day_18

import aoc2021.day_18.BinaryTree.Companion.asBinaryTree
import aoc2021.day_18.BinaryTree.Companion.toBinaryTree
import aoc2021.day_18.Day18.magnitudeAfterSum
import aoc2021.main
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import utils.AocTask
import utils.extensions.head
import utils.extensions.orderedPairs
import utils.extensions.tail
import utils.extensions.uniquePairs
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.time.measureTime

typealias Tree = MutableList<Any?>

object Day18: AocTask() {

    override fun executeTask() {
        measureTime {
            val trees = testInput.toBinaryTreeList()
            trees.forEach(::println)
            println("Magnitude after summing trees = ${trees.magnitudeAfterSum()}")
        }.let { println("Test part took $it\n") }

        measureTime {
            val trees = input.toBinaryTreeList()
            trees.forEach(::println)
            println("Magnitude after summing trees = ${trees.magnitudeAfterSum()}")
        }.let { println("Part 1 took $it\n") }

        measureTime {
            val trees = input.toBinaryTreeList()
            val maxMagnitude = trees.orderedPairs().maxOf { (it.first.toString().toBinaryTree()!! + it.second.toString().toBinaryTree()!!).magnitude }
            println("Max magnitude among all pairs of trees = $maxMagnitude")
        }.let { println("Part 2 took $it\n") }
    }

    private fun String.toBinaryTreeList(): List<BinaryTree> = lines().mapNotNull { it.toBinaryTree() }

    private fun List<BinaryTree>.magnitudeAfterSum(): Int {
        var tree = first()
        for (i in 1..< size) {
            tree = tree + get(i)
        }
        println("After sum = $tree")
        return tree.magnitude
    }

}