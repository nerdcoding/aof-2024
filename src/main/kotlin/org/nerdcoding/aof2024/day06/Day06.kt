package org.nerdcoding.aof2024.day06

import java.io.File

private const val INPUT_FILE_LOCATION = "./src/main/resources/day06/input.txt"

private val map = Map(readInputFile())

fun main() {
    val startingGuardPosition = map.findStartingGuardPosition()

    var currentGuardPosition: Map.GuardPosition? = startingGuardPosition
    while (currentGuardPosition != null) {
        currentGuardPosition = map.moveGuard(currentGuardPosition)

    }
    println("Visited positions part1: ${map.countVisitedPositions()}")
}

private fun readInputFile(): Array<CharArray> =
    File(INPUT_FILE_LOCATION)
        .useLines {
            lines -> lines.map {
                singleLine -> singleLine.toCharArray()
            }.toList().toTypedArray()
    }

