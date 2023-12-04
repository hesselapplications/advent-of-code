import kotlin.math.pow

data class ScratchCard(
    val id: Int,
    val winningNumbers: Set<Int>,
    val cardNumbers: Set<Int>,
) {
    fun numMatches() = winningNumbers.intersect(cardNumbers).size
}
fun String.extractUniqueNumbers() = Regex("""\d+""").findAll(this).map { it.value.toInt() }.toSet()

fun parseScratchCards(lines: List<String>) = lines.map { line ->
    val parts = line.split(":", "|")
    ScratchCard(
        id = parts[0].extractUniqueNumbers().first(),
        winningNumbers = parts[1].extractUniqueNumbers(),
        cardNumbers = parts[2].extractUniqueNumbers(),
    )
}

fun main() {
    val input = readInput("Day04")
    val scratchCards = parseScratchCards(input)

    // Part 1
    fun ScratchCard.points() = when (val numMatches = numMatches()) {
        0 -> 0 // no matches, no points
        else -> 2.0.pow(numMatches - 1).toInt() // 2 to the power of (matches - 1)
    }
    scratchCards.sumOf { it.points() }.println()

    // Part 2
    val idToNumCardsMap = scratchCards.associate { it.id to 1 }.toMutableMap() // start with one of each card
    scratchCards.forEach { scratchCard ->
        with(scratchCard) {
            val numCurrentCards = idToNumCardsMap[id]!! // number of current cards
            val copyIds = (id + 1)..(id + numMatches()) // range of copies based on the number of matches
            for (copyId in copyIds) {
                // each copy gets increased by the number of current cards
                idToNumCardsMap[copyId] = idToNumCardsMap[copyId]!! + numCurrentCards
            }
        }
    }

    idToNumCardsMap.values.sum().println() // sum the count of each card
}