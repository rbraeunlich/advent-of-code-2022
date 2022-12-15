import kotlin.math.abs

fun main() {
    fun part1(input: List<String>, lineToCheck: Int): Int {
        val sensorsAndClosestBeacons = input.map { line ->
            val sensorAndBeaconLine = line.replace("Sensor at ", "").replace(" closest beacon is at ", "")
            val sensorLine = sensorAndBeaconLine.split(":")[0]
            val sensorX = sensorLine.split(", ")[0].replace("x=", "").toInt()
            val sensorY = sensorLine.split(", ")[1].replace("y=", "").toInt()
            val beaconLine = sensorAndBeaconLine.split(":")[1]
            val beaconX = beaconLine.split(", ")[0].replace("x=", "").toInt()
            val beaconY = beaconLine.split(", ")[1].replace("y=", "").toInt()
            SensorAndClosestBeacon(sensorX to sensorY, beaconX to beaconY)
        }
        val minX =
            sensorsAndClosestBeacons.flatMap { listOf(it.sensorCoordinates.first, it.beaconCoordinates.first) }.min()
        val minY =
            sensorsAndClosestBeacons.flatMap { listOf(it.sensorCoordinates.second, it.beaconCoordinates.second) }.min()
        val maxX =
            sensorsAndClosestBeacons.flatMap { listOf(it.sensorCoordinates.first, it.beaconCoordinates.first) }.max()
        val maxY =
            sensorsAndClosestBeacons.flatMap { listOf(it.sensorCoordinates.second, it.beaconCoordinates.second) }.max()
        val sensorCave = SensorCave(minX, minY, maxX, maxY)
        sensorsAndClosestBeacons.forEach { sensorCave.add(it) }
//        sensorCave.coverSensorArea(sensorsAndClosestBeacons.first { it.sensorCoordinates == 8 to 7 }, lineToCheck)
        sensorsAndClosestBeacons.forEach {
            sensorCave.coverSensorArea(it, lineToCheck)
//            sensorCave.printContents()
        }
//        sensorCave.printContents()
        val allBeaconsInRow = sensorsAndClosestBeacons.map { it.beaconCoordinates }
            .filter { it.second == lineToCheck }.toSet()
        return (sensorCave.caveContent - allBeaconsInRow)
            .count { it.second == lineToCheck }
    }

    fun part2(input: List<String>, maxCoordinate: Int): Int {
        val sensorsAndClosestBeacons = input.map { line ->
            val sensorAndBeaconLine = line.replace("Sensor at ", "").replace(" closest beacon is at ", "")
            val sensorLine = sensorAndBeaconLine.split(":")[0]
            val sensorX = sensorLine.split(", ")[0].replace("x=", "").toInt()
            val sensorY = sensorLine.split(", ")[1].replace("y=", "").toInt()
            val beaconLine = sensorAndBeaconLine.split(":")[1]
            val beaconX = beaconLine.split(", ")[0].replace("x=", "").toInt()
            val beaconY = beaconLine.split(", ")[1].replace("y=", "").toInt()
            SensorAndClosestBeacon(sensorX to sensorY, beaconX to beaconY)
        }
//        val minX =
//            sensorsAndClosestBeacons.flatMap { listOf(it.sensorCoordinates.first, it.beaconCoordinates.first) }.min()
//        val minY =
//            sensorsAndClosestBeacons.flatMap { listOf(it.sensorCoordinates.second, it.beaconCoordinates.second) }.min()
//        val maxX =
//            sensorsAndClosestBeacons.flatMap { listOf(it.sensorCoordinates.first, it.beaconCoordinates.first) }.max()
//        val maxY =
//            sensorsAndClosestBeacons.flatMap { listOf(it.sensorCoordinates.second, it.beaconCoordinates.second) }.max()
//        val sensorCave = SensorCave(minX, minY, maxX, maxY)
//        sensorsAndClosestBeacons.forEach { sensorCave.add(it) }
//        sensorsAndClosestBeacons.forEach {
//            sensorCave.coverSensorAreaWithinLimit(it, maxCoordinate)
////            sensorCave.printContents()
//        }
//        sensorCave.printContents()
        for (x in (0..maxCoordinate)) {
            for (y in (0..maxCoordinate)) {
                if (sensorsAndClosestBeacons.all { sensorAndBeacon ->
                        manhattanDistrance(
                            x to y,
                            sensorAndBeacon.sensorCoordinates
                        ) > sensorAndBeacon.manhattanDistance
                    }) {
                    return x * 4000000 + y
                }
            }
        }
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15_test")
    check(part1(testInput, 10) == 26)
    check(part2(testInput, 20) == 56000011)
    val input = readInput("Day15")
    println(part1(input, 2000000))
    println(part2(input, 4000000))
}

data class SensorAndClosestBeacon(
    val sensorCoordinates: Pair<Int, Int>,
    val beaconCoordinates: Pair<Int, Int>
) {
    val manhattanDistance: Int =
        manhattanDistrance(sensorCoordinates, beaconCoordinates)
}

fun manhattanDistrance(from: Pair<Int, Int>, to: Pair<Int, Int>) =
    abs(from.first - to.first) + abs(from.second - to.second)

data class SensorCave(
    val minX: Int,
    val minY: Int,
    val maxX: Int,
    val maxY: Int,
    val caveContent: MutableSet<Pair<Int, Int>> = mutableSetOf(),
) {
    fun printContents() {
        (minY..maxY).forEach { y ->
            (minX..maxX).forEach { x ->
                if (caveContent.contains(x to y)) {
                    print("#")
                } else {
                    print(".")
                }
            }
            println()
        }
    }

    fun add(sensorAndClosestBeacon: SensorAndClosestBeacon) {
        caveContent.add(sensorAndClosestBeacon.sensorCoordinates)
        caveContent.add(sensorAndClosestBeacon.beaconCoordinates)
    }

    fun coverSensorArea(sensorAndClosestBeacon: SensorAndClosestBeacon, lineToCheck: Int) {
        val sensorCoordinates = sensorAndClosestBeacon.sensorCoordinates
        val distance = sensorAndClosestBeacon.manhattanDistance
        if (reachesLineToCheck(sensorCoordinates, distance, lineToCheck)) {
            val remainingDistanceInLineToCheck = distance - abs(sensorCoordinates.second - lineToCheck)
            // is below
            val y = lineToCheck
            ((sensorCoordinates.first - remainingDistanceInLineToCheck)..(sensorCoordinates.first + remainingDistanceInLineToCheck)).forEach {
                caveContent.add(it to y)
            }
        }

//        // fill initial row
//        fillRow(sensorCoordinates.second, sensorCoordinates.first, distance, lineToCheck)
//        // afterwards it is always pairwise filled
//        ((distance) downTo 0).forEachIndexed { index, dist ->
//            fillRow(sensorCoordinates.second + index, sensorCoordinates.first, dist, lineToCheck)
//            fillRow(sensorCoordinates.second - index, sensorCoordinates.first, dist, lineToCheck)
//        }
    }

    fun coverSensorAreaWithinLimit(sensorAndClosestBeacon: SensorAndClosestBeacon, limit: Int) {
        val sensorCoordinates = sensorAndClosestBeacon.sensorCoordinates
        val distance = sensorAndClosestBeacon.manhattanDistance
        println(distance)
        // fill initial row
        fillRowWithLimit(sensorCoordinates.second, sensorCoordinates.first, distance, limit)
        // afterwards it is always pairwise filled
        ((distance) downTo 0).forEachIndexed { index, dist ->
            fillRowWithLimit(sensorCoordinates.second + index, sensorCoordinates.first, dist, limit)
            fillRowWithLimit(sensorCoordinates.second - index, sensorCoordinates.first, dist, limit)
        }
    }

    private fun reachesLineToCheck(sensorCoordinates: Pair<Int, Int>, distance: Int, lineToCheck: Int): Boolean =
        // from below
        sensorCoordinates.first - distance < lineToCheck ||
                // from above
                sensorCoordinates.first + distance > lineToCheck


    private fun fillRow(y: Int, x: Int, distance: Int) {
        ((x - distance)..(x + distance)).forEach {
            caveContent.add(it to y)
        }
    }

    private fun fillRowWithLimit(y: Int, x: Int, distance: Int, limit: Int) {
        if (y !in 0..limit) {
            return
        }
        ((x - distance)..(x + distance))
            .filter { x in 0..limit }
            .forEach {
                caveContent.add(it to y)
            }
    }
}
