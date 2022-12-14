fun main() {
    fun parseLine(line: String): RockLines {
        val map = line.split(" -> ")
            .map { pair ->
                val split = pair.split(",")
                split[0].toInt() to split[1].toInt()
            }
        return RockLines(map)
    }

    fun part1(input: List<String>): Int {
        val allRockLines = input.map { line ->
            parseLine(line)
        }
        val minX = allRockLines.minOf { line -> line.fromTo.minOf { it.first } }
        val minY = allRockLines.minOf { line -> line.fromTo.minOf { it.second } }
        val maxX = allRockLines.maxOf { line -> line.fromTo.maxOf { it.first } }
        val maxY = allRockLines.maxOf { line -> line.fromTo.maxOf { it.second } }
        val cave = Cave(minX, minY, maxX, maxY)
        allRockLines.forEach { cave.applyRockLine(it) }

        var sandFellOff = false
        while (!sandFellOff) {
            sandFellOff = cave.applySand()
        }

        return cave.sandCounter
    }

    fun part2(input: List<String>): Int {
        val allRockLines = input.map { line ->
            parseLine(line)
        }
        val minX = allRockLines.minOf { line -> line.fromTo.minOf { it.first } }
        val minY = allRockLines.minOf { line -> line.fromTo.minOf { it.second } }
        val maxX = allRockLines.maxOf { line -> line.fromTo.maxOf { it.first } } + 300
        val maxY = allRockLines.maxOf { line -> line.fromTo.maxOf { it.second } } + 2
        val cave = Cave(minX, minY, maxX, maxY)
        allRockLines.forEach { cave.applyRockLine(it) }
        cave.applyRockLine(RockLines(listOf(0 to maxY, maxX to maxY)))

        var topReached = false
        while (!topReached) {
            topReached = cave.applySandUntilTopReached()
        }

        return cave.sandCounter +1
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test")
    check(part1(testInput) == 24)
//    check(part2(testInput) == 93)

    val input = readInput("Day14")
    println(part1(input))
    println(part2(input))
}

data class RockLines(val fromTo: List<Pair<Int, Int>>)

data class Cave(
    val minX: Int,
    val minY: Int,
    val maxX: Int,
    val maxY: Int,
    val caveContent: MutableList<MutableList<String>> = MutableList(maxY + 1) {
        MutableList(maxX + 1) { _ -> "." }
    },
    var sandCounter: Int = 0
) {

    fun applyRockLine(rockLines: RockLines) {
        var startingPoint = rockLines.fromTo.first()
        for (point in rockLines.fromTo.drop(1)) {
            if (startingPoint.first == point.first) {
                addVerticalRock(startingPoint, point)
            } else {
                addHorizontalRock(startingPoint, point)
            }
            startingPoint = point
        }
    }

    private fun addVerticalRock(from: Pair<Int, Int>, to: Pair<Int, Int>) {
        val start = minOf(from.second, to.second)
        val end = maxOf(from.second, to.second)
        (start..end).forEach { y ->
            caveContent[y][from.first] = "#"
        }
    }

    private fun addHorizontalRock(from: Pair<Int, Int>, to: Pair<Int, Int>) {
        val start = minOf(from.first, to.first)
        val end = maxOf(from.first, to.first)
        (start..end).forEach { x ->
            caveContent[from.second][x] = "#"
        }
    }

    private fun canSandMoveDown(currentSandPosition: Pair<Int, Int>): Boolean {
        val newPosition = currentSandPosition.first to currentSandPosition.second + 1
        val positionContent = caveContent.getOrNull(newPosition.second)?.getOrNull(newPosition.first)
        return positionContent?.let { it == "." } ?: true
    }

    private fun canSandMoveDownAndLeft(currentSandPosition: Pair<Int, Int>): Boolean {
        val newPosition = currentSandPosition.first - 1 to currentSandPosition.second + 1
        val positionContent = caveContent.getOrNull(newPosition.second)?.getOrNull(newPosition.first)
        return positionContent?.let { it == "." } ?: true
    }

    private fun canSandMoveDownAndRight(currentSandPosition: Pair<Int, Int>): Boolean {
        val newPosition = currentSandPosition.first + 1 to currentSandPosition.second + 1
        val positionContent = caveContent.getOrNull(newPosition.second)?.getOrNull(newPosition.first)
        return positionContent?.let { it == "." } ?: true
    }

    private fun isSandOffBounds(currentSandPosition: Pair<Int, Int>): Boolean {
        return currentSandPosition.first > maxX || currentSandPosition.second > maxY
    }

    fun applySand(): Boolean {
//        The sand is pouring into the cave from point 500,0.
        var currentSandPosition = 500 to 0
        while (true) {
            if(isSandOffBounds(currentSandPosition)) {
                return true
            }
            else if (canSandMoveDown(currentSandPosition)) {
                currentSandPosition = currentSandPosition.first to currentSandPosition.second + 1
            } else if (canSandMoveDownAndLeft(currentSandPosition)) {
                currentSandPosition = currentSandPosition.first - 1 to currentSandPosition.second + 1
            } else if (canSandMoveDownAndRight(currentSandPosition)) {
                currentSandPosition = currentSandPosition.first + 1 to currentSandPosition.second + 1
            } else {
                // sand comes to rest
                caveContent[currentSandPosition.second][currentSandPosition.first] = "o"
                sandCounter++
                return false
            }
        }
    }

    fun applySandUntilTopReached(): Boolean {
        var iteration = 0
//        The sand is pouring into the cave from point 500,0.
        var currentSandPosition = 500 to 0
        while (true) {
            iteration++
            if(iteration % 50 == 0) {
                printContents()
            }
            if (canSandMoveDown(currentSandPosition)) {
                currentSandPosition = currentSandPosition.first to currentSandPosition.second + 1
            } else if (canSandMoveDownAndLeft(currentSandPosition)) {
                currentSandPosition = currentSandPosition.first - 1 to currentSandPosition.second + 1
            } else if (canSandMoveDownAndRight(currentSandPosition)) {
                currentSandPosition = currentSandPosition.first + 1 to currentSandPosition.second + 1
            } else if(currentSandPosition == 500 to 0) {
                return true
            }
            else {
                // sand comes to rest
                caveContent[currentSandPosition.second][currentSandPosition.first] = "o"
                sandCounter++
                return false
            }
        }
    }

    fun printContents() {
        caveContent.forEach { row ->
            println(row)
        }
    }
}
