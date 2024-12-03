package `2024`

import println
import readInput

fun main() {
    val input = readInput("2024/Day03").joinToString("")

    fun MatchResult.multiply() = groupValues[1].toInt() * groupValues[2].toInt()

    // Part 1
    Regex("""mul\((\d+),(\d+)\)""")
        .findAll(input)
        .sumOf { it.multiply() }
        .println()

    // Part 2
    var enabled = true
    var sum = 0

    Regex("""mul\((\d+),(\d+)\)|do\(\)|don't\(\)""")
        .findAll(input)
        .forEach {
            when (it.value) {
                "do()" -> enabled = true
                "don't()" -> enabled = false
                else -> if (enabled) sum += it.multiply()
            }
        }

    sum.println()
}