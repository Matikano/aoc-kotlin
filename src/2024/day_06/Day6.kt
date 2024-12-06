package `2024`.day_06

import `2024`.AocTask



object Day6: AocTask {

    private const val OBSTACLE = '#'
    private const val CHARACTER = '^'

    override val fileName: String
        get() = "src/2024/day_06/input.txt"

    override fun executeTask() {
        println("-------------------------------------")
        println("AoC 2024 Task ${this.javaClass.simpleName}")
        println()

        val grid = readToGrid()
        val guard = grid.findGuard()

        println("Guard = $guard")

        // Part 1
        guard.startPatrol(grid)
        println("Guard visited ${guard.visitedPositionsCount} distinct positions!")

        // Part 2
        var loopingObstacles = 0

        grid.data.forEachIndexed { rowIndex, row ->
            row.indices.forEach { colIndex ->
                if (grid.getValue(Position(colIndex, rowIndex)) == CHARACTER)
                    return@forEach

                grid.copyWithNewObstacleAt(
                    colIndex = colIndex,
                    rowIndex = rowIndex
                ).let { newGrid ->
                    newGrid.findGuard().apply {
                        startPatrol(newGrid)
                    }.also {
                        if (it.isInALoop)
                           loopingObstacles++
                    }
                }
            }
        }

        println("There are $loopingObstacles obstructions that would make Guard loop")
    }

    private fun readToGrid(): Grid =
        mutableListOf<String>().apply {
            readFileByLines { line ->
                add(line)
            }
        }.let { Grid(it) }

    // Part 1
    private fun Grid.findGuard(): Guard {
        data.forEachIndexed { rowIndex, row ->
            row.indices.forEach { colIndex ->
                if (row[colIndex] == CHARACTER)
                    return Guard(
                        direction = Direction.UP
                    ).apply {
                        position = Position(
                            colIndex = colIndex,
                            rowIndex = rowIndex
                        )
                    }
            }
        }

        throw IllegalStateException("Guard was not found in the input data")
    }

    private fun Guard.startPatrol(grid: Grid) {
        while (true) {
            val nextPosition = position.nextPosition(direction)

            if (!grid.isInsideGrid(nextPosition))
                break

            if (grid.getValue(nextPosition) == OBSTACLE) {
                turn(nextPosition)
                if (isInALoop)
                    break
            }
            else {
                moveToPosition(nextPosition)
            }
        }
    }

    // Part 2
    private fun Grid.copyWithNewObstacleAt(colIndex: Int, rowIndex: Int): Grid =
        copy(
            data = data.mapIndexed { x, row ->
                if (x == rowIndex)
                    row.replaceCharAt(colIndex, OBSTACLE)
                else row
            }
        )

    private fun String.replaceCharAt(index: Int, char: Char): String =
        if (index in indices)
            buildString {
                append(this@replaceCharAt)
                setCharAt(index, char)
            }
        else throw StringIndexOutOfBoundsException()

}
