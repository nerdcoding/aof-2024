package org.nerdcoding.aof2024.day10

import java.io.File
import java.util.*

private const val INPUT_FILE_LOCATION = "./src/main/resources/day10/input.txt"


fun main() {
    val matrix = readInputFile()
    val paths = findPathsBFS(matrix)
    printResult(paths)

    val result = paths.values
        .map { it.size }
        .sumOf { it }
    println("Result part1: $result")
}

private fun readInputFile(): Array<IntArray> =
   File(INPUT_FILE_LOCATION)
        .useLines { lines ->
            lines.map { singleLine ->
                singleLine
                    .map { it.digitToInt() }
                    .toIntArray()
            }.toList().toTypedArray()
        }

private fun findPathsBFS(matrix: Array<IntArray>): Map<Pair<Int, Int>, MutableList<List<Pair<Int, Int>>>> {
    val directions = listOf(Pair(0, 1), Pair(1, 0), Pair(0, -1), Pair(-1, 0)) // allowed movement directions
    // Key: start point
    //      -> MutableList: contains all possible paths from the start point
    //              -> List: contains all points of a specific paths
    val pathsByStartPoint = mutableMapOf<Pair<Int, Int>, MutableList<List<Pair<Int, Int>>>>()
    //val paths = mutableListOf<List<Pair<Int, Int>>>()

    val startPoints = mutableListOf<Pair<Int, Int>>()
    for (i in matrix.indices) {
        for (j in matrix[i].indices) {
            if (matrix[i][j] == 0) startPoints.add(Pair(i, j))
        }
    }

    for (start in startPoints) {
        val queue: Queue<Pair<Pair<Int, Int>, List<Pair<Int, Int>>>> = LinkedList()
        //val visited = mutableSetOf<Pair<Int, Int>>() // already visited points
        pathsByStartPoint[start] = mutableListOf()
        queue.add(Pair(start, listOf(start)))
        //visited.add(start)

        while (queue.isNotEmpty()) {
            val (current, path) = queue.poll()
            val (x, y) = current

            if (matrix[x][y] == 9) {
                pathsByStartPoint[start]!!.add(path)
                continue
            }

            for ((dx, dy) in directions) {
                val nx = x + dx
                val ny = y + dy

                if (nx in matrix.indices && ny in matrix[0].indices) {
                    val currentValue = matrix[x][y]
                    val nextValue = matrix[nx][ny]

                    if (nextValue > currentValue && nextValue - currentValue <= 1 ) {
                        //visited.add(Pair(nx, ny))
                        queue.add(Pair(Pair(nx, ny), path + Pair(nx, ny)))
                    }
                }
            }
        }
    }
    return pathsByStartPoint
}

private fun printResult(paths: Map<Pair<Int, Int>, MutableList<List<Pair<Int, Int>>>>) {
    for ((key, value) in paths) {
        println("Key: $key")
        println("Value:")
        for (list in value) {
            println("  List: $list")
        }
    }
}