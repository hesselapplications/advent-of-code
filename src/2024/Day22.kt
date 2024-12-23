fun main() {
    // Read the input file
    val initialSecrets = readInput("2024/Day22").map { it.toLong() }
    val numIterations = 2000

    // Define utils for generating the next secret
    fun Long.mix(other: Long) = this xor other
    fun Long.prune() = this % 16777216
    fun Long.price() = this % 10

    // Helper to generate the next secret
    fun Long.nextSecret(): Long {
        val step1 = (this * 64).mix(this).prune()
        val step2 = Math.floorDiv(step1, 32).mix(step1).prune()
        val step3 = (step2 * 2048).mix(step2).prune()
        return step3
    }

    // Helper to generate the next many secrets
    fun Long.nextSecrets() = generateSequence(this) { it.nextSecret() }

    // Part 1
    initialSecrets
        .sumOf { it.nextSecrets().drop(numIterations).first() }
        .println()

    // Part 2
    val totalPrices = mutableMapOf<List<Long>, Long>()

    initialSecrets.forEach { initialSecret ->
        // Generate the secret numbers
        val secretNumbers = initialSecret.nextSecrets().take(numIterations).toList()

        // Calculate the price changes
        val changes = secretNumbers.zipWithNext { a, b -> b.price() - a.price() }

        // Only track each unique sequence of 4 changes
        val seen = mutableSetOf<List<Long>>()

        // Iterate over each window
        for (i in 0..changes.size - 4) {

            // Get the next window of 4 changes
            val sequence = changes.subList(i, i + 4)

            if (sequence !in seen) {
                // If we haven't seen this sequence before, add it to the total prices
                totalPrices[sequence] = totalPrices.getOrDefault(sequence, 0) + secretNumbers[i + 4].price()
                seen.add(sequence)
            }
        }
    }

    totalPrices.values.max().println()
}
