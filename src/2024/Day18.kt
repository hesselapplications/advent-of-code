fun main() {
    // Read input
    val fallingBytes = readInput("2024/Day18")
        .map { it.split(",").map(String::toInt) }
        .map { (x, y) -> Point(x, y) }

    // Determine the grid size
    val maxX = fallingBytes.maxOf { it.x }
    val maxY = fallingBytes.maxOf { it.y }

    // Define the start and end points
    val start = Point(0, 0)
    val end = Point(maxX, maxY)

    // Populate an empty grid
    val grid = mutableMapOf<Point, Char>()
    for (y in 0..maxX) {
        for (x in 0..maxY) {
            grid[Point(x, y)] = '.'
        }
    }

    // Finds the shortest path from start to end, using BFS
    fun shortestPath(
        grid: Grid,
    ): Int {
        val directions = listOf(Direction.N, Direction.S, Direction.E, Direction.W)
        val visited = mutableSetOf<Point>()
        val queue = ArrayDeque<Pair<Point, Int>>() // Pair of point and current distance
        queue.add(start to 0)

        // Explore the grid
        while (queue.isNotEmpty()) {
            val (current, distance) = queue.removeFirst()

            // Return the distance if the end is reached
            if (current == end) return distance

            // Otherwise, explore the neighbors
            if (current !in visited) {
                visited.add(current)
                directions.forEach { direction ->
                    val neighbor = current.neighbor(direction)
                    if (grid[neighbor] == '.' && neighbor !in visited) {
                        queue.add(neighbor to distance + 1)
                    }
                }
            }
        }

        // No path found
        return -1
    }

    // Part 1
    val gridPart1 = grid.toMutableMap()

    // Fill the grid with the first 1024 falling bytes
    fallingBytes.take(1024).forEach { point ->
        gridPart1[point] = '#'
    }

    // Find the shortest path length from start to end and print it
    shortestPath(gridPart1).println()

    // Part 2
    val gridPart2 = grid.toMutableMap()

    // Progressively fill the grid with each falling byte
    fallingBytes.forEach { point ->
        gridPart2[point] = '#'

        // Check if the path is still reachable
        if (shortestPath(gridPart2) == -1) {
            // If not, print the point and break
            "${point.x},${point.y}".println()
            return
        }
    }
}
