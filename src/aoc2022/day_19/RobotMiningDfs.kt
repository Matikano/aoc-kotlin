package aoc2022.day_19

import aoc2022.day_19.RobotMiningDfs.RobotMiningState
import utils.abstractions.RecursiveMemo1
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min

class RobotMiningDfs(
    private val blueprint: Blueprint
): RecursiveMemo1<RobotMiningState, Int>() {
    override fun MutableMap<RobotMiningState, Int>.recurse(state: RobotMiningState): Int = getOrPut(state) {
        val time = state.time
        val robots = state.robots
        val ores = state.ores

        when (time) {
            0 -> ores[OreType.GEODE]!!
            1 -> ores[OreType.GEODE]!! + robots[OreType.GEODE]!!
            else -> {
                var maxVal = ores[OreType.GEODE]!! + robots[OreType.GEODE]!! * time

                for ((oreType, recipe) in blueprint.recipes) {
                    if (oreType != OreType.GEODE && robots[oreType]!! * time >= blueprint.maxSpend[oreType]!! * time - ores[oreType]!!)
                        continue

                    var wait = 0
                    var broke = false
                    for ((cost, type) in recipe) {
                        if (robots[type] == 0) {
                            broke = true
                            break
                        }
                        wait = max(wait, ceil((cost - ores[type]!!) / robots[type]!!.toDouble()).toInt())
                    }

                    if (!broke) {
                        val remainingTime = time - wait - 1
                        if (remainingTime <= 0)
                            continue

                        val newRobots = robots.toMutableMap()
                        val newOres = ores.toMutableMap().apply {
                            keys.forEach { key -> this[key] = ores[key]!! + robots[key]!! * (wait + 1) }
                        }

                        for ((cost, type) in recipe)
                            newOres[type] = newOres[type]!! - cost

                        newRobots[oreType] = newRobots[oreType]!! + 1

                        OreType.entries
                            .filterNot { it == OreType.GEODE }
                            .forEach { type ->
                                newOres[type] = min(newOres[type]!!, blueprint.maxSpend[type]!! * remainingTime)
                            }

                        val newState = RobotMiningState(
                            time = remainingTime,
                            oreRobots = newRobots[OreType.ORE]!!,
                            clayRobots = newRobots[OreType.CLAY]!!,
                            obsidianRobots = newRobots[OreType.OBSIDIAN]!!,
                            geodeRobots = newRobots[OreType.GEODE]!!,
                            ore = newOres[OreType.ORE]!!,
                            clay = newOres[OreType.CLAY]!!,
                            obsidian = newOres[OreType.OBSIDIAN]!!,
                            geode = newOres[OreType.GEODE]!!,
                        )

                        maxVal = max(maxVal, recurse(newState))
                    }
                }

                maxVal
            }
        }
    }

    data class RobotMiningState(
        val time: Int,
        val oreRobots: Int = 1,
        val clayRobots: Int = 0,
        val obsidianRobots: Int = 0,
        val geodeRobots: Int = 0,
        val ore: Int = 0,
        val clay: Int = 0,
        val obsidian: Int = 0,
        val geode: Int = 0
    ) {
        val robots = mapOf(
            OreType.ORE to oreRobots,
            OreType.CLAY to clayRobots,
            OreType.OBSIDIAN to obsidianRobots,
            OreType.GEODE to geodeRobots
        )

        val ores = mapOf(
            OreType.ORE to ore,
            OreType.CLAY to clay,
            OreType.OBSIDIAN to obsidian,
            OreType.GEODE to geode
        )
    }
}
