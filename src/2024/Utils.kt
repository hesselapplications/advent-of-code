import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

enum class Direction {
    N, S, E, W, NE, NW, SE, SW
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