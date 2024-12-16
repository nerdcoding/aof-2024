package org.nerdcoding.aof2024.day15

import org.nerdcoding.aof2024.day15.InputPuzzleReader.Box
import org.nerdcoding.aof2024.day15.InputPuzzleReader.Empty
import org.nerdcoding.aof2024.day15.InputPuzzleReader.Movement
import org.nerdcoding.aof2024.day15.InputPuzzleReader.Point
import org.nerdcoding.aof2024.day15.InputPuzzleReader.Robot
import org.nerdcoding.aof2024.day15.InputPuzzleReader.Wall
import org.nerdcoding.aof2024.day15.InputPuzzleReader.Warehouse

fun main() {
    val reader = InputPuzzleReader()
    val warehouse = reader.readWarehouse()
    val movements = reader.readMovements()
    printWarehouse(warehouse)

    for (movement in movements) {
        move(warehouse, searchRobot(warehouse), movement)
        //printWarehouse(warehouse)
    }

    println("Part1 GPS: ${calculateGPS(warehouse)}")
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
        is Box -> moveRobotWithBoxes(
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
        boxesToMove: List<Box>) {

    if (boxesToMove.isEmpty()) {
        return
    }

    // move each box
    for (box in boxesToMove) {
        val nextBoxPoint = determineNextPoint(box.point, movement)
        warehouse.map[nextBoxPoint] = Box(nextBoxPoint)
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
        movement: Movement): List<Box> {

    var mutableNextPoint = nextPoint
    var boxesToMove = mutableListOf<Box>()
    while (warehouse.map.containsKey(mutableNextPoint)) {
        when (warehouse.map[mutableNextPoint]) {
            is Wall -> {
                boxesToMove = mutableListOf<Box>()
                break
            }
            is Empty -> {
                break
            }
            is Box -> {
                boxesToMove.add(warehouse.map[mutableNextPoint] as Box)
                mutableNextPoint = determineNextPoint(mutableNextPoint, movement)
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
        .filter { item -> item is Box }
        .map { box -> (box.point.y * 100) + box.point.x }
        .sumOf { it }

private fun printWarehouse(warehouse: Warehouse) {
    for (y in 0 until warehouse.maxSizeY) {
        for (x in 0 until warehouse.maxSizeX) {
            when (warehouse.map[Point(x,y)]) {
                is Wall -> print("#")
                is Box -> print("O")
                is Robot -> print("@")
                is Empty -> print(".")
            }
        }
        println()
    }
    println("*********************************************************************\n")
}