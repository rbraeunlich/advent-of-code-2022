fun main() {
    fun part1(input: List<String>): Int {
        var maxCalories = 0
        var currentCalories = 0
        input.forEach { line ->
            if(line.isBlank()) {
                if(currentCalories > maxCalories) {
                    println("$currentCalories is more than $maxCalories")
                    maxCalories = currentCalories
                }
                currentCalories = 0
            } else {
                currentCalories += line.toInt()
            }
        }
        return maxCalories
    }

    fun part2(input: List<String>): Int {
        val allCalories = mutableListOf<Int>()
        var currentCalories = 0
        input.forEach { line ->
            if(line.isBlank()) {
                allCalories.add(currentCalories)
                currentCalories = 0
            } else {
                currentCalories += line.toInt()
            }
        }
        allCalories.sort()
        return allCalories.takeLast(3).sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
//    check(part1(testInput) == 1)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
