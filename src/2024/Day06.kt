fun main() {
    val grid = readInput("2024/Day06").toGrid()

    fun Point.isObstacle() = grid[this] == '#'

    fun Direction.rotateRight() = when (this) {
        Direction.N -> Direction.E
        Direction.E -> Direction.S
        Direction.S -> Direction.W
        Direction.W -> Direction.N
        else -> error("Invalid direction")
    }

    val startingPoint = grid.filterValues { it == '^' }.keys.first()
    var currentPoint = startingPoint
    var currentDirection = Direction.N

    // Part 1

    // Keep track of visited points
    val visitedPoints = mutableSetOf<Point>()

    while (currentPoint in grid) {
        // Mark the current point as visited
        visitedPoints.add(currentPoint)

        // Determine where the guard is heading next
        val nextPoint = currentPoint.neighbor(currentDirection)

        if (nextPoint.isObstacle()) {
            // If there's an obstacle, rotate right
            currentDirection = currentDirection.rotateRight()
        } else {
            // Otherwise, move forward
            currentPoint = nextPoint
        }
    }

    visitedPoints.size.println()

    // Part 2
    var loopsFound = 0

    // Any points along the guard's original path can be new obstacles
    visitedPoints.forEach { newObstaclePoint ->

        // Reset the guard's position and direction when checking each potential new obstacle
        currentPoint = startingPoint
        currentDirection = Direction.N

        // Keep track of visited points, this time with direction
        val visitedPointsWithDirection = mutableSetOf<Pair<Point, Direction>>()

        while (currentPoint in grid) {
            // Mark the current point and direction as visited
            visitedPointsWithDirection.add(currentPoint to currentDirection)

            // Determine where the guard is heading next
            val nextPoint = currentPoint.neighbor(currentDirection)

            if (nextPoint.isObstacle() || nextPoint == newObstaclePoint) {
                // If there's an existing obstacle, or the potential new obstacle, rotate right
                currentDirection = currentDirection.rotateRight()
            } else {
                // Otherwise, move forward
                currentPoint = nextPoint
            }

            if (currentPoint to currentDirection in visitedPointsWithDirection) {
                // If the same point and direction has already been visited, a loop has been found
                loopsFound++
                break
            }
        }
    }
    loopsFound.println()
}