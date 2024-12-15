package org.nerdcoding.aof2024.day14

import java.io.File
import java.io.PrintStream
import kotlin.math.abs

private const val INPUT_FILE_LOCATION = "./src/main/resources/day14/input.txt"

private const val sizeX = 101
private const val sizeY = 103

fun main() {
    // Part2: print all positions to output file and search for ester egg
    val outputFile = File("output1.txt")
    PrintStream(outputFile).use { fileStream ->
        System.setOut(fileStream)

        var robots = readInputFile()
        printRobots(robots)

        repeat(10000) { index ->
            robots = moveRobots(robots)
            println("INDEX $index")
            printRobots(robots)
        }

        println("Result part1: ${calculateSafetyFactor(robots)}")
    }

}

private fun moveRobots(robots: List<Robot>) =
    robots.map {
        robot -> moveRobot(robot)
    }.toList()

private fun moveRobot(robot: Robot) =
    Robot(
        checkEdgeWrapping(
            Point(
                robot.position.x + robot.velocities.x,
                robot.position.y + robot.velocities.y,
            )
        ),
        Point(
            robot.velocities.x,
            robot.velocities.y
        )
    )

private fun checkEdgeWrapping(point: Point) =
    Point(
        if (point.x >= sizeX)
            point.x - sizeX
        else if (point.x < 0)
            point.x + sizeX
        else
            point.x,

        if (point.y >= sizeY)
            point.y - sizeY
        else if (point.y < 0)
            point.y + sizeY
        else
            point.y,
    )

private fun calculateSafetyFactor(robots: List<Robot>): Int {
    val firstQuadrants = robots.filter {
        robot -> robot.position.x < abs(sizeX/2) && robot.position.y < abs(sizeY/2)
    }.count()
    val secondQuadrants = robots.filter {
            robot -> robot.position.x > abs(sizeX/2) && robot.position.y < abs(sizeY/2)
    }.count()
    val thirdQuadrants = robots.filter {
            robot -> robot.position.x < abs(sizeX/2) && robot.position.y > abs(sizeY/2)
    }.count()
    val fourthQuadrants = robots.filter {
            robot -> robot.position.x > abs(sizeX/2) && robot.position.y > abs(sizeY/2)
    }.count()

    return firstQuadrants * secondQuadrants * thirdQuadrants * fourthQuadrants
}

private fun readInputFile(): List<Robot>  {
    val regex = Regex("""p=(-?\d+),(-?\d+) v=(-?\d+),(-?\d+)""")
    return File(INPUT_FILE_LOCATION)
        .useLines {
            lines -> lines.map {
                line -> val matchResult = regex.matchEntire(line)
                matchResult?.destructured?.let { (p1, p2, v1, v2) ->
                    Robot(
                        Point(p1.toInt(), p2.toInt()),
                        Point(v1.toInt(), v2.toInt())
                    )
                }
        }.filterNotNull().toList()
    }
}

private fun printRobots(robots: List<Robot>) {
    for (y in 0 until sizeY) {
        for (x in 0 until sizeX) {
            val numberOfRobots = numberOfRobotsAtPosition(x, y, robots)
            print(
                if (numberOfRobots == 0)
                    "."
                else
                    numberOfRobots
            )
        }
        println('\n')
    }
    println("*********************************************************************\n")
}

private fun numberOfRobotsAtPosition(x: Int, y: Int, robots: List<Robot>) =
    robots.filter {
        robot -> robot.position.x == x && robot.position.y == y
    }.count()

private class Robot(var position: Point, var velocities: Point)
private class Point(var x: Int, var y: Int)