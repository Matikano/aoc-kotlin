package aoc2024.day_02

import utils.AocTask
import utils.extensions.numsInt
import utils.extensions.subListsWithOneDroppedElement


object Day2: AocTask() {

    private const val MIN_DIFFERENCE = 1
    private const val MAX_DIFFERENCE = 3

    override fun executeTask() {
        with(readToList()) {
            // Part 1
            println("Safe records = ${countSafeRecords()}")

            // Part 2
            println("Safe records after dampening = ${countSafeRecordsWithDampener()}")
        }
    }

    private fun readToList(): List<List<Int>> = inputToList().map { it.numsInt() }

    // Part 1
    private fun List<List<Int>>.countSafeRecords(): Int =
        count { it.isAscendingWithDifference() or it.isDescendingWithDifference() }

    private fun List<Int>.isAscendingWithDifference(
        minDifference: Int = MIN_DIFFERENCE,
        maxDifference: Int = MAX_DIFFERENCE
    ): Boolean =
        zipWithNext { a, b ->
            a - b
        }.all { it in minDifference..maxDifference }

    private fun List<Int>.isDescendingWithDifference(
        minDifference: Int = MIN_DIFFERENCE,
        maxDifference: Int = MAX_DIFFERENCE
    ): Boolean =
        zipWithNext { a, b ->
            b - a
        }.all { it in minDifference..maxDifference }

    // Part 2
    private fun List<Int>.isSafeAfterDampening(): Boolean =
        subListsWithOneDroppedElement()
            .any { it.isDescendingWithDifference() or it.isAscendingWithDifference() }

    private fun List<List<Int>>.countSafeRecordsWithDampener(): Int =
        count {
            it.isAscendingWithDifference() or
                it.isDescendingWithDifference() or
                it.isSafeAfterDampening()
        }
}





