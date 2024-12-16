package aoc2024

import Direction
import Point
import println
import readInput
import toGrid
import java.util.*

fun main() {
    val grid = readInput("2024/Day16").toGrid()

    // Find start and end points
    val start = grid.filterValues { it == 'S' }.keys.first()
    val end = grid.filterValues { it == 'E' }.keys.first()

    // Holds the score and steps to reach a given point from the start
    data class Path(
        val score: Int,
        val steps: List<Pair<Point, Direction>>, // Last element is the current step
    )

    // Priority queue to explore the best paths first
    val queue = PriorityQueue<Path>(Comparator.comparing { it.score })

    // Start exploration from the start point
    val firstStep = start to Direction.E
    queue.add(Path(
        score = 0,
        steps = listOf(firstStep),
    ))

    // Keep track of the optimal scores and seats at each step
    var lowestEndScore = Int.MAX_VALUE
    val lowestScores = mutableMapOf(firstStep to 0)
    val bestSeats = mutableMapOf(firstStep to setOf(start))

    // Explore the grid
    while (queue.isNotEmpty()) {
        // Get the path to explore, prioritizing the best ones
        val currentPath = queue.remove()

        // Stop exploring if the current path is worse than the best one to reach the end
        if (currentPath.score > lowestEndScore) {
            break
        }

        // Get the current step, point and direction
        val currentStep = currentPath.steps.last()
        val (currentPoint, currentDirection) = currentStep

        // Explore the possible directions
        listOf(Direction.N, Direction.S, Direction.E, Direction.W)
            .filter { it != currentDirection.opposite() } // Avoid going back
            .filter { grid[currentPoint.neighbor(it)] != '#' } // Avoid walls
            .forEach { nextDirection ->
                // Calculate the next point, step and score
                val nextPoint = when (nextDirection) {
                    currentDirection -> currentPoint.neighbor(nextDirection)
                    else -> currentPoint
                }
                val nextStep = nextPoint to nextDirection
                val nextScore = currentPath.score + if (nextDirection == currentDirection) 1 else 1000

                // Create the next path
                val nextPath = Path(
                    score = nextScore,
                    steps = currentPath.steps + nextStep
                )

                // Keep track of the best scores and seats
                val lowestScore = lowestScores.getOrDefault(nextStep, Int.MAX_VALUE)
                if (nextScore < lowestScore) {
                    // New lowest score
                    lowestScores[nextStep] = nextScore
                    bestSeats[nextStep] = bestSeats[currentStep]!! + nextPoint

                    if (nextPoint == end) {
                        // Update the lowest end score if the end is reached
                        lowestEndScore = nextScore

                    } else {
                        // Continue exploring
                        queue.add(nextPath)
                    }

                } else if (nextScore == lowestScore) {
                    // Same score, add the seats from the alternative path
                    bestSeats[nextStep] = bestSeats[nextStep]!! + bestSeats[currentStep]!!
                }
            }
    }

    // Part 1
    lowestEndScore.println()

    // Part 2
    bestSeats.keys
        .filter { it.first == end } // Only consider the end point
        .flatMap { bestSeats[it]!! }.toSet() // Flatten to unique seats
        .size.println() // Print the number of unique seats
}