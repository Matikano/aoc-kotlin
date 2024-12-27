package aoc2023.day_06

data class Race(
    private val time: Long,
    private val recordDistance: Long
) {

    val possibleNewRecords: Int
        get() = (0..time).count { buttonHoldTime ->
            buttonHoldTime * (time - buttonHoldTime) > recordDistance
        }
}