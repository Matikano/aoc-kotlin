package aoc2024.day_12

import utils.models.Direction
import utils.models.GridCell
import utils.models.Position

typealias Plant = List<GridCell<Char>>

data class Garden(
    val fields: List<GridCell<Char>>
) {
    val rows: Int
        get() = fields.maxOf { it.position.rowIndex }

    val cols: Int
        get() = fields.maxOf { it.position.colIndex }

    private val positionToPlantNumberMap: MutableMap<Position, Int> = mutableMapOf()

    operator fun get(position: Position): GridCell<Char>? =
        fields.firstOrNull { it.position == position }

    fun findAllPlants() {
        var plantNumber = 0
        fields.forEach { cell ->
            if (cell.position !in positionToPlantNumberMap.keys) {
                depthFirstSearch(cell.position, cell.value, plantNumber)
                plantNumber++
            }
        }
    }

    val plants: Map<Int, List<GridCell<Char>>>
        get() = buildMap {
            positionToPlantNumberMap.forEach { (k, v) ->
                this[v] = (this[v] ?: emptyList()) + this@Garden[k]!!
            }
        }

    val totalPrice: Int
        get() = plants.values.sumOf { it.price }

    val discountPrice: Int
        get() = plants.values.sumOf { it.discountPrice }


    private fun depthFirstSearch(position: Position, char: Char, plantNumber: Int) {
        val directions = Direction.validEntries
        if (isValidPosition(position)) {
            if (positionToPlantNumberMap.containsKey(position))
                return
            if (this[position]?.value == char) {
                positionToPlantNumberMap[position] = plantNumber
                directions.forEach { direction ->
                    depthFirstSearch(position + direction, char, plantNumber)
                }
            }
        }
    }

    operator fun Plant.get(position: Position): GridCell<Char>? =
        firstOrNull { it.position == position }

    val Plant.price: Int
        get() = size * calculatePerimeter()

    val Plant.discountPrice: Int
        get() = size * calculateSides()

    private fun isValidPosition(position: Position): Boolean =
        position.colIndex in 0..cols &&
                position.rowIndex in 0..rows

    private fun Plant.perimeter(): Plant = buildList {
        val directions = Direction.validEntries

        this@perimeter.forEach { cell ->
            val isCellInPerimeter = directions.map { direction ->
                cell.position + direction
            }.any { adjacentPosition ->
                this@perimeter[adjacentPosition] == null
            }

            if (isCellInPerimeter)
                add(cell)
        }
    }

    fun Plant.calculatePerimeter(): Int =
        perimeter().sumOf { cell ->
            Direction.validEntries.count { direction ->
                this[cell.position + direction] == null
            }
        }

    fun Plant.calculateSides(): Int {
        val cornerDirections = listOf(
            -.5f to -.5f,
             .5f to -.5f,
             .5f to  .5f,
            -.5f to  .5f,
        )

        val cornerCandidates = perimeter()
            .map { it.position }
            .flatMap { position ->
                cornerDirections.map { direction ->
                    position.colIndex + direction.first to position.rowIndex + direction.second
                }
            }.toSet()

        return cornerCandidates.foldIndexed(0) { _, acc, (cornerRow, cornerCol) ->
            val cornerCells = cornerDirections.map {
                it.first + cornerRow to it.second + cornerCol
            }
            val cornerConfig = cornerCells.map {
                this[Position(it.first.toInt(), it.second.toInt())] == null
            }

            val opposingConfig = listOf(true, false, true, false)
            val number = cornerConfig.count { it }

            when (number) {
                1, 3 -> acc + 1
                2 -> if (cornerConfig == opposingConfig || cornerConfig == opposingConfig.reversed()) {
                    acc + 2
                } else acc
                else -> acc
            }
        }
    }
}