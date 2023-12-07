fun main() {

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

    fun String.extractLongs() = Regex("""\d+""").findAll(this).map { it.value.toLong() }.toList()

    fun parseMapRange(rangeText: String): MapRange {
        val (destinationRangeStart, sourceRangeStart, rangeLength) = rangeText.extractLongs()
        return MapRange(
            destinationStartInclusive = destinationRangeStart,
            destinationEndInclusive = destinationRangeStart + rangeLength - 1,
            sourceStartInclusive = sourceRangeStart,
            sourceEndInclusive = sourceRangeStart + rangeLength - 1,
        )
    }

    fun parseAlmanacMap(mapText: String): AlmanacMap {
        val parts = mapText.split("\n")
        return AlmanacMap(
            sourceCategory = parts.first().substringBefore("-"),
            destinationCategory = parts.first().substringAfterLast("-").replace(" map:", ""),
            ranges = parts.drop(1).map { parseMapRange(it) }
        )
    }

    val input = readInput("Day05")
    val parts = input.joinToString("\n").split("\n\n")

    val almanacMaps = parts.drop(1).map { parseAlmanacMap(it) }.associateBy { it.sourceCategory }

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

    // Part 1
    parts.first().extractLongs()
        .minOf { findSeedLocation(it) }
        .println()

    // Part 2
    var minLocation = Long.MAX_VALUE
    parts.first().extractLongs()
        .chunked(2)
        .forEach { (seedRangeStart, seedRangeLength) ->
            val seedRange = seedRangeStart.rangeUntil(seedRangeStart + seedRangeLength)
            seedRange.forEach { seed ->
                val location = findSeedLocation(seed)
                if (location < minLocation) {
                    minLocation = location
                }
            }
        }

    minLocation.println()
}