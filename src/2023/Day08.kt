package `2023`

import println
import readInput

fun main() {
    val input = readInput("2023/Day08").joinToString("\n")

    val parts = input.split("\n\n")
    val directions = parts[0].toList()

    val nodeRegex = Regex("""(\w+) = \((\w+), (\w+)\)""")
    val nodeMap = parts[1].split("\n")
        .mapNotNull { nodeRegex.find(it)?.destructured }
        .associate { (id, leftId, rightId) -> id to Pair(leftId, rightId) }

    fun countSteps(nodeId: String, isEndingNode: (String) -> Boolean): Long {
        var currentStep = 0L
        var currentNodeId = nodeId
        while (!isEndingNode(currentNodeId)) {
            val directionIndex = (currentStep % directions.size).toInt()
            val direction = directions[directionIndex]
            val (leftId, rightId) = nodeMap[currentNodeId]!!
            currentNodeId = if (direction == 'L') leftId else rightId
            currentStep++
        }
        return currentStep
    }

    // Part 1
    nodeMap.keys
        .first { it == "AAA" }
        .let { countSteps(it) { id -> id == "ZZZ" } }
        .println()

    // Part 2
    fun gcd(a: Long, b: Long): Long {
        return if (b == 0L) a else gcd(b, a % b)
    }

    nodeMap.keys
        .filter { it.endsWith("A") }
        .map { countSteps(it) { id -> id.endsWith("Z") } }
        .reduce { acc, i -> acc * i / gcd(acc, i) }
        .println()
}