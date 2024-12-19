fun main() {
    // Parse input
    val input = readInput("2024/Day19")
    val patterns = input.first().split(", ")
    val designs = input.drop(2)

    // Cache of design -> numWaysToMakeDesign
    val cache = mutableMapOf<String, Long>()

    fun numWaysToMakeDesign(design: String): Long {
        // Base case, we can make the design
        if (design.isEmpty()) return 1L

        // Check cache
        if (design in cache) return cache[design]!!

        // If not cached, try to make the design from each pattern
        val numWays = patterns
            .filter { design.startsWith(it) }
            .sumOf { numWaysToMakeDesign(design.removePrefix(it)) }

        // Cache and return
        cache[design] = numWays
        return numWays
    }

    // Part 1, output the number of possible designs
    designs.count { numWaysToMakeDesign(it) > 0 }.println()

    // Part 2, output the total number of ways to make all designs
    designs.sumOf { numWaysToMakeDesign(it) }.println()
}
