fun main() {
    val grid = readInput("2025/Day04").toGrid().toMutableMap()
    val paperRoll = '@'

    fun canRemove(point: Point): Boolean {
        if (grid[point] != paperRoll) return false
        val neighborPaperRolls = Direction.entries
            .mapNotNull { grid[point.neighbor(it)] }
            .count { it == paperRoll }
        return neighborPaperRolls < 4
    }

    // Part 1
    val part1 = grid.keys.count { canRemove(it) }
    println("Part 1: $part1")

    // Part 2
    var part2 = 0
    while (true) {
        part2 += grid.keys
            .filter { canRemove(it) }
            .ifEmpty { break }
            .onEach { grid[it] = '.' }
            .size
    }
    println("Part 2: $part2")

}