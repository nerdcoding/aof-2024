package org.nerdcoding.aof2024.day01

import java.io.File
import kotlin.math.abs

private const val INPUT_FILE_LOCATION = "./src/main/resources/day01/input.txt"

fun main() {
    val leftList = readInputFile(0);
    val rightList = readInputFile(1);

    val distance = calculatePart1Distance(leftList, rightList)
    val similarityScore = calculatePart2SimilarityScore(leftList, rightList)

    println("Part 1 distance $distance")
    println("Part 2 similarityScore $similarityScore")
}

private fun readInputFile(readColumnNumber: Int): List<Int> = File(INPUT_FILE_LOCATION).useLines {
        lines -> lines.map {
            singleLine -> val (first, second) = singleLine.split("\\s+".toRegex())
                .map { it.toInt() }
            when (readColumnNumber) {
                0 -> first
                1 -> second
                else -> throw IllegalArgumentException("Invalid column number")
            }
        }.toList()
}

private fun calculatePart1Distance(leftList: List<Int>, rightList: List<Int>): Int {
    var leftListCopy = leftList.map { it }
    var rightListCopy = rightList.map { it }

    var distance = 0
    while (leftListCopy.isNotEmpty() && rightListCopy.isNotEmpty()) {
        val (smallestLeft, leftListWithoutSmallest) = pickSmallestFromList(leftListCopy)
        val (smallestRight, rightListWithoutSmallest) = pickSmallestFromList(rightListCopy)

        distance += abs(smallestLeft - smallestRight);

        leftListCopy = leftListWithoutSmallest
        rightListCopy = rightListWithoutSmallest
    }
    return distance;
}

private fun pickSmallestFromList(list: List<Int>): Pair<Int, List<Int>> {
    val smallest = list.min();
    val listWithoutSmallest = list.toMutableList()
        .apply { removeAt(indexOf(smallest)) }
        .toList()

    return Pair(smallest, listWithoutSmallest)
}

private fun calculatePart2SimilarityScore(leftList: List<Int>, rightList: List<Int>): Int {
    var similarityScore = 0
    leftList.map {
        left -> val count = rightList.count { it == left}
        similarityScore += (left * count)
    }

    return similarityScore;
}

