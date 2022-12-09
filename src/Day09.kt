fun main() {
    fun parseMoves(input: List<String>): List<HeadMove> {
        return input.map {
            val split = it.split(" ")
            HeadMove(Direction.valueOf(split[0]), split[1].toInt())
        }
    }

    fun part1(input: List<String>): Int {
        val moves = parseMoves(input)
        val head = BridgeHead(0 to 0)
        val tail = BridgeTail(0 to 0, mutableSetOf(0 to 0))
        moves.forEach { move ->
            for (i in 0 until move.steps) {
                head.applyMove(move.direction)
                tail.followHead(head.currentPosition)
            }
        }
        return tail.visitedPositions.size
    }

    fun part2(input: List<String>): Int {
        val moves = parseMoves(input)
        val head = BridgeHead(0 to 0)
        val knots = (1 until 9).map { BridgeTail(0 to 0, mutableSetOf(0 to 0)) }
        val tail = BridgeTail(0 to 0, mutableSetOf(0 to 0))
        moves.forEach { move ->
            for (i in 0 until move.steps) {
                head.applyMove(move.direction)
                knots.first().followHead(head.currentPosition)
                knots.forEachIndexed { index, knot ->
                    if(index != 0) {
                        knots[index].followHead(knots[index - 1].currentPosition)
                    }
                }
                tail.followHead(knots.last().currentPosition)
            }
        }
        return tail.visitedPositions.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 1)
    val testInput2 = readInput("Day09_test2")
    check(part2(testInput2) == 36)

    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))
}

data class BridgeHead(var currentPosition: Pair<Int, Int>) {
    fun applyMove(direction: Direction) {
        currentPosition = when (direction) {
            Direction.R -> (currentPosition.first + 1) to currentPosition.second
            Direction.L -> (currentPosition.first - 1) to currentPosition.second
            Direction.U -> currentPosition.first to (currentPosition.second + 1)
            Direction.D -> currentPosition.first to (currentPosition.second - 1)
        }
    }

}

data class BridgeTail(var currentPosition: Pair<Int, Int>, val visitedPositions: MutableSet<Pair<Int, Int>>) {
    fun followHead(headPosition: Pair<Int, Int>) {
        if (isAdjacentToHead(headPosition)) {
            // do nothing
        } else {
            // same row
            if (currentPosition.first == headPosition.first) {
                // update y
                val moveUp = headPosition.second > currentPosition.second
                if (moveUp) {
                    currentPosition = currentPosition.first to (currentPosition.second + 1)
                } else {
                    currentPosition = currentPosition.first to (currentPosition.second - 1)
                }
            } // same column
            else if (currentPosition.second == headPosition.second) {
                // update x
                val moveRight = headPosition.first > currentPosition.first
                if (moveRight) {
                    currentPosition = (currentPosition.first + 1) to (currentPosition.second)
                } else {
                    currentPosition = (currentPosition.first - 1) to (currentPosition.second)
                }
            } else {
                // diagonally different
                val moveUp = headPosition.second > currentPosition.second
                if (moveUp) {
                    currentPosition = currentPosition.first to (currentPosition.second + 1)
                } else {
                    currentPosition = currentPosition.first to (currentPosition.second - 1)
                }
                val moveRight = headPosition.first > currentPosition.first
                if (moveRight) {
                    currentPosition = (currentPosition.first + 1) to (currentPosition.second)
                } else {
                    currentPosition = (currentPosition.first - 1) to (currentPosition.second)
                }
            }
            visitedPositions.add(currentPosition)
        }
    }

    private fun isAdjacentToHead(headPosition: Pair<Int, Int>): Boolean {
        val isCovered = headPosition == currentPosition
        val topLeft = (currentPosition.first - 1) to (currentPosition.second + 1)
        val top = currentPosition.first to (currentPosition.second + 1)
        val topRight = (currentPosition.first + 1) to (currentPosition.second + 1)
        val left = (currentPosition.first - 1) to currentPosition.second
        val right = (currentPosition.first + 1) to currentPosition.second
        val bottomLeft = (currentPosition.first - 1) to (currentPosition.second - 1)
        val bottom = currentPosition.first to (currentPosition.second - 1)
        val bottomRight = (currentPosition.first + 1) to (currentPosition.second - 1)

        return isCovered || setOf(topLeft, top, topRight, left, right, bottomLeft, bottom, bottomRight).contains(
            headPosition
        )
    }

}

data class HeadMove(val direction: Direction, val steps: Int)

enum class Direction {
    R, L, U, D
}