package `2023`

import println
import readInput

enum class Direction {
    NORTH, SOUTH, EAST, WEST;
}

data class Tile(
    val char: Char,
    val rowIndex: Int,
    val colIndex: Int,
)

data class TileMap(
    private val tiles: List<Tile>
) {
    // Create map of coordinates -> tile, so we can lookup a tile in O(1) time
    private val tileMap = tiles.associateBy { Pair(it.rowIndex, it.colIndex) }

    fun getNeighbor(tile: Tile, direction: Direction): Tile {
        with(tile) {
            val coordinates = when (direction) {
                Direction.NORTH -> Pair(rowIndex - 1, colIndex)
                Direction.SOUTH -> Pair(rowIndex + 1, colIndex)
                Direction.EAST -> Pair(rowIndex, colIndex + 1)
                Direction.WEST -> Pair(rowIndex, colIndex - 1)
            }
            return tileMap[coordinates]!!
        }
    }

}

// Map of both ways each pipe can flow
val flows = mapOf(
    '|' to mapOf(
        Direction.NORTH to Direction.NORTH,
        Direction.SOUTH to Direction.SOUTH,
    ),
    '-' to mapOf(
        Direction.EAST to Direction.EAST,
        Direction.WEST to Direction.WEST,
    ),
    'L' to mapOf(
        Direction.SOUTH to Direction.EAST,
        Direction.WEST to Direction.NORTH,
    ),
    'J' to mapOf(
        Direction.EAST to Direction.NORTH,
        Direction.SOUTH to Direction.WEST,
    ),
    '7' to mapOf(
        Direction.EAST to Direction.SOUTH,
        Direction.NORTH to Direction.WEST,
    ),
    'F' to mapOf(
        Direction.NORTH to Direction.EAST,
        Direction.WEST to Direction.SOUTH,
    ),
)

fun main() {
    val input = readInput("2023/Day10")

    val tiles = input.flatMapIndexed { rowIndex, line ->
        line.mapIndexed { colIndex, char ->
            Tile(
                char = char,
                rowIndex = rowIndex,
                colIndex = colIndex,
            )
        }
    }

    val tileMap = TileMap(tiles)

    // Part 1
    val tileLoop = mutableListOf<Tile>()
    var currentTile = tiles.first { it.char == 'S' } // start at the starting tile of course
    var currentDirection = Direction.EAST // small cheat from looking at the input file
    var returnedToStart = false

    while (!returnedToStart) {
        currentTile = tileMap.getNeighbor(currentTile, currentDirection) // go to next tile
        tileLoop.add(currentTile)
        returnedToStart = currentTile.char == 'S' // check if we've made it back to the start

        // next direction flows out of the current direction
        if (!returnedToStart) currentDirection = flows[currentTile.char]!![currentDirection]!!
    }
    (tileLoop.size / 2).println()

    // Part 2
    val tileLoopCoordinates = tileLoop.map { Pair(it.rowIndex, it.colIndex) }.toSet()
    var insideLoopCount = 0
    input.forEachIndexed { rowIndex, line ->
        var encounteredCount = 0
        line.forEachIndexed { colIndex, char ->
            val coordinates = Pair(rowIndex, colIndex)
            if (coordinates in tileLoopCoordinates) {
                if (char in setOf('|', 'F', '7')) encounteredCount++

            } else if (encounteredCount.rem(2) != 0) {
                insideLoopCount++
            }
        }
    }
    insideLoopCount.println()
}