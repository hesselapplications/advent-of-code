fun main() {
    val idRanges = readInput("2025/Day02")
        .single()
        .split(",")
        .map { range ->
            val (start, end) = range.split("-").map { it.toLong() }
            start..end
        }

    // Part 1
    var part1 = 0L
    for (idRange in idRanges) {
        for (id in idRange) {
            val idString = id.toString()
            if (idString.length % 2 != 0) continue
            val firstHalf = idString.take(idString.length / 2)
            val secondHalf = idString.substring(idString.length / 2)
            if (firstHalf == secondHalf) {
                part1 += id
            }
        }
    }
    println("Part 1: $part1")

    // Part 2
    var part2 = 0L
    for (idRange in idRanges) {
        for (id in idRange) {
            val idString = id.toString()
            for (substringLength in 1 .. idString.length / 2) {
                val chunks = idString.chunked(substringLength)
                if (chunks.all { it == chunks[0] }) {
                    part2 += id
                    break
                }
            }
        }
    }
    println("Part 2: $part2")

}