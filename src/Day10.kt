import java.util.Arrays

fun main() {
    fun part1(input: MutableList<String>): Int {
        var currSignalStrength = 1
        var upcomingToAdd = 0
        var totalSignalStrength = 0
        for (cycle in 1..220) {
            if (cycle in listOf(20, 60, 100, 140, 180, 220)) {
                totalSignalStrength += (currSignalStrength * cycle)
            }
            val currentAction = input[cycle - 1]
            if (currentAction == "noop") {
                currSignalStrength += upcomingToAdd
                upcomingToAdd = 0
                continue
            } else {
                val toAdd = currentAction.split(" ")[1].toInt()
                upcomingToAdd += toAdd
                // add a noop to simulate that adding takes two cycles
                input.add(cycle, "noop")
            }

        }
        return totalSignalStrength
    }

    fun part2(input: MutableList<String>): Array<Array<String>> {
        var registerX = 1
        var upcomingToAdd = 0
        val display = Array<Array<String>>(6) { Array<String>(40) { "." } }
        for (cycle in 1..240) {
            val currentAction = input[cycle - 1]
            if (currentAction == "noop") {
                registerX += upcomingToAdd
                upcomingToAdd = 0

            } else {
                val toAdd = currentAction.split(" ")[1].toInt()
                upcomingToAdd += toAdd
                // add a noop to simulate that adding takes two cycles
                input.add(cycle, "noop")
            }

            val xPosition = cycle % 40
            val yPosition = (cycle / 40) % 40
            if(registerX == xPosition || registerX-1 == xPosition || registerX+1 == xPosition){
                display[yPosition][xPosition] = "#"
            }

        }
        return display
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    check(part1(testInput.toMutableList()) == 13140)

    val input = readInput("Day10")
    println(part1(input.toMutableList()))
//    println(part2(input.toMutableList()).toString())
    for(line in part2(input.toMutableList())) {
        println(Arrays.toString(line))
    }
}
