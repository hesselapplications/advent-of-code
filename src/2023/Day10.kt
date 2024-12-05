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
                Direction.N -> Pair(rowIndex - 1, colIndex)
                Direction.S -> Pair(rowIndex + 1, colIndex)
                Direction.E -> Pair(rowIndex, colIndex + 1)
                Direction.W -> Pair(rowIndex, colIndex - 1)
                else -> throw IllegalArgumentException("Invalid direction")
            }
            return tileMap[coordinates]!!
        }
    }

}

// Map of both ways each pipe can flow
val flows = mapOf(
    '|' to mapOf(
        Direction.N to Direction.N,
        Direction.S to Direction.S,
    ),
    '-' to mapOf(
        Direction.E to Direction.E,
        Direction.W to Direction.W,
    ),
    'L' to mapOf(
        Direction.S to Direction.E,
        Direction.W to Direction.N,
    ),
    'J' to mapOf(
        Direction.E to Direction.N,
        Direction.S to Direction.W,
    ),
    '7' to mapOf(
        Direction.E to Direction.S,
        Direction.N to Direction.W,
    ),
    'F' to mapOf(
        Direction.N to Direction.E,
        Direction.W to Direction.S,
    ),
)

fun main() {
    val input = readInput("Day10")

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
    var currentDirection = Direction.E // small cheat from looking at the input file
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