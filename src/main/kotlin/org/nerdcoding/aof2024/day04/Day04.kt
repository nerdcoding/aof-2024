package org.nerdcoding.aof2024.day04

import java.io.File

private const val INPUT_FILE_LOCATION = "./src/main/resources/day04/input.txt"

private val wordSearch = WordSearch(readInputFile())

fun main() {
    println("XMAS count part1: ${countXmasPart1()}")
    println("XMAS count part2: ${countXmasPart2()}")
}

private fun readInputFile(): Array<CharArray> =
    File(INPUT_FILE_LOCATION)
        .useLines {
            lines -> lines.map {
                singleLine -> singleLine.toCharArray()
            }.toList().toTypedArray()
    }

private fun countXmasPart1(): Int {
    var xmasCount = 0;
    for (positionOfX in findAllPositionsOfChar('X')) {
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
    return xmasCount
}

private fun countXmasPart2(): Int {
    /*
     1      2      3      4
    M M    M S    S S    S M
     A      A      A      A
    S S    M S    M M    S M

    MMSS   MSMS   SSMM   SMSM
    */

    var xmasCount = 0;
    for (positionOfA in findAllPositionsOfChar('A')) {
        if (isMMSS(positionOfA)) {
            xmasCount++
        }
        if (isMSMS(positionOfA)) {
            xmasCount++
        }
        if (isSSMM(positionOfA)) {
            xmasCount++
        }
        if (isSMSM(positionOfA)) {
            xmasCount++
        }
    }
    return xmasCount
}

private fun findAllPositionsOfChar(char: Char): List<Pair<Int, Int>> {
    val result = mutableListOf<Pair<Int, Int>>()
    for (y in 0 until wordSearch.getSize()) {
        for (x in 0 until wordSearch.getSize()) {
            val point = Pair(x, y)
            if (wordSearch.getCharAtPoint(point) == char) {
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

private fun isMMSS(startPoint: Pair<Int, Int>): Boolean =
    checkSingleUpLeft(startPoint, 'M')
            && checkSingleUpRight(startPoint, 'M')
            && checkSingleDownLeft(startPoint, 'S')
            && checkSingleDownRight(startPoint, 'S')

private fun isMSMS(startPoint: Pair<Int, Int>): Boolean =
    checkSingleUpLeft(startPoint, 'M')
            && checkSingleUpRight(startPoint, 'S')
            && checkSingleDownLeft(startPoint, 'M')
            && checkSingleDownRight(startPoint, 'S')

private fun isSSMM(startPoint: Pair<Int, Int>): Boolean =
    checkSingleUpLeft(startPoint, 'S')
            && checkSingleUpRight(startPoint, 'S')
            && checkSingleDownLeft(startPoint, 'M')
            && checkSingleDownRight(startPoint, 'M')

private fun isSMSM(startPoint: Pair<Int, Int>): Boolean =
    checkSingleUpLeft(startPoint, 'S')
            && checkSingleUpRight(startPoint, 'M')
            && checkSingleDownLeft(startPoint, 'S')
            && checkSingleDownRight(startPoint, 'M')


private fun checkSingleUpRight(startPoint: Pair<Int, Int>, searchChar: Char): Boolean {
    val movedPoint = wordSearch.moveInDirection(
        wordSearch.moveInDirection(startPoint, WordSearch.Direction.UP),
        WordSearch.Direction.RIGHT
    )

    return wordSearch.getCharAtPoint(movedPoint) == searchChar
}

private fun checkSingleUpLeft(startPoint: Pair<Int, Int>, searchChar: Char): Boolean {
    val movedPoint = wordSearch.moveInDirection(
        wordSearch.moveInDirection(startPoint, WordSearch.Direction.UP),
        WordSearch.Direction.LEFT
    )

    return wordSearch.getCharAtPoint(movedPoint) == searchChar
}

private fun checkSingleDownRight(startPoint: Pair<Int, Int>, searchChar: Char): Boolean {
    val movedPoint = wordSearch.moveInDirection(
        wordSearch.moveInDirection(startPoint, WordSearch.Direction.DOWN),
        WordSearch.Direction.RIGHT
    )

    return wordSearch.getCharAtPoint(movedPoint) == searchChar
}

private fun checkSingleDownLeft(startPoint: Pair<Int, Int>, searchChar: Char): Boolean {
    val movedPoint = wordSearch.moveInDirection(
        wordSearch.moveInDirection(startPoint, WordSearch.Direction.DOWN),
        WordSearch.Direction.LEFT
    )

    return wordSearch.getCharAtPoint(movedPoint) == searchChar
}
