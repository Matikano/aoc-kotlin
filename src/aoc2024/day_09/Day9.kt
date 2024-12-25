package aoc2024.day_09

import utils.AocTask
import kotlin.time.measureTime

object Day9: AocTask() {

    override fun executeTask() {
        with(readToDiskMap()) {
            measureTime {
                // Part 1
                println("Checksum of diskMap = $checkSum")
            }.let { println("Part 1 took $it") }

            println()

            measureTime {
                // Part 2
                println("Compacted checksum of diskMap = $compactedCheckSum")
            }.let { println("Part 2 took $it") }
        }
    }

    private fun readToDiskMap(): DiskMap =
        DiskMap(inputToString().trim())
}