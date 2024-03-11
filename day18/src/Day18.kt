package com.simonrules.aoc2023.day18

import com.simonrules.aoc2023.utils.ListPlan2D
import com.simonrules.aoc2023.utils.Point2D
import java.io.File

class Day18(path: String) {
    private val plan2D: ListPlan2D<Char>
    private var min = Point2D(0, 0)
    private var max = Point2D(0, 0)

    init {
        var pos = Point2D(0, 0)

        // First pass to get the dimensions
        File(path).forEachLine {
            val (dir, dist, _) = it.split(" ")
            pos = when (dir[0]) {
                'R' -> pos.move(dist.toInt(), 0)
                'L' -> pos.move(-dist.toInt(), 0)
                'U' -> pos.move(0, -dist.toInt())
                'D' -> pos.move(0, dist.toInt())
                else -> pos
            }
            if (pos.x > max.x) {
                max = Point2D(pos.x, max.y)
            } else if (pos.x < min.x) {
                min = Point2D(pos.x, min.y)
            }
            if (pos.y > max.y) {
                max = Point2D(max.x, pos.y)
            } else if (pos.y < min.y) {
                min = Point2D(min.x, pos.y)
            }
        }
        if (pos != Point2D(0, 0)) {
            println("warning: path did not return to the start")
        }
        plan2D = ListPlan2D(Point2D(min.x - 1, min.y - 1), Point2D(max.x + 1, max.y + 1), '.')

        // Second pass to generate the plan
        File(path).forEachLine {
            val (dir, dist, _) = it.split(" ")
            when (dir[0]) {
                'R' -> {
                    for (x in 0 until dist.toInt()) {
                        plan2D.setAt(Point2D(pos.x + x, pos.y), '#')
                    }
                }
                'L' -> {
                    for (x in 0 until dist.toInt()) {
                        plan2D.setAt(Point2D(pos.x - x, pos.y), '#')
                    }
                }
                'U' -> {
                    for (y in 0 until dist.toInt()) {
                        plan2D.setAt(Point2D(pos.x, pos.y - y), '#')
                    }
                }
                'D' -> {
                    for (y in 0 until dist.toInt()) {
                        plan2D.setAt(Point2D(pos.x, pos.y + y), '#')
                    }
                }
            }
            pos = when (dir[0]) {
                'R' -> pos.move(dist.toInt(), 0)
                'L' -> pos.move(-dist.toInt(), 0)
                'U' -> pos.move(0, -dist.toInt())
                'D' -> pos.move(0, dist.toInt())
                else -> pos
            }
        }
    }

    fun part1(): Int {
        // Strategy: flood fill the area outside the trench because it's easy to locate.
        // Count the unfilled pixels which are either the trench or the area inside.
        plan2D.floodFill(Point2D(min.x - 1, min.y - 1), '*')
        return plan2D.count('.') + plan2D.count('#')
    }
}

fun main() {
    val day18 = Day18("day18/input.txt")
    println(day18.part1())
}
