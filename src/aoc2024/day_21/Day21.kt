package aoc2024.day_21

import utils.AocTask
import utils.models.Grid
import utils.models.GridCell
import utils.models.Position

object Day21: AocTask {
    private val NUMERIC_LAYOUT = """
        789
        456
        123
        #0A
    """.trimIndent()

    private val KEYPAD_LAYOUT = """
        #^A
        <v>
        """.trimIndent()

    private val testInput = """
        029A
        980A
        179A
        456A
        379A
    """.trimIndent()

    override val fileName: String
        get() = "src/aoc2024/day_21/input.txt"

    override fun executeTask() {
        println("-------------------------------------")
        println("AoC 2024 Task ${this.javaClass.simpleName}")
        println()
    }

    private fun String.layoutToGrid() =
        Grid(
            cells = lines().flatMapIndexed { rowIndex: Int, row: String ->
                row.mapIndexed { colIndex, char ->
                    GridCell(
                        position = Position(colIndex = colIndex, rowIndex = rowIndex),
                        value = char
                    )
                }
            }
        )
}