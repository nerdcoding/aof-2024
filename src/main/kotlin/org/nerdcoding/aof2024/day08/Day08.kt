package org.nerdcoding.aof2024.day08

import java.io.File

private const val INPUT_FILE_LOCATION = "./src/main/resources/day08/input.txt"

val inputLines = File(INPUT_FILE_LOCATION).readLines()
val frequencyPoints = readInputFile()

fun main() {
    val resultPart1 = countAntiNodes(::generateAntiNodesPart1)
    val resultPart2 = countAntiNodes(::generateAntiNodesPart2)

    println("Result part1 $resultPart1")
    println("Result part2 $resultPart2")
}

private fun readInputFile(): Map<Char, List<Pair<Int, Int>>> {
    // Maps all frequencies (Char) to points on the input puzzle (Pair<Int, Int>)
    return inputLines.flatMapIndexed { y, row ->
        row.mapIndexed { x, frequency ->
            if (frequency != '.') {
                frequency to Pair(x, y)
            } else {
                null
            }
        }
    }.filterNotNull()
        .groupBy({ it.first }, { it.second })
}

private fun countAntiNodes(generator: (Pair<Int, Int>, Pair<Int, Int>, Pair<Int, Int>) -> Set<Pair<Int, Int>>): Int =
    frequencyPoints.values.flatMap {
        pointList -> pointList.flatMapIndexed {
            index, point1 -> pointList.drop(index + 1)
                .flatMap {
                    point2 -> generator.invoke(point1, point2, point1 - point2)
                }
        }.filter { it.isOnOriginalGrid() }
    }.toSet().size

private fun generateAntiNodesPart1(
        point1: Pair<Int, Int>,
        point2: Pair<Int, Int>,
        diff: Pair<Int, Int>): Set<Pair<Int, Int>> =
    if (point1.second > point2.second) {
        setOf(point1 - diff, point2 + diff)
    } else {
        setOf(point1 + diff, point2 - diff)
    }

private fun generateAntiNodesPart2(
        point1: Pair<Int, Int>,
        point2: Pair<Int, Int>,
        diff: Pair<Int, Int>): Set<Pair<Int, Int>> =
    generateSequence(point1) { it - diff }
        .takeWhile { it.isOnOriginalGrid() }
        .toSet() + generateSequence(point1) { it + diff }
            .takeWhile { it.isOnOriginalGrid() }
            .toSet()

fun Pair<Int, Int>.isOnOriginalGrid(): Boolean =
    second in inputLines.indices && first in inputLines[second].indices
operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>): Pair<Int, Int> =
    Pair(first + other.first, second + other.second)
operator fun Pair<Int, Int>.minus(other: Pair<Int, Int>): Pair<Int, Int> =
    Pair(first - other.first, second - other.second)