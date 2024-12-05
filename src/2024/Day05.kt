import kotlin.io.path.Path
import kotlin.io.path.readLines

fun main() {
    val input = Path("src/2024/Day05.txt").readLines()

    val rules = input.filter { "|" in it }.map { it.split("|") }
    val updates = input.filter { "," in it }.map { it.split(",") }

    fun List<String>.before(nbr: String) = rules
        .filter {
            it[1] == nbr // Second part of the rule is the number we are looking for
            && it[0] in this // First part of the rule is in the update list
            && it[1] in this // Second part of the rule is in the update list
        }
        .map { it[0] } // First part of the rule contains the numbers that precede the number we are looking for

    fun Collection<List<String>>.middleSum() = println(
        sumOf {
            it[it.size / 2].toInt() // Sum up the middle number of the update list
        }
    )

    // Part 1
    val correctUpdates = updates.filter { update ->
        update.all { nbr ->
            val actualBefore = update.takeWhile { it != nbr }.toSet() // Take all numbers before the current number
            val expectedBefore = update.before(nbr).toSet() // Take all numbers that should be before the current number
            actualBefore == expectedBefore // Ensure the rule is satisfied
        }
    }.also { it.middleSum() }

    // Part 2
    (updates - correctUpdates).map {
        // Sort the numbers by the count of numbers that should be before them
        it.sortedBy { nbr -> it.before(nbr).size }
    }.middleSum()
}