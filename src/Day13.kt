import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kotlin.math.max

fun main() {
    val objectMapper = jacksonObjectMapper()
    fun comparePairs(first: List<*>, second: List<*>): Boolean? {
        val size = max(first.size, second.size)
        for (index in 0 until size) {
            val firstValue = first.getOrNull(index)
            val secondValue = second.getOrNull(index)
//            println("Comparing")
//            println(firstValue)
//            println(secondValue)
            // running out of items
            if (firstValue == null && secondValue != null) {
                return true
            }
            if (firstValue != null && secondValue == null) {
                return false
            }
            // int comparision
            if (firstValue is Int && secondValue is Int) {
                if (firstValue < secondValue) {
                    return true
                } else if (firstValue == secondValue) {
                    continue
                } else {
                    return false
                }
            }
            // list comparision
            if (firstValue is List<*> && secondValue is List<*>) {
                val listComparision = comparePairs(firstValue, secondValue)
                if (listComparision == null) {
                    continue
                } else {
                    return listComparision
                }
            }
            // list and int
            if (firstValue is List<*> && secondValue is Int) {
                val listComparision = comparePairs(firstValue, listOf(secondValue))
                if (listComparision == null) {
                    continue
                } else {
                    return listComparision
                }
            }
            if (firstValue is Int && secondValue is List<*>) {
                val listComparision = comparePairs(listOf(firstValue), secondValue)
                if (listComparision == null) {
                    continue
                } else {
                    return listComparision
                }
            }
            return null
        }
        return null
    }

    fun part1(input: List<String>): Int {
        return input.chunked(3).mapIndexed { index, chunk ->
            val first = objectMapper.readValue(chunk[0], object : TypeReference<List<Any>>() {})
            val second = objectMapper.readValue(chunk[1], object : TypeReference<List<Any>>() {})
            val isInOrder = comparePairs(first, second)
            if (isInOrder ?: false) {
                index + 1
            } else {
                0
            }
        }.sum()
    }

    fun sortPairs(first: List<*>, second: List<*>): Int {
        val size = max(first.size, second.size)
        for (index in 0 until size) {
            val firstValue = first.getOrNull(index)
            val secondValue = second.getOrNull(index)
//            println("Comparing")
//            println(firstValue)
//            println(secondValue)
            // running out of items
            if (firstValue == null && secondValue != null) {
                return -1
            }
            if (firstValue != null && secondValue == null) {
                return 1
            }
            // int comparision
            if (firstValue is Int && secondValue is Int) {
                if (firstValue < secondValue) {
                    return -1
                } else if (firstValue == secondValue) {
                    continue
                } else {
                    return 1
                }
            }
            // list comparision
            if (firstValue is List<*> && secondValue is List<*>) {
                val listComparision = sortPairs(firstValue, secondValue)
                if (listComparision == 0) {
                    continue
                } else {
                    return listComparision
                }
            }
            // list and int
            if (firstValue is List<*> && secondValue is Int) {
                val listComparision = sortPairs(firstValue, listOf(secondValue))
                if (listComparision == 0) {
                    continue
                } else {
                    return listComparision
                }
            }
            if (firstValue is Int && secondValue is List<*>) {
                val listComparision = sortPairs(listOf(firstValue), secondValue)
                if (listComparision == 0) {
                    continue
                } else {
                    return listComparision
                }
            }
            return 0
        }
        return 0
    }

    fun part2(input: List<String>): Int {
        val filterNotNull: List<List<Any>> = input.mapNotNull { chunk ->
            if (chunk.trim().isEmpty()) {
                null
            } else {
                objectMapper.readValue(chunk, object : TypeReference<List<Any>>() {})
            }
        }
        val readValue: List<Any> = objectMapper.readValue("[[2]]", object : TypeReference<List<Any>>() {})
        val readValue1: List<Any> = objectMapper.readValue("[[6]]", object : TypeReference<List<Any>>() {})
        val signals: List<List<Any>> = (filterNotNull + readValue + readValue1) as List<List<Any>>

        val sortedSignals = signals.sortedWith{ first, second -> sortPairs(first, second)}
        return (sortedSignals.indexOf(listOf(2)) +1) * (sortedSignals.indexOf(listOf(6)) + 1)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 140)


    val input = readInput("Day13")
    println(part1(input))
    println(part2(input))
}
