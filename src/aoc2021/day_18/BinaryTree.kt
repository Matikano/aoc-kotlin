package aoc2021.day_18

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import utils.extensions.tail
import kotlin.compareTo
import kotlin.div
import kotlin.math.ceil
import kotlin.math.floor

sealed class BinaryTree(
    var parent: BinaryTree? = null
) {
    val magnitude: Int
        get() = when (this) {
            is Leaf -> value
            is Branch -> 3 * left.magnitude + 2 * right.magnitude
        }

    operator fun plus(other: BinaryTree): BinaryTree =
        Branch(
            left = this,
            right = other
        ).apply {
            this@BinaryTree.parent = this
            other.parent = this
            reduce()
        }

    override fun toString(): String =
        when (this) {
            is Leaf -> value.toString()
            is Branch -> "[$left,$right]"
        }

    class Branch(
        parent: BinaryTree? = null,
        var left: BinaryTree,
        var right: BinaryTree
    ): BinaryTree(parent) {
        val leftNeighbor: Leaf?
            get() = findLeftNeighbor()

        val rightNeighbor: Leaf?
            get() = findRightNeighbor()

        private fun findLeftNeighbor(): Leaf? {
            var current: BinaryTree? = this
            while (current?.parent is Branch && current == (current.parent as Branch).left) {
                current = current.parent
            }
            if (current?.parent is Branch) {
                return findRightmostLeaf((current.parent as Branch).left)
            }
            return null
        }

        private fun findRightmostLeaf(node: BinaryTree?): Leaf? {
            var current = node
            while (current is Branch) {
                current = current.right
            }
            return current as? Leaf
        }


        private fun findRightNeighbor(): Leaf? {
            var current: BinaryTree? = this
            while (current?.parent is Branch && current == (current.parent as Branch).right) {
                current = current.parent
            }
            if (current?.parent is Branch) {
                return findLeftmostLeaf((current.parent as Branch).right)
            }
            return null
        }

        private fun findLeftmostLeaf(node: BinaryTree?): Leaf? {
            var current = node
            while (current is Branch) {
                current = current.left
            }
            return current as? Leaf
        }
    }

    class Leaf(
        parent: BinaryTree? = null,
        var value: Int
    ): BinaryTree(parent)

    fun reduce() {
        while (explode() || split()) {}
    }

    fun explode(): Boolean = explodeRecursive(this, 0)

    fun split(): Boolean = splitRecursive(this)

    private fun splitRecursive(node: BinaryTree): Boolean {
        return when (node) {
            is Leaf -> {
                if (node.value >= 10) {
                    val leftValue = floor(node.value / 2f).toInt()
                    val rightValue = ceil(node.value / 2f).toInt()

                    val leftLeaf = Leaf(value = leftValue)
                    val rightLeaf = Leaf(value = rightValue)
                    val branch = Branch(
                        left = leftLeaf,
                        right = rightLeaf,
                        parent = node.parent
                    )

                    leftLeaf.parent = branch
                    rightLeaf.parent = branch

                    if (node.parent is Branch) {
                        if ((node.parent as Branch).left == node) {
                            (node.parent as Branch).left = branch
                        } else {
                            (node.parent as Branch).right = branch
                        }
                    }
                    true
                } else false
            }
            is Branch -> splitRecursive(node.left) || splitRecursive(node.right)
        }
    }


    private fun explodeRecursive(node: BinaryTree, depth: Int): Boolean {
        return if (node is Branch) {
            if (depth >= 4 && node.left is Leaf && node.right is Leaf) {
                    val leftLeaf = node.left as Leaf
                    val rightLeaf = node.right as Leaf

                    node.leftNeighbor?.apply {
                        value += leftLeaf.value
                    }
                    node.rightNeighbor?.apply {
                        value += rightLeaf.value
                    }

                    // Replace the branch with a leaf with value 0
                    val zeroLeaf = Leaf(value = 0, parent = node.parent)
                    if (node.parent is Branch) {
                        if ((node.parent as Branch).left == node) {
                            (node.parent as Branch).left = zeroLeaf
                        } else {
                            (node.parent as Branch).right = zeroLeaf
                        }
                    }
                    true // Explosion occurred
            } else explodeRecursive(node.left, depth + 1) || explodeRecursive(node.right, depth + 1)
        } else false // No explosion occurred in this subtree
    }

    companion object {
        fun String.toBinaryTree(): BinaryTree? {
            val gson = Gson()
            return try {
                val list: List<Any?>? = gson.fromJson(this, object : TypeToken<List<Any?>>() {}.type)
                list.asBinaryTree()
            } catch (e: Exception) {
                error("Invalid input string for BinaryTree parsing $this")
                null
            }
        }

        fun Any?.asBinaryTree(parent: BinaryTree? = null): BinaryTree? =
            when (this) {
                is Double, is Float -> Leaf(value = toInt(), parent = parent)
                is Int -> Leaf(value = this, parent = parent)
                is List<*> -> {
                    if (isEmpty())
                        null
                    else {
                        val leftChild = if (isNotEmpty()) first().asBinaryTree() else null
                        val rightChild = if (size > 1) tail().first().asBinaryTree() else null

                        if (leftChild == null && rightChild == null) {
                            if (size == 1) {
                                when (val value = first()) {
                                    is Double, is Float -> Leaf(value = value.toInt(), parent = parent)
                                    is Int -> Leaf(value = value, parent = parent)
                                    else -> null
                                }
                            } else null
                        } else {
                            val branch = Branch(
                                left = leftChild!!,
                                right = rightChild!!,
                                parent = parent
                            )

                            leftChild.parent = branch
                            rightChild.parent = branch

                            branch
                        }
                    }
                }
                else -> null
            }
    }
}