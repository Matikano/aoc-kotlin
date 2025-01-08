package aoc2022.day_21

import utils.AocTask
import utils.models.Operation.Companion.toOperation
import kotlin.time.measureTime

object Day21: AocTask() {

    private const val ROOT_MONKEY = "root"
    const val HUMAN_NODE = "humn"

    override fun executeTask() {
        measureTime {
            val monkeys = testInput.toMonkeys()
            val monkeyJobEvaluator = MonkeyJobEvaluator(monkeys)

            val rootMonkeyValue = monkeyJobEvaluator(ROOT_MONKEY)
            println("Test root monkey value = $rootMonkeyValue")

            println("Human value to pass = ${monkeys.findHumanValueToPassTest()}")
        }.let { println("Test part took $it\n") }

        measureTime {
            val monkeys = input.toMonkeys()
            val monkeyJobEvaluator = MonkeyJobEvaluator(monkeys)

            val rootMonkeyOperandValue = monkeyJobEvaluator((monkeys[ROOT_MONKEY] as Job.Expression).operand1)
            println("Root monkey value = $rootMonkeyOperandValue")
        }.let { println("Part 1 took $it\n") }

        measureTime {
            val monkeys = input.toMonkeys().toMutableMap()
            println(monkeys.findHumanValueToPassTest())
        }.let { println("Part 2 took $it\n") }
    }

    private fun Map<String, Job>.findHumanValueToPassTest(): Long {
        val (rootOperand1, rootOperand2, _) = (get(ROOT_MONKEY) as Job.Expression)
        val map = toMutableMap()
        var evaluator = MonkeyJobEvaluator(map)
        var value = 3099532691300
        map[HUMAN_NODE] = Job.Value(value)
        var (value1, value2) = evaluator(rootOperand1) to evaluator(rootOperand2)
        println("Value = $value, Value1 = $value1, Value2 = $value2")
        while (value1 != value2) {
            if (value1 > value2) {
                value -= value / 2
            } else {
                value += value / 2
            }
            map[HUMAN_NODE] = Job.Value(value)
            evaluator = MonkeyJobEvaluator(map)
            value1 = evaluator(rootOperand1)
            value2 = evaluator(rootOperand2)
        }

        return value
    }

    private fun String.toMonkeys(): Map<String, Job> = lines().map { it.toMonkey() }.associate { it.name to it.job }

    private fun String.toMonkey(): Monkey {
        val (name, jobString) = split(": ")

        val job = when {
            jobString.toLongOrNull() != null -> Job.Value(jobString.toLong())
            else -> {
                val (operand1, operator, operand2) = jobString.split(" ")
                Job.Expression(
                    operand1 = operand1,
                    operand2 = operand2,
                    operation = operator.toOperation()
                )
            }
        }

        return Monkey(name, job)
    }

}