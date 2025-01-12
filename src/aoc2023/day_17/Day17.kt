package aoc2023.day_17

import utils.AocTask
import utils.models.Grid.Companion.toCharGrid
import kotlin.time.measureTime

object Day17: AocTask() {

    override fun executeTask() {

        measureTime {
            val testMaze = CrucibleMaze(testInput.toCharGrid())
            val leastHeatLossPath = testMaze.findLeastHeatLossPath()
            val leastHeatLoss = leastHeatLossPath.first().cost
            println("Least heat loss for test maze = $leastHeatLoss")
            val leastHeatLossUltraPath = testMaze.findLeastHeatLossPathUltra()
            val leastHeatLossUltra = leastHeatLossUltraPath.first().cost
            println("Least ultra heat loss for test maze = $leastHeatLossUltra")
        }.let { println("Test part took $it\n") }

        with(CrucibleMaze(input.toCharGrid())) {
            measureTime {
                val leastHeatLossPath = findLeastHeatLossPath()
                val leastHeatLoss = leastHeatLossPath.first().cost
                println("Least heat loss for maze = $leastHeatLoss")
            }.let { println("Part 1 took $it\n") }

            measureTime {
                val leastHeatLossUltraPath = findLeastHeatLossPathUltra()
                val leastHeatLossUltra = leastHeatLossUltraPath.first().cost
                println("Least ultra heat loss for maze = $leastHeatLossUltra")
            }.let { println("Part 2 took $it\n") }
        }
    }
}