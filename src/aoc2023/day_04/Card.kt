package aoc2023.day_04

import kotlin.math.pow

data class Card(
    val id: Int,
    val winningNumbers: List<Int>,
    val actualNumbers: List<Int>
){
    val winningNumbersCount: Int
        get() = winningNumbers.count { it in actualNumbers }

    val score: Int
        get() = if (winningNumbersCount > 0)
            2.0.pow(winningNumbersCount - 1).toInt()
        else 0
}
