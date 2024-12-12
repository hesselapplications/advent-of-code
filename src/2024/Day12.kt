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
                grid[plot.neighbor(it)] != grid[plot]
            }
        }
    }.println()

    // Part 2
    fun numEdges(
        region: Set<Point>,
        direction: Direction,
        groupByAxis: (Point) -> Int,
        sortAxis: (Point) -> Int,
    ): Int {
        // Find points on the edge of the region in the given direction
        val points = region.filter { it.neighbor(direction) !in region }

        // Group them by row or column, so we can inspect them in order
        val grouped = points.groupBy(groupByAxis).values

        // Sort the numbers along each row or column
        val sorted = grouped.map { numbers ->
            numbers.map(sortAxis).sorted()
        }

        // Count the number of continuously increasing segments in each row or column
        val numContinuousSegments = sorted.map {
            it.zipWithNext().count { (a, b) -> a + 1 != b } + 1
        }

        // Count the number of continuous segments in each row or column
        return numContinuousSegments.sum()
    }

    regions.sumOf { region ->
        region.size * listOf(
            numEdges(region, Direction.N, { it.y }, { it.x }),
            numEdges(region, Direction.S, { it.y }, { it.x }),
            numEdges(region, Direction.E, { it.x }, { it.y }),
            numEdges(region, Direction.W, { it.x }, { it.y }),
        ).sum()
    }.println()
}
