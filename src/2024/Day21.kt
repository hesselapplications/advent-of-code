fun main() {
    val input = readInput("2024/Day21")

    val grid = listOf(
        // Numeric keypad
        "789",
        "456",
        "123",
        " 0A",

        // Directional keypad, substituting A with @ so we can differentiate
        " ^@",
        "<v>"
    ).toGrid()

    // Map keystrokes to points
    val keypad = grid.entries.associate { (point, keystroke) -> keystroke to point }

    // Function to get all directional possible paths from a point to another
    fun getDirectionalPaths(
        currentPoint: Point,
        targetPoint: Point,
    ): List<String> {
        // Calculate the difference between the x and y coordinates
        val dx = targetPoint.x - currentPoint.x
        val dy = targetPoint.y - currentPoint.y

        // Build the directional steps required to reach the target point
        val steps = buildString {
            if (dx < 0) append("<".repeat(-dx))
            if (dx > 0) append(">".repeat(dx))
            if (dy < 0) append("^".repeat(-dy))
            if (dy > 0) append("v".repeat(dy))
        }

        // Get all permutations of the steps, effectively getting all possible paths
        val permutations = steps.toList().permutations().map { it.joinToString("") }

        // Filter out paths that are invalid
        return permutations.filter { path ->
            path.asSequence()
                .runningFold(currentPoint) { pos, dir -> pos.neighbor(dir.toDirection()) }
                .all { grid.isWithinBounds(it) && grid[it] != ' ' }
        }
    }

    // Data class to represent a layer of the recursion
    data class Layer(
        val targetKeystrokes: String,
        val currentDepth: Int,
        val depthLimit: Int,
    )

    // Cache to store the min length of each layer
    val cache = mutableMapOf<Layer, Long>()

    // Recursive function to calculate the minimum length of the paths
    fun minLength(layer: Layer): Long {
        // If the layer is already in the cache, return the value
        if (cache.containsKey(layer)) return cache[layer]!!

        // Destructure the layer
        val (targetKeystrokes, currentDepth, depthLimit) = layer

        // Get the initial point based on the current depth
        var currentPoint = if (currentDepth == 0) keypad['A']!! else keypad['@']!!
        var minLength = 0L

        // Iterate over each target keystroke
        for (targetKeystroke in targetKeystrokes) {
            val targetPoint = keypad[targetKeystroke]!!

            // Get all possible paths from the current point to the target point
            // Append "@" to the end of each path to submit it
            val directionalPaths = getDirectionalPaths(currentPoint, targetPoint)
                .map { "$it@" }
                .ifEmpty { listOf("@") }

            minLength += if (currentDepth == depthLimit) {
                // If the current depth is the depth limit, return the min length of the paths
                directionalPaths.minOf { it.length }.toLong()

            } else {
                // Otherwise, calculate the min length for the next layer
                directionalPaths.minOf {
                    minLength(
                        Layer(
                            targetKeystrokes = it,
                            currentDepth = currentDepth + 1,
                            depthLimit = depthLimit
                        )
                    )
                }
            }

            // Update the current point for the next iteration
            currentPoint = targetPoint
        }

        // Store the min length in the cache and return it
        cache[layer] = minLength
        return minLength
    }

    // Function to calculate the complexity
    fun calculateComplexity(limit: Int) = input.sumOf { targetKeystrokes ->
        // Calculate the min length of the paths
        val minLength = minLength(
            Layer(
                targetKeystrokes = targetKeystrokes,
                currentDepth = 0,
                depthLimit = limit
            )
        )

        // Get the numeric code from the target keystrokes
        val numericCode = targetKeystrokes.filter { it.isDigit() }.toLong()

        // Return the complexity
        minLength * numericCode
    }

    // Part 1
    calculateComplexity(2).println()

    // Part 2
    calculateComplexity(25).println()
}
