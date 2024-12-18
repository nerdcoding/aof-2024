package org.nerdcoding.aof2024.day16

import java.io.File
import java.io.PrintStream
import java.util.PriorityQueue
import kotlin.math.abs

private const val INPUT_FILE_LOCATION = "./src/main/resources/day16/input.txt"


fun main() {
    val maze = readInputFile()
    val startPoint = findPointForChar('S', maze)
    val endPoint = findPointForChar('E', maze)

    val result = dijkstraSearch(startPoint, endPoint, maze)
    println("Part 1: $result")
}

private fun dijkstraSearch(start: Point, end: Point, maze: List<String>): Long {
    val visited = HashSet<ReindeerLocation>()
    val scores = HashMap<ReindeerLocation, Long>()
    val queue = PriorityQueue<Pair<ReindeerLocation, Long>>(compareBy { it.second })
    queue.add(ReindeerLocation(start, Direction.EAST, 0) to 0)

    while (queue.isNotEmpty()) {
        val (currentLocation, score) = queue.poll()
        if (currentLocation.position == end) {
            return score
        }

        if (visited.contains(currentLocation)) {
            continue
        }
        visited.add(currentLocation)

        val nextPossibleSteps = currentLocation.findNextPossibleSteps()
        for (nextStep in nextPossibleSteps) {
            if (findCharAtPoint(nextStep.position, maze) == '#' || visited.contains(nextStep)) {
                continue
            }
            val nextScore = score + nextStep.cost
            if (nextScore < scores.getOrDefault(nextStep, Long.MAX_VALUE)) {
                scores[nextStep] = nextScore
                queue.add(nextStep to nextScore)
            }
        }
    }
    return Long.MAX_VALUE
}

private fun findCharAtPoint(point: Point, maze: List<String>): Char =
    maze[point.y][point.x]

private fun findPointForChar(char: Char, maze: List<String>): Point =
    maze.flatMapIndexed { y, row ->
        row.mapIndexed { x, c ->
            if (c == char) Point(x, y) else null
        }
    }.filterNotNull().first()

private fun readInputFile(): List<String> =
    File(INPUT_FILE_LOCATION).readLines().reversed()

private enum class Direction { NORTH, EAST, SOUTH, WEST }
private data class Point(val x: Int, val y: Int)
private data class ReindeerLocation(
        val position: Point,
        val direction: Direction,
        val cost: Int) {

    fun findNextPossibleSteps(): List<ReindeerLocation> =
        when (direction) {
            Direction.NORTH -> listOf<ReindeerLocation>(
                ReindeerLocation(position.copy(), Direction.EAST, 1000),
                ReindeerLocation(position.copy(y = position.y + 1), Direction.NORTH, 1),
                ReindeerLocation(position.copy(), Direction.WEST, 1000)
            )
            Direction.EAST -> listOf<ReindeerLocation>(
                ReindeerLocation(position.copy(), Direction.SOUTH, 1000),
                ReindeerLocation(position.copy(x = position.x + 1), Direction.EAST, 1),
                ReindeerLocation(position.copy(), Direction.NORTH, 1000)
            )
            Direction.SOUTH -> listOf<ReindeerLocation>(
                ReindeerLocation(position.copy(), Direction.WEST, 1000),
                ReindeerLocation(position.copy(y = position.y - 1), Direction.SOUTH, 1),
                ReindeerLocation(position.copy(), Direction.EAST, 1000)
            )
            Direction.WEST -> listOf<ReindeerLocation>(
                ReindeerLocation(position.copy(), Direction.NORTH, 1000),
                ReindeerLocation(position.copy(x = position.x - 1), Direction.WEST, 1),
                ReindeerLocation(position.copy(), Direction.SOUTH, 1000)
            )
        }
}