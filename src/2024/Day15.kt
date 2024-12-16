fun main() {
    val input = readInput("2024/Day15")

    val moves = input
        .filter { !it.startsWith('#') }
        .flatMap { it.toCharArray().toList() }
        .mapNotNull {
            when (it) {
                '^' -> Direction.N
                'v' -> Direction.S
                '<' -> Direction.W
                '>' -> Direction.E
                else -> null
            }
        }

    fun findPointsToPush(
        currentGrid: Grid,
        startPoint: Point,
        direction: Direction,
    ): Set<Point> {
        val remainingCheckPoints = mutableSetOf(startPoint)
        val pointsToPush = mutableSetOf<Point>()

        // Queues up the next point to check, avoiding infinite loops
        fun checkNext(point: Point) {
            if (point !in pointsToPush) remainingCheckPoints.add(point)
        }

        while (remainingCheckPoints.isNotEmpty()) {
            // Get the next point to check, add it to the list of points to push
            val checkPoint = remainingCheckPoints.first()
            remainingCheckPoints.remove(checkPoint)
            pointsToPush.add(checkPoint)

            when (currentGrid[checkPoint]) {
                '#' -> return emptySet() // Nothing can be pushed
                'O' -> checkNext(checkPoint.neighbor(direction)) // Small box, check next for a chain
                '[' -> {
                    // Start of a large box, check other half, and check next for a chain
                    checkNext(checkPoint.neighbor(direction))
                    checkNext(checkPoint.neighbor(Direction.E))
                }
                ']' -> {
                    // End of a large box, check other half, and check next for a chain
                    checkNext(checkPoint.neighbor(direction))
                    checkNext(checkPoint.neighbor(Direction.W))
                }
            }
        }

        return pointsToPush
    }

    fun getAnswer(startGrid: Grid) {
        val resultGrid = moves.fold(startGrid) { currentGrid, direction ->

            val currentPoint = currentGrid.filterValues { it == '@' }.keys.first()
            val nextPoint = currentPoint.neighbor(direction)
            val nextGrid = currentGrid.toMutableMap()

            when (currentGrid[nextPoint]) {
                '.' -> {
                    // Move robot to empty space
                    nextGrid[currentPoint] = '.'
                    nextGrid[nextPoint] = '@'
                }
                'O', '[', ']' -> {
                    // Determine which boxes to push
                    val boxesToPush = findPointsToPush(currentGrid, nextPoint, direction)

                    // If any, push them
                    if (boxesToPush.isNotEmpty()) {
                        boxesToPush.forEach { point ->
                            // Get the previous point
                            val previousPoint = point.neighbor(direction.opposite())

                            nextGrid[point] = if (previousPoint in boxesToPush) {
                                // If the previous point is also being pushed, move it
                                currentGrid[previousPoint]!!

                            } else {
                                // Otherwise, it becomes an empty space
                                '.'
                            }
                        }

                        // Move the robot
                        nextGrid[currentPoint] = '.'
                        nextGrid[nextPoint] = '@'
                    }
                }
            }

            nextGrid
        }

        // Calculate "GPS" coordinates for all boxes
        resultGrid
            .filter { it.value == 'O' || it.value == '[' }.keys
            .sumOf { 100 * it.y + it.x }
            .println()
    }

    // Part 1
    getAnswer(
        startGrid = input
            .filter { it.startsWith('#') }
            .toGrid()
    )

    // Part 2
    getAnswer(
        startGrid = input
            .filter { it.startsWith('#') }
            .map {
                it
                    .replace("#", "##")
                    .replace("O", "[]")
                    .replace(".", "..")
                    .replace("@", "@.")
            }
            .toGrid()
    )

}