fun main() {

    fun parseFunnel(line: String): List<List<Int>> {
        var numbers = Regex("""-?\d+""").findAll(line)
            .map { it.value.toInt() }.toList()

        return buildList {
            do {
                add(numbers)
                numbers = numbers.zipWithNext { a, b -> b - a } // differences
            } while (last().any { it != 0 }) // break if we have all zeros
        }
    }

    fun predictNext(funnel: List<List<Int>>): Int {
        return funnel
            .map { it.last() } // get the rightmost values in the funnel
            .reversed() // start at the bottom zero
            .fold(0) { currentPrediction, next -> currentPrediction + next } // build up predication
    }

    fun predictPrevious(funnel: List<List<Int>>): Int {
        return funnel
            .map { it.first() } // get the leftmost values in the funnel
            .reversed() // start at the bottom zero
            .fold(0) { currentPrediction, next -> next - currentPrediction } // build up predication
    }

    val input = readInput("Day09")
    val funnels = input.map { parseFunnel(it) }

    // Part 1
    funnels.sumOf { predictNext(it) }.println()

    // Part 2
    funnels.sumOf { predictPrevious(it) }.println()
}