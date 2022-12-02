fun main() {
    fun calculateRound(round: RoundPart1): Int =
        when (round.opponentsTurn) {
            "A" -> if (round.myTurn == "Y") 6 else if (round.myTurn == "X") 3 else 0
            "B" -> if (round.myTurn == "Z") 6 else if (round.myTurn == "Y") 3 else 0
            "C" -> if (round.myTurn == "X") 6 else if (round.myTurn == "Z") 3 else 0
            else -> 0
        }


    fun calculateChoicePoints(round: RoundPart1): Int =
        when (round.myTurn) {
            "X" -> 1
            "Y" -> 2
            "Z" -> 3
            else -> 0
        }

    fun part1(input: List<String>): Int {
        var totalScore = 0
        input.forEach {
            val split = it.split(" ")
            val round = RoundPart1(split[0], split[1])
            val roundPoints = calculateRound(round)
            val choicePoints = calculateChoicePoints(round)
            totalScore += (roundPoints + choicePoints)
        }
        return totalScore
    }

    fun toEnum(s: String): RockPaperScissors =
        when (s) {
            "A" -> RockPaperScissors.ROCK
            "B" -> RockPaperScissors.PAPER
            "C" -> RockPaperScissors.SCISSORS
            else -> throw RuntimeException()
        }

    fun calculateRound(round: RoundPart2): Int =
        when (round.myTurn) {
            "X" -> 0
            "Y" -> 3
            "Z" -> 6
            else -> 0
        }


    fun calculateChoicePoints(round: RoundPart2): Int {
        val choice = when(round.myTurn) {
            "X" -> round.opponentsTurn.winTo()
            "Y" -> round.opponentsTurn.makeDraw()
            "Z" -> round.opponentsTurn.loseTo()
            else -> throw RuntimeException()
        }
        return choice.score
    }

    fun part2(input: List<String>): Int {
        var totalScore = 0
        input.forEach {
            val split = it.split(" ")
            val round = RoundPart2(toEnum(split[0]), split[1])
            val roundPoints = calculateRound(round)
            val choicePoints = calculateChoicePoints(round)
            totalScore += (roundPoints + choicePoints)
        }
        return totalScore
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 15)
    check(part2(testInput) == 12)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}

data class RoundPart1(
    /**
     * A - Rock
     * B - Paper
     * C - Scissors
     */
    val opponentsTurn: String,
    /**
     * X - Rock 1
     * Y - Paper 2
     * Z - Scissors 3
     */
    val myTurn: String
)

data class RoundPart2(
    /**
     * A - Rock
     * B - Paper
     * C - Scissors
     */
    val opponentsTurn: RockPaperScissors,
    /**
     * X - Lose
     * Y - Draw
     * Z - Win
     */
    val myTurn: String
)

enum class RockPaperScissors(val score: Int) {
    ROCK(1), PAPER(2), SCISSORS(3);

    fun makeDraw() =
        when (this) {
            ROCK -> ROCK
            PAPER -> PAPER
            SCISSORS -> SCISSORS
        }

    fun loseTo() =
        when (this) {
            ROCK -> PAPER
            PAPER -> SCISSORS
            SCISSORS -> ROCK
        }

    fun winTo() =
        when (this) {
            ROCK -> SCISSORS
            PAPER -> ROCK
            SCISSORS -> PAPER
        }
}