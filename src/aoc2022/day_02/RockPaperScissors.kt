package aoc2022.day_02

enum class RockPaperScissors(val score: Int) {
    ROCK(1),
    PAPER(2),
    SCISSORS(3);

    fun matchScore(other: RockPaperScissors): Int =
        when (other) {
            winsAgainst() -> 6
            this -> 3
            losesAgainst() -> 0
            else -> throw IllegalArgumentException()
        } + score

    fun winsAgainst(): RockPaperScissors = when (this) {
        ROCK -> SCISSORS
        PAPER -> ROCK
        SCISSORS -> PAPER
    }

    fun losesAgainst(): RockPaperScissors = when (this) {
        ROCK -> PAPER
        PAPER -> SCISSORS
        SCISSORS -> ROCK
    }

    companion object {
        fun String.toRPS(): RockPaperScissors =
            when (this) {
                in "AX" -> ROCK
                in "BY" -> PAPER
                in "CZ" -> SCISSORS
                else -> throw IllegalArgumentException("Unsupported mapping from $this to Rock/Paper/Scissors")
            }

        fun findCorrectRPS(against: RockPaperScissors, matchResultCode: String): RockPaperScissors {
            return when (matchResultCode) {
                "Y" -> against
                "X" -> against.winsAgainst()
                "Z" -> against.losesAgainst()
                else -> throw IllegalArgumentException("Unsupported mapping from $this to Rock/Paper/Scissors")
            }
        }
    }
}