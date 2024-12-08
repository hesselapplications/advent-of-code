fun main() {
    val grid = readInput("2024/Day08").toGrid()

    val nodesGroupedByFrequency = grid
        .filterValues { it != '.' } // Find all nodes with a frequency signature
        .entries.groupBy({ it.value }, { it.key }) // Group them by frequency signature

    fun countAntiNodes(resonantHarmonics: Boolean) {
        val antiNodes = mutableSetOf<Point>()

        nodesGroupedByFrequency.values.forEach { frequencyNodes ->
            // Generate all possible unique pairs of frequency nodes
            val pairs = frequencyNodes.flatMap { node ->
                frequencyNodes.filter { it != node }.map { it to node }
            }

            // For each pair, calculate the anti-nodes
            pairs.forEach { (a, b) ->

                // Calculate the difference between the two nodes
                val (xDiff, yDiff) = a.x - b.x to a.y - b.y

                if (!resonantHarmonics) {
                    // Only add the first anti-node in each direction, if within the grid bounds
                    a.neighbor(x = xDiff, y = yDiff)
                        .takeIf(grid::isWithinBounds)
                        ?.let(antiNodes::add)

                    b.neighbor(x = -xDiff, y = -yDiff)
                        .takeIf(grid::isWithinBounds)
                        ?.let(antiNodes::add)

                } else {
                    // Add all anti-nodes in each direction, including the starting nodes, if within the grid bounds
                    generateSequence(a) { it.neighbor(x = xDiff, y = yDiff) }
                        .takeWhile(grid::isWithinBounds)
                        .forEach(antiNodes::add)

                    generateSequence(b) { it.neighbor(x = -xDiff, y = -yDiff) }
                        .takeWhile(grid::isWithinBounds)
                        .forEach(antiNodes::add)
                }
            }
        }

        antiNodes.size.println()
    }

    // Part 1
    countAntiNodes(resonantHarmonics = false)

    // Part 2
    countAntiNodes(resonantHarmonics = true)
}