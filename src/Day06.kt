fun main() {
    val input = readInput("Day06")
    val times = input.first().extractLongs()
    val recordDistances = input.last().extractLongs()

    fun findWaysToBeat(time: Long, recordDistance: Long): Int {
        var waysToWin = 0
        for (buttonHoldTime in 1..<time) {
            val remainingTime = time - buttonHoldTime
            val distance = buttonHoldTime * remainingTime
            if (distance > recordDistance) waysToWin++
        }
        return waysToWin
    }

    // Part 1
    times.zip(recordDistances).map { (time, recordDistance) -> findWaysToBeat(time, recordDistance) }
        .reduce(Int::times)
        .println()

    // Part 2
    val time = times.joinToString("").toLong()
    val recordDistance = recordDistances.joinToString("").toLong()
    findWaysToBeat(time, recordDistance).println()
}