fun main() {
    // Parse input
    val input = readInput("2024/Day19")
    val patterns = input.first().split(", ")
    val designs = input.drop(2)

    // Cache of design -> canConstruct
    val cache = mutableMapOf<String, Long>()

    fun canMakeDesign(design: String): Long {
        // Base case, we can make the design
        if (design.isEmpty()) return 1L

        // Check cache
        if (design in cache) return cache[design]!!

        // If not cached, try to make the design from each pattern
        val numWays = patterns
            .filter { design.startsWith(it) }
            .sumOf { canMakeDesign(design.removePrefix(it)) }

        // Cache and return
        cache[design] = numWays
        return numWays
    }

    // Part 1
    designs.count { canMakeDesign(it) > 0 }.println()

    // Part 2
    designs.sumOf { canMakeDesign(it) }.println() // Output the number of possible designs
}
