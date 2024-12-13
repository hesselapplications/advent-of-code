fun main() {
    data class Machine(
        val aX: Long,
        val aY: Long,
        val bX: Long,
        val bY: Long,
        val prizeX: Long,
        val prizeY: Long
    ) {
        val offset = 10000000000000L
        fun offsetPrize() = copy(prizeX = prizeX + offset, prizeY = prizeY + offset)
    }

    val machines = readInput("2024/Day13").chunked(4) { lines ->
        val matches = Regex("-?\\d+")
            .findAll(lines.joinToString(" "))
            .map { it.value.toLong() }
            .toList()

        Machine(
            aX = matches[0],
            aY = matches[1],
            bX = matches[2],
            bY = matches[3],
            prizeX = matches[4],
            prizeY = matches[5],
        )
    }

    fun Machine.tokensToWin(): Long {
        // Solve the system of equations to find the number of presses for each button
        val numPressesA = (prizeX * bY - prizeY * bX) / (bY * aX - bX * aY)
        val numPressesB = (prizeX * aY - prizeY * aX) / (aY * bX - bY * aX)

        // Verify that the solution is correct
        val foundA = aX * numPressesA + bX * numPressesB == prizeX
        val foundB = aY * numPressesA + bY * numPressesB == prizeY

        // If not, return zero
        if (!foundA || !foundB) return 0

        // Otherwise, calculate the total tokens and return
        return 3 * numPressesA + numPressesB
    }

    // Part 1
    machines.sumOf { it.tokensToWin() }.println()

    // Part 2
    machines.sumOf { it.offsetPrize().tokensToWin() }.println()
}
