package aoc2024.day_21

import utils.AocTask
import utils.extensions.cartesianProduct
import utils.extensions.head
import utils.extensions.numsInt
import utils.extensions.tail
import utils.models.Direction
import utils.models.Direction.Companion.toSequence
import utils.models.Grid
import utils.models.GridCell
import utils.models.Position
import java.util.ArrayDeque
import kotlin.collections.mutableMapOf
import kotlin.time.measureTime

typealias Sequences = Map<Pair<Char, Char>, MutableList<List<Direction>>>

object Day21: AocTask() {

    private const val GAP_SYMBOL = '#'
    private const val PART_1_KEYPAD_ROBOTS_COUNT = 2
    private const val PART_2_KEYPAD_ROBOTS_COUNT = 25

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

    private val numpadSequences by lazy {
        NUMERIC_LAYOUT.layoutToGrid().sequences()
    }

    private val keypadSequences by lazy {
        KEYPAD_LAYOUT.layoutToGrid().sequences()
    }

    private val keypadLengths by lazy {
        keypadSequences.map { (key, value) ->
            key to value.first().size
        }.toMap()
    }

    override fun executeTask() {
        measureTime {
            val sumOfComplexities = testInput.lines()
                .sumOf { it.complexity(PART_1_KEYPAD_ROBOTS_COUNT) }
            println("Test part complexity sum = $sumOfComplexities")
        }.let { println("Test part took $it") }

        measureTime {
            val complexitySum = inputToList()
                .sumOf { it.complexity(PART_1_KEYPAD_ROBOTS_COUNT ) }
            println("Complexity sum for $PART_1_KEYPAD_ROBOTS_COUNT keypadRobots equals: $complexitySum")
        }.let { println("Part 1 took $it") }

        measureTime {
            val complexitySum = inputToList()
                .sumOf { it.complexity(PART_2_KEYPAD_ROBOTS_COUNT ) }
            println("Complexity sum for $PART_2_KEYPAD_ROBOTS_COUNT keypadRobots equals: $complexitySum")
        }.let { println("Part 2 took $it") }
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

    private fun String.complexity(keypadCount: Int = PART_1_KEYPAD_ROBOTS_COUNT): Long =
        numpadSequences.solve(this)
            .minOf { sequence ->
                ComputeLengthAtDepth(keypadLengths, keypadSequences).invoke(sequence, keypadCount)
            } * numsInt().first()

    private fun Grid<Char>.sequences(): Sequences {
        val sequences = mutableMapOf<Pair<Char, Char>, MutableList<List<Direction>>>()
        val positions = cells.associate { it.value to positionOf(it.value) }

        for (from in positions.keys.filterNot { it == GAP_SYMBOL }) {
            for (to in positions.keys.filterNot { it == GAP_SYMBOL }) {
                if (from == to) {
                    sequences[from to to] = mutableListOf(listOf(Direction.NONE))
                    continue
                }

                val possibilities = mutableListOf<List<Direction>>()
                val queue = ArrayDeque<Pair<Position, List<Direction>>>().apply {
                    offer(positions[from]!! to emptyList())
                }

                queue.processQueue(
                    grid = this,
                    to = to,
                    possibilities = possibilities,
                )

                sequences[from to to] = possibilities
            }
        }

        return sequences
    }

    private fun Sequences.solve(input: String): List<String> {
        val options = (Direction.NONE.symbol + input)
            .zip(input)
            .map {
                this[it]!!.map { it.toSequence() }
            }

        return cartesianProduct(options.head(), *options.tail().toTypedArray()).map { it.joinToString("") }
    }

    private fun ArrayDeque<Pair<Position, List<Direction>>>.processQueue(
        grid: Grid<Char>,
        to: Char,
        possibilities: MutableList<List<Direction>>,
    ) {
        var optimal = Int.MAX_VALUE

        while (isNotEmpty()) {
            val (position, moves) = poll()

            Direction.validEntries.map { direction ->
                position + direction to direction
            }.forEach { (newPosition, direction) ->
                if (!grid.isInBounds(newPosition)) return@forEach
                if (grid[newPosition]!!.value == GAP_SYMBOL) return@forEach
                if (grid[newPosition]!!.value == to) {
                    if (optimal < moves.size + 1) return
                    optimal = moves.size + 1
                    possibilities.add(moves + direction + Direction.NONE)
                } else {
                    offer(newPosition to (moves + direction))
                }
            }
        }
    }
}