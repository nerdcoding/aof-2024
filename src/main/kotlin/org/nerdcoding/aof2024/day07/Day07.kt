package org.nerdcoding.aof2024.day07

import java.io.File


private const val INPUT_FILE_LOCATION = "./src/main/resources/day07/input.txt"

enum class Operation {
    ADDITION,
    MULTIPLICATION;

    fun calculate(a: Long, b: Long): Long =
        when (this) {
            ADDITION -> a + b
            MULTIPLICATION -> a * b
        }
}

fun main() {
    val resultPart1 = readInputFile()
        .filter { it.isSolvable(setOf(Operation.ADDITION, Operation.MULTIPLICATION)) }
        .sumOf { it.result }

    println("Part1 sum $resultPart1")
}

private fun readInputFile(): List<Equation> =
    File(INPUT_FILE_LOCATION)
        .useLines {
            lines -> lines.map {
                singleLine -> val items = singleLine.split(":")
                Equation(
                    items[0].trim().toLong(),
                    items[1]
                        .trim()
                        .split("\\s+".toRegex())
                        .map { it.toLong() }
                )
            }.toList()
        }