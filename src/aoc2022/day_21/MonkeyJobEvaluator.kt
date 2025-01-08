package aoc2022.day_21

import utils.abstractions.RecursiveMemo1

class MonkeyJobEvaluator(
    val monkeys: Map<String, Job>
): RecursiveMemo1<String, Long>() {
    override fun MutableMap<String, Long>.recurse(monkey: String): Long = getOrPut(monkey) {
        when (val job = monkeys[monkey]) {
            is Job.Value -> job.value
            is Job.Expression -> job.operation.compute(recurse(job.operand1), recurse(job.operand2))
            else -> error("Unexpected Job type")
        }
    }
}