import java.util.Stack

fun main() {
    fun parseCrates(input: List<String>): List<MutableList<String>> {
        val stacks = mutableListOf<MutableList<String>>()
        for (line in input) {
            if (line.isEmpty()) {
                break;
            } else {
                line.chunked(4).forEachIndexed { index, s ->
                    if (stacks.getOrNull(index) == null) {
                        stacks.add(index, mutableListOf())
                    }
                    if (s.isEmpty() || !s.contains("[")) {
                        // do nothing
                    } else {
                        stacks[index].add(s.replace("[", "").replace("]", "").trim())
                    }
                }
            }
        }
        return stacks
    }

    fun parseMoves(input: List<String>): List<Move> {
        val moves = mutableListOf<Move>()
        var hasSeenEmptyLine = false
        for (line in input) {
            if (line.isEmpty()) {
                hasSeenEmptyLine = true
                continue
            } else if (!hasSeenEmptyLine) {
                continue
            } else {
                val split = line.split(" ")
                val amount = split[1].toInt()
                val from = split[3].toInt()
                val to = split[5].toInt()
                moves.add(Move(amount, from, to))
            }
        }
        return moves
    }

    fun applyMoves(crates: List<MutableList<String>>, moves: List<Move>) {
        moves.forEach { move ->
            (1..move.amount).forEach {
                val source = crates[move.from - 1]
                val destination = crates[move.to - 1]
                destination.add(0, source.removeFirst())
            }
        }
    }

    fun part1(input: List<String>): String {
        val crates: List<MutableList<String>> = parseCrates(input)
        println(crates)
        val moves = parseMoves(input)
        applyMoves(crates, moves)
        return crates.map { it.first() }.joinToString(separator = "")
    }

    fun applyMoves2(crates: List<MutableList<String>>, moves: List<Move>) {
        moves.forEach { move ->
            val source = crates[move.from - 1]
            val destination = crates[move.to - 1]
            val toMove = source.take(move.amount)
            (1..move.amount).forEach { source.removeFirst() }
            destination.addAll(0, toMove)
        }
    }

    fun part2(input: List<String>): String {
        val crates: List<MutableList<String>> = parseCrates(input)
        println(crates)
        val moves = parseMoves(input)
        applyMoves2(crates, moves)
        return crates.map { it.first() }.joinToString(separator = "")
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == "CMZ")
    check(part2(testInput) == "MCD")

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}

data class Move(val amount: Int, val from: Int, val to: Int)
