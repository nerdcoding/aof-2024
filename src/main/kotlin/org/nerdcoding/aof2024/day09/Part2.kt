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

private fun calculateChecksum(blocks: List<Block>): Long =
    blocks.flatMapIndexed {
            index, block -> if (block.value != null && block.valueSize != null) {
                generateSequence { block.value }
                    .take(block.valueSize!!)
                    .toList()
            } else {
                generateSequence { 0 }
                    .take(block.freeSpace)
                    .toList()
            }
    }.mapIndexed {
            index, value -> index.toLong() * value.toLong()
    }.sumOf { it }


private fun rearrangeBlocks(blocks: List<Block>): List<Block> {
    val rearrangedBlocks = blocks.toMutableList()


    while (searchLastMovableFile(rearrangedBlocks) != null) {
        val movableFile = searchLastMovableFile(rearrangedBlocks)!!

        var moved = false
        for (blockIndex in rearrangedBlocks.indices) {
            if (blockIndex >= rearrangedBlocks.indexOf(movableFile)) {
                break
            }
            if (rearrangedBlocks[blockIndex].freeSpace == 0) {
                continue
            }

            if (rearrangedBlocks[blockIndex].freeSpace == movableFile.valueSize!!) {
                rearrangedBlocks[blockIndex].value = movableFile.value
                rearrangedBlocks[blockIndex].valueSize = movableFile.valueSize
                rearrangedBlocks[blockIndex].freeSpace = 0
                rearrangedBlocks[blockIndex].moveable = false

                updateFreeSpaceAfterMove(rearrangedBlocks, movableFile, blockIndex)

                moved = true
                break
            }  else if (rearrangedBlocks[blockIndex].freeSpace > movableFile.valueSize!!) { // movableFile always have a valueSize
                val remainingSpace = rearrangedBlocks[blockIndex].freeSpace - movableFile.valueSize!!
                rearrangedBlocks[blockIndex].value = movableFile.value
                rearrangedBlocks[blockIndex].valueSize = movableFile.valueSize
                rearrangedBlocks[blockIndex].freeSpace = 0
                rearrangedBlocks[blockIndex].moveable = false

                rearrangedBlocks.add(
                    blockIndex+1,
                    Block(null, null, remainingSpace, false)
                )

                updateFreeSpaceAfterMove(rearrangedBlocks, movableFile, blockIndex)

                moved = true
                break
            }
        }

        if (!moved) {
            movableFile.moveable = false
        }
    }

    return rearrangedBlocks
}

private fun searchLastMovableFile(blocks: List<Block>): Block? =
    blocks.findLast {
        block -> block.moveable
    }

private fun updateFreeSpaceAfterMove(rearrangedBlocks: MutableList<Block>, movableFile: Block, blockIndex: Int) {
    val movableFileIndex = rearrangedBlocks.indexOf(movableFile)
    rearrangedBlocks[movableFileIndex].value = null
    rearrangedBlocks[movableFileIndex].valueSize = null
    rearrangedBlocks[movableFileIndex].freeSpace = rearrangedBlocks[blockIndex].valueSize!!
    rearrangedBlocks[movableFileIndex].moveable = false

    var addFreeSpace = 0
    var removeLeft: Block? = null;
    var removeRight: Block? = null;
    if (movableFileIndex-1 >= 0 && rearrangedBlocks[movableFileIndex-1].freeSpace > 0) {
        addFreeSpace += rearrangedBlocks[movableFileIndex-1].freeSpace
        removeLeft = rearrangedBlocks[movableFileIndex-1]
    }
    if (movableFileIndex+1 < rearrangedBlocks.size && rearrangedBlocks[movableFileIndex+1].freeSpace > 0) {
        addFreeSpace += rearrangedBlocks[movableFileIndex+1].freeSpace
        removeRight = rearrangedBlocks[movableFileIndex+1]
    }
    if (addFreeSpace > 0) {
        rearrangedBlocks[movableFileIndex].freeSpace += addFreeSpace
    }
    if (removeLeft != null) {
        rearrangedBlocks.remove(removeLeft)
    }
    if (removeRight != null) {
        rearrangedBlocks.remove(removeRight)
    }
}

private fun createBlocks(inputString: String): List<Block> {
    var currentFileId = 0

    return inputString.mapIndexed() {
            index, value ->
        if (isFileIndex(index)) {
            createFiles(currentFileId++, inputString[index].digitToInt())
        } else if (inputString[index].digitToInt() > 0) {
            createSpaces(inputString[index].digitToInt())
        } else {
            null
        }
    }.filterNotNull()
}


private fun createFiles(currentFileId: Int, numberOfFiles: Int): Block =
    Block(currentFileId, numberOfFiles, 0, true)

private fun createSpaces(numberOfSpaces: Int): Block =
    Block(null, null, numberOfSpaces, false)

private fun isFileIndex(index: Int): Boolean =
    index % 2 == 0

class Block(var value: Int?, var valueSize: Int?, var freeSpace: Int, var moveable: Boolean) {
    override fun toString(): String {
        return "Block(value=$value, valueSize=$valueSize, freeSpace=$freeSpace)"
    }
}
//private class Space(value: Int, size: Int) : Block(value, size)
//private class MyFile(value: Int, size: Int) : Block(value, size)

