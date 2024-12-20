import kotlin.math.abs

fun main() {
    // Read the input file
    val grid = readInput("2024/Day20").toGrid()

    // Find the start and end points
    val start = grid.entries.single { it.value == 'S' }.key
    val end = grid.entries.single { it.value == 'E' }.key

    fun numCheats(
        maxCheatDistance: Int,
        minPicoSecondsSaved: Int = 100,
    ) {
        // Find the shortest path from start to end
        val path = shortestPath(
            start = start,
            end = end,
            isValidNextStep = { grid[it] == '.' || it == end },
        )!!

        // Count the number of cheats found
        var numCheats = 0

        // For each point in the path, use it as the start of a potential cheat
        path.forEachIndexed { startIndex, startPoint ->

            // For each point after the start point, use it as the end of a potential cheat
            path.subList(startIndex + 1, path.size).forEachIndexed { endIndex, endPoint ->

                // Calculate the distance between the cheat start and end points
                val cheatDistance = abs(endPoint.x - startPoint.x) + abs(endPoint.y - startPoint.y)

                // Calculate the pico-seconds saved by taking the cheat
                val picoSecondsSaved = endIndex - cheatDistance + 1

                // If the cheat is allowed and saves more than the minimum number of pico-seconds, increment the number of cheats
                if (cheatDistance <= maxCheatDistance && picoSecondsSaved >= minPicoSecondsSaved) {
                    numCheats++
                }
            }
        }

        // Print the number of cheats
        numCheats.println()
    }

    // Part 1
    numCheats(maxCheatDistance = 2)

    // Part 2
    numCheats(maxCheatDistance = 20)
}
