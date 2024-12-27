package aoc2023.day_08

import utils.extensions.lcm

data class Network(
    val instructions: String,
    val connections: Map<String, Pair<String, String>>
) {

    val startingNodes: List<String>
        get() = connections.keys.filter { it.endsWith(START_POSITION_CHAR) }

    fun solve(start: String = START_POSITION, end: String = END_POSITION): Int {
        var currentPosition = start
        var steps = 0

        while (currentPosition != end) {
            val instruction = instructions[steps++ % instructions.length]
            currentPosition = with (connections[currentPosition]!!) {
                when(instruction) {
                    LEFT_INSTRUCTION_SYMBOL -> first
                    RIGHT_INSTRUCTION_SYMBOL -> second
                    else -> throw IllegalArgumentException("Invalid symbol $instruction for instruction")
                }
            }
        }

        return steps
    }

    fun solveForGhost(start: String): Int {
        var currentPosition = start
        var steps = 0

        while (!currentPosition.endsWith(END_POSITION_CHAR)) {
            val instruction = instructions[steps++ % instructions.length]
            currentPosition = with (connections[currentPosition]!!) {
                when(instruction) {
                    LEFT_INSTRUCTION_SYMBOL -> first
                    RIGHT_INSTRUCTION_SYMBOL -> second
                    else -> throw IllegalArgumentException("Invalid symbol $instruction for instruction")
                }
            }
        }

        return steps
    }

    fun solveByGhosts(): Long {
        val solveSteps = startingNodes.map(::solveForGhost)
        return solveSteps.map { it.toLong() }.lcm()
    }

    companion object {
        private const val START_POSITION = "AAA"
        private const val END_POSITION = "ZZZ"
        private const val LEFT_INSTRUCTION_SYMBOL = 'L'
        private const val RIGHT_INSTRUCTION_SYMBOL = 'R'
        private const val START_POSITION_CHAR = 'A'
        private const val END_POSITION_CHAR = 'Z'
    }
}
