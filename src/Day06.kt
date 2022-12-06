fun main() {
    fun part1(input: List<String>): Int {
        val buffer = mutableListOf<Char>()
        input.first().forEachIndexed { index, c ->
            if (buffer.size < 4) {
                buffer.add(c)
            } else if (buffer.size == 4 && buffer.allDifferent()) {
                return index
            } else {
                buffer.removeFirst()
                buffer.add(c)
            }
        }
        return -1
    }

    fun part2(input: List<String>): Int {
        val buffer = mutableListOf<Char>()
        input.first().forEachIndexed { index, c ->
            if (buffer.size < 14) {
                buffer.add(c)
            } else if (buffer.size == 14 && buffer.allDifferent()) {
                return index
            } else {
                buffer.removeFirst()
                buffer.add(c)
            }
        }
        return -1
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part1(testInput) == 5)
    check(part2(testInput) == 23)
    val testInput2 = readInput("Day06_test2")
    check(part1(testInput2) == 6)
    check(part2(testInput2) == 23)
    val testInput3 = readInput("Day06_test3")
    check(part1(testInput3) == 10)
    check(part2(testInput3) == 29)
    val testInput4 = readInput("Day06_test4")
    check(part1(testInput4) == 11)
    check(part2(testInput4) == 26)

    val input = readInput("Day06")
    println(part1(input))
    println(part2(input))
}

private fun List<Char>.allDifferent(): Boolean =
    this.size == this.toSet().size

