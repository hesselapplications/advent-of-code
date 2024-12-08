fun main() {
    val input = readInput("2024/Day07")

    val equations = input.map { line ->
        val (testValue, numbers) = line.split(": ")
        testValue.toLong() to numbers.split(" ").map { it.toLong() }
    }

    fun testValueReachable(
        testValue: Long,
        current: Long,
        remaining: List<Long>,
        combine: Boolean,
    ): Boolean {
        // Base case
        if (remaining.isEmpty()) return testValue == current

        // Get next number
        val next = remaining.first()

        // Try all possible operations
        val multiplied = testValueReachable(testValue, current * next, remaining.drop(1), combine)
        val added = testValueReachable(testValue, current + next, remaining.drop(1), combine)
        val combined = testValueReachable(testValue, "$current$next".toLong(), remaining.drop(1), combine)

        // Return if any of the operations are true
        return multiplied || added || (combine && combined)
    }

    fun calibrationResult(combine: Boolean) = equations.sumOf { (testValue, numbers) ->
        // Check if the test value is reachable, if so add it to the sum
        if (testValueReachable(testValue, numbers.first(), numbers.drop(1), combine)) testValue else 0
    }

    // Part 1
    calibrationResult(combine = false).println()

    // Part 2
    calibrationResult(combine = true).println()

}