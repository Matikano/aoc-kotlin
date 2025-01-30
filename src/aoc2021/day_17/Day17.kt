package aoc2021.day_17

import aoc2024.day_14.Velocity
import utils.AocTask
import utils.extensions.numsInt
import kotlin.time.measureTime

object Day17: AocTask() {

    private val TEST_SIMULATION_X_RANGE = 1..50
    private val TEST_SIMULATION_Y_RANGE = -20..20
    private val SIMULATION_X_RANGE = 1..500
    private val SIMULATION_Y_RANGE = -500 .. 500

    override fun executeTask() {
        measureTime {
            val targetArea = testInput.toTargetArea()
            val maxHeight = simulateThrows(TEST_SIMULATION_X_RANGE, TEST_SIMULATION_Y_RANGE, targetArea)
            val numberOfDistinctVelocities = allValidStartingVelocities(TEST_SIMULATION_X_RANGE, TEST_SIMULATION_Y_RANGE, targetArea)
            println("Maximum height for test data = $maxHeight")
            println("Count of valid starting velocities = $numberOfDistinctVelocities")
        }.let { println("Test part took $it\n") }

        with(input.toTargetArea()) {
            measureTime {
                val maxHeight = simulateThrows(SIMULATION_X_RANGE, SIMULATION_Y_RANGE, this)
                println("Maximum height = $maxHeight")
            }.let { println("Part 1 took $it\n") }

            measureTime {
                val numberOfDistinctVelocities = allValidStartingVelocities(SIMULATION_X_RANGE, SIMULATION_Y_RANGE, this)
                println("Count of valid starting velocities = $numberOfDistinctVelocities")
            }.let { println("Part 2 took $it\n") }
        }
    }

    private fun simulateThrows(
        simulationXRange: IntRange,
        simulationYRange: IntRange,
        targetArea: TargetArea
    ): Int =
        (simulationXRange).flatMap { dx ->
            (simulationYRange).map { dy ->
                Probe(startVelocity = Velocity(dx, dy))
            }
        }.map { probe ->
            probe.getPositionsForValidTrajectories(targetArea)
        }.filter { trajectoryPositions ->
            trajectoryPositions.any { it.rowIndex in targetArea.yRange && it.colIndex in targetArea.xRange }
        }.flatMap { it }
        .maxOf { it.rowIndex }

    private fun allValidStartingVelocities(
        simulationXRange: IntRange,
        simulationYRange: IntRange,
        targetArea: TargetArea
    ): Int =
        (simulationXRange).flatMap { dx ->
            (simulationYRange).map { dy ->
                Probe(startVelocity = Velocity(dx, dy))
            }
        }.filter { probe ->
            probe.getPositionsForValidTrajectories(targetArea).any {
                it.rowIndex in targetArea.yRange &&
                        it.colIndex in targetArea.xRange
            }
        }.map { it.startVelocity }
            .distinct()
            .count()


    private fun String.toTargetArea(): TargetArea {
        val (xStart, xEnd, yStart, yEnd) = numsInt()
        return TargetArea(
            xRange = xStart..xEnd,
            yRange = yStart..yEnd
        )
    }
}