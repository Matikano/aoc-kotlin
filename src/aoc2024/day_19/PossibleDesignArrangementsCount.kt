package aoc2024.day_19

import utils.abstractions.RecursiveMemo1

class PossibleDesignArrangementsCount(
    private val patterns: List<String>
): RecursiveMemo1<String, Long>() {
    override fun MutableMap<String, Long>.recurse(design: String): Long =
        getOrPut(design) {
            if (design.isEmpty()) 1L
            else {
                var count = 0L
                for (pattern in patterns) {
                    if (design.startsWith(pattern))
                        count += recurse(design.substringAfter(pattern))
                }
                count
            }
        }
}