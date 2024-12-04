package `2024`

import Direction
import Point
import println
import readInput
import toGrid

fun main() {
    val input = readInput("2024/Day04")
    val grid = input.toGrid()

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