package org.nerdcoding.aof2024.day11

import java.io.File
import kotlin.math.absoluteValue

private const val INPUT_FILE_LOCATION = "./src/main/resources/day11/input.txt"

private var cache: MutableMap<Pair<Long, Int>, Long> = mutableMapOf()

fun main() {
    val stones = readInputFile()

    println("Result part1: ${blinkStones(stones, 25)}")
    println("Result part2: ${blinkStones(stones, 75)}")
}

private fun readInputFile(): List<Long> =
   File(INPUT_FILE_LOCATION).readText()
       .split("\\s+".toRegex())
       .map { it.toLong() }

private fun blinkStones(stones: List<Long>, depth: Int): Long {
    return stones
        .map { stone -> blinkStone(stone, depth) }
        .sum()
}

private fun blinkStone(stone: Long, depth: Int): Long {
    val result: Long
    if (depth == 0) {
        result = 1
    } else if (cache.containsKey(Pair(stone, depth))) {
        result = cache[Pair(stone, depth)]!!
    } else if (stone == 0L) {
        result = blinkStone(1L, depth-1)
        cache.put(Pair(stone, depth), result)
    } else if (isEvenDigits(stone)) {
        val stoneString = stone.absoluteValue.toString()
        val length = stoneString.length

        result = blinkStone(
            stoneString.substring(0, length / 2).toLong(),
            depth-1
        ) + blinkStone(
                stoneString.substring(length / 2).toLong(),
                depth-1
        )
        cache.put(Pair(stone, depth), result)
    } else {
        result = blinkStone((stone * 2024).toLong(), depth-1)
        cache.put(Pair(stone, depth), result)
    }

    return result
}

private fun isEvenDigits(number: Long): Boolean =
    number.absoluteValue.toString().length % 2 == 0