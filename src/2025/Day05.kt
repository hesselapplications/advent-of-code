fun main() {
    val input = readInput("2025/Day05")

    val freshIngredientIdRanges = input
        .takeWhile { it.isNotBlank() }
        .map { it.split("-") }
        .map { it[0].toLong() .. it[1].toLong() }
        .sortedBy { it.first }

    // Part 1
    val availableIngredientIds = input
        .takeLastWhile { it.isNotBlank() }
        .map { it.toLong() }

    val part1 = availableIngredientIds.count { id ->
        freshIngredientIdRanges.any { range -> id in range }
    }
    println("Part 1: $part1")

    // Part 2
    val mergedFreshIngredientIdRanges = freshIngredientIdRanges.fold(mutableListOf<LongRange>()) { acc, range ->
        if (acc.isEmpty() || acc.last().last < range.first - 1) {
            acc.add(range)
        } else {
            val lastRange = acc.removeAt(acc.size - 1)
            acc.add(lastRange.first .. maxOf(lastRange.last, range.last))
        }
        acc
    }

    val part2 = mergedFreshIngredientIdRanges.sumOf { it.last - it.first + 1 }

    println("Part 2: $part2")

}