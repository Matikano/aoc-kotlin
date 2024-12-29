package aoc2023.day_19

data class Rule(
    val categoryLabel: Char,
    val operator: Char,
    val comparisonValue: Int,
    val nextWorkflow: String
) {

    override fun toString(): String = "$categoryLabel$operator$comparisonValue:$nextWorkflow"

    fun processPart(part: Part): String? {
        val comparisonCategory = when(categoryLabel) {
            CATEGORY_X -> part.x
            CATEGORY_M -> part.m
            CATEGORY_A -> part.a
            CATEGORY_S -> part.s
            else -> throw IllegalArgumentException("Unsupported category label = $categoryLabel (only accepts \"xmas\"")
        }

        val satisfiesRule: Boolean = when (operator) {
            LESS_OPERATOR -> comparisonCategory < comparisonValue
            GREATER_OPERATOR -> comparisonCategory > comparisonValue
            else -> throw IllegalArgumentException("Unsupported operator character = $operator (only accepts '<' and '>'")
        }

        return nextWorkflow.takeIf { satisfiesRule }
    }

    companion object {
        const val LESS_OPERATOR = '<'
        const val GREATER_OPERATOR = '>'

        const val CATEGORY_X = 'x'
        const val CATEGORY_M = 'm'
        const val CATEGORY_A = 'a'
        const val CATEGORY_S = 's'
    }
}
