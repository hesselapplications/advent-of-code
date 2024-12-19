
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.File
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

/**
 * A tool for normalizing Advent of Code (AOC) leaderboard scores to focus on a subset of days and participants.
 *
 * By focusing on a subset of the data, this program ensures a normalized comparison by:
 * - Accounting only for specific participants
 * - Focusing on days when everyone started puzzles right after release, removing biases from late starts
 * - Ignoring external influences on the scores, such as the presence of other participants on the leaderboard
 */
fun main() {
    // Read the leaderboard from json
    val leaderboard = ObjectMapper().readTree(
        File("src/main/kotlin/leaderboard.json").readText()
    )

    // Members we are interested in
    val members = leaderboard.get("members").filter {
        it.get("name").textValue() in setOf(
            "Noah Hessel",
            "Whelch",
            "Justin Aronson",
        )
    }

    // Days we are interested in
    val days = (2..20)

    // Store the results for each member, day, level, and completion time
    data class LevelResult(
        val member: String,
        val day: Int,
        val level: Int,
        val completedAt: LocalDateTime?,
        var pointsEarned: Int = 0,
    )

    // Build a list of results
    val results = buildList {
        for (member in members) {
            for (day in days) {
                for (level in 1..2) {
                    add(
                        LevelResult(
                            member = member.get("name").textValue(),
                            day = day,
                            level = level,
                            completedAt = member
                                .get("completion_day_level")
                                .get(day.toString())
                                ?.get(level.toString())
                                ?.get("get_star_ts")
                                ?.longValue()
                                ?.let { Instant.ofEpochSecond(it).atZone(ZoneId.of("America/Chicago")).toLocalDateTime() },
                        )
                    )
                }
            }
        }
    }.sortedWith(
        // Sort the results by day ascending, level ascending, and completion time ascending with nulls last
        compareBy(
            { it.day },
            { it.level },
            { it.completedAt == null },
            { it.completedAt },
        )
    )

    // Calculate the points for each level
    // Chunk the results by the number of members
    // The sorting ensures each chunk will then contain each members time for that particular level, in order of fastest to slowest
    results.chunked(members.size).forEach { chunk ->
        chunk.forEachIndexed { index, result ->
            // For N members, the first member to get each star gets N points, the second gets N-1, and the last gets 1.
            result.pointsEarned = if (result.completedAt != null) {
                members.size - index
            } else {
                0
            }
        }
    }

    // Calculate the total points for each member, and print the results
    results
        .groupBy { it.member }
        .mapValues { (_, memberResults) -> memberResults.sumOf { it.pointsEarned } }
        .entries.sortedByDescending { it.value }
        .forEach { (member, totalPoints) -> println("$member: $totalPoints") }
}