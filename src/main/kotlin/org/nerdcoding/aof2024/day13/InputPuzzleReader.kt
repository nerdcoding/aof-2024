package org.nerdcoding.aof2024.day13

import java.io.File
import kotlin.collections.chunked

private const val INPUT_FILE_LOCATION = "./src/main/resources/day13/input.txt"

class InputPuzzleReader {

    enum class Part {
        PART1,
        PART2
    }

    fun readInputFile(part: Part): List<ClawMachine> =
        File(INPUT_FILE_LOCATION)
            .readLines()
            .chunked(4)
            .map {
                clawMachineLines -> ClawMachine(
                    parseButtonLine(clawMachineLines[0]),
                    parseButtonLine(clawMachineLines[1]),
                    parsePrizeLine(clawMachineLines[2], part)
                )
            }.toList()

    private fun parseButtonLine(line: String): Button {
        if (!line.startsWith("Button")) {
            throw IllegalArgumentException("No button line provided: $line")
        }

        val matchResult = """X\+(\d+),\s*Y\+(\d+)""".toRegex()
            .find(line)!!
        val (x, y) = matchResult.destructured
        return Button(x.toLong(), y.toLong())
    }

    private fun parsePrizeLine(line: String, part: Part): Prize {
        if (!line.startsWith("Prize")) {
            throw IllegalArgumentException("No prize line provided: $line")
        }

        val matchResult = """X=(\d+),\s*Y=(\d+)""".toRegex()
            .find(line)!!
        val (x, y) = matchResult.destructured
        return when (part) {
            Part.PART1 -> Prize(
                x.toLong(),
                y.toLong()
            )
            Part.PART2 -> Prize(
                x.toLong()+10000000000000,
                y.toLong()+10000000000000
            )
        }
    }

}