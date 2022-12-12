import kotlin.math.absoluteValue

fun main() {
    check(simulateHeadTailMovements(readInput("Day09_test")).size == 13)


    // Part1
    println(simulateHeadTailMovements(readInput("Day09")).size)
}

private fun simulateHeadTailMovements(inputMoves: List<String>): HashSet<Position> {
    val tailPositions = hashSetOf(Position(0, 0))
    var currentHeadPosition = Position(0, 0)
    var currentTailPosition = Position(0, 0)
    inputMoves.forEach {
        val (movement, distance) = it.toMovement()
        repeat(distance) {
            val prevHeadPosition = currentHeadPosition
            currentHeadPosition = currentHeadPosition.move(movement)
            if (currentTailPosition.isFarFrom(currentHeadPosition)) {
                currentTailPosition = prevHeadPosition
                tailPositions.add(currentTailPosition)
            }
        }
    }
    return tailPositions
}

private fun String.toMovement(): Pair<Movement, Int> {
    val (move, distance) = this.split(" ")
    return when (move) {
        "R" -> Pair(Movement.Right, distance.toInt())
        "L" -> Pair(Movement.Left, distance.toInt())
        "D" -> Pair(Movement.Down, distance.toInt())
        "U" -> Pair(Movement.Up, distance.toInt())
        else -> throw Exception()
    }
}

private data class Position(val posX: Int, val posY: Int)


private fun Position.isFarFrom(position: Position): Boolean =
    (this.posX - position.posX).absoluteValue > 1 || (this.posY - position.posY).absoluteValue > 1

private fun Position.move(move: Movement): Position = when (move) {
    Movement.Right -> Position(posX + 1, posY)
    Movement.Down -> Position(posX, posY - 1)
    Movement.Left -> Position(posX - 1, posY)
    Movement.Up -> Position(posX, posY + 1)
}

private enum class Movement {
    Left, Right, Down, Up,
}