package `2023`

import println
import readInput
import kotlin.math.absoluteValue

fun main() {
    val input = readInput("2023/Day11")

    data class Galaxy(
        var row: Long,
        var col: Long,
    )

    val galaxies = input.flatMapIndexed { rowIndex, row ->
        row.mapIndexedNotNull { colIndex, char ->
            if (char == '#') Galaxy(rowIndex.toLong(), colIndex.toLong()) else null
        }
    }

    val rowAllPeriods = ".".repeat(input[0].length)
    val rowsToExpand = input.indices
        .filter { input[it] == rowAllPeriods } // find rows with only periods
        .sortedDescending() // reverse so we can expand rows without messing up the indexes

    val colsToExpand = input
        .map { row -> row.indices.filter { row[it] == '.' }.toSet() } // find indexes of periods in each row
        .reduce { acc, set -> acc intersect set } // reduce to only the indexes that are in all rows
        .sortedDescending() // reverse so we can expand columns without messing up the indexes

    fun sumGalaxyPairSteps(galaxies: List<Galaxy>, expandBy: Long): Long {
        // copy the list so we don't modify the original
        val expandedGalaxies = galaxies.map { it.copy() }

        // expand rows
        rowsToExpand.forEach { rowToExpand ->
            expandedGalaxies.forEach {
                if (it.row > rowToExpand) it.row += expandBy // only expand rows after the row we're expanding
            }
        }

        // expand columns
        colsToExpand.forEach { colToExpand ->
            expandedGalaxies.forEach {
                if (it.col > colToExpand) it.col += expandBy // only expand columns after the column we're expanding
            }
        }

        // find all pairs of galaxies
        val galaxyPairs = expandedGalaxies.flatMapIndexed { index, galaxyA ->
            expandedGalaxies // only look at galaxies after the current one to avoid duplicates
                .subList(index + 1, expandedGalaxies.size)
                .map { galaxyB -> galaxyA to galaxyB }
        }

        // sum the steps between each pair of galaxies
        return galaxyPairs.sumOf {
            (it.first.row - it.second.row).absoluteValue +
            (it.first.col - it.second.col).absoluteValue
        }
    }

    // Part 1
    sumGalaxyPairSteps(galaxies, 1).println()

    // Part 2
    sumGalaxyPairSteps(galaxies, 999999).println()
}