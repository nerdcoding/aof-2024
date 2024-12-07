package org.nerdcoding.aof2024.day03

import java.io.File

private const val INPUT_FILE_LOCATION = "./src/main/resources/day03/input.txt"
private val REGEX = Regex("""mul\((\d{1,3}),(\d{1,3})\)""")

fun main() {
    val multiplication = readMultiplicationsFromInputFile();
    val part1Result = calculateResultPart1(multiplication)

    println("Part 1 result: $part1Result")
    //println("Part 2 safe reports: ")
}

private fun readMultiplicationsFromInputFile(): List<Pair<Int, Int>> = File(INPUT_FILE_LOCATION).useLines {
    lines -> lines.flatMap {
        singleLine -> REGEX.findAll(singleLine)
            .map { match ->
                val x = match.groupValues[1].toInt()
                val y = match.groupValues[2].toInt()
                Pair(x,y)
            }
    }.toList()
}

private fun calculateResultPart1(multiplications: List<Pair<Int, Int>>): Int {
    return multiplications
        .sumOf { (x, y) -> x * y }
}

