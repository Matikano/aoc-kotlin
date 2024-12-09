package aoc2024.day_09

import aoc2024.emptySpaces
import aoc2024.swap
import aoc2024.swapRange

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
        get() = compressedDiskBlocksChain
            .foldIndexed(0L) { index, acc, number ->
                acc + (number ?: 0) * index
            }

    val compactedCheckSum: Long
        get() = compactedDiskBlocksChain
            .foldIndexed(0L) { index, acc, number ->
                acc + (number ?: 0) * index
            }

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
        var indexOfFirstEmptySpace = indexOfFirst { it == null }
        var indexOfLastDigit = indexOfLast { it != null }
        var output = this

        while (indexOfFirstEmptySpace <= indexOfLastDigit) {
            output = output.swap(indexOfFirstEmptySpace, indexOfLastDigit)
            indexOfFirstEmptySpace = output.indexOfFirst { it == null }
            indexOfLastDigit = output.indexOfLast { it != null }
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
                    if (indexOfFirstNumber > it.last() )
                        output = output.swapRange(
                            firstRange = it.take(digitChainSize),
                            secondRange = (indexOfFirstNumber..indexOfLastNumber).toList()
                        )
                }
        }

        return output
    }
}
