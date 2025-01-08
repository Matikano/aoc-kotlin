package aoc2022.day_21

import utils.models.Operation

sealed class Job {
    data class Value(val value: Long): Job() {
        override fun toString(): String = value.toString()
    }

    data class Expression(
        val operand1: String,
        val operand2: String,
        val operation: Operation
    ): Job() {
        override fun toString(): String = "$operand1 ${operation.symbol} $operand2"
    }
}