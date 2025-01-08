package aoc2022.day_19

import utils.AocTask
import utils.extensions.numsInt
import kotlin.time.measureTime

object Day19: AocTask() {

    override fun executeTask() {
        measureTime {
            val blueprints = testInput.toBlueprints()

            val blueprintQualitySum = blueprints.qualityLevelSum()
            println("Test quality level sum of blueprints = $blueprintQualitySum")
        }.let { println("Test part took $it\n") }

        measureTime {
            val blueprints = input.toBlueprints()
            println("Quality level sum of blueprints = ${blueprints.qualityLevelSum()}")
        }.let { println("Part 1 took $it\n") }

        measureTime {
            val blueprints = input.toBlueprints()
            println("Product of max geodes of first 3 blueprints = ${blueprints.maxGeodesProductOfFirstThree()}")
        }.let { println("Part 2 took $it\n") }
    }

    private fun List<Blueprint>.maxGeodesProductOfFirstThree(): Int = take(3).fold(1) { acc, bp -> acc * bp.maxGeodes  }

    private fun List<Blueprint>.qualityLevelSum(): Int = mapIndexed { index, blueprint ->
        (index + 1) * blueprint.quality
    }.sum()

    private fun String.toBlueprints(): List<Blueprint> = lines().map { it.toBlueprint() }

    private fun String.toBlueprint(): Blueprint {
        val (oreSegment, claySegment, obsidianSegment, geodeSegment) = split(": ").last().split(". ")
        return Blueprint(
            recipes = mapOf(
                OreType.ORE to setOf(RobotCost(oreSegment.numsInt().first(), OreType.ORE)),
                OreType.CLAY to setOf(RobotCost(claySegment.numsInt().first(), OreType.ORE)),
                OreType.OBSIDIAN to setOf(RobotCost(obsidianSegment.numsInt().first(), OreType.ORE),
                    RobotCost(obsidianSegment.numsInt().last(), OreType.CLAY)),
                OreType.GEODE to setOf(RobotCost(geodeSegment.numsInt().first(), OreType.ORE),
                    RobotCost(geodeSegment.numsInt().last(), OreType.OBSIDIAN))
            ),
        )
    }
}