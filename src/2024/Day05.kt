import java.io.File

fun main() {
    // Read the input file, split it into rules and pages
    val (rules, pages) = File("5").readText().split("\n\n")

    // Keep track of the sum of the middle number for both part 1 and part 2
    val answers = mutableListOf(0,0)

    // Split the pages into individual pages and process them
    pages.lines().forEach { page ->

        // Split the page into individual numbers
        val pageNumbers = page.split(",")

        // Sort the page numbers based on the rules
        val sortedPageNumbers = pageNumbers.sortedWith { x, y -> if ("$x|$y" in rules) -1 else 1 }

        // If the page numbers are correctly ordered, add the middle number to part 1 answer, else add to part 2 answer
        val correctlyOrdered = pageNumbers == sortedPageNumbers
        val part = if (correctlyOrdered) 0 else 1
        answers[part] += sortedPageNumbers[sortedPageNumbers.size / 2].toInt()
    }

    println(answers)
}