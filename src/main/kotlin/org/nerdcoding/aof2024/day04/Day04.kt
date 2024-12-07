package org.nerdcoding.aof2024.day04

import java.io.File

private const val INPUT_FILE_LOCATION = "./src/main/resources/day04/input.txt"

private val wordSearch = WordSearch(readInputFile())

fun main() {

    val allPositionsOfX = findAllPositionsOfX()
    var xmasCount = 0;
    for (positionOfX in allPositionsOfX) {
        if (checkRight(positionOfX, 'M')) {
            xmasCount++
        }
        if (checkLeft(positionOfX, 'M')) {
            xmasCount++
        }
        if (checkUp(positionOfX, 'M')) {
            xmasCount++
        }
        if (checkDown(positionOfX, 'M')) {
            xmasCount++
        }
        if (checkUpRight(positionOfX, 'M')) {
            xmasCount++
        }
        if (checkUpLeft(positionOfX, 'M')) {
            xmasCount++
        }
        if (checkDownRight(positionOfX, 'M')) {
            xmasCount++
        }
        if (checkDownLeft(positionOfX, 'M')) {
            xmasCount++
        }
    }

    println("XMAS count: $xmasCount")
}

private fun readInputFile(): Array<CharArray> =
    File(INPUT_FILE_LOCATION)
        .useLines {
            lines -> lines.map {
                singleLine -> singleLine.toCharArray()
            }.toList().toTypedArray()
    }

private fun findAllPositionsOfX(): List<Pair<Int, Int>> {
    val result = mutableListOf<Pair<Int, Int>>()
    for (y in 0 until wordSearch.getSize()) {
        for (x in 0 until wordSearch.getSize()) {
            val point = Pair(x, y)
            if (wordSearch.getCharAtPoint(point) == 'X') {
                result.add(point)
            }
        }
    }

    return result;
}

private fun checkRight(startPoint: Pair<Int, Int>, searchChar: Char): Boolean {
    val movedPoint = wordSearch.moveInDirection(startPoint, WordSearch.Direction.RIGHT)
    if (wordSearch.getCharAtPoint(movedPoint) == searchChar) {
        return findNextChar(searchChar)?.let {
            checkRight(movedPoint, it)
        } ?: true
    }

    return false;
}

private fun checkLeft(startPoint: Pair<Int, Int>, searchChar: Char): Boolean {
    val movedPoint = wordSearch.moveInDirection(startPoint, WordSearch.Direction.LEFT)
    if (wordSearch.getCharAtPoint(movedPoint) == searchChar) {
        return findNextChar(searchChar)?.let {
            checkLeft(movedPoint, it)
        } ?: true
    }

    return false;
}

private fun checkUp(startPoint: Pair<Int, Int>, searchChar: Char): Boolean {
    val movedPoint = wordSearch.moveInDirection(startPoint, WordSearch.Direction.UP)
    if (wordSearch.getCharAtPoint(movedPoint) == searchChar) {
        return findNextChar(searchChar)?.let {
            checkUp(movedPoint, it)
        } ?: true
    }

    return false;
}

private fun checkDown(startPoint: Pair<Int, Int>, searchChar: Char): Boolean {
    val movedPoint = wordSearch.moveInDirection(startPoint, WordSearch.Direction.DOWN)
    if (wordSearch.getCharAtPoint(movedPoint) == searchChar) {
        return findNextChar(searchChar)?.let {
            checkDown(movedPoint, it)
        } ?: true
    }

    return false;
}

private fun checkUpRight(startPoint: Pair<Int, Int>, searchChar: Char): Boolean {
    val movedPoint = wordSearch.moveInDirection(
        wordSearch.moveInDirection(startPoint, WordSearch.Direction.UP),
        WordSearch.Direction.RIGHT
    )

    if (wordSearch.getCharAtPoint(movedPoint) == searchChar) {
        return findNextChar(searchChar)?.let {
            checkUpRight(movedPoint, it)
        } ?: true
    }

    return false;
}

private fun checkUpLeft(startPoint: Pair<Int, Int>, searchChar: Char): Boolean {
    val movedPoint = wordSearch.moveInDirection(
        wordSearch.moveInDirection(startPoint, WordSearch.Direction.UP),
        WordSearch.Direction.LEFT
    )

    if (wordSearch.getCharAtPoint(movedPoint) == searchChar) {
        return findNextChar(searchChar)?.let {
            checkUpLeft(movedPoint, it)
        } ?: true
    }

    return false;
}

private fun checkDownRight(startPoint: Pair<Int, Int>, searchChar: Char): Boolean {
    val movedPoint = wordSearch.moveInDirection(
        wordSearch.moveInDirection(startPoint, WordSearch.Direction.DOWN),
        WordSearch.Direction.RIGHT
    )

    if (wordSearch.getCharAtPoint(movedPoint) == searchChar) {
        return findNextChar(searchChar)?.let {
            checkDownRight(movedPoint, it)
        } ?: true
    }

    return false;
}

private fun checkDownLeft(startPoint: Pair<Int, Int>, searchChar: Char): Boolean {
    val movedPoint = wordSearch.moveInDirection(
        wordSearch.moveInDirection(startPoint, WordSearch.Direction.DOWN),
        WordSearch.Direction.LEFT
    )

    if (wordSearch.getCharAtPoint(movedPoint) == searchChar) {
        return findNextChar(searchChar)?.let {
            checkDownLeft(movedPoint, it)
        } ?: true
    }

    return false;
}


private fun findNextChar(searchChar: Char): Char? =
    when (searchChar) {
        'X' -> 'M'
        'M' -> 'A'
        'A' -> 'S'
        else -> null
    }