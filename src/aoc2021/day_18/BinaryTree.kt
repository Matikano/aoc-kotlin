package aoc2021.day_18

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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

    fun copy(): BinaryTree = toString().toBinaryTree() ?: error("Error while copying BinaryTree")

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
            get() {
                var current: BinaryTree? = this
                while (current == (current?.parent as? Branch)?.left)
                    current = current?.parent

                return findRightmostLeaf((current?.parent as? Branch)?.left)
            }

        val rightNeighbor: Leaf?
            get() {
                var current: BinaryTree? = this
                while (current == (current?.parent as? Branch)?.right)
                    current = current?.parent

                return findLeftmostLeaf((current?.parent as? Branch)?.right)
            }

        private fun findRightmostLeaf(node: BinaryTree?): Leaf? {
            var current = node
            while (current is Branch)
                current = current.right

            return current as? Leaf
        }

        private fun findLeftmostLeaf(node: BinaryTree?): Leaf? {
            var current = node
            while (current is Branch)
                current = current.left

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

    fun explode(): Boolean = explode(this, 0)
    fun split(): Boolean = split(this)

    private fun split(node: BinaryTree): Boolean =
         when (node) {
            is Leaf -> (node.value >= SPLIT_VALUE).also { shouldSplit ->
                if (shouldSplit) {
                    val leftLeaf = Leaf(value = floor(node.value / 2f).toInt())
                    val rightLeaf = Leaf(value = ceil(node.value / 2f).toInt())

                    Branch(
                        left = leftLeaf,
                        right = rightLeaf,
                        parent = node.parent
                    ).also { splitBranch ->
                        leftLeaf.parent = splitBranch
                        rightLeaf.parent = splitBranch
                        val parent = node.parent as Branch

                        if (parent.left == node) parent.left = splitBranch
                        else parent.right = splitBranch
                    }
                }
            }

            is Branch -> split(node.left) || split(node.right)
        }

    private fun explode(node: BinaryTree, depth: Int): Boolean =
        when (node) {
            is Branch -> if (depth >= EXPLODE_DEPTH && node.left is Leaf && node.right is Leaf) {
                val leftLeaf = node.left as Leaf
                val rightLeaf = node.right as Leaf
                val parent = node.parent as Branch

                node.leftNeighbor?.apply { value += leftLeaf.value }
                node.rightNeighbor?.apply { value += rightLeaf.value }

                // Replace the branch with a leaf with value 0
                Leaf(value = 0, parent = node.parent).also { zeroLeaf ->
                    if (parent.left == node) parent.left = zeroLeaf
                    else parent.right = zeroLeaf
                }

                true
            } else explode(node.left, depth + 1) || explode(node.right, depth + 1)

            is Leaf -> false
        }

    companion object {
        private const val SPLIT_VALUE = 10
        private const val EXPLODE_DEPTH = 4

        fun String.toBinaryTree(): BinaryTree? =
            with(Gson()) {
                try {
                    val list: List<Any?>? = fromJson(this@toBinaryTree, object : TypeToken<List<Any?>>() {}.type)
                    list.asBinaryTree()
                } catch (_: Exception) {
                    error("Invalid input string for BinaryTree parsing $this")
                    null
                }
            }

        private fun Any?.asBinaryTree(parent: BinaryTree? = null): BinaryTree? =
            when (this) {
                is Double, is Float -> Leaf(value = toInt(), parent = parent)
                is Int -> Leaf(value = this, parent = parent)
                is List<*> -> takeIf { it.isNotEmpty() }?.let {
                    val leftChild = first().asBinaryTree()
                    val rightChild = if (size > 1) last().asBinaryTree() else null

                    if (leftChild == null && rightChild == null) {
                        when (val value = firstOrNull()) {
                            is Double, is Float -> Leaf(value = value.toInt(), parent = parent)
                            is Int -> Leaf(value = value, parent = parent)
                            else -> null
                        }
                    } else {
                        Branch(
                            left = leftChild!!,
                            right = rightChild!!,
                            parent = parent
                        ).also {
                            leftChild.parent = it
                            rightChild.parent = it
                        }
                    }
                }
                else -> null
            }
    }
}