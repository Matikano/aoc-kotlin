package aoc2021.day_03

import utils.AocTask
import utils.extensions.binaryNegation
import utils.extensions.transpose
import kotlin.time.measureTime

object Day3: AocTask() {

    override fun executeTask() {
        measureTime {
            val binaryNumbers = testInput.toList()
            val gammaRate = binaryNumbers.toGammaRate()
            println("Power consumption = ${gammaRate * gammaRate.binaryNegation}")
            println("Oxygen rating = ${binaryNumbers.toOxygenRating()}, CO2 rating = ${binaryNumbers.toCO2Rating()}")
        }.let { println("Test part took $it\n") }

        measureTime {
            val binaryNumbers = input.toList()
            val gammaRate = binaryNumbers.toGammaRate()
            println("Power consumption = ${gammaRate * gammaRate.binaryNegation}")
        }.let { println("Part 1 took $it\n") }

        measureTime {
            val binaryNumbers = input.toList()
            val oxygenRate = binaryNumbers.toOxygenRating()
            val co2Rating = binaryNumbers.toCO2Rating()
            println("Life support rating = ${oxygenRate * co2Rating}")
        }.let { println("Part 2 took $it\n") }
    }

    private fun String.toList(): List<String> = lines()
    private fun List<String>.toGammaRate(): Int {
        val sums = mutableListOf<Int>()
        forEach { row ->
            row.forEachIndexed { index, char ->
                if (index !in sums.indices)
                    sums.add(index, char.digitToInt())
                else sums[index] += char.digitToInt()
            }
        }
        return sums.map { if (it > size / 2) 1 else 0 }.joinToString("").toInt(2)
    }

    private fun List<String>.toOxygenRating(): Int {
        var list = this
        var leadingBits = ""
        var currentBit = 0

        while (true) {
            val sums = mutableListOf<Int>()
            list.forEach { row ->
                row.forEachIndexed { index, char ->
                    if (index !in sums.indices)
                        sums.add(index, char.digitToInt())
                    else sums[index] += char.digitToInt()
                }
            }
            leadingBits += if (sums[currentBit++] >= list.size / 2f ) "1" else "0"
            list = list.filter { it.startsWith(leadingBits) }
            if (list.size == 1){
                return list.first().toInt(2)
            }
        }

        return list.first().toInt(2)
    }

    private fun List<String>.toCO2Rating(): Int {
        var list = this
        var leadingBits = ""
        var currentBit = 0

        while (true) {
            val sums = mutableListOf<Int>()
            list.forEach { row ->
                row.forEachIndexed { index, char ->
                    if (index !in sums.indices)
                        sums.add(index, char.digitToInt())
                    else sums[index] += char.digitToInt()
                }
            }
            leadingBits += if (sums[currentBit++] < list.size / 2f) "1" else "0"
            list = list.filter { it.startsWith(leadingBits) }
            if (list.size == 1) {
                return list.first().toInt(2)
            }
        }

        return list.first().toInt(2)
    }
}