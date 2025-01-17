package aoc2021.day_12

data class CaveSystem(
    private val caveConnections: Map<String, Set<String>>
) {

    fun distinctPaths(visitedTwiceCap: Int = 0): Set<List<String>> {
        val visited = mutableSetOf<String>()
        val visitedTwice = mutableSetOf<String>()

        fun dfs(cave: String, path: List<String>): Set<List<String>> {
            val paths = mutableSetOf<List<String>>()
            if (cave == END_CAVE) {
                paths.add(path + cave)
                return paths
            }

            for (neighbor in caveConnections.getOrDefault(cave, emptySet()).filter { it != START_CAVE }) {
                if (neighbor.all { it.isLowerCase() }) {
                    when (neighbor) {
                        in visitedTwice -> continue
                        in visited -> if (visitedTwice.size < visitedTwiceCap && neighbor != START_CAVE ) {
                            visitedTwice.add(neighbor)
                        } else continue
                        else -> visited.add(neighbor)
                    }
                }

                paths.addAll(dfs(neighbor, path + cave))

                if (neighbor.all { it.isLowerCase() }) {
                    if (neighbor in visitedTwice) visitedTwice.remove(neighbor)
                    else visited.remove(neighbor)
                }
            }

            return paths
        }

        return dfs(START_CAVE, emptyList())
    }

    companion object {
        private const val START_CAVE = "start"
        private const val END_CAVE = "end"
    }
}
