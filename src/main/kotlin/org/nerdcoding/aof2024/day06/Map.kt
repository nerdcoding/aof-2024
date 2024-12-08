package org.nerdcoding.aof2024.day06

import org.nerdcoding.aof2024.day06.Map.MoveDirection.DOWN
import org.nerdcoding.aof2024.day06.Map.MoveDirection.LEFT
import org.nerdcoding.aof2024.day06.Map.MoveDirection.RIGHT
import org.nerdcoding.aof2024.day06.Map.MoveDirection.UP

class Map(private val map: Array<CharArray>)  {

    private val guardDirections = setOf('^', '>', 'v', '<')

    enum class MoveDirection {
        UP,
        RIGHT,
        DOWN,
        LEFT;

        fun changeDirection() =
            when (this) {
                UP -> RIGHT
                RIGHT -> DOWN
                DOWN -> LEFT
                LEFT -> UP
            }
    }

    fun printMap() =
        map.forEach { row -> println(row.joinToString(" ")) }

    fun countVisitedPositions() =
        map.sumOf {
            row -> row.count { it == 'X' }
        }

    fun findStartingGuardPosition(): GuardPosition {
        for (y in map.indices) {
            for (x in map[y].indices) {
                if (map[y][x] in guardDirections) {
                    return GuardPosition(x, y, determineDirection(map[y][x]))
                }
            }
        }

        throw IllegalArgumentException("map without starting point of guard")
    }

    fun moveGuard(currentGuardPosition: GuardPosition): GuardPosition? {
        val possibleNextGuardPosition = determineNextGuardPosition(currentGuardPosition)
        return if (isOutsideOfMap(possibleNextGuardPosition)) {
            // we left the map, mark current point in map as visited and we are finished
            markAsVisited(currentGuardPosition)
            null
        } else if (isNotBlocked(possibleNextGuardPosition)) {
            // mark current point in map as visited and move to next point
            markAsVisited(currentGuardPosition)
            possibleNextGuardPosition
        } else {
            // next point is blocked, stay on current point and change direction
            GuardPosition(
                currentGuardPosition.x,
                currentGuardPosition.y,
                currentGuardPosition.direction.changeDirection()
            )
        }
    }

    private fun determineDirection(guardDirection: Char) =
        when (guardDirection) {
            '^' -> UP
            '>' -> RIGHT
            'v' -> DOWN
            '<' -> LEFT
            else -> throw IllegalArgumentException("invalid guardDirection provided $guardDirection")
        }

    private fun determineNextGuardPosition(currentGuardPosition: GuardPosition): GuardPosition =
        when (currentGuardPosition.direction) {
            UP -> GuardPosition(
                currentGuardPosition.x,
                currentGuardPosition.y - 1,
                UP
            )
            RIGHT -> GuardPosition(
                currentGuardPosition.x + 1,
                currentGuardPosition.y,
                RIGHT
            )
            DOWN -> GuardPosition(
                currentGuardPosition.x,
                currentGuardPosition.y + 1,
                DOWN
            )
            LEFT -> GuardPosition(
                currentGuardPosition.x - 1,
                currentGuardPosition.y,
                LEFT
            )
        }

    private fun isOutsideOfMap(guardPosition: GuardPosition): Boolean =
        guardPosition.y !in map.indices || guardPosition.x !in map[guardPosition.y].indices

    private fun isNotBlocked(guardPosition: GuardPosition): Boolean =
        map[guardPosition.y][guardPosition.x] != '#'

    private fun markAsVisited(guardPosition: GuardPosition): Unit {
        map[guardPosition.y][guardPosition.x] = 'X'
    }



   class GuardPosition(val x: Int, val y: Int, val direction: MoveDirection)

}


