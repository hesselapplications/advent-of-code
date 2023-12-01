fun main() {
    fun String.concatFirstAndLastCharsToInt() = "${first()}${last()}".toInt()
    val input = readInput("Day01")

    // Part 1
    input.sumOf { line ->
        line.filter { it.isDigit() }.concatFirstAndLastCharsToInt() // extract numeric digits
    }.println()

    // Part 2
    val digits = listOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")
    input.sumOf { line ->
        line.mapIndexedNotNull { charIndex, char -> // Not null ignores any non-digits
            if (char.isDigit()) char.digitToInt() // extract numeric digit

            else digits // extract text digit
                .firstOrNull { digit -> line.startsWith(digit, charIndex) } // check digits starting at the current char
                ?.let { digit -> digits.indexOf(digit) + 1 } // text digit index + 1 gets numeric digit
        }
        .joinToString("").concatFirstAndLastCharsToInt()
    }.println()
}