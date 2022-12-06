fun main() {
    // test if implementation meets criteria from the description sample:
    val testInput = readInput("Day05_test")
    check(day5(testInput) == "CMZ")

    val input = readInput("Day05")

    // Part 1
    check(day5(input) == "LJSVLTWQM")

    // Part 2
    check(day5(input, shouldReverse = false) == "BRQWDBBJM")
}

private fun day5(input: List<String>, shouldReverse: Boolean = true): String {

    val lineLength = input.first().length

    // when only 1 stack given, line length will be 4 chars or fewer,
    // just return the first crane at the top.
    if (lineLength <= 4) {
        return input.first()[1].toString()
    }

    val lineIterator = input.iterator()
    val table = readInitialTable(lineIterator)

    // next we have an empty line, just discard it
    lineIterator.next()

    // the following lines contain each a crane movement instruction,
    // we read each and execute the movement on our table
    while (lineIterator.hasNext()) {
        val instruction = lineIterator.next()
        val (numCranes, origin, destiny) = readInstruction(instruction)
        moveCrane(table, origin, numCranes, destiny, shouldReverse)
    }

    // print top/first crane in each stack of our table
    return table.map { column ->
        column.first()
    }.joinToString("")
}

private fun readInitialTable(
    lineIterator: Iterator<String>
): MutableList<List<Char>> {
    var line = lineIterator.next()
    val numColumns = (line.length + 1) / 4
    val table = MutableList(numColumns) { listOf<Char>() }

    while (!line[1].isDigit()) {
        var i = 0
        line.chunked(4).forEach {
            if (it[1].isLetter()) {
                table[i] = table[i] + it[1]
            }
            i++
        }
        line = lineIterator.next()
    }

    return table
}

private fun readInstruction(instruction: String) =
    instruction.split(" ")
        .filter { it.first().isDigit() }
        .map { it.toInt() }

private fun moveCrane(
    table: MutableList<List<Char>>,
    origin: Int,
    numCranes: Int,
    destiny: Int,
    shouldReverse: Boolean = true
) {
    val cranesToMove = table[origin - 1].take(numCranes)
    table[origin - 1] = table[origin - 1].drop(numCranes)
    table[destiny - 1] = (if (shouldReverse) cranesToMove.reversed() else cranesToMove) + table[destiny - 1]
}