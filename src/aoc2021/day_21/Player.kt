package aoc2021.day_21

data class Player(
    var position: Int,
    var score: Int = 0
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Player

        if (position != other.position) return false
        if (score != other.score) return false

        return true
    }

    override fun hashCode(): Int {
        var result = position
        result = 31 * result + score
        return result
    }

    fun copy(): Player = Player(position, score)

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
