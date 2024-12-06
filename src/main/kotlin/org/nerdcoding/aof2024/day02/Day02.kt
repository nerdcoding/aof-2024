package org.nerdcoding.aof2024.day02

import java.io.File
import kotlin.math.abs

private const val INPUT_FILE_LOCATION = "./src/main/resources/day02/input.txt"

fun main() {
    val reports = readInputFile();

    val safeReports = countSafeReportsPart1(reports)

    println("Part 1 safe reports: $safeReports")
}

private fun readInputFile(): List<List<Int>> = File(INPUT_FILE_LOCATION).useLines {
    lines -> lines.map {
        singleLine -> singleLine.split("\\s+".toRegex())
            .map { it.toInt() }
    }.toList()
}

private fun countSafeReportsPart1(reports: List<List<Int>>): Int {
    return reports.count { report -> isValidReport(report) }
}

private fun isValidReport(report: List<Int>): Boolean {
    return (areIncreasingLevels(report) || areDecreasingLevels(report))
            && isValidAdjacentLevelDifference(report)
}

private fun areIncreasingLevels(report: List<Int>): Boolean {
    return report.zipWithNext().all { (a, b) -> a < b }
}

private fun areDecreasingLevels(report: List<Int>): Boolean {
    return report.zipWithNext().all { (a, b) -> a > b }
}

private fun isValidAdjacentLevelDifference(
        report: List<Int>): Boolean {

    return report.zipWithNext()
        .all { (a, b) -> abs(a - b) in 1..3 }
}

