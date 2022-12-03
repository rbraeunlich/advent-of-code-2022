fun main() {
    fun assignPoints(c: Char): Int {
        val lowercase = ('a'..'z').toList()
        val uppercase  =('A'..'Z').toList()
        val i = if (lowercase.contains(c)) {
            lowercase.indexOf(c) + 1
        } else {
            uppercase.indexOf(c) + 27
        }
//        println(i)
        return i
    }

    fun part1(input: List<String>): Int {
        var sum = 0
        for (line in input){
            val firstCompartment = line.take(line.length / 2)
            val secondCompartment = line.takeLast(line.length / 2)
//            println(firstCompartment)
//            println(secondCompartment)
            for (c in firstCompartment) {
                if (secondCompartment.contains(c)) {
                    sum += assignPoints(c)
                    break
                }
            }
        }
        return sum
    }


    fun findBadge(group: List<String>): Char {
        group[0].forEach { c ->
            if(group[1].contains(c) && group[2].contains(c)) {
                return c
            }
        }
        throw RuntimeException()
    }

    fun part2Rec(group: List<String>, remainder: List<String>): Int {
        return if(group.isEmpty()) {
            0
        } else {
            val badge: Char = findBadge(group)
            val point = assignPoints(badge)
            point + part2Rec(remainder.take(3), remainder.drop(3))
        }
    }

    fun part2(input: List<String>): Int {
        return part2Rec(input.take(3), input.drop(3))
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 157)
    check(part2(testInput) == 70)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}
