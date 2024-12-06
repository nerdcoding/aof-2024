package org.nerdcoding.aof2024.day01

import java.io.File
import kotlin.math.abs

private const val INPUT_FILE_LOCATION = "./src/main/resources/day01/input.txt"

fun main() {
    var leftList = readInputFile(0);
    var rightList = readInputFile(1);

    var distance = 0;
    while (leftList.isNotEmpty() && rightList.isNotEmpty()) {
        val (smallestLeft, leftListWithoutSmallest) = pickSmallestFromList(leftList)
        val (smallestRight, rightListWithoutSmallest) = pickSmallestFromList(rightList)

        distance += abs(smallestLeft - smallestRight);

        leftList = leftListWithoutSmallest
        rightList = rightListWithoutSmallest
    }

    println("Distance $distance")
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

private fun pickSmallestFromList(list: List<Int>): Pair<Int, List<Int>> {
    val smallest = list.min();
    val listWithoutSmallest = list.toMutableList()
        .apply { removeAt(indexOf(smallest)) }
        .toList()

    return Pair(smallest, listWithoutSmallest)
}


