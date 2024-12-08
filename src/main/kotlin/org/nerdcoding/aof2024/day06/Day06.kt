package org.nerdcoding.aof2024.day06

import org.nerdcoding.aof2024.day06.Map.MoveResult
import java.io.File

private const val INPUT_FILE_LOCATION = "./src/main/resources/day06/input.txt"


fun main() {
    solvePart1()
    solvePart2()
}

private fun readInputFile(): Array<CharArray> =
    File(INPUT_FILE_LOCATION)
        .useLines {
            lines -> lines.map {
                singleLine -> singleLine.toCharArray()
            }.toList().toTypedArray()
    }

private fun solvePart1() {
    val map = Map(readInputFile())
    val startingGuardPosition = map.findStartingGuardPosition()

    var moveResult = MoveResult(startingGuardPosition, Map.GuardState.NEW_POSITION)
    while (moveResult.guardState == Map.GuardState.NEW_POSITION) {
        moveResult = map.moveGuard(moveResult.guardPosition)
    }
    println("Visited positions part1: ${map.countVisitedPositions()}")
}

private fun solvePart2() {

    val map = Map(readInputFile())

    var loopCounter = 0
    for (y in 0 until map.getMapSize()) {
        for (x in 0 until map.getMapSize()) {
            val newMap = Map(readInputFile())
            if (newMap.findValueAtPosition(x, y) == '.') {
                newMap.changeValueAtPosition(x, y)
                if (moveGuardInMap(newMap) == Map.GuardState.IN_LOOP) {
                    loopCounter += 1
                }
            }
        }
    }

    println("Loop counter part2: $loopCounter")
}

private fun moveGuardInMap(map: Map): Map.GuardState {
    val startingGuardPosition = map.findStartingGuardPosition()

    var moveResult = MoveResult(startingGuardPosition, Map.GuardState.NEW_POSITION)
    while (moveResult.guardState == Map.GuardState.NEW_POSITION) {
        moveResult = map.moveGuard(moveResult.guardPosition)
    }
    return moveResult.guardState
}
