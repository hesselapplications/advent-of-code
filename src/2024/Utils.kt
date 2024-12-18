import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines

fun readInput(name: String) = Path("src/$name.txt").readLines()

fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

fun Any?.println() = println(this)

enum class Direction {
    N, S, E, W, NE, NW, SE, SW;

    fun opposite() = when (this) {
        N -> S
        S -> N
        E -> W
        W -> E
        NE -> SW
        NW -> SE
        SE -> NW
        SW -> NE
    }

}

data class Point(
    val x: Int,
    val y: Int,
) {

    fun neighbor(
        direction: Direction,
        distance: Int = 1,
    ) = when (direction) {
        Direction.N -> neighbor(y = -distance)
        Direction.S -> neighbor(y = distance)
        Direction.E -> neighbor(x = distance)
        Direction.W -> neighbor(x = -distance)
        Direction.NE -> neighbor(x = distance, y = -distance)
        Direction.NW -> neighbor(x = -distance, y = -distance)
        Direction.SE -> neighbor(x = distance, y = distance)
        Direction.SW -> neighbor(x = -distance, y = distance)
    }

    fun neighbor(
        x: Int = 0,
        y: Int = 0,
    ) = Point(this.x + x, this.y + y)

}

typealias Grid = Map<Point, Char>

fun List<String>.toGrid(): Grid {
    val input = this
    return buildMap {
        input.forEachIndexed { y, line ->
            line.forEachIndexed { x, c -> put(Point(x, y), c) }
        }
    }
}

fun Grid.prettyPrint() {
    val grid = this
    val xs = grid.keys.map { it.x }
    val ys = grid.keys.map { it.y }

    buildString {
        for (y in ys.minOrNull()!!..ys.maxOrNull()!!) {
            for (x in xs.minOrNull()!!..xs.maxOrNull()!!) {
                append(grid[Point(x, y)] ?: ' ')
            }
            appendLine()
        }
    }.println()
}

fun Grid.isWithinBounds(point: Point) = point in keys

// Finds the shortest path from start to end, using BFS
fun shortestPath(
    start: Point,
    end: Point,
    isValidNextStep: (Point) -> Boolean,
    directions: List<Direction> = listOf(Direction.N, Direction.S, Direction.E, Direction.W),
): List<Point>? {
    val visited = mutableSetOf<Point>()
    val queue = ArrayDeque<Pair<Point, List<Point>>>() // Pair of point and current path
    queue.add(start to listOf(start))

    // Explore the grid
    while (queue.isNotEmpty()) {
        val (current, path) = queue.removeFirst()

        // Return the path if the end is reached
        if (current == end) return path

        // Otherwise, explore the neighbors
        if (current !in visited) {
            visited.add(current)
            directions.forEach { direction ->
                val neighbor = current.neighbor(direction)
                if (isValidNextStep(neighbor) && neighbor !in visited) {
                    queue.add(neighbor to path + neighbor)
                }
            }
        }
    }

    // No path found
    return null
}