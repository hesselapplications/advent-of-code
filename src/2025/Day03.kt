fun main() {
    val banks = readInput("2025/Day03")

    fun maxJoltage(bank: String, numBatteries: Int): Long {
        var selectedBatteries = ""
        var startIndex = 0
        while (selectedBatteries.length < numBatteries) {
            val endIndex = bank.length - (numBatteries - selectedBatteries.length - 1)
            val availableBatteries = bank.substring(startIndex, endIndex)

            val nextBattery = availableBatteries.max()
            selectedBatteries += nextBattery

            val nextIndex = availableBatteries.indexOf(nextBattery)
            startIndex += nextIndex + 1
        }
        return selectedBatteries.toLong()
    }

    // Part 1
    val part1 = banks.sumOf { maxJoltage(it, 2) }
    println("Part 1: $part1")

    // Part 2
    val part2 = banks.sumOf { maxJoltage(it, 12) }
    println("Part 2: $part2")

}