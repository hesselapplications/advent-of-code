fun main() {
    fun String.extractLongs() = Regex("""\d+""").findAll(this).map { it.value.toLong() }.toList()

    val input = readInput("2023/Day06")
    val times = input.first().extractLongs()
    val recordDistances = input.last().extractLongs()

    fun waysToWin(time: Long, recordDistance: Long) = (1..<time)
        .count { buttonHoldTime ->
            buttonHoldTime * (time - buttonHoldTime) > recordDistance
        }

    // Part 1
    times.zip(recordDistances).map { (time, recordDistance) -> waysToWin(time, recordDistance) }
        .reduce(Int::times)
        .println()

    // Part 2
    val time = times.joinToString("").toLong()
    val recordDistance = recordDistances.joinToString("").toLong()
    waysToWin(time, recordDistance).println()
}