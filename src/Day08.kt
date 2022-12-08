fun main() {
    check(
        ForestRanger(readInput("Day08_test"))
            .getTreesVisibleFromOutside()
            .size == 21
    )
    check(
        ForestRanger(readInput("Day08_test"))
            .getMaxVisibilityIndex() == 8
    )

    // Part1
    println(
        ForestRanger(readInput("Day08"))
            .getTreesVisibleFromOutside()
            .size
    )


    // Part2
    println(
        ForestRanger(readInput("Day08"))
            .getMaxVisibilityIndex()
    )
}

class ForestRanger(gridInput: List<String>) {
    private var treeGrid: List<List<Tree>> = gridInput.mapIndexed { posX, row ->
        row.mapIndexed { posY, height ->
            Tree(posX, posY, height.digitToInt())
        }
    }

    private val visibleTrees = hashSetOf<Tree>()
    fun getTreesVisibleFromOutside(): Set<Tree> {
        checkHorizontalVisibilities()
        checkVerticalVisibilities()
        return visibleTrees
    }

    fun getMaxVisibilityIndex(): Int {
        var maxVisibility = Int.MIN_VALUE
        for (i in treeGrid.indices) {
            for (j in treeGrid.indices) {
                val visibilityIndex = getVisibilityIndex(i, j)
                if (visibilityIndex > maxVisibility) {
                    maxVisibility = visibilityIndex
                }
            }
        }
        return maxVisibility
    }

    private fun getVisibilityIndex(posX: Int, posY: Int): Int {
        if (posX == 0 || posY == 0 || posX == treeGrid.size - 1 || posY == treeGrid.size - 1) {
            return 0
        }
        val tree = treeGrid[posX][posY]
        var upIndex = 0
        for (i in posX - 1 downTo 0) {
            upIndex++
            if (treeGrid[i][posY].height >= tree.height) {
                break
            }
        }
        var downIndex = 0
        for (i in posX + 1 until treeGrid.size) {
            downIndex++
            if (treeGrid[i][posY].height >= tree.height) {
                break
            }
        }
        var leftIndex = 0
        for (i in posY - 1 downTo 0) {
            leftIndex++
            if (treeGrid[posX][i].height >= tree.height) {
                break
            }
        }
        var rightIndex = 0
        for (i in posY + 1 until treeGrid.size) {
            rightIndex++
            if (treeGrid[posX][i].height >= tree.height) {
                break
            }
        }

        return upIndex * downIndex * leftIndex * rightIndex
    }

    private fun checkVerticalVisibilities() {
        for (i in treeGrid.indices) {
            var biggestHeight = Int.MIN_VALUE
            // Check visibility from the top
            for (j in 0 until treeGrid.first().size) {
                val tree = treeGrid[j][i]
                if (tree.height > biggestHeight) {
                    biggestHeight = tree.height
                    visibleTrees.add(tree)
                }
            }
            biggestHeight = Int.MIN_VALUE
            // Check visibility from the bottom
            for (j in (0 until treeGrid.first().size).reversed()) {
                val tree = treeGrid[j][i]
                if (tree.height > biggestHeight) {
                    biggestHeight = tree.height
                    visibleTrees.add(tree)
                }
            }
        }
    }

    private fun checkHorizontalVisibilities() {
        treeGrid.forEach { row ->
            var biggestHeight = Int.MIN_VALUE
            // check visibility from the left
            row.forEach { tree ->
                if (tree.height > biggestHeight) {
                    biggestHeight = tree.height
                    visibleTrees.add(tree)
                }
            }
            biggestHeight = Int.MIN_VALUE
            // check visibility from the right
            row.reversed().forEach { tree ->
                if (tree.height > biggestHeight) {
                    biggestHeight = tree.height
                    visibleTrees.add(tree)
                }
            }
        }
    }
}

data class Tree(val posX: Int, val posY: Int, val height: Int)