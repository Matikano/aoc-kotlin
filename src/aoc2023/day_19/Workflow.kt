package aoc2023.day_19

data class Workflow(
    val label: String,
    val rules: List<Rule>,
    val fallback: String
) {
    override fun toString(): String = "$label{${rules.joinToString(",")},$fallback}"

    fun processPart(part: Part): String {
        var nextWorkflowLabel: String? = null

        for (rule in rules) {
            nextWorkflowLabel = rule.processPart(part)
            if (nextWorkflowLabel != null)
                return nextWorkflowLabel
        }

        return fallback
    }
}
