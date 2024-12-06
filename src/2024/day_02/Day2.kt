package `2024`.day_02

import `2024`.AocTask


object Day2: AocTask {

    private const val MIN_DIFFERENCE = 1
    private const val MAX_DIFFERENCE = 3
    private const val SEPARATOR = " "

    override val fileName: String
        get() = "src/2024/day_02/input.txt"

    override fun executeTask() {
        println("-------------------------------------")
        println("AoC 2024 Task ${this.javaClass.simpleName}")
        println()

        with(readToList()) {
            // Part 1
            println("Safe records = ${countSafeRecords()}")

            // Part 2
            println("Safe records after dampening = ${countSafeRecordsWithDampener()}")
        }
    }

    private fun readToList(): List<List<Int>> =
        mutableListOf<List<Int>>().apply {
            readFileByLines { line ->
                add(line.split(SEPARATOR).map { it.toInt() })
            }
        }

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
    private fun <T> List<T>.getListsWithOneItemDropped(): List<List<T>> =
        mutableListOf<List<T>>().also { list ->
            indices.forEach {
                list.add(
                    this.filterIndexed { index, _ -> index != it }
                )
            }
        }

    private fun List<Int>.isSafeAfterDampening(): Boolean =
        getListsWithOneItemDropped()
            .any { it.isDescendingWithDifference() or it.isAscendingWithDifference() }

    private fun List<List<Int>>.countSafeRecordsWithDampener(): Int =
        count {
            it.isAscendingWithDifference() or
                    it.isDescendingWithDifference() or
                    it.isSafeAfterDampening()
        }
}





