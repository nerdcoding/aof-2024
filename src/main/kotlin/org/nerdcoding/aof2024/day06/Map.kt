package org.nerdcoding.aof2024.day06

import org.nerdcoding.aof2024.day06.Map.MoveDirection.DOWN
import org.nerdcoding.aof2024.day06.Map.MoveDirection.LEFT
import org.nerdcoding.aof2024.day06.Map.MoveDirection.RIGHT
import org.nerdcoding.aof2024.day06.Map.MoveDirection.UP


class Map(private val map: Array<CharArray>)  {

    private val guardDirections = setOf('^', '>', 'v', '<')
    private val visited = mutableMapOf<Pair<Int, Int>, MutableList<MoveDirection>>()

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

    enum class GuardState {
        NEW_POSITION,
        OUT_OF_MAP,
        IN_LOOP
    }

    fun printMap() =
        map.forEach { row -> println(row.joinToString(" ")) }

    fun countVisitedPositions() =
        map.sumOf {
            row -> row.count { it == 'X' }
        }

    fun getMapSize() =
        map.size

    fun findValueAtPosition(x: Int, y: Int) =
        map[y][x]

    fun changeValueAtPosition(x: Int, y: Int) {
        map[y][x] = '#'
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

    fun moveGuard(currentGuardPosition: GuardPosition): MoveResult {
        val possibleNextGuardPosition = determineNextGuardPosition(currentGuardPosition)
        return if (isOutsideOfMap(possibleNextGuardPosition)) {
            // we left the map, mark current point in map as visited and we are finished
            markAsVisited(currentGuardPosition)
            MoveResult(
                possibleNextGuardPosition,
                GuardState.OUT_OF_MAP
            )
        } else if (isInLoop(possibleNextGuardPosition)) {
            // we visited a position for the second time
            MoveResult(
                possibleNextGuardPosition,
                GuardState.IN_LOOP
            )
        } else if (isNotBlocked(possibleNextGuardPosition)) {
            // mark current point in map as visited and move to next point
            markAsVisited(currentGuardPosition)
            MoveResult(
                possibleNextGuardPosition,
                GuardState.NEW_POSITION
            )
        } else {
            // next point is blocked, stay on current point and change direction
            MoveResult(
                GuardPosition(
                    currentGuardPosition.x,
                    currentGuardPosition.y,
                    currentGuardPosition.direction.changeDirection()
                ),
                GuardState.NEW_POSITION
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

    private fun isInLoop(guardPosition: GuardPosition): Boolean =
        visited.values.any { list ->
            list.size != list.toSet().size
        }

    private fun isNotBlocked(guardPosition: GuardPosition): Boolean =
        map[guardPosition.y][guardPosition.x] != '#'

    private fun markAsVisited(guardPosition: GuardPosition) {
        map[guardPosition.y][guardPosition.x] = 'X'

        if (!visited.containsKey(Pair(guardPosition.x, guardPosition.y))) {
            visited[Pair(guardPosition.x, guardPosition.y)] = mutableListOf()
        }
        visited[Pair(guardPosition.x, guardPosition.y)]?.add(guardPosition.direction)

    }



   class GuardPosition(val x: Int, val y: Int, val direction: MoveDirection)

   class MoveResult(val guardPosition: GuardPosition, val guardState: GuardState)

}


