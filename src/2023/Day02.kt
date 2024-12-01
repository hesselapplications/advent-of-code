fun main() {

    data class Cubes(
        val red: Int,
        val green: Int,
        val blue: Int,
    )

    data class Game(
        val id: Int,
        val draws: List<Cubes>
    )

    fun parseCube(cubes: List<String>, color: String): Int {
        val cubeText = cubes.firstOrNull { it.contains(color) } ?: return 0
        return cubeText.replace(color, "").trim().toInt()
    }

    fun parseDraw(text: String): Cubes {
        val cubes = text.split(",")
        return Cubes(
            red = parseCube(cubes, "red"),
            green = parseCube(cubes, "green"),
            blue = parseCube(cubes, "blue"),
        )
    }

    fun parseGame(text: String): Game {
        val parts = text.split(":")
        return Game(
            id = parts.first().substringAfter("Game ").toInt(),
            draws = parts.last().split(";").map { parseDraw(it) }
        )
    }

    val input = readInput("2023/Day02")
    val games = input.map { parseGame(it) }

    // Part 1
    games.filter { game ->
        game.draws.none { it.red > 12 || it.green > 13 || it.blue > 14 }
    }.sumOf { it.id }.println()

    // Part 2
    fun Game.getMinCubes() = Cubes(
        red = draws.maxOf { it.red },
        green = draws.maxOf { it.green },
        blue = draws.maxOf { it.blue },
    )
    fun Cubes.getPowerOfCubes() = red * green * blue
    games.sumOf { it.getMinCubes().getPowerOfCubes() }.println()
}