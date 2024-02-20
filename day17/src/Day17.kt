package com.simonrules.aoc2023.day17

import com.simonrules.aoc2023.utils.Point2D
import java.io.File

enum class Direction { UP, RIGHT, DOWN, LEFT }

class Day17(path: String) {
    private val map = File(path).readLines()
    private val width = map[0].length
    private val height = map.size

    private fun getMapAt(map: List<String>, pos: Point2D): Char {
        return map[pos.y][pos.x]
    }

    private fun isInBounds(pos: Point2D): Boolean {
        return pos.x in 0..<width && pos.y in 0..<height
    }

    private fun getNextPoint(direction: Direction, point: Point2D): Point2D {
        return when(direction) {
            Direction.UP -> Point2D(point.x, point.y - 1)
            Direction.RIGHT -> Point2D(point.x + 1, point.y)
            Direction.DOWN -> Point2D(point.x, point.y + 1)
            Direction.LEFT -> Point2D(point.x - 1, point.y)
        }
    }

    private fun rotateLeft(direction: Direction): Direction {
        return when(direction) {
            Direction.UP -> Direction.LEFT
            Direction.LEFT -> Direction.DOWN
            Direction.DOWN -> Direction.RIGHT
            Direction.RIGHT -> Direction.UP
        }
    }

    private fun rotateRight(direction: Direction): Direction {
        return when(direction) {
            Direction.UP -> Direction.RIGHT
            Direction.LEFT -> Direction.UP
            Direction.DOWN -> Direction.LEFT
            Direction.RIGHT -> Direction.DOWN
        }
    }

    data class State(val point: Point2D, val heatLoss: Int, val numStraights: Int, val direction: Direction)

    private fun bfs(start: Point2D, end: Point2D): Int {
        var minHeatLoss = Int.MAX_VALUE
        val visited = HashSet<State>()
        val queue = ArrayDeque<State>()

        // initial state; for (0, 0) can only pick right or down
        queue.addLast(State(Point2D(start.x + 1, start.y), 0, 1, Direction.RIGHT))
        queue.addLast(State(Point2D(start.x, start.y + 1), 0, 1, Direction.DOWN))

        while (queue.isNotEmpty()) {
            val s = queue.removeFirst()

            if (s in visited) {
                continue
            }
            visited.add(s)

            val heatLoss = s.heatLoss + getMapAt(map, s.point).digitToInt()
            if (heatLoss > minHeatLoss) {
                continue
            }

            // Check to see if end was reached
            if (s.point == end) {
                // found
                if (heatLoss < minHeatLoss) {
                    minHeatLoss = heatLoss
                }
            }

            // Disallow next states that result in a u-turn or >3 straights

            // Straight - increment count, stay in same direction
            if (s.numStraights < 3) {
                val nextPoint = getNextPoint(s.direction, s.point)
                if (isInBounds(nextPoint)) {
                    queue.addLast(State(nextPoint, heatLoss, s.numStraights + 1, s.direction))
                }
            }

            // Left
            var nextDirection = rotateLeft(s.direction)
            var nextPoint = getNextPoint(nextDirection, s.point)
            if (isInBounds(nextPoint)) {
                queue.addLast(State(nextPoint, heatLoss, 1, nextDirection))
            }

            // Right
            nextDirection = rotateRight(s.direction)
            nextPoint = getNextPoint(nextDirection, s.point)
            if (isInBounds(nextPoint)) {
                queue.addLast(State(nextPoint, heatLoss, 1, nextDirection))
            }
        }

        return minHeatLoss
    }

    fun part1(): Int {
        // try bfs, but probably need dijkstra
        val start = Point2D(0, 0)
        val end = Point2D(width - 1, height - 1)

        return bfs(start, end)
    }

    fun part2(): Int {
        return 0
    }
}

fun main() {
    val day17 = Day17("day17/test1.txt")
    println(day17.part1())
    println(day17.part2())
}
