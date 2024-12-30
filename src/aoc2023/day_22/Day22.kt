package aoc2023.day_22

import utils.AocTask
import utils.extensions.duplicates
import utils.extensions.emptySpaces
import utils.extensions.length
import utils.extensions.numsInt
import kotlin.math.max
import kotlin.time.measureTime

object Day22: AocTask() {

    override fun executeTask() {
        measureTime {
            val bricks = testInput.toListOfBricks()
            val bricksAfterFalling = bricks.toMutableList().fallDown()

            println(bricksAfterFalling.supportsMap())
            println(bricksAfterFalling.supportedByMap())

            println("Possible disintegration for test data = ${bricksAfterFalling.possibleDisintergrations()}")
        }.let { println("Test part took $it\n") }

        measureTime {
            val bricks = input.trim().toListOfBricks()
            val bricksAfterFalling = bricks.toMutableList().fallDown()

            println("Possible disintegration = ${bricksAfterFalling.possibleDisintergrations()}")
        }.let { println("Part 1 took $it\n") }

        measureTime {
            val bricks = input.trim().toListOfBricks()
            val bricksAfterFalling = bricks.toMutableList().fallDown()

            println("Chain disintegration sum = ${bricksAfterFalling.chainDisintegrationSum()}")
        }.let { println("Part 2 took $it\n") }
    }

    private fun List<Brick>.chainDisintegrationSum(): Int {
        val supports = supportsMap()
        val supportedBy = supportedByMap()

        var total = 0

        indices.forEach { index ->
            val queue = ArrayDeque(supports[index]!!.filter { supportedBy[it]!!.size == 1 })
            val falling = mutableSetOf(*queue.toTypedArray())
            falling.add(index)

            while (queue.isNotEmpty()) {
                val fallingIndex = queue.removeFirst()
                for (supported in supports[fallingIndex]!! - falling) {
                    if (falling.containsAll(supportedBy[supported]!!)){
                        queue.add(supported)
                        falling.add(supported)
                    }
                }
            }
            total += falling.size - 1
        }

        return total
    }

    private fun List<Brick>.possibleDisintergrations(): Int {
        val supports = supportsMap()
        val supportedBy = supportedByMap()
        return count {
            val index = indexOf(it)
            supports[index]!!.all { supportedBy[it]!!.size >= 2 }
        }
    }

    private fun List<Brick>.supportedByMap(): Map<Int, Set<Int>> {
        val map = indices.associateWith { mutableSetOf<Int>() }.toMutableMap()

        forEachIndexed { upperIndex, upper ->
            take(upperIndex).forEachIndexed { lowerIndex, lower ->
                if (lower.supports(upper))
                    map[upperIndex]!!.add(lowerIndex)
            }
        }

        return map
    }

    private fun List<Brick>.supportsMap(): Map<Int, Set<Int>> {
        val map = indices.associateWith { mutableSetOf<Int>() }.toMutableMap()

        forEachIndexed { upperIndex, upper ->
            take(upperIndex).forEachIndexed { lowerIndex, lower ->
                if (lower.supports(upper))
                    map[lowerIndex]!!.add(upperIndex)
            }
        }

        return map
    }

    private fun MutableList<Brick>.fallDown(): List<Brick> = this.also {
        forEachIndexed { index, brick ->
            var maxZ = 1

            (0..< index).forEach { previousIndex ->
                val previousBrick = this[previousIndex]
                if (brick.overlaps(previousBrick))
                    maxZ = max(maxZ, previousBrick.zRange.last + 1)
            }

            this[index] = brick.copy(zRange =  maxZ ..< brick.zRange.length + maxZ)
        }
    }.sortedBy { it.zRange.first }

    private fun String.toListOfBricks() =
        lines().map { it.toBrick() }.sortedBy { it.zRange.first }

    private fun String.toBrick(): Brick {
        val (start, end) = split('~')
        val (xRange, yRange, zRange) = start.numsInt()
            .zip(end.numsInt()) { a, b -> a..b }
        return Brick(
            xRange = xRange,
            yRange = yRange,
            zRange = zRange
        )
    }
}