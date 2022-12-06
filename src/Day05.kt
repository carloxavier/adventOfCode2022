fun main() {
    // test if implementation meets criteria from the description sample:
    val testInput = readInput("Day05_test")
    check(day5(testInput) == "CMZ")

    val input = readInput("Day05")
    println(day5(input))
    println(day5(input, shouldReverse = false))
}

private fun day5(input: List<String>, shouldReverse: Boolean = true): String {
    val lineIterator = input.iterator()

    var line = lineIterator.next()
    val lineLength = line.length

    // when there's just 1 column, line length will be 4 chars or fewer,
    // just return the first crane at the top.
    if (lineLength <= 4) {
        return line.secondChar().toString()
    }

    // Init an empty table of empty stacks,
    //  first find number of columns n, knowing each line length is 4n-1 chars
    val numColumns = (line.length + 1) / 4
    val table = MutableList(numColumns) { listOf<Char>() }

    // populate table with initial setup for each stack,
    //  the last line has a number for each column,
    //  we stop reading input when we find that line with a digit at the front.
    while (!line.secondChar().isDigit()) {
        var i = 0
        line.chunked(4).forEach {
            if (it.secondChar().isLetter()) {
                table[i] = table[i] + it.secondChar()
            }
            i++
        }
        line = lineIterator.next()
    }

    // next we have an empty line, we just discard it
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

private fun String.secondChar() = this[1]

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