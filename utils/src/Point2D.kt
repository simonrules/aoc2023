package com.simonrules.aoc2023.utils

import kotlin.math.absoluteValue

data class Point2D(val x: Int, val y: Int) {
    fun getNorth(): Point2D {
        return Point2D(x, y - 1)
    }

    fun getSouth(): Point2D {
        return Point2D(x, y + 1)
    }

    fun getEast(): Point2D {
        return Point2D(x + 1, y)
    }

    fun getWest(): Point2D {
        return Point2D(x - 1, y)
    }

    fun move(dx: Int, dy: Int): Point2D {
        return Point2D(x + dx, y + dy)
    }

    fun manhattanDistance(other: Point2D): Int {
        return (x - other.x).absoluteValue + (y - other.y).absoluteValue
    }
}
