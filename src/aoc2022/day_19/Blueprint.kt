package aoc2022.day_19

import aoc2022.day_19.RobotMiningDfs.RobotMiningState


data class Blueprint(
    val recipes: Map<OreType, Set<RobotCost>>,
) {
    private val robotMiningDfs = RobotMiningDfs(this)

    val maxSpend: Map<OreType, Int> = OreType.entries
        .filterNot { it == OreType.GEODE }
        .associate { type ->
            type to recipes.values.flatMap { it }
                .filter { it.type == type }
                .maxOf { it.cost }
        }

    val quality: Int
        get() = robotMiningDfs(RobotMiningState(MINING_TIME))

    val maxGeodes: Int
        get() = robotMiningDfs(RobotMiningState(MINING_TIME_PART_2))

    companion object {
        const val MINING_TIME = 24
        const val MINING_TIME_PART_2 = 32
    }
}
