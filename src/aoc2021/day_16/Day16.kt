package aoc2021.day_16

import aoc2021.day_16.LengthEncoding.Companion.toLengthEncoding
import aoc2021.day_16.OperationType.Companion.toOperationType
import utils.AocTask
import kotlin.time.measureTime

object Day16: AocTask() {

    override fun executeTask() {
        measureTime {
            val packet = testInput.toPacket()
            println("Version sum = ${packet?.versionSum}")
            println("Packet value = ${packet?.value}")
        }.let { println("Test part took $it\n") }

        measureTime {
            val packet = input.toPacket()
            println("Version sum = ${packet?.versionSum}")
        }.let { println("Part 1 took $it\n") }

        measureTime {
            val packet = input.toPacket()
            println("Packet = $packet")
            println("Value sum = ${packet?.value}")
        }.let { println("Part 2 took $it\n") }
    }

    private fun String.toPacket(): Packet? {
        val data = buildString {
            val binary = buildString {
                this@toPacket.forEach { append(it.hexadecimalCharToBinaryString()) }
            }
            append(binary)
        }

        println(data)

        fun parse(startIndex: Int, endIndex: Int = -1): Pair<Packet, Int?>? {
            if (startIndex == endIndex)
                return null

            if (startIndex > data.length - 4)
                return null

            val version = data.substring(startIndex ..< startIndex + 3).toInt(2)
            val operationType = data.substring(startIndex + 3 ..< startIndex + 6).toInt(2).toOperationType()

            return when (operationType) {
                OperationType.NONE -> {
                    var index = startIndex + 6
                    var literalValueString = ""
                    var end = false

                    while (!end) {
                        if (data[index] == '0')
                            end = true
                        literalValueString += data.substring(index + 1 ..< index + 5)
                        index += 5
                    }

                    Packet.Literal(version, literalValueString.toLong(2)) to index
                }

                else -> {
                    val subPackets = mutableListOf<Packet>()
                    var nextStart: Int? = null

                    val lengthEncoding = data[startIndex + 6].digitToInt().toLengthEncoding()

                    when (lengthEncoding) {
                        LengthEncoding.BITS -> {
                            val bitCount = data.substring(startIndex + 7 ..< startIndex + 22).toInt(2)
                            var index: Int? = startIndex + 22
                            val endIndex = index!! + bitCount
                            var previousIndex: Int? = null

                            while (index != null) {
                                previousIndex = index
                                val packet = parse(index, endIndex)
                                index = packet?.second
                                packet?.let {
                                    subPackets.add(packet.first)
                                }
                            }
                            nextStart = previousIndex
                        }

                        LengthEncoding.SUB_PACKETS -> {
                            var remainingSubPackets = data.substring(startIndex + 7 ..< startIndex + 18).toInt(2)
                            var index: Int? = startIndex + 18
                            while (remainingSubPackets > 0) {
                                val packetPair = parse(index!!)
                                remainingSubPackets--
                                packetPair?.first?.let { subPackets.add(it) }
                                index = packetPair?.second
                            }
                            nextStart = index
                        }
                    }

                    Packet.Operator(
                        version = version,
                        operationType = operationType,
                        subPackets = subPackets
                    ) to nextStart
                }
            }
        }

        return parse(0)?.first
    }

    private fun Char.hexadecimalCharToBinaryString(): String =
        when (this) {
            '0' -> "0000"
            '1' -> "0001"
            '2' -> "0010"
            '3' -> "0011"
            '4' -> "0100"
            '5' -> "0101"
            '6' -> "0110"
            '7' -> "0111"
            '8' -> "1000"
            '9' -> "1001"
            'A' -> "1010"
            'B' -> "1011"
            'C' -> "1100"
            'D' -> "1101"
            'E' -> "1110"
            'F' -> "1111"
            else -> throw IllegalArgumentException("Unsupported hexadecimal char to binary string conversion for $this")
        }
}