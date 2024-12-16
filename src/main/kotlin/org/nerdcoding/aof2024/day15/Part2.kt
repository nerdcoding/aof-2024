package org.nerdcoding.aof2024.day15

import org.nerdcoding.aof2024.day15.InputPuzzleReader.Box
import org.nerdcoding.aof2024.day15.InputPuzzleReader.BoxLeft
import org.nerdcoding.aof2024.day15.InputPuzzleReader.BoxRight
import org.nerdcoding.aof2024.day15.InputPuzzleReader.Empty
import org.nerdcoding.aof2024.day15.InputPuzzleReader.Movement
import org.nerdcoding.aof2024.day15.InputPuzzleReader.Point
import org.nerdcoding.aof2024.day15.InputPuzzleReader.Robot
import org.nerdcoding.aof2024.day15.InputPuzzleReader.Wall
import org.nerdcoding.aof2024.day15.InputPuzzleReader.Warehouse
import kotlin.collections.set


fun main() {
    val reader = InputPuzzleReader()
    val warehouse = reader.readWarehousePart2()
    val movements = reader.readMovements()
    printWarehouse(warehouse)

    for (movement in movements) {
        //println(movement)
        move(warehouse, searchRobot(warehouse), movement)
        //printWarehouse(warehouse)
    }

    println("Part2 GPS: ${calculateGPS(warehouse)}")
}

private fun move(warehouse: Warehouse, robotPosition: Point, movement: Movement) {
    var nextPoint = determineNextPoint(robotPosition, movement)

    when (warehouse.map[nextPoint]) {
        is Wall -> return
        is Empty -> moveRobot(
            warehouse,
            robotPosition,
            nextPoint
        )
        is BoxLeft, is BoxRight -> moveRobotWithBoxes(
            warehouse,
            robotPosition,
            movement,
            determineBoxesToMove(warehouse, nextPoint, movement)
        )
    }
}

private fun moveRobot(
    warehouse: Warehouse,
    currentPosition: Point,
    nextPosition: Point) {

    warehouse.map[currentPosition] = Empty(currentPosition)
    warehouse.map[nextPosition] = Robot(nextPosition)
}

private fun moveRobotWithBoxes(
    warehouse: Warehouse,
    robotPosition: Point,
    movement: Movement,
    boxesToMove: Map<Point, Box>) {

    if (boxesToMove.isEmpty()) {
        return
    }

    // move each box
    val alreadyProcessedPoints = mutableSetOf<Point>()
    for (y in 0 until warehouse.maxSizeY) {
        for (x in 0 until warehouse.maxSizeX) {
            val currentPoint = Point(x, y)
            if (boxesToMove.contains(currentPoint)) {
                if (boxesToMove[currentPoint] is BoxLeft) {
                    val nextBoxLeftPoint = determineNextPoint(currentPoint, movement)
                    warehouse.map[nextBoxLeftPoint] = BoxLeft(nextBoxLeftPoint)
                    alreadyProcessedPoints.add(nextBoxLeftPoint)

                    if (!alreadyProcessedPoints.contains(currentPoint)) {
                        warehouse.map[currentPoint] = Empty(currentPoint)
                    }
                }
                if (boxesToMove[currentPoint] is BoxRight) {
                    val nextBoxRightPoint = determineNextPoint(currentPoint, movement)
                    warehouse.map[nextBoxRightPoint] = BoxRight(nextBoxRightPoint)
                    alreadyProcessedPoints.add(nextBoxRightPoint)

                    if (!alreadyProcessedPoints.contains(currentPoint)) {
                        warehouse.map[currentPoint] = Empty(currentPoint)
                    }
                }
            }
        }
    }

    // move robot
    val nextRobotPoint = determineNextPoint(robotPosition, movement)
    warehouse.map[robotPosition] = Empty(robotPosition)
    warehouse.map[nextRobotPoint] = Robot(nextRobotPoint)
}

private fun determineNextPoint(basePoint: Point, movement: Movement) =
    when (movement) {
        Movement.UP -> Point(basePoint.x, basePoint.y-1)
        Movement.DOWN -> Point(basePoint.x, basePoint.y+1)
        Movement.RIGHT -> Point(basePoint.x+1, basePoint.y)
        Movement.LEFT -> Point(basePoint.x-1, basePoint.y)
    }

private fun determineBoxesToMove(
    warehouse: Warehouse,
    nextPoint: Point,
    movement: Movement): Map<Point, Box> {

    var pointsToCheck = mutableListOf<Point>(nextPoint)
    val alreadyCheckedPoints = mutableSetOf<Point>()
    var boxesToMove = mutableMapOf<Point, Box>()

    while (pointsToCheck.isNotEmpty()) {
        val pointToCheck = pointsToCheck.removeAt(0)
        alreadyCheckedPoints.add(pointToCheck)
        if (warehouse.map.containsKey(pointToCheck)) {
            when (warehouse.map[pointToCheck]) {
                is Wall -> {
                    boxesToMove = mutableMapOf<Point, Box>()
                    break
                }
                is Empty -> {
                    continue
                }
                is BoxLeft -> {
                    boxesToMove.put(pointToCheck, warehouse.map[pointToCheck] as BoxLeft)
                    val rightBoxPoint = Point(pointToCheck.x+1, pointToCheck.y)
                    boxesToMove.put(rightBoxPoint, warehouse.map[rightBoxPoint] as BoxRight)

                    var nextPoint = determineNextPoint(pointToCheck, movement)
                    if (Movement.RIGHT == movement) {
                        nextPoint = determineNextPoint(nextPoint, movement)
                    }
                    if (Movement.UP == movement || Movement.DOWN == movement) {
                        val nextRightBoxPoint = determineNextPoint(rightBoxPoint, movement)
                        if (!alreadyCheckedPoints.contains(nextRightBoxPoint)) {
                            pointsToCheck.add(nextRightBoxPoint)
                        }
                    }
                    if (!alreadyCheckedPoints.contains(nextPoint)) {
                        pointsToCheck.add(nextPoint)
                    }
                }
                is BoxRight -> {
                    boxesToMove.put(pointToCheck, warehouse.map[pointToCheck] as BoxRight)
                    val leftBoxPoint = Point(pointToCheck.x-1, pointToCheck.y)
                    boxesToMove.put(leftBoxPoint, warehouse.map[leftBoxPoint] as BoxLeft)

                    var nextPoint = determineNextPoint(pointToCheck, movement)
                    if (Movement.LEFT == movement) {
                        nextPoint = determineNextPoint(nextPoint, movement)
                    }
                    if (Movement.UP == movement || Movement.DOWN == movement) {
                        val nextLeftBoxPoint = determineNextPoint(leftBoxPoint, movement)
                        if (!alreadyCheckedPoints.contains(nextLeftBoxPoint)) {
                            pointsToCheck.add(nextLeftBoxPoint)
                        }
                    }
                    if (!alreadyCheckedPoints.contains(nextPoint)) {
                        pointsToCheck.add(nextPoint)
                    }
                }
            }
        }
    }

    return boxesToMove
}

private fun searchRobot(warehouse: Warehouse): Point =
    warehouse.map.values
        .filter { item -> item is Robot }
        .map { item -> item.point }
        .first()

private fun calculateGPS(warehouse: Warehouse): Int =
    warehouse.map.values
        .filter { item -> item is BoxLeft }
        .map { box -> (box.point.y * 100) + box.point.x }
        .sumOf { it }

private fun printWarehouse(warehouse: Warehouse) {
    for (y in 0 until warehouse.maxSizeY) {
        for (x in 0 until warehouse.maxSizeX) {
            when (warehouse.map[Point(x,y)]) {
                is Wall -> print("#")
                is BoxLeft -> print("[")
                is BoxRight -> print("]")
                is Robot -> print("@")
                is Empty -> print(".")
            }
        }
        println()
    }
    println("*********************************************************************\n")
}