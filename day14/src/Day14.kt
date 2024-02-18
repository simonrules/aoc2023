package com.simonrules.aoc2023.day14

import com.simonrules.aoc2023.utils.Point2D
import java.io.File

class Day14(path: String) {
    private val originalMap = File(path).readLines()
    private val width = originalMap[0].length
    private val height = originalMap.size
    private val MOVABLE = 'O'
    private val FIXED = '#'
    private val EMPTY = '.'

    private fun getMapAt(map: List<String>, pos: Point2D): Char {
        return map[pos.y][pos.x]
    }

    private fun getColumn(map: List<String>, j: Int): String {
        var column = ""
        for (i in 0 until height) {
            column += getMapAt(map, Point2D(j, i))
        }

        return column
    }

    private fun getColumnLoad(input: String): Int {
        var sum = 0
        input.forEachIndexed { i, c ->
            sum += if (c == MOVABLE) input.length - i else 0
        }

        return sum
    }

    private fun getLoad(map: List<String>): Int {
        var sum = 0
        map.forEachIndexed { i, row ->
            val load = map.size - i
            row.forEach { c ->
                sum += if (c == MOVABLE) load else 0
            }
        }
        return sum
    }

    private fun getRow(map: List<String>, i: Int): String {
        return map[i]
    }

    private fun tilt(input: String, reverse: Boolean = false): String {
        // String-based solution: split by the fixed rocks, then reassemble each part with movable rocks
        // furthest left (left == north or left == west)
        val parts = input.split(FIXED)

        val newParts = parts.map { part ->
            val numMovable = part.count { it == MOVABLE }
            val numEmpty = part.count { it == EMPTY }
            if (reverse) {
                EMPTY.toString().repeat(numEmpty) + MOVABLE.toString().repeat(numMovable)
            } else {
                MOVABLE.toString().repeat(numMovable) + EMPTY.toString().repeat(numEmpty)
            }
        }
        return newParts.joinToString(FIXED.toString())
    }

    private fun assembleMapFromColumns(columns: List<String>): List<String> {
        val map = mutableListOf<String>()

        for (y in 0 until height) {
            var row = ""
            columns.indices.forEach { x ->
                row += columns[x][y]
            }
            map.add(row)
        }

        return map.toList()
    }

    private fun tiltNorthSouth(map: List<String>, south: Boolean = false): List<String> {
        val columns = mutableListOf<String>()
        for (col in 0 until width) {
            val tilted = tilt(getColumn(map, col), south)
            columns.add(tilted)
        }

        return assembleMapFromColumns(columns)
    }

    private fun tiltEastWest(map: List<String>, east: Boolean = false): List<String> {
        val rows = mutableListOf<String>()
        map.forEach { row ->
            val tilted = tilt(row, east)
            rows.add(tilted)
        }

        return rows.toList()
    }

    private fun tiltCycle(map: List<String>): List<String> {
        var currentMap = map

        // tilt north, west, south, east
        currentMap = tiltNorthSouth(currentMap)
        currentMap = tiltEastWest(currentMap)
        currentMap = tiltNorthSouth(currentMap, south = true)
        currentMap = tiltEastWest(currentMap, east = true)

        return currentMap
    }

    private fun printMap(map: List<String>) {
        map.forEach {
                row ->
            println(row)
        }
        println()
    }

    fun part1(): Int {
        var sum = 0
        for (col in 0 until width) {
            val tilted = tilt(getColumn(originalMap, col))
            sum += getColumnLoad(tilted)
        }

        return sum
    }

    fun part2(): Int {
        val cycles = 1000000000L
        val stabiliseCycles = 1000L

        var currentMap = originalMap.toList()

        // Stabilise
        repeat(stabiliseCycles.toInt()) {
            currentMap = tiltCycle(currentMap)
        }
        val stabilisedLoad = getLoad(currentMap)

        // Find number of iterations till this value next occurs
        var iterations = 0
        do {
            currentMap = tiltCycle(currentMap)
            iterations++
        } while (getLoad(currentMap) != stabilisedLoad)

        val cyclesToAdd = (cycles - stabiliseCycles) % iterations

        repeat(cyclesToAdd.toInt()) {
            currentMap = tiltCycle(currentMap)
        }

        return getLoad(currentMap)
    }
}

fun main() {
    val day14 = Day14("day14/input.txt")
    println(day14.part1())
    println(day14.part2())
}