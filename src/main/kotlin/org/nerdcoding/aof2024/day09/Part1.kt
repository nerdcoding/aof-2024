package org.nerdcoding.aof2024.day09

import java.io.File

private const val INPUT_FILE_LOCATION = "./src/main/resources/day09/input.txt"

private val inputString = File(INPUT_FILE_LOCATION).readText()

fun main() {
    val blocks = createBlocks(inputString)
    val compactedBlocks = rearrangeBlocks(blocks)
    val checksum = calculateChecksum(compactedBlocks)

    println("$checksum")
}

private fun calculateChecksum(blocks: List<Int>): Long =
    blocks
        .mapIndexed {
            index, value -> index.toLong() * value.toLong()
    }.sumOf { it }

private fun rearrangeBlocks(blocks: List<Int>): List<Int> {
    val rearrangedBlocks = blocks.toMutableList()

    var beginIndex = 0
    var endIndex = rearrangedBlocks.size -1

    while (-1 in rearrangedBlocks) {
        if (rearrangedBlocks[beginIndex] != -1) {
            beginIndex++
            continue
        }
        rearrangedBlocks[beginIndex] = rearrangedBlocks[endIndex]
        rearrangedBlocks.removeAt(endIndex)
        endIndex--
    }

    return rearrangedBlocks
}

private fun createBlocks(inputString: String): List<Int> {
    var currentFileId = 0

    return inputString.flatMapIndexed {
            index, value ->
        if (isFileIndex(index)) {
            createFiles(currentFileId++, inputString[index].digitToInt())
        } else {
            createSpaces(inputString[index].digitToInt())
        }
    }
}


private fun createFiles(currentFileId: Int, numberOfFiles: Int): List<Int> =
    generateSequence { currentFileId }
        .take(numberOfFiles)
        .toList()

private fun createSpaces(numberOfSpaces: Int): List<Int> =
    generateSequence { -1 }
        .take(numberOfSpaces)
        .toList()

private fun isFileIndex(index: Int): Boolean =
    index % 2 == 0


