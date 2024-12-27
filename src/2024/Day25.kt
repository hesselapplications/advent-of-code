fun main() {
    // Rotate a string counter-clockwise, turning columns into rows, so we can count the number of '#' in each
    fun String.rotateCounterClockwise(): String {
        val lines = lines()
        val maxLength = lines.maxOf { it.length }

        val rotatedLines = (0 until maxLength).map { col ->
            lines.reversed().map {
                if (col < it.length) it[col] else ' '
            }.joinToString("")
        }

        return rotatedLines.joinToString("\n")
    }

    // Get the height of each line
    fun String.heights() = lines().map { line -> line.count { it == '#' } - 1 }

    // Parse the input
    val (keys, locks) = readInput("2024/Day25")
        .joinToString("\n")
        .split("\n\n")
        .map { it.rotateCounterClockwise() }
        .partition { it.startsWith("#") }
        .let {
            it.first.map { key -> key.heights() } to it.second.map { lock -> lock.heights() }
        }

    // There is no overlap if each key + lock height is <= 5, meaning the key can unlock the lock
    fun canUnlock(
        key: List<Int>,
        lock: List<Int>,
    ) = key.zip(lock).all { (k, l) -> k + l <= 5 }

    // Part 1, try each key with each lock
    var count = 0
    for (lock in locks) {
        for (key in keys) {
            if (canUnlock(key, lock)) {
                count++
            }
        }
    }
    count.println()

    // Part 2, rejoice! All the previous puzzles have already been solved :)
}