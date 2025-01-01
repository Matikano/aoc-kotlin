package aoc2023.day_25

import org.jgrapht.alg.StoerWagnerMinimumCut
import org.jgrapht.graph.DefaultEdge
import org.jgrapht.graph.SimpleGraph
import utils.AocTask
import kotlin.time.measureTime
import org.jgrapht.Graph as JGraph

object Day25: AocTask() {

    override fun executeTask() {
        measureTime {
            val jgraph = testInput.toJGraph()
            val (group1, group2) = jgraph.minCutSets()
            val result = group1.size * group2.size
            println("First cycle set after mincut = $group1")
            println("Second cycle set after mincut = $group2")
            println("Result = $result")

        }.let { println("Test part took $it\n") }

        measureTime {
            val graph = input.trim().toJGraph()
            val (group1, group2) = graph.minCutSets()
            val result = group1.size * group2.size
            println("Result = $result")
        }.let { println("Test part took $it\n") }
    }

    private fun <V, E> JGraph<V, E>.minCutSets(): Pair<Set<V>, Set<V>> {
        val vertices = vertexSet()
        val minCutVertices = StoerWagnerMinimumCut<V, E>(this).minCut()
        return minCutVertices to (vertices - minCutVertices)
    }

    private fun String.toJGraph(): JGraph<String, DefaultEdge> =
        SimpleGraph<String, DefaultEdge>(DefaultEdge::class.java).apply {
            this@toJGraph.lines().forEach { line ->
                val (source, destinationsString) = line.split(": ")
                val destinations = destinationsString.split(" ")
                addVertex(source)
                destinations.forEach { node ->
                    addVertex(node)
                    addEdge(source, node)
                }
            }
        }
}