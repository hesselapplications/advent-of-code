fun main() {
    val input = readInput("2025/Day01")

    // Parts 1 & 2
    val dialNumPositions = 100
    var dialPosition = 50
    var part1 = 0
    var part2 = 0

    println("The dial starts by pointing at $dialPosition.")
    input.forEach { turn ->
        val direction = if (turn[0] == 'L') -1 else 1
        val clicks = turn.substring(1).toInt()

        repeat(clicks) {
            dialPosition = (dialPosition + direction).mod(dialNumPositions)
            if (dialPosition == 0) {
                part2++
            }
        }

        if (dialPosition == 0) {
            part1++
        }
    }

    println("Part 1: $part1")
    println("Part 2: $part2")

}