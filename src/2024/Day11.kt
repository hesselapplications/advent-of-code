fun main() {
    // Read input, group by stone count
    val stoneCounts = readInput("2024/Day11")[0]
        .split(" ")
        .map { it.toLong() }
        .groupingBy { it }.eachCount()
        .mapValues { it.value.toLong() }

    // Store previously computed results
    val cache = mutableMapOf<Long, List<Long>>()

    // Function to compute the resultant stones after a blink
    fun blink(stone: Long): List<Long> = cache.getOrPut(stone) {
        when {
            stone == 0L -> listOf(1L)
            stone.toString().length % 2 == 0 -> stone.toString().let {
                val half = it.length / 2
                listOf(it.substring(0, half).toLong(), it.substring(half).toLong())
            }
            else -> listOf(stone * 2024)
        }
    }

    // Function to perform the blink operation for all stones a given number of times
    fun blinkAll(blinks: Int) {
        var currentCounts = stoneCounts
        repeat(blinks) {
            val newCounts = mutableMapOf<Long, Long>()
            for ((stone, count) in currentCounts) {
                for (newStone in blink(stone)) {
                    newCounts[newStone] = newCounts.getOrDefault(newStone, 0L) + count
                }
            }
            currentCounts = newCounts
        }
        currentCounts.values.sum().println()
    }

    // Part 1
    blinkAll(25)

    // Part 2
    blinkAll(75)
}
