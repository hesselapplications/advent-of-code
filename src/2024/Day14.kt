fun main() {
    val input = readInput("2024/Day14")

    // Part 1
    val width = 101
    val height = 103

    data class Robot(
        val x: Int,
        val y: Int,
        val vx: Int,
        val vy: Int,
    ) {

        fun move(seconds: Int) = copy(
            x = (x + vx * seconds).mod(width),
            y = (y + vy * seconds).mod(height),
        )

    }

    val regex = Regex("p=(.*),(.*) v=(.*),(.*)")
    val robots = input.map {
        val (x, y, vx, vy) = regex.find(it)!!.destructured
        Robot(x.toInt(), y.toInt(), vx.toInt(), vy.toInt())
    }

    // Part 1
    robots
        .map { it.move(100) }
        .groupingBy { // Group by quadrant
            when {
                it.x < width / 2 && it.y < height / 2 -> '1'
                it.x < width / 2 && it.y > height / 2 -> '2'
                it.x > width / 2 && it.y < height / 2 -> '3'
                it.x > width / 2 && it.y > height / 2 -> '4'
                else -> null
            }
        }
        .eachCount() // Count the number of robots in each quadrant
        .filterKeys { it != null } // Ignore those not in a quadrant (in the middle)
        .map { it.value } // Get the number of robots in each quadrant
        .reduce(Int::times) // Multiply them together
        .println()

    // Part 2
    fun List<Robot>.containsCluster(): Boolean {
        // Get the locations of all robots
        val robotLocations = map { Point(it.x, it.y) }.toSet()

        // Check if any robot is surrounded by other robots (cluster)
        return robotLocations.any { robot ->
            Direction.entries.all { robot.neighbor(it) in robotLocations }
        }
    }

    generateSequence(0) { it + 1 } // Generate an infinite sequence of seconds
        .dropWhile { numSeconds -> !robots.map { it.move(numSeconds) }.containsCluster() } // Iterate until a cluster is found
        .first().println() // Print the number of seconds
}