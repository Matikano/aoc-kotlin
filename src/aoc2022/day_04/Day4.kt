package aoc2022.day_04

import utils.AocTask
import utils.extensions.contains
import utils.extensions.numsInt
import utils.extensions.overlaps
import kotlin.time.measureTime

typealias Assignment = Pair<IntRange, IntRange>

object Day4: AocTask() {

    override fun executeTask() {
        measureTime {
            val assignments = testInput.toAssignments()
            println(assignments)
            println("Sum of faulty assignments = ${assignments.count { it.faulty }}")
        }.let { println("Test part took $it\n") }

        measureTime {
            val assignments = input.toAssignments()
            println("Sum of faulty assignments = ${assignments.count { it.faulty }}")
        }.let { println("Part took $it\n") }

        measureTime {
            val assignments = input.toAssignments()
            println("Sum of faulty assignments = ${assignments.count { it.overlapping }}")
        }.let { println("Part 2 took $it\n") }
    }

    private fun String.toAssignments(): List<Assignment> = lines().map { it.toAssignment() }

    private fun String.toAssignment(): Assignment {
        val (range1Start, range1End, range2Start, range2End) = replace('-', ',').numsInt()
        return range1Start..range1End to range2Start..range2End
    }

    private val Assignment.faulty: Boolean
        get() = first in second || second in first

    private val Assignment.overlapping: Boolean
        get() = first overlaps second
}