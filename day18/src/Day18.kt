package com.simonrules.aoc2023.day18

import com.simonrules.aoc2023.utils.ListPlan2D
import com.simonrules.aoc2023.utils.Point2D
import java.io.File

class Day18(val path: String) {
    fun part1(): Int {
        var min = Point2D(0, 0)
        var max = Point2D(0, 0)
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
        val plan2D = ListPlan2D(Point2D(min.x - 1, min.y - 1), Point2D(max.x + 1, max.y + 1), '.')

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

        plan2D.print()
        // Strategy: flood fill the area outside the trench because it's easy to locate.
        // Count the unfilled pixels which are either the trench or the area inside.
        plan2D.floodFill(Point2D(min.x - 1, min.y - 1), '*')
        return plan2D.count('.') + plan2D.count('#')
    }

    private fun decodeFromHexString(hex: String): Int {
        var output = 0
        hex.forEach {
            output *= 16
            output += when (it) {
                '0' -> 0
                '1' -> 1
                '2' -> 2
                '3' -> 3
                '4' -> 4
                '5' -> 5
                '6' -> 6
                '7' -> 7
                '8' -> 8
                '9' -> 9
                'a' -> 10
                'b' -> 11
                'c' -> 12
                'd' -> 13
                'e' -> 14
                'f' -> 15
                else -> 0
            }
        }
        return output
    }

    fun part2(): Long {
        var pos = Point2D(0, 0)

        File(path).forEachLine {
            val (_, _, code) = it.split(" ")
            // first 5 hex characters
            val dist = decodeFromHexString(code.substring(2, 7))
            // and the 6th
            pos = when (code[7]) {
                '0' -> pos.move(dist, 0) // R
                '1' -> pos.move(0, dist) // D
                '2' -> pos.move(-dist, 0) // L
                '3' -> pos.move(0, -dist) // U
                else -> pos
            }
        }
        if (pos != Point2D(0, 0)) {
            println("warning: path did not return to the start")
        }

        return 0L
    }
}

fun main() {
    val day18 = Day18("day18/input.txt")
    //println(day18.part1())
    println(day18.part2())
}
