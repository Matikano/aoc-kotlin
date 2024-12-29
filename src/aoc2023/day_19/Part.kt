package aoc2023.day_19

data class Part(
    val x: Int,
    val m: Int,
    val a: Int,
    val s: Int
) {
    val ratingNumber: Int
        get() = x + m + a + s

    fun isAccepted(workflowMap: Map<String, Workflow>, workflowLabel: String = INITIAL_WORKFLOW_LABEL): Boolean =
        when (workflowLabel) {
            ACCEPTED_LABEL -> true
            REJECTED_LABEL -> false
            else -> isAccepted(workflowMap, workflowMap[workflowLabel]!!.processPart(this))
        }

    companion object {
        const val INITIAL_WORKFLOW_LABEL = "in"
        const val ACCEPTED_LABEL = "A"
        const val REJECTED_LABEL = "R"
    }
}
