package com.simonrules.aoc2023.day21

import com.simonrules.aoc2023.utils.Point2D
import com.simonrules.aoc2023.utils.ListPlan2D
import com.simonrules.aoc2023.utils.Plan2D

import java.io.File

class Day21(filename: String) {
    private val plan2D: Plan2D<Char>
    private lateinit var start: Point2D

    init {
        val input = File(filename).readLines()
        val width = input[0].length
        val height = input.size

        plan2D = ListPlan2D(Point2D(0, 0), Point2D(width - 1, height - 1), '.')

        for (y in 0 until height) {
            for (x in 0 until width) {
                val c = input[y][x]
                if (c == 'S') {
                    start = Point2D(x, y)
                }

                plan2D.setAt(Point2D(x, y), c)
            }
        }
    }

    private fun explore(p: Point2D, distance: Int, queue: ArrayDeque<State>, visited: MutableMap<Point2D, Int>) {
        if (!visited.containsKey(p) && plan2D.inBounds(p) && plan2D.getAt(p) == '.') {
            queue.addLast(State(p, distance))
            visited[p] = distance
        }
    }

    data class State(val point: Point2D, val distance: Int)

    // do breadth first search from starting point and label the distances
    // then count the number of spaces marked with an even number.
    // this is because the goals are even, and walking back/forward at any
    // point can use 2 steps without moving.
    private fun bfs(maxSteps: Int): Int {
        val queue = ArrayDeque<State>()
        val visited = mutableMapOf<Point2D, Int>()

        // first point
        queue.addLast(State(start, 0))
        visited[start] = 0

        while (queue.isNotEmpty()) {
            val s = queue.removeFirst()

            if (s.distance < maxSteps) {
                explore(s.point.getNorth(), s.distance + 1, queue, visited)
                explore(s.point.getSouth(), s.distance + 1, queue, visited)
                explore(s.point.getEast(), s.distance + 1, queue, visited)
                explore(s.point.getWest(), s.distance + 1, queue, visited)
            }
        }

        return visited.count { it.value % 2 == 0 }
    }

    fun part1(): Int {
        return bfs(64)
    }
}

fun main() {
    val day21 = Day21("day21/input.txt")
    println(day21.part1())
}
