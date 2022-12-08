fun main() {
    fun isVisibleInRow(columnIndex: Int,rowIndex: Int, row: IntArray): Boolean {
        // check left side
        val left = 0 until columnIndex
        val fromLeft = left.map { leftIndex ->
            if (row[leftIndex] >= row[columnIndex]) {
//                println("Tree ${row[rowIndex]} column $columnIndex and row $rowIndex is not visible in left row")
                false
            } else {
                true
            }
        }.reduce { acc, b -> acc && b }
        // check right side
        val right = (columnIndex + 1) .. (row.size-1)
        val fromRight = right.map { rightIndex ->
            if (row[rightIndex] >= row[columnIndex]) {
//                println("Tree ${row[rowIndex]} column $columnIndex and row $rowIndex is not visible in right row")
                false
            }
            else {
                true
            }
        }.reduce { acc, b -> acc && b }
        return (fromLeft || fromRight)
    }

    fun isVisibleInColumn(columnIndex: Int, rowIndex: Int, treeGrid: Array<IntArray?>): Boolean {
        // check top
        val top = 0 until rowIndex
        val fromTop = top.map { topIndex ->
            if(treeGrid[topIndex]!![columnIndex] >= treeGrid[rowIndex]!![columnIndex]) {
//                println("Tree column $columnIndex and row $rowIndex is not visible in column")
                false
            } else {
                true
            }
        }.reduce { acc, b -> acc && b }
        // check bottom
        val bottom = (rowIndex+1) .. (treeGrid.size-1)
        val fromBottom  = bottom.map { bottomIndex ->
            if(treeGrid[bottomIndex]!![columnIndex] >= treeGrid[rowIndex]!![columnIndex]) {
//                println("Tree column $columnIndex and row $rowIndex is not visible in column")
                false
            } else {
                true
            }
        }.fold(true) { acc, b -> acc && b }
        return (fromTop || fromBottom)
    }

    fun isTreeVisible(columnIndex: Int, rowIndex: Int, tree: Int, rowSize: Int, treeGrid: Array<IntArray?>): Boolean {
        // trees on the edge
        if (columnIndex == 0 || rowIndex == 0) {
            return true
        } else if (columnIndex == (rowSize - 1) || rowIndex == (rowSize - 1)) {
            return true
        } else {
            val visibleInRow = isVisibleInRow(columnIndex, rowIndex, treeGrid[rowIndex]!!)
            val visibleInColumn = isVisibleInColumn(
                columnIndex,
                rowIndex,
                treeGrid
            )
            val result = visibleInRow || visibleInColumn
            return result
        }
    }

    fun part1(input: List<String>): Int {
        val treeGrid = arrayOfNulls<IntArray>(input.size)
        input.forEachIndexed { index, row ->
            val currentRow = row.map { it.toString().toInt() }.toIntArray()
            treeGrid[index] = currentRow
        }
        var visibleTreeCounter = 0
        treeGrid.forEachIndexed { rowIndex, treeRow ->
            treeRow!!.forEachIndexed { columnIndex, tree ->
                if (isTreeVisible(columnIndex, rowIndex, tree, treeRow.size, treeGrid)) {
                    visibleTreeCounter++
                }
            }
        }
        return visibleTreeCounter
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    println(part1(testInput))
    check(part1(testInput) == 21)
    val testInput2 = readInput("Day08_test2")
    check(part1(testInput2) == 43)
    val testInput3 = readInput("Day08_test3")
    check(part1(testInput3) == 40)
    val testInput4 = readInput("Day08_test4")
    check(part1(testInput4) == 49)
//    2169 too high
    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}
