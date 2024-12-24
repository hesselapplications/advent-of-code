fun main() {
    val input = readInput("2024/Day23")

    // Build graph
    val graph = buildMap<String, List<String>> {
        input.forEach { line ->
            val (a, b) = line.split("-")
            this[a] = this[a].orEmpty() + b
            this[b] = this[b].orEmpty() + a
        }
    }

    // Bron-Kerbosch algorithm
    fun getCliques(
        currentClique: Set<String>,
        remainingNodes: MutableSet<String>,
        visitedNodes: MutableSet<String>
    ): List<Set<String>> {
        if (remainingNodes.isEmpty() && visitedNodes.isEmpty()) {
            return listOf(currentClique)
        }

        return buildList {
            remainingNodes.toList().forEach { v ->
                val neighbors = graph[v].orEmpty()
                addAll(getCliques(
                    currentClique = currentClique + v,
                    remainingNodes = remainingNodes.intersect(neighbors).toMutableSet(),
                    visitedNodes = visitedNodes.intersect(neighbors).toMutableSet()
                ))

                remainingNodes.remove(v)
                visitedNodes.add(v)
            }
        }
    }

    // Find all cliques
    val cliques = getCliques(
        currentClique = emptySet(),
        remainingNodes = graph.keys.toMutableSet(),
        visitedNodes = mutableSetOf(),
    )

    // Part 1
    cliques
        .filter { it.size >= 3 } // Only consider cliques of size 3 or more
        .flatMap { it.toList().combinations(3) } // Generate all combinations of 3 nodes
        .filter { it.any { n -> n.startsWith("t") } } // Filter to cliques containing a node starting with "t"
        .distinct().size // Count distinct cliques
        .println()

    // Part 2
    cliques
        .maxBy { it.size } // Find maximal clique
        .sorted() // Sort nodes alphabetically
        .joinToString(",") // Join nodes with commas
        .println()
}