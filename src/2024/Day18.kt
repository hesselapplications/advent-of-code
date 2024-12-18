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

    // Define the shortest path function
    fun Grid.shortestFallingBytesPath() = shortestPath(
        start = start,
        end = end,
        isValidNextStep = { point -> this[point] == '.' },
    )

    // Part 1
    val gridPart1 = grid.toMutableMap()

    // Fill the grid with the first 1024 falling bytes
    fallingBytes.take(1024).forEach { point ->
        gridPart1[point] = '#'
    }

    // Find the shortest path length from start to end and print it
    (gridPart1.shortestFallingBytesPath()!!.size - 1).println()

    // Part 2
    val gridPart2 = grid.toMutableMap()

    // Progressively fill the grid with each falling byte
    fallingBytes.forEach { point ->
        gridPart2[point] = '#'

        // Check if the path is still reachable
        if (gridPart2.shortestFallingBytesPath() == null) {
            // If not, print the point and break
            "${point.x},${point.y}".println()
            return
        }
    }
}
