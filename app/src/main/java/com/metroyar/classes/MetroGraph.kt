package com.metroyar.classes

import com.metroyar.utils.GlobalObjects.adjNodesLineNum
import com.metroyar.utils.log
import java.util.PriorityQueue

class MetroGraph(numberOfStationsInGraph: Int) {
    private val adjacencyList: Array<MutableList<Int>> =
        Array(numberOfStationsInGraph) { mutableListOf() }

    fun addEdge(v: Int, w: Int) {
        adjacencyList[v].add(w)
        adjacencyList[w].add(v)
    }

    private data class Node(val id: Int, val distance: Int, val interchanges: Int)

    fun findPath(src: Int, dst: Int): List<Int> {
        try {
            val queue = PriorityQueue<Node> { nodeA, nodeB ->
                if (nodeA.interchanges != nodeB.interchanges) nodeA.interchanges - nodeB.interchanges
                else nodeA.distance - nodeB.distance
            }
            val previous = HashMap<Int, Int>()
            val visited = HashSet<Int>()
            val interchanges = HashMap<Int, Int>()

            queue.add(Node(src, 0, 0))
            interchanges[src] = 0

            while (queue.isNotEmpty()) {
                val node = queue.remove()
                val current = node.id
                val distance = node.distance
                val interchange = node.interchanges

                if (visited.contains(current)) continue
                visited.add(current)

                if (current == dst) break

                for (adjNode in adjacencyList[current]) {
                    val newDistance = distance + 1
                    val lineChange =
                        adjNodesLineNum[Pair(
                            current,
                            adjNode
                        )] != adjNodesLineNum[Pair(previous[current] ?: src, current)]


                    val newInterchange = if (lineChange) interchange + 1 else interchange

                    if (!visited.contains(adjNode) && (!interchanges.containsKey(adjNode)
                                || newInterchange <= interchanges[adjNode]!!)
                    ) {
                        queue.add(Node(adjNode, newDistance, newInterchange))
                        previous[adjNode] = current
                        interchanges[adjNode] = newInterchange
                    }
                }
            }
            // Build the path
            return generateSequence(dst) { previous[it] }.toList().reversed()
        } catch (e: Exception) {
            log("err", e.message)
            return emptyList()
        }
    }
}
