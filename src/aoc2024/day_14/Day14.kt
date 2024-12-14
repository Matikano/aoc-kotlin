package aoc2024.day_14

import aoc2024.AocTask
import utils.extensions.nums
import utils.models.Position
import kotlin.time.measureTime

typealias Quadrant = Pair<IntRange, IntRange>

object Day14: AocTask {

    private const val ITERATIONS = 100
    private val GRID_BOUNDS = Bounds(
        width = 101,
        height = 103
    )

    private val quadrants = GRID_BOUNDS.toQuadrants()
    private operator fun Quadrant.contains(position: Position): Boolean =
        position.colIndex in first && position.rowIndex in second

    override val fileName: String
        get() = "src/aoc2024/day_14/input.txt"

    override fun executeTask() {
        println("-------------------------------------")
        println("AoC 2024 Task ${this.javaClass.simpleName}")
        println()

        with(readToList()) {
            // Part 1
            measureTime {
                moveRobots(ITERATIONS)

                val robotsInQuadrants = quadrants.map { quadrant ->
                    filter { it.position in quadrant }
                }

                val safetyFactor = robotsInQuadrants.calculateSafetyFactor()

                println("Safety factor after $ITERATIONS seconds is: $safetyFactor")
            }.let { println("Part 1 took $it") }
        }

        with(readToList()) {
            measureTime {
                // Part 2
                var count = 0
                while (map { it.position }.distinct().size != size) {
                    moveRobots()
                    count++
                }
                println("It took a minimum of $count seconds for robots to form a christmas tree")
            }.let { println("Part 2 took $it") }
        }
    }

    private fun readToList(): List<Robot> =
        readFileToList().map { line ->
            line.nums().let { (px, py, dx, dy) ->
                Robot(
                    position = Position(
                        colIndex = px,
                        rowIndex = py
                    ),
                    velocity = Velocity(
                        dx = dx,
                        dy = dy
                    ),
                    bounds = GRID_BOUNDS
                )
            }
        }

    private fun List<Robot>.moveRobots(times: Int = 1) = forEach { it.move(times) }

    private fun Bounds.toQuadrants(): List<Quadrant> =
        listOf(
            0..< width / 2 to 0..< height / 2,
            0..< width / 2 to height / 2 + 1..< height,
            width / 2 + 1..< width to 0..< height / 2,
            width / 2 + 1..< width to height / 2 + 1..< height
        )

    private fun List<List<Robot>>.calculateSafetyFactor(): Int =
        fold(1) { acc, robots ->
            acc * robots.size
        }
}