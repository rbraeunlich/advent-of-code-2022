fun main() {
    fun parseInput(input: List<String>): ElevationMap {
        val elevationMap = ElevationMap(-1 to -1, -1 to -1, mutableListOf())
        input.forEachIndexed { index, line ->
            elevationMap.grid.add(index, line.toCharArray())
            if (line.contains("S", false)) {
                elevationMap.start = line.indexOf('S', ignoreCase = false) to index
            }
            if (line.contains("E", false)) {
                elevationMap.destination = line.indexOf('E', ignoreCase = false) to index
            }
        }
        return elevationMap
    }

    fun filterNeighboringFieldsThatAreOffMap(
        neighboringFields: List<Pair<Int, Int>>,
        elevationMap: ElevationMap
    ): List<Pair<Int, Int>> {
        return neighboringFields.filterNot {
            it.first < 0 || it.second < 0 || it.first >= elevationMap.gridWidth || it.second >= elevationMap.gridHeight
        }
    }

    fun findShortestPath(elevationMap: ElevationMap): Int {
        fun filterNonReachableFields(
            currentPosition: Pair<Int, Int>,
            neighborignFields: List<Pair<Int, Int>>
        ): List<Pair<Int, Int>> {
            val currentHeight = if (elevationMap.grid[currentPosition.second][currentPosition.first] == 'E') {
                'z'
            } else {
                elevationMap.grid[currentPosition.second][currentPosition.first]
            }
            return neighborignFields.filter { neighbor ->
                val neighborHeight = if (elevationMap.grid[neighbor.second][neighbor.first] == 'S') {
                    'a'
                } else {
                    elevationMap.grid[neighbor.second][neighbor.first]
                }
                kotlin.math.abs(currentHeight - neighborHeight) == 1 || neighborHeight >= currentHeight
            }
        }

        fun findShortestPathRec(
            currentPosition: Pair<Int, Int>,
            takenPath: List<Pair<Int, Int>>,
        ): List<Pair<Int, Int>>? {
            if (elevationMap.visited[currentPosition.second][currentPosition.first] <= takenPath.size){
                return null
            } else {
                elevationMap.visited[currentPosition.second][currentPosition.first] = takenPath.size
            }
            val neighborignFields =
                filterNeighboringFieldsThatAreOffMap(currentPosition.neighboringFields(), elevationMap)
            val reachableFields = filterNonReachableFields(currentPosition, neighborignFields)
//            val uniqueReachableFields = reachableFields - takenPath
            return if (reachableFields.contains(elevationMap.start)) {
                takenPath
            } else if (reachableFields.isEmpty()) {
                null
            } else {
                val mapNotNull = reachableFields.mapNotNull { field ->
                    val possibleShortestPath = findShortestPathRec(field, takenPath + field)
                    possibleShortestPath
                }
                mapNotNull.minByOrNull { it.size }
            }
        }

        val destination = elevationMap.destination

        val findShortestPathRec = findShortestPathRec(destination, listOf(destination))
        return findShortestPathRec?.size ?: Integer.MAX_VALUE
    }

    fun part1(input: List<String>): Int {
        val elevationMap = parseInput(input)
        return findShortestPath(elevationMap)
    }

    fun part2(input: List<String>): Int {
        val elevationMap = parseInput(input)
        val elevationMaps: List<ElevationMap> = elevationMap.grid.flatMapIndexed { y: Int, line ->
            line.mapIndexed{ x,  c ->
                if(c == 'a' || c == 'S') {
                    val anotherElevationMap = parseInput(input)
                    anotherElevationMap.start = x to y
                    anotherElevationMap
                } else {
                    null
                }
            }
        }.filterNotNull()
        return elevationMaps.parallelStream().mapToInt { findShortestPath(it) }.min().orElse(Integer.MAX_VALUE)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
    check(part1(testInput) == 31)
    println("Test 1 geschafft")
    check(part2(testInput) == 29)
    println("Test 2 geschafft")

    val input = readInput("Day12")
    println(part1(input))
    println(part2(input))
}

private fun Pair<Int, Int>.neighboringFields(): List<Pair<Int, Int>> {
    val right = this.first + 1 to this.second
    val left = this.first - 1 to this.second
    val up = this.first to this.second - 1
    val down = this.first to this.second + 1
    return listOf(right, left, up, down)
}

data class ElevationMap(
    var start: Pair<Int, Int>,
    var destination: Pair<Int, Int>,
    val grid: MutableList<CharArray>,
) {

    val gridWidth by lazy { grid[0].size }
    val gridHeight by lazy { grid.size }
    val visited: MutableList<IntArray> by lazy {
        MutableList(grid.size) { IntArray(grid[0].size) { Integer.MAX_VALUE } }
    }
}