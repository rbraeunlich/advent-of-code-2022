fun main() {
    fun parseOperation(operationLine: String): (Long) -> Long {
        val operationString = operationLine.trim().replace("Operation: new = old ", "")
        val operand = operationString.split(" ")[0]
        val numberString = operationString.split(" ")[1]
        if (numberString == "old") {
            if (operand == "+") {
                return { worryLevel: Long -> worryLevel + worryLevel }
            } else {
                return { worryLevel: Long -> worryLevel * worryLevel }
            }
        } else {
            val number = numberString.toLong()
            if (operand == "+") {
                return { worryLevel: Long -> worryLevel + number }
            } else {
                return { worryLevel: Long -> worryLevel * number }
            }
        }
    }

    fun parseMonkeys(input: List<String>): List<Monkey> {
        return input.chunked(7).map { monkeyLines ->
            val monkeyNumber = monkeyLines[0].trim().split(" ")[1].replace(":", "").toInt()
            val startingItems = monkeyLines[1].trim().replace("Starting items: ", "").split(", ").map { it.toLong() }
            val operation = parseOperation(monkeyLines[2])
            val testNumber = monkeyLines[3].trim().replace("Test: divisible by ", "").toLong()
            val test = { worryLevel: Long -> worryLevel % testNumber == 0L }
            val ifTrueTarget = monkeyLines[4].trim().replace("If true: throw to monkey ", "").toInt()
            val ifFalseTarget = monkeyLines[5].trim().replace("If false: throw to monkey ", "").toInt()
            Monkey(
                monkeyNumber,
                startingItems.toMutableList(),
                operation,
                test,
                ifTrueTarget,
                ifFalseTarget,
                itemsInspected = 0L,
                ifTrueDivsionNumber = testNumber
            )
        }
    }

    fun part1(input: List<String>): Long {
        val monkeys = parseMonkeys(input)
        repeat(20) {
            monkeys.forEach { monkey ->
                monkey.items.forEach { item ->
                    monkey.itemsInspected++
                    val newWorryLevel = monkey.operation(item) / 3L
                    val test = monkey.test(newWorryLevel)
                    val target = if (test) {
                        monkey.ifTrueTarget
                    } else {
                        monkey.ifFalseTarget
                    }
                    monkeys[target].items.add(newWorryLevel)
                }
                monkey.items.clear()
            }
        }
        return monkeys.map { it.itemsInspected }.sortedDescending().take(2).reduce { a, b -> a * b }
    }

//    fun reduceWorryLevel(worryLevel: Long): Long {
//        var newWorryLevel = worryLevel
//        if(worryLevel % 2 == 0L && (worryLevel / 2) % 2 == 0L) {
//            do {
//                newWorryLevel /= 2
//            } while (newWorryLevel % 2 == 0L && (newWorryLevel / 2) == 0L)
//        }
//        if(worryLevel % 3 == 0L && (worryLevel / 3) % 3 == 0L) {
//            do {
//                newWorryLevel /= 3
//            } while (newWorryLevel % 3 == 0L && (newWorryLevel / 3) == 0L)
//        }
//        if(worryLevel % 5 == 0L && (worryLevel / 5) % 5 == 0L) {
//            do {
//                newWorryLevel /= 5
//            } while (newWorryLevel % 5 == 0L && (newWorryLevel / 5) == 0L)
//        }
//        if(worryLevel % 7 == 0L && (worryLevel / 7) % 7 == 0L) {
//            do {
//                newWorryLevel /= 7
//            } while (newWorryLevel % 7 == 0L && (newWorryLevel / 7) == 0L)
//        }
//        return newWorryLevel
//    }

    fun part2(input: List<String>): Long {
        val monkeys = parseMonkeys(input)
        val commonDivisor = monkeys.map { it.ifTrueDivsionNumber }.reduce { acc, l -> acc * l }
        var round = 0
        repeat(10000) {
            round++
            monkeys.forEach { monkey ->
                monkey.items.forEach { item ->
                    monkey.itemsInspected++
                    var newWorryLevel = monkey.operation(item)
                    val test = monkey.test(newWorryLevel)
                    val target = if (test) {
                        monkey.ifTrueTarget
                    } else {
                        monkey.ifFalseTarget
                    }
                    newWorryLevel = newWorryLevel % commonDivisor
                    monkeys[target].items.add(newWorryLevel)
                }
                monkey.items.clear()
            }
        }
        return monkeys.map { it.itemsInspected }.sortedDescending().take(2).reduce { a, b -> a * b }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    check(part1(testInput) == 10605L)
    println("Check 1 passed")
    check(part2(testInput) == 2713310158L)
    println("Check 2 passed")

    val input = readInput("Day11")
    println(part1(input))
    println(part2(input))
}

data class Monkey(
    val number: Int,
    val items: MutableList<Long>,
    val operation: (Long) -> Long,
    val test: (Long) -> Boolean,
    val ifTrueTarget: Int,
    val ifFalseTarget: Int,
    var itemsInspected: Long = 0L,
    val ifTrueDivsionNumber: Long
)
