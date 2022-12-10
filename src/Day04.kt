fun main() {
    check(getContainedPairsCount("Day04_test", ::isPairFullyContained) == 2)
    check(getContainedPairsCount("Day04_test", ::isPairOverlapped) == 4)

    // Part1
    println(getContainedPairsCount("Day04", ::isPairFullyContained))

    // Part2
    println(getContainedPairsCount("Day04", ::isPairOverlapped))
}

private fun getContainedPairsCount(fileName: String, comparison: (Int, Int, Int, Int) -> Boolean) = readInput(fileName).filter { line ->
    val (x1, x2, y1, y2) = line.split(",", "-").map { it.toInt() }
    comparison(x1, x2, y1, y2)
}.size

private fun isPairFullyContained(x1: Int, x2:Int, y1: Int, y2: Int) = (x1 in y1..y2 && x2 in y1..y2) || (y1 in x1..x2 && y2 in x1..x2)
private fun isPairOverlapped(x1: Int, x2:Int, y1: Int, y2: Int) = (x1 in y1..y2 || x2 in y1..y2) || (y1 in x1..x2 || y2 in x1..x2)
