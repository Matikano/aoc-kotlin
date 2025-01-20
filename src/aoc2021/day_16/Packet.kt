package aoc2021.day_16

import aoc2021.day_16.OperationType.BIGGER
import aoc2021.day_16.OperationType.EQUAL
import aoc2021.day_16.OperationType.LESS
import aoc2021.day_16.OperationType.MAX
import aoc2021.day_16.OperationType.MIN
import aoc2021.day_16.OperationType.NONE
import aoc2021.day_16.OperationType.PRODUCT
import aoc2021.day_16.OperationType.SUM
import utils.extensions.toInt

sealed class Packet(
    val version: Int
) {

    abstract val value: Long

    val versionSum: Int
        get() = when (this) {
            is Literal -> version
            is Operator -> version + subPackets.sumOf { it.versionSum }
        }

    class Literal(
        version: Int,
        override val value: Long
    ): Packet(version)

    class Operator(
        version: Int,
        val operationType: OperationType,
        val subPackets: List<Packet>
    ): Packet(version) {
        override val value: Long
            get() = with (subPackets) {
                return@with when (operationType) {
                    SUM -> sumOf { it.value }
                    PRODUCT-> fold(1) { acc, packet -> acc * packet.value }
                    MIN -> minOf { it.value }
                    MAX -> maxOf { it.value }
                    NONE -> throw IllegalStateException("Operation type = $operationType should be of type Packet.Literal")
                    BIGGER -> {
                        assert(size == 2)
                        (first().value > last().value).toInt().toLong()
                    }
                    LESS -> {
                        assert(size == 2)
                        (first().value < last().value).toInt().toLong()
                    }
                    EQUAL -> {
                        assert(size == 2)
                        (first().value == last().value).toInt().toLong()
                    }
                }
            }
    }
}