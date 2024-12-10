fun main() {
    val grid = readInput("2024/Day10").toGrid().mapValues { it.value.toString().toInt() }
    val trailheads = grid.filterValues { it == 0 }.keys
    val directions = listOf(Direction.N, Direction.S, Direction.E, Direction.W)

    fun Point.nextSteps(): List<Point> {
        val height = grid[this]!!
        return directions
            .map { neighbor(it) }
            .filter { grid[it] == height + 1 }
    }

    // Part 1
    fun getTrailNines(
        point: Point,
        height: Int,
    ): Set<Point> {
        if (height == 9) return setOf(point)
        return point.nextSteps().flatMap { getTrailNines(it, height + 1) }.toSet()
    }

    trailheads.sumOf { getTrailNines(it, 0).size }.println()

    // Part 2
    fun getTrailRating(
        point: Point,
        height: Int,
    ): Int {
        if (height == 9) return 1
        return point.nextSteps().sumOf { getTrailRating(it, height + 1) }
    }

    trailheads.sumOf { getTrailRating(it, 0) }.println()
}