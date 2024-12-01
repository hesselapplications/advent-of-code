package `2023`

import println
import readInput

fun main() {
    data class Match(
        val value: String,
        val rowIndex: Int,
        val colBeginIndex: Int,
        val colEndIndex: Int,
    )

    fun MatchResult.toMatch(rowIndex: Int) = Match(
        value = value,
        rowIndex = rowIndex,
        colBeginIndex = range.first,
        colEndIndex = range.last,
    )

    fun findMatches(regex: Regex, input: List<String>) = input.flatMapIndexed { rowIndex, line ->
        regex.findAll(line).map { it.toMatch(rowIndex) }
    }

    fun Match.isAdjacentTo(otherMatch: Match) =
        rowIndex in (otherMatch.rowIndex - 1)..(otherMatch.rowIndex + 1) && (
                colBeginIndex in (otherMatch.colBeginIndex - 1) .. (otherMatch.colEndIndex + 1)
                        || colEndIndex in (otherMatch.colBeginIndex - 1) .. (otherMatch.colEndIndex + 1)
                )

    val input = readInput("2023/Day03")
    val numbers = findMatches(Regex("""\d+"""), input)
    val symbols = findMatches(Regex("""[^\d.]"""), input)

    // Part 1
    val partNumbers = numbers.filter { number -> symbols.any { number.isAdjacentTo(it) } }
    partNumbers.sumOf { it.value.toInt() }.println()

    // Part 2
    symbols
        .filter { it.value == "*" } // find all potential gears
        .associateWith { gear -> partNumbers.filter { gear.isAdjacentTo(it) } } // find adjacent parts to each
        .filterValues { it.size == 2 } // real gears are only adjacent to two parts
        .values.sumOf { partNumbers -> partNumbers.map { it.value.toInt() }.reduce(Int::times) } // multiply parts and sum
        .println()
}