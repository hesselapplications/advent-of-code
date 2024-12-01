package `2024`

import println
import readInput
import kotlin.math.abs

fun main() {
    val input = readInput("2024/Day01")

    val (leftList, rightList) = input.map {
        val (left, right) = it.split("   ")
        Pair(left.toInt(), right.toInt())
    }.unzip()

    val sortedLeft = leftList.sorted()
    val sortedRight = rightList.sorted()

    // Part 1
    sortedLeft
        .zip(sortedRight)
        .sumOf { (left, right) -> abs(left - right) }
        .println()

    // Part 2
    val rightCounts = rightList
        .groupingBy { it }
        .eachCount()

    leftList
        .sumOf { left -> left * (rightCounts[left] ?: 0) }
        .println()
}