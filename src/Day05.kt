data class AlmanacMap(
    val sourceCategory: String,
    val destinationCategory: String,
    val ranges: List<MapRange>,
) {
    fun findDestination(source: Long): Long {
        // Find the first range that contains a destination mapping, if none return the source
        return ranges.firstNotNullOfOrNull { it.findDestination(source) } ?: source
    }
}

data class MapRange(
    val destinationStartInclusive: Long,
    val destinationEndInclusive: Long,
    val sourceStartInclusive: Long,
    val sourceEndInclusive: Long,
) {
    fun findDestination(source: Long): Long? {
        if (source < sourceStartInclusive || source > sourceEndInclusive) return null // outside the range
        val offset = source - sourceStartInclusive // find position of the source in the source range
        return destinationStartInclusive + offset // apply that offset to the destination range
    }
}

fun parseAlmanacMap(mapText: String): AlmanacMap {
    val parts = mapText.split("\n")
    return AlmanacMap(
        sourceCategory = parts.first().substringBefore("-"),
        destinationCategory = parts.first().substringAfterLast("-").replace(" map:", ""),
        ranges = parts.drop(1).map { parseMapRange(it) }
    )
}

fun parseMapRange(rangeText: String): MapRange {
    val (destinationRangeStart, sourceRangeStart, rangeLength) = rangeText.extractLongs()
    return MapRange(
        destinationStartInclusive = destinationRangeStart,
        destinationEndInclusive = destinationRangeStart + rangeLength - 1,
        sourceStartInclusive = sourceRangeStart,
        sourceEndInclusive = sourceRangeStart + rangeLength - 1,
    )
}

fun String.extractLongs() = Regex("""\d+""").findAll(this).map { it.value.toLong() }.toList()

fun main() {
    val input = readInput("Day05")
    val parts = input.joinToString("\n").split("\n\n")
    val seeds = parts.first().extractLongs()
    val almanacMaps = parts.drop(1).map { parseAlmanacMap(it) }.associateBy { it.sourceCategory }

    // Part 1
    fun findSeedLocation(seed: Long): Long {
        // start with the seed and seed map
        var source = seed
        var sourceCategory = "seed"

        // loop through each category until we get the location
        while (sourceCategory != "location") {
            val map = almanacMaps[sourceCategory]!!

            // destination becomes our new source
            source = map.findDestination(source)
            sourceCategory = map.destinationCategory
        }

        // our final "source" is the location
        return source
    }

    seeds.minOf { findSeedLocation(it) }.println() // closest location

    // Part 2
}