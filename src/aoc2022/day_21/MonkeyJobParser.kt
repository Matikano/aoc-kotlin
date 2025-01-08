package aoc2022.day_21

import aoc2022.day_21.Day21.HUMAN_NODE
import utils.abstractions.RecursiveMemo1

class MonkeyJobParser(
    val monkeys: Map<String, Job>
): RecursiveMemo1<String, String>() {
    override fun MutableMap<String, String>.recurse(monkey: String): String = getOrPut(monkey) {
        when (val job = monkeys[monkey]) {
            is Job.Value -> if (monkey == HUMAN_NODE) "x" else "(${job.value})"
            is Job.Expression -> "(${recurse(job.operand1)} ${job.operation.symbol} ${recurse(job.operand2)})"
            else -> error("Unexpected Job type")
        }
    }
}