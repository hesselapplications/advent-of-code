fun main() {
    val grid = readInput("2024/Day12").toGrid()
    val directions = setOf(Direction.N, Direction.S, Direction.E, Direction.W)

    // Explore regions and group plots with the same plant
    val visited = mutableSetOf<Point>()
    val regions = grid.entries.map { (plot, plant) ->
        val region = mutableSetOf<Point>()
        val stack = mutableListOf(plot)
        while (stack.isNotEmpty()) {
            val current = stack.removeLast()
            if (visited.add(current)) {
                region += current
                stack += directions
                    .map { current.neighbor(it) }
                    .filter { grid[it] == plant }
            }
        }
        region
    }

    // Part 1
    regions.sumOf { region ->
        region.size * region.sumOf { plot ->
            directions.count {
                grid[plot.neighbor(it)] != grid[plot] // Count plots with different neighboring plants
            }
        }
    }.println()

    // Part 2
    fun List<Int>.numContinuousSegments(): Int {
        val numGaps = sorted().zipWithNext().count { (a, b) -> a + 1 != b } // Count breaks in the sequence
        return numGaps + 1 // If there are n gaps, there are n+1 segments
    }

    regions.sumOf { region ->
        region.size * listOf(
            region
                .filter { it.neighbor(Direction.N) !in region } // Top edges
                .groupBy(Point::y, Point::x).values // Group by row, map to x coordinates
                .sumOf { it.numContinuousSegments() }, // Count continuous segments in each row

            region
                .filter { it.neighbor(Direction.S) !in region } // Bottom edges
                .groupBy(Point::y, Point::x).values // Group by row, map to x coordinates
                .sumOf { it.numContinuousSegments() }, // Count continuous segments in each row

            region
                .filter { it.neighbor(Direction.W) !in region } // Left edges
                .groupBy(Point::x, Point::y).values // Group by column, map to y coordinates
                .sumOf { it.numContinuousSegments() }, // Count continuous segments in each column

            region
                .filter { it.neighbor(Direction.E) !in region } // Right edges
                .groupBy(Point::x, Point::y).values // Group by column, map to y coordinates
                .sumOf { it.numContinuousSegments() }, // Count continuous segments in each column
        ).sum()
    }.println()
}
