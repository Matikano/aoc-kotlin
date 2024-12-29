package aoc2023.day_19

import aoc2023.day_19.Part.Companion.INITIAL_WORKFLOW_LABEL
import aoc2023.day_19.Part.Companion.ACCEPTED_LABEL
import aoc2023.day_19.Part.Companion.REJECTED_LABEL
import aoc2023.day_19.Rule.Companion.CATEGORY_A
import aoc2023.day_19.Rule.Companion.CATEGORY_M
import aoc2023.day_19.Rule.Companion.CATEGORY_S
import aoc2023.day_19.Rule.Companion.CATEGORY_X
import aoc2023.day_19.Rule.Companion.LESS_OPERATOR
import utils.AocTask
import utils.extensions.head
import utils.extensions.isValid
import utils.extensions.length
import utils.extensions.numsInt
import utils.extensions.tail
import java.math.BigInteger
import kotlin.math.min
import kotlin.time.measureTime

object Day19: AocTask() {

    private  val INITIAL_RANGE = 1..4000

    val rangesMap = mapOf(
        CATEGORY_X to INITIAL_RANGE,
        CATEGORY_M to INITIAL_RANGE,
        CATEGORY_A to INITIAL_RANGE,
        CATEGORY_S to INITIAL_RANGE,
    )

    override fun executeTask() {

        measureTime {
            val (workflows, parts) = testInput.toWorkflowsAndParts()
            val workflowMap = workflows.associate { it.label to it }
            val ratings = parts.filter { it.isAccepted(workflowMap) }.sumOf { it.ratingNumber }
            println("Sum of rating numbers of accepted test parts = $ratings")

            val distinctCombinations = distinctAcceptableParts(workflowMap, rangesMap)
            println("Test distinct combinations = $distinctCombinations")
        }.let { println("Test part took $it\n") }

        measureTime {
            val (workflows, parts) = input.trim().toWorkflowsAndParts()
            val workflowMap = workflows.associate { it.label to it }
            val ratings = parts.filter { it.isAccepted(workflowMap) }.sumOf { it.ratingNumber }
            println("Sum of rating numbers of accepted parts = $ratings")
        }.let { println("Part 1 took $it\n") }

        measureTime {
            val (workflows, _) = input.trim().toWorkflowsAndParts()
            val workflowMap = workflows.associate { it.label to it }
            val distinctCombinations = distinctAcceptableParts(workflowMap, rangesMap)

            println("Distinct combinations = $distinctCombinations")
        }.let { println("Part 2 took $it\n") }
    }

    private fun distinctAcceptableParts(
        workflowMap: Map<String, Workflow>,
        categoryRanges: Map<Char, IntRange>,
        workflowLabel: String = INITIAL_WORKFLOW_LABEL
    ): BigInteger {
        if (workflowLabel == REJECTED_LABEL)
            return BigInteger.ZERO
        if (workflowLabel == ACCEPTED_LABEL)
            return categoryRanges.values.fold(BigInteger.ONE) { acc, a ->
                acc.times(BigInteger.valueOf(a.length.toLong()))
            }

        val workflow = workflowMap[workflowLabel]!!
        val (rules, fallbackWorkflow) = workflow.rules to workflow.fallback

        var total = BigInteger.ZERO
        var done = false
        val ranges = categoryRanges.toMutableMap()

        for ((category, operator, comparisonValue, targetWorkflow) in rules) {
            val currentRange = ranges[category]!!
            var acceptableRange: IntRange
            var rejectedRange: IntRange
            if (operator == LESS_OPERATOR) {
                acceptableRange = currentRange.start..min(comparisonValue - 1, currentRange.endInclusive)
                rejectedRange = comparisonValue..currentRange.endInclusive
            } else {
                acceptableRange = comparisonValue + 1 .. currentRange.endInclusive
                rejectedRange = currentRange.start .. comparisonValue
            }

            if (acceptableRange.isValid) {
                val newRanges = ranges.toMap().toMutableMap()
                newRanges[category] = acceptableRange
                total += distinctAcceptableParts(workflowMap, newRanges, targetWorkflow)
            }

            if (rejectedRange.isValid) {
                ranges[category] = rejectedRange
            } else {
                done = true
                break
            }
        }

        if (!done)
            total += distinctAcceptableParts(workflowMap, ranges, fallbackWorkflow)

        return total
    }

    private fun String.toWorkflowsAndParts(): Pair<List<Workflow>, List<Part>> {
        val (workflows, parts) = split("\n\n")

        return workflows.toWorkflows() to parts.toParts()
    }

    private fun String.toWorkflows(): List<Workflow> = lines().map { line ->
        val (label, rulesChain) = line.dropLast(1).split('{')

        val splitRules = rulesChain.split(',')
        val fallback = splitRules.last()
        val ruleStrings = splitRules.dropLast(1)

        Workflow(
            label = label,
            rules = ruleStrings.map { it.toRule() },
            fallback = fallback
        )
    }

    private fun String.toParts(): List<Part> = lines().map { line ->
        val (x, m, a, s) = line.numsInt()
        Part(x, m, a, s)
    }

    private fun String.toRule(): Rule {
        val (comparison, workflow) = split(":")
        return Rule(
            categoryLabel = comparison.head(),
            operator = comparison.tail().head(),
            comparisonValue = comparison.tail().tail().toInt(),
            nextWorkflow = workflow
        )
    }
}