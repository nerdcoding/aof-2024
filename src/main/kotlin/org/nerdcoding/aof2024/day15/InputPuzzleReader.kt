package org.nerdcoding.aof2024.day15


import java.io.File

private const val INPUT_FILE_LOCATION = "./src/main/resources/day15/input.txt"

class InputPuzzleReader() {

    val lines: List<String> = File(INPUT_FILE_LOCATION).readLines()

    fun readWarehouse(): Warehouse  {
        val warehouseMap = lines
            .asSequence()
            .takeWhile {
                it.isNotBlank()
            }.flatMapIndexed {
                y, line -> line.mapIndexed {
                    x, char -> Point(x,y) to createWarehouseItem(Point(x,y), char)
                }
            }.toMap()
        var maxSizeX = lines.first().length
        var maxSizeY = lines.asSequence().takeWhile { it.isNotBlank() }.count()

        return Warehouse(warehouseMap.toMutableMap(), maxSizeX, maxSizeY)
    }

    fun readMovements(): List<Movement> =
        lines
            .asSequence()
            .dropWhile {
                it.isNotBlank()
            }
            .drop(1)
            .flatMap {
                line -> line.map { char -> createMovement(char) }
            }.toList()

    private fun createWarehouseItem(point: Point, char: Char): Item =
        when (char) {
            '#' -> Wall(point)
            'O' -> Box(point)
            '@' -> Robot(point)
            '.' -> Empty(point)
            else -> throw IllegalArgumentException("Unknown Warehouse item $char was provided")
        }

    private fun createMovement(char: Char): Movement =
        when (char) {
            '^' -> Movement.UP
            'v' -> Movement.DOWN
            '>' -> Movement.RIGHT
            '<' -> Movement.LEFT
            else -> throw IllegalArgumentException("Unknown movement direction $char was provided")
        }

    class Warehouse(
        val map: MutableMap<Point, Item>,
        val maxSizeX: Int,
        val maxSizeY: Int
    )
    data class Point(val x: Int, val y: Int) {
        override fun toString(): String {
            return "Point(x=$x, y=$y)"
        }
    }

    abstract class Item(val point: Point)
    class Wall(point: Point) : Item(point) {
        override fun toString(): String {
            return "Wall(point=${point})"
        }
    }
    class Robot(point: Point) : Item(point) {
        override fun toString(): String {
            return "Robot(point=${point})"
        }
    }
    class Box(point: Point) : Item(point) {
        override fun toString(): String {
            return "Box(point=${point})"
        }
    }
    class Empty(point: Point) : Item(point) {
        override fun toString(): String {
            return "Empty(point=${point})"
        }
    }

    enum class Movement {
        UP,
        DOWN,
        RIGHT,
        LEFT
    }

}