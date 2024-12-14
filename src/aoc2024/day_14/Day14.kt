package aoc2024.day_14

import aoc2024.AocTask
import aoc2024.day_13.Bounds
import aoc2024.main
import utils.extensions.nums
import utils.models.Position
import kotlin.time.measureTime

typealias Quadrant = Pair<IntRange, IntRange>

object Day14: AocTask {

    const val ITERATIONS = 100
    private val GRID_BOUNDS = Bounds(
        width = 101,
        height = 103
    )

    val firstQuadrant = 0..< GRID_BOUNDS.width / 2 to 0..< GRID_BOUNDS.height / 2
    val secondQuadrant = 0..< GRID_BOUNDS.width / 2 to GRID_BOUNDS.height / 2 + 1..< GRID_BOUNDS.height
    val thirdQuadrant = GRID_BOUNDS.width / 2 + 1..< GRID_BOUNDS.width to 0..< GRID_BOUNDS.height / 2
    val fourthQuadrant = GRID_BOUNDS.width / 2 + 1..< GRID_BOUNDS.width to GRID_BOUNDS.height / 2 + 1..< GRID_BOUNDS.height

    private operator fun Quadrant.contains(position: Position): Boolean =
        position.colIndex in this.first && position.rowIndex in this.second

    override val fileName: String
        get() = "src/aoc2024/day_14/input.txt"

    override fun executeTask() {
        println("-------------------------------------")
        println("AoC 2024 Task ${this.javaClass.simpleName}")
        println()

        with(readToList()) {
            // Part 1
            measureTime {
                println("Quadrant #1 = $firstQuadrant")
                println("Quadrant #2 = $secondQuadrant")
                println("Quadrant #3 = $thirdQuadrant")
                println("Quadrant #4 = $fourthQuadrant")

                repeat(ITERATIONS) {
                    moveRobots()
                }
                val allQuadrantRobots = listOf(
                    filter { it.position in firstQuadrant },
                    filter { it.position in secondQuadrant },
                    filter { it.position in thirdQuadrant },
                    filter { it.position in fourthQuadrant }
                )

                allQuadrantRobots.forEachIndexed { index, robots ->
                    println("Quadrant ${index + 1} = ${robots.size}")
                }

                val safetyFactor = allQuadrantRobots.fold(1) { acc, robots ->
                    acc * robots.size
                }

                println("Safety factor after $ITERATIONS seconds is: $safetyFactor")
            }.let { println("Part 1 took $it") }
        }

//        with(readToList()) {
//            measureTime {
//                // Part 2
//                var count = 0
//
//                while (true) {
//                    moveRobots()
//                    count++
//                    if (map { it.position } == christmasTreePositions)
//                        break
//                }
//
//                println("It took a minimum of $count seconds for robots to form a christmas tree")
//            }.let { println("Part 2 took $it") }
//        }
    }

    private fun List<Robot>.moveRobots() = forEach { it.move() }

    private fun readToList(): List<Robot> = buildList {
        readFileByLines { line ->
            val (px, py, dx, dy) = line.nums()
            add(
                Robot(
                    position = Position(px, py),
                    velocity = Velocity(dx, dy),
                    bounds = GRID_BOUNDS
                )
            )
        }
    }
}