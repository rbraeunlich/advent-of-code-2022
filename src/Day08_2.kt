import kotlin.math.max

fun main() {
    fun visilityInRow(columnIndex: Int, rowIndex: Int, row: IntArray): Int {
        // check left side
        val left = (columnIndex - 1) downTo 0
        var leftScore = 0
        for (leftIndex in left) {
            if (row[leftIndex] < row[columnIndex]) {
                leftScore++
            } else {
                leftScore++
                break
            }
        }
        val right = (columnIndex + 1)..(row.size - 1)
        var rightScore = 0
        for (rightIndex in right) {
            if (row[rightIndex] < row[columnIndex]) {
                rightScore++
            } else {
                rightScore++
                break
            }
        }
        return leftScore * rightScore
    }

    fun visibilityInColumn(columnIndex: Int, rowIndex: Int, treeGrid: Array<IntArray?>): Int {
        // check top
        val top = (rowIndex - 1) downTo 0
        var topScore = 0
        for (topIndex in top) {
            if (treeGrid[topIndex]!![columnIndex] < treeGrid[rowIndex]!![columnIndex]) {
                topScore++
            } else {
                topScore++
                break
            }
        }
        // check bottom
        val bottom = (rowIndex + 1)..(treeGrid.size - 1)
        var bottomScore = 0
        for (bottomIndex in bottom) {
            if (treeGrid[bottomIndex]!![columnIndex] < treeGrid[rowIndex]!![columnIndex]) {
                bottomScore++
            } else {
                bottomScore++
                break
            }
        }
        return topScore * bottomScore
    }

    fun treeVisibility(columnIndex: Int, rowIndex: Int, tree: Int, rowSize: Int, treeGrid: Array<IntArray?>): Int {
        val rowScore = visilityInRow(columnIndex, rowIndex, treeGrid[rowIndex]!!)
        val columnScore = visibilityInColumn(
            columnIndex,
            rowIndex,
            treeGrid
        )
        val result = rowScore * columnScore
        return result
    }

    fun part2(input: List<String>): Int {
        val treeGrid = arrayOfNulls<IntArray>(input.size)
        input.forEachIndexed { index, row ->
            val currentRow = row.map { it.toString().toInt() }.toIntArray()
            treeGrid[index] = currentRow
        }
        var maxVisibility = 0
        treeGrid.forEachIndexed { rowIndex, treeRow ->
            treeRow!!.forEachIndexed { columnIndex, tree ->
                val treeVisibility: Int = treeVisibility(columnIndex, rowIndex, tree, treeRow.size, treeGrid)
                maxVisibility = max(maxVisibility, treeVisibility)

            }
        }
        return maxVisibility
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    println(part2(testInput))
    check(part2(testInput) == 8)
//    2169 too high
    val input = readInput("Day08")
    println(part2(input))
}
