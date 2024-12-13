package aoc2024.day_13

typealias Combination = Pair<Long, Long>

data class ClawMachine(
    val buttonA: Button,
    val buttonB: Button,
    val prize: Pair<Long, Long>
) {
    private val combination: Combination?
        get() {
             val aCount = (prize.first * buttonB.dy - prize.second * buttonB.dx) /
                     (buttonA.dx * buttonB.dy - buttonA.dy * buttonB.dx).toDouble()
             val bCount = (prize.first - buttonA.dx * aCount) / buttonB.dx.toDouble()

             return if (aCount % 1 == 0.0 && bCount % 1 == 0.0)
                aCount.toLong() to bCount.toLong()
             else null
        }

    val cheapestCombination: Long?
        get() = combination
            ?.takeIf { it.first <= TOKEN_LIMIT && it.second <= TOKEN_LIMIT }
            ?.cost()

    val cheapestCombinationPart2: Long?
        get() = combination?.cost()

    private fun Combination.cost() = first * buttonA.cost + second * buttonB.cost

    companion object {
        private const val TOKEN_LIMIT = 100L
    }
}
