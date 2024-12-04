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
        Direction.N -> Point(x, y - distance)
        Direction.S -> Point(x, y + distance)
        Direction.E -> Point(x + distance, y)
        Direction.W -> Point(x - distance, y)
        Direction.NE -> Point(x + distance, y - distance)
        Direction.NW -> Point(x - distance, y - distance)
        Direction.SE -> Point(x + distance, y + distance)
        Direction.SW -> Point(x - distance, y + distance)
    }

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