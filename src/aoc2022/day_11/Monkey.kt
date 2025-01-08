package aoc2022.day_11

import utils.models.Operation

data class Monkey(
    val items: MutableList<Long>,
    val operation: Operation,
    val operand: Long? = null,
    val modulus: Int,
    val positiveTestIndex: Int,
    val negativeTestIndex: Int,
) {
    var itemInspectionCount = 0L

    fun processRound(keepWorryLevelLow: Boolean = true): List<ItemThrow> {
        var item: Long
        val throws = mutableListOf<ItemThrow>()

        while (items.isNotEmpty()) {
            item = items.removeFirst()
            itemInspectionCount++

            var itemToThrow = operation.compute(item, operand ?: item)
            if (keepWorryLevelLow)
                itemToThrow /= ITEM_INSPECTION_FACTOR

            throws.add(
                ItemThrow(
                    item = itemToThrow,
                    targetIndex = if (itemToThrow % modulus == 0L) positiveTestIndex else negativeTestIndex
                )
            )
        }

        return throws
    }

    fun addItem(item: Long) = items.add(item)

    companion object {
        private const val ITEM_INSPECTION_FACTOR = 3L
    }
}
