fun main() {
    fun part1(input: List<String>): Int {
        var overlaps = 0
        input.forEach { line ->
            val pair = line.split(",").let { it[0] to it[1] }
            val firstRange = pair.first.split("-").let {
                it[0].toInt()..it[1].toInt()
            }
            val secondRange = pair.second.split("-").let {
                it[0].toInt()..it[1].toInt()
            }
            println(firstRange)
            println(secondRange)
            if(firstRange.containsAllOf(secondRange) || secondRange.containsAllOf(firstRange)) {
                overlaps++
            }
        }
        return overlaps
    }

    fun part2(input: List<String>): Int {
        var overlapsAtAll = 0
        input.forEach { line ->
            val pair = line.split(",").let { it[0] to it[1] }
            val firstRange = pair.first.split("-").let {
                it[0].toInt()..it[1].toInt()
            }
            val secondRange = pair.second.split("-").let {
                it[0].toInt()..it[1].toInt()
            }
            println(firstRange)
            println(secondRange)
            if(firstRange.containsAnyOf(secondRange) || secondRange.containsAnyOf(firstRange)) {
                overlapsAtAll++
            }
        }
        return overlapsAtAll    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}

private fun IntRange.containsAnyOf(range: IntRange): Boolean =
    this.toList().any { range.contains(it) }

private fun IntRange.containsAllOf(range: IntRange): Boolean =
    this.toList().containsAll(range.toList())

