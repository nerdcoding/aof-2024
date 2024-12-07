package org.nerdcoding.aof2024.day04


class WordSearch(private val wordSearch: Array<CharArray>) {

    init {
        require(wordSearch.isNotEmpty() && wordSearch.all { it.isNotEmpty() }) {
            "wordSearch matrix cannot be empty"
        }
        require(wordSearch.all { it.size == wordSearch[0].size }) {
            "All rows must have the same size"
        }
    }

    enum class Direction {
        UP, DOWN, LEFT, RIGHT
    }

    fun getSize(): Int {
        return wordSearch.size
    }

    fun getCharAtPoint(point: Pair<Int, Int>): Char? {
        if (point.second < 0 || point.second >= wordSearch.size) {
            return null
        }
        if (point.first < 0 || point.first >= wordSearch[point.second].size) {
            return null
        }

        return wordSearch[point.second][point.first]
    }

    fun moveInDirection(point: Pair<Int, Int>, direction: Direction): Pair<Int, Int> =
         when (direction) {
            Direction.UP -> Pair(point.first, point.second - 1)
            Direction.DOWN -> Pair(point.first, point.second + 1)
            Direction.LEFT -> Pair(point.first - 1, point.second)
            Direction.RIGHT -> Pair(point.first + 1, point.second)
        }
}