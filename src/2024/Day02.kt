package `2024`

import println
import readInput
import kotlin.math.abs

fun main() {
    val reports = readInput("2024/Day02").map {
        it.split(" ").map(String::toInt)
    }

    fun List<Int>.isSafe(): Boolean {
        val pairs = this.zipWithNext() // Pair each level with the next

        val unsafeDifference = pairs.any { (a, b) -> abs(a - b) !in 1..3 } // Any difference outside 1-3
        if (unsafeDifference) return false

        return pairs.all { (a, b) -> a < b } // All increasing
                || pairs.all { (a, b) -> a > b } // All decreasing
    }

    // Part 1
    reports.count { it.isSafe() }.println()

    // Part 2
    reports.count { levels ->
        levels.indices.any { index ->
            levels.toMutableList().apply { removeAt(index) }.isSafe() // Remove one level
        }
    }.println()
}