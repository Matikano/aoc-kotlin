package aoc2023.day_22

import utils.AocTask
import utils.extensions.duplicates
import utils.extensions.emptySpaces
import utils.extensions.length
import utils.extensions.numsInt
import kotlin.time.measureTime

object Day22: AocTask() {

    override fun executeTask() {
        measureTime {
            val bricks = testInput.toListOfBricks()
            val bricksAfterFalling = bricks.toMutableList().fallDown()

            println(bricksAfterFalling.supportsMap())
            println(bricksAfterFalling.supportedByMap())

            println("Possible disintegrations for test data = ${bricksAfterFalling.possibleDisintergrations()}")
        }.let { println("Test part took $it\n") }

        measureTime {
            val bricks = input.trim().toListOfBricks()
            val bricksAfterFalling = bricks.toMutableList().fallDown()

            println(bricksAfterFalling.supportsMap())
            println(bricksAfterFalling.supportedByMap())

            println("Possible disintegrations = ${bricksAfterFalling.possibleDisintergrations()}")
        }.let { println("Part 1 took $it\n") }
    }

    private fun List<Brick>.possibleDisintergrations(): Int {
        val supports = supportsMap()
        val supportedBy = supportedByMap()
        return count {
            val index = indexOf(it)
            supports[index]!!.all { supportedBy[it]!!.size > 1 }
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
                maxZ = if (brick.overlaps(previousBrick))
                    previousBrick.zRange.last + 1
                else previousBrick.zRange.last
            }

            this[index] = brick.copy(zRange = maxZ..< brick.zRange.length + maxZ)
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