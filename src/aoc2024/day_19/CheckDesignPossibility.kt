package aoc2024.day_19

import utils.abstractions.RecursiveMemo1

class CheckDesignPossibility(
    private val patterns: List<String>
): RecursiveMemo1<String, Boolean>() {
    override fun MutableMap<String, Boolean>.recurse(design: String): Boolean =
        getOrPut(design) {
            if (design.isEmpty()) true
            else patterns.any { pattern ->
                design.startsWith(pattern) && recurse(design.substringAfter(pattern))
            }
        }
}