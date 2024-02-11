package com.simonrules.aoc2023.day11

import com.simonrules.aoc2023.utils.Point2D
import java.io.File
import kotlin.math.absoluteValue

class Day11(path: String) {
    private val map = File(path).readLines()
    private val width = map[0].length
    private val height = map.size
    private val emptyRows = mutableListOf<Int>()
    private val emptyCols = mutableListOf<Int>()
    private val galaxies = mutableListOf<Point2D>()

    init {
        // Locate empty rows and columns
        map.forEachIndexed { y, row ->
            if (row.all { it == '.' }) {
                emptyRows.add(y)
            }

            for (x in 0..<width) {
                val pos = Point2D(x, y)
                if (getMapAt(pos) == '#')
                galaxies.add(pos)
            }
        }

        for (x in 0..<width) {
            var allEmpty = true
            for (y in 0..<height) {
                if (getMapAt(Point2D(x, y)) != '.') {
                    allEmpty = false
                    break
                }
            }
            if (allEmpty) {
                emptyCols.add(x)
            }
        }
    }

    private fun getMapAt(pos: Point2D): Char {
        return map[pos.y][pos.x]
    }

    private fun getExpansionDistance(a: Point2D, b: Point2D): Int {
        // reorder x and y if needed
        val extraCols = if (a.x < b.x) {
            emptyCols.count { it in a.x..b.x }
        } else {
            emptyCols.count { it in b.x..a.x }
        }
        val extraRows = if (a.y < b.y) {
            emptyRows.count { it in a.y..b.y }
        } else {
            emptyRows.count { it in b.y..a.y }
        }

        return (a.x - b.x).absoluteValue + extraCols + (a.y - b.y).absoluteValue + extraRows
    }

    fun part1(): Long {
        var distance = 0L
        for (a in 0 until galaxies.size) {
            for (b in a + 1 until galaxies.size) {
                distance += getExpansionDistance(galaxies[a], galaxies[b])
            }
        }
        return distance
    }
}

fun main() {
    val day11 = Day11("day11/input.txt")
    println(day11.part1())
}
