package aoc2021.day_06

import aoc2021.day_06.LanternFishCount.LanternFishState
import utils.abstractions.RecursiveMemo1

object LanternFishCount: RecursiveMemo1<LanternFishState, Long>() {
    private const val NEW_LANTERN_FISH_VALUE = 8
    private const val REFRESHING_FISH_VALUE = 6

    override fun MutableMap<LanternFishState, Long>.recurse(
        state: LanternFishState
    ): Long = getOrPut(state) {
        val (time, value) = state
        when (time) {
            0 -> 1
            else -> {
                when (state.value) {
                    0 -> recurse(LanternFishState(time = time - 1, value = REFRESHING_FISH_VALUE)) +
                            recurse(LanternFishState(time = time - 1, value = NEW_LANTERN_FISH_VALUE))
                    else -> recurse(LanternFishState(time = time - 1, value = value - 1))
                }
            }
        }
    }

    data class LanternFishState(
        val time: Int,
        val value: Int
    )
}
