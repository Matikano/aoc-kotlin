package aoc2022.day_11

data class Monkey(
    val items: MutableList<Int>,
    val operation: Operation,
    val operand: Int? = null,
    val modulus: Int,
    val positiveTestIndex: Int,
    val negativeTestIndex: Int,
) {
    var itemInspectionCount = 0

    fun processRound(): List<ItemThrow> {
        var item: Int
        val throws = mutableListOf<ItemThrow>()

        while (items.isNotEmpty()) {
            item = items.removeFirst()
            itemInspectionCount++

            val itemToThrow = operation.compute(item, operand ?: item) / ITEM_INSPECTION_FACTOR

            throws.add(
                ItemThrow(
                    item = itemToThrow,
                    targetIndex = if (itemToThrow % modulus == 0) positiveTestIndex else negativeTestIndex
                )
            )
        }

        return throws
    }

    fun addItem(item: Int) = items.add(item)

    companion object {
        private const val ITEM_INSPECTION_FACTOR = 3
    }
}
