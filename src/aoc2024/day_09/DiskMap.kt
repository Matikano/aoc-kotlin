package aoc2024.day_09

import utils.*

data class DiskMap(
    private val layout: String
) {

    private val diskBlocksChain: List<Int?>
        get() = layout.calculateDiskBlocks()

    private val compactedDiskBlocksChain: List<Int?>
        get() = diskBlocksChain.compactDiskBlockChain()

    private val compressedDiskBlocksChain: List<Int?>
        get() = diskBlocksChain.compressDiskBlockChain()

    val checkSum: Long
        get() = compressedDiskBlocksChain.checkSum()

    val compactedCheckSum: Long
        get() = compactedDiskBlocksChain.checkSum()

    private fun String.calculateDiskBlocks(): List<Int?> = buildList {
        var blockId = 0

        this@calculateDiskBlocks.forEachIndexed { index, char ->
            if (index % 2 == 0) {
                val id = blockId++
                repeat(char.digitToInt()) {
                    add(id)
                }
            } else {
                repeat(char.digitToInt()) {
                    add(null)
                }
            }
        }
    }

    private fun <T> List<T>.compressDiskBlockChain(): List<T> {
        var indexOfFirstEmptySpace = indexOfNextNull(0)
        var indexOfLastDigit = indexOfPreviousNotNull(size - 1)
        var output = this

        while (indexOfFirstEmptySpace <= indexOfLastDigit) {
            output = output.swap(indexOfFirstEmptySpace, indexOfLastDigit)
            indexOfFirstEmptySpace = output.indexOfNextNull(indexOfFirstEmptySpace)
            indexOfLastDigit = output.indexOfPreviousNotNull(indexOfLastDigit)
        }

        return output
    }

    private fun List<Int?>.compactDiskBlockChain(): List<Int?> {
        var output = this

        val numbers = filterNotNull()
            .distinct()
            .reversed()

        for (number in numbers) {
            val indexOfFirstNumber = output.indexOfFirst { it == number }
            val indexOfLastNumber = output.indexOfLast { it == number }
            val emptySpaces = output.emptySpaces().map { it.toList() }
            val digitChainSize = indexOfLastNumber + 1 - indexOfFirstNumber

            emptySpaces
                .firstOrNull { it.size >= digitChainSize }
                ?.let {
                    if (indexOfFirstNumber > it.last())
                        output = output.swapRange(
                            firstRange = it.take(digitChainSize),
                            secondRange = (indexOfFirstNumber..indexOfLastNumber).toList()
                        )
                }
        }

        return output
    }

    private fun List<Int?>.checkSum(): Long =
        foldIndexed(0) { index, acc, number ->
            acc + (number ?: 0) * index
        }
}
