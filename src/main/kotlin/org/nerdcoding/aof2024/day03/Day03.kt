package org.nerdcoding.aof2024.day03

import java.io.File

private const val INPUT_FILE_LOCATION = "./src/main/resources/day03/input.txt"
private val FIND_MUL_REGEX = Regex("""mul\((\d{1,3}),(\d{1,3})\)""")
private val DELETE_DONT_REGEX = Regex("""don't\(\).*?(?:do\(\)|$)""")

fun main() {

    val inputFile = readFileAsString()
    val multiplicationsPart1 = searchMultiplications(inputFile)
    val part1Result = calculateResult(multiplicationsPart1)
    println("Part 1 result: $part1Result")

    val multiplicationsPart2 = searchMultiplications(
        removeDonts(inputFile)
    )
    val part2Result = calculateResult(multiplicationsPart2)
    println("Part 2 result: $part2Result")
}

private fun readFileAsString(): String =
    File(INPUT_FILE_LOCATION).readText()

private fun searchMultiplications(input: String): List<Pair<Int, Int>> =
    FIND_MUL_REGEX.findAll(input)
        .map { match ->
            val x = match.groupValues[1].toInt()
            val y = match.groupValues[2].toInt()
            Pair(x,y)
        }.toList()

private fun calculateResult(multiplications: List<Pair<Int, Int>>): Int =
     multiplications
        .sumOf { (x, y) -> x * y }

private fun removeDonts(input: String): String =
    input
        .replace("\n", "|") // replace newlines with a separator
        .replace(DELETE_DONT_REGEX, "") // remove everything after 'don't' until to the next 'do'

