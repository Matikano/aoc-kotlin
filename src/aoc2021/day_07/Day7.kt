package aoc2021.day_07

import utils.AocTask
import utils.extensions.NthSum
import utils.extensions.numsInt
import kotlin.math.absoluteValue
import kotlin.time.measureTime

object Day7: AocTask() {

    override fun executeTask() {
        measureTime {
            val list = testInput.numsInt()
            println("Minimum fuel for alignment = ${list.minimumFuelForAlignment}")
            println("Minimum fuel for increasing alignment = ${list.minimumFuelForAlignmentWithIncreasingCost}")
        }.let { println("Test part took $it\n") }

        measureTime {
            val list = input.numsInt()
            println("Minimum fuel for alignment = ${list.minimumFuelForAlignment}")
        }.let { println("Part 1 took $it\n") }

        measureTime {
            val list = input.numsInt()
            println("Minimum fuel for increasing alignment = ${list.minimumFuelForAlignmentWithIncreasingCost}")
        }.let { println("Part 2 took $it\n") }
    }

    private val List<Int>.minimumFuelForAlignment: Int
        get() = distances.values.min()

    private val List<Int>.minimumFuelForAlignmentWithIncreasingCost: Long
        get() = increasingCostDistances.values.min()

    private val List<Int>.distances: Map<Int, Int>
        get() = (min()..max()).associate { value -> value to sumOf { (value - it).absoluteValue }  }

    private val List<Int>.increasingCostDistances: Map<Int, Long>
        get() = (min()..max()).associate { value -> value to sumOf { NthSum((value - it).absoluteValue.toLong()) }  }
}