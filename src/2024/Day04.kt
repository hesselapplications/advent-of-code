package `2024`

import println
import readInput

private enum class Direction {
    N, S, E, W, NE, NW, SE, SW
}

private data class Point(
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

fun main() {
    val input = readInput("2024/Day04")

    val grid = mutableMapOf<Point, Char>()
    input.forEachIndexed { y, line ->
        line.forEachIndexed { x, c ->
            grid[Point(x, y)] = c
        }
    }

    // Part 1
    fun Point.nextThreeLetters(direction: Direction) = buildString {
        for (distance in 1..3) {
            append(grid[neighbor(direction, distance)])
        }
    }

    grid.filterValues { it == 'X' }.keys
        .sumOf { point ->
            Direction.entries.count { direction ->
                "X" + point.nextThreeLetters(direction) == "XMAS"
            }
        }.println()

    // Part 2
    fun Point.firstCrossingWord() = buildString {
        append(grid[neighbor(Direction.NW)])
        append("A")
        append(grid[neighbor(Direction.SE)])
    }

    fun Point.secondCrossingWord() = buildString {
        append(grid[neighbor(Direction.NE)])
        append("A")
        append(grid[neighbor(Direction.SW)])
    }

    fun String.isMasOrSam() = this == "MAS" || this == "SAM"

    grid.filterValues { it == 'A' }.keys
        .count { point -> point.firstCrossingWord().isMasOrSam() && point.secondCrossingWord().isMasOrSam() }
        .println()
}