package `2023`

import println
import readInput

fun main() {
    fun String.concatFirstAndLastCharsToInt() = "${first()}${last()}".toInt()
    val input = readInput("2023/Day01")

    // Part 1
    input.sumOf { line ->
        line.filter { it.isDigit() }.concatFirstAndLastCharsToInt() // extract numeric digits
    }.println()

    // Part 2
    val digits = listOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")
    input.sumOf { line ->
        line.mapIndexedNotNull { charIndex, char -> // ignore nulls
            if (char.isDigit()) char.digitToInt() // extract numeric digit

            else digits // extract text digit starting at the current char index
                .firstOrNull { digit -> line.startsWith(digit, charIndex) } // returns null if none found
                ?.let { digit -> digits.indexOf(digit) + 1 } // text digit index + 1 gets numeric digit
        }
        .joinToString("").concatFirstAndLastCharsToInt()
    }.println()
}