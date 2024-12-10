fun main() {
    val input = readInput("2024/Day09")

    val diskMap = input[0].toCharArray().map { it.toString().toInt() }

    val disk = buildList {
        var id = 0
        diskMap.forEachIndexed { index, length ->
            repeat(length) { add(if (index % 2 == 0) id else null) }
            if (index % 2 == 0) id++
        }
    }

    fun List<Int?>.prettyPrint() = joinToString("") { it?.toString() ?: "." }.println()
    fun List<Int?>.checkSum() = foldIndexed(0L) { index, acc, id -> acc + (id ?: 0) * index }.println()

    // Part 1
    val diskPart1 = disk.toMutableList()

    var currentIndex = diskPart1.lastIndex
    var nextAvailableIndex = diskPart1.indexOfFirst { it == null }

    while (nextAvailableIndex < currentIndex) {
        // Move the block
        diskPart1[nextAvailableIndex] = diskPart1[currentIndex]
        diskPart1[currentIndex] = null
        currentIndex--
        nextAvailableIndex = diskPart1.indexOfFirst { it == null }
    }

    diskPart1.checkSum()

    // Part 2
    val diskPart2 = disk.toMutableList()

    currentIndex = diskPart2.lastIndex

    while (currentIndex >= 0) {
        if (diskPart2[currentIndex] == null) {
            currentIndex--
            continue
        }

        val fileStart = diskPart2.indexOfFirst { it == diskPart2[currentIndex] }
        val fileSize = currentIndex - fileStart + 1

        // Find the earliest contiguous free space for the file
        var freeStart = -1
        for (i in 0..(fileStart - fileSize)) {
            if ((i until i + fileSize).all { diskPart2[it] == null }) {
                freeStart = i
                break
            }
        }

        if (freeStart != -1) {
            // Move the file
            repeat(fileSize) { offset ->
                diskPart2[freeStart + offset] = diskPart2[fileStart + offset]
                diskPart2[fileStart + offset] = null
            }
        }

        currentIndex -= fileSize
    }

    diskPart2.checkSum()

}
