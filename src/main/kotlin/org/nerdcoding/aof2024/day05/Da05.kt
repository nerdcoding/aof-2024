package org.nerdcoding.aof2024.day05

import java.io.File

private const val INPUT_FILE_LOCATION = "./src/main/resources/day05/input.txt"


fun main() {
    val input = File(INPUT_FILE_LOCATION).readLines()
    val rulesMap = createRules(input)
    val resultPart1 = createUpdates(input)
        .filter { update -> isUpdateValid(update, rulesMap) }
        .sumOf { update -> getMiddleNumber(update) }

    println("Part1 sum $resultPart1")
}

private fun createRules(input: List<String>): Map<Int, Set<Int>> {
    val rulePairs = input
        .takeWhile { it.isNotEmpty() }
        .map { row -> Pair(
            row.split("|")[0].toInt(),
            row.split("|")[1].toInt()
        )}
        .toSet()

    // Create a Map with all right pages as Keys and the left pages as Set. Example
    // 97|13
    // 61|13
    // 29|13
    // Will lead to "13 -> [97, 61, 29]"
    return rulePairs
        .groupBy({ it.second }, { findPagesWithEnding(it.second, rulePairs) })
        .mapValues { (_, sets) -> sets.flatten().toSet() }
}

private fun findPagesWithEnding(ending: Int, pairs: Set<Pair<Int, Int>>): Set<Int> =
    pairs
        .filter { pair -> pair.second == ending }
        .map { pair -> pair.first }
        .toSet()

private fun createUpdates(input: List<String>): List<List<Int>> =
    input
        .dropWhile { it.isNotEmpty() }
        .drop(1)
        .map { row -> row.split(",")
            .map { it.toInt() }
        }

private fun isUpdateValid(update: List<Int>, rules: Map<Int, Set<Int>>): Boolean =
    update
        .map {
            updateValue -> numbersAfterBase(update, updateValue)
                .none { it in (rules[updateValue] ?: emptySet()) }
        }
        .all { it }

private fun numbersAfterBase(update: List<Int>, base: Int): List<Int> =
    update
        .dropWhile { it != base }.drop(1)

private fun getMiddleNumber(update: List<Int>): Int =
    update[update.size / 2]