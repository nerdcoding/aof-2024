package org.nerdcoding.aof2024.day02

import java.io.File
import kotlin.math.abs

private const val INPUT_FILE_LOCATION = "./src/main/resources/day02/input.txt"

fun main() {
    val reports = readInputFile();

    val safeReportsPart1 = countSafeReportsPart1(reports)
    val safeReportsPart2 = countSafeReportsPart2(reports)

    println("Part 1 safe reports: $safeReportsPart1")
    println("Part 2 safe reports: $safeReportsPart2")
}

private fun readInputFile(): List<List<Int>> = File(INPUT_FILE_LOCATION).useLines {
        lines -> lines.map {
            singleLine -> singleLine.split("\\s+".toRegex())
                .map { it.toInt() }
        }.toList()
}

private fun countSafeReportsPart1(reports: List<List<Int>>): Int {
    return reports.count { report -> isValidReportPart1(report) }
}

private fun countSafeReportsPart2(reports: List<List<Int>>): Int {
    return reports.count { report -> isValidReportPart2(report) }
}

private fun isValidReportPart1(report: List<Int>): Boolean {
    return (areIncreasingLevels(report) || areDecreasingLevels(report))
            && isValidAdjacentLevelDifference(report)
}

private fun isValidReportPart2(report: List<Int>): Boolean {
    return report.indices
        .map { toRemove -> report.filterIndexed { i, _ -> i != toRemove } }
        .any { isValidReportPart1(it) }
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

