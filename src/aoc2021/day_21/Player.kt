package aoc2021.day_21

data class Player(
    var position: Int,
    var score: Int = 0
) {
    fun takeATurn(rollSum: Int) {
        position = (position - 1 + rollSum) % POSITIONS.size + 1
        score += position
    }

    val isWinning: Boolean
        get() = score >= WINNING_SCORE_THRESHOLD

    companion object {
        val POSITIONS = (1..10).toList()
        const val WINNING_SCORE_THRESHOLD = 1000
    }
}
