package `2024`

import println
import readInput
import kotlin.math.abs

fun main() {
    val input = readInput("2024/Day02")

    val reports = input.map { report ->
        report.split(" ").map { it.toInt() }
    }

    fun safeLevels(levels: List<Int>): Boolean {
        val pairs = levels.zipWithNext() // Pair each level with the next

        val safeDifference = pairs.all { (a, b) -> abs(a - b) in 1..3 } // All differences within 1-3
        if (!safeDifference) return false

        return pairs.all { (a, b) -> a < b } // All increasing
                || pairs.all { (a, b) -> a > b } // All decreasing
    }

    // Part 1
    reports
        .count { levels -> safeLevels(levels) }
        .println()

    // Part 2
    reports.count { levels ->
        levels.indices.any { index ->
            val dampedLevels = levels.toMutableList().apply { removeAt(index) } // Remove one level
            safeLevels(dampedLevels)
        }
    }.println()

}