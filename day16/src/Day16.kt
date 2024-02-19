package com.simonrules.aoc2023.day16

import com.simonrules.aoc2023.utils.Point2D
import java.io.File

enum class Direction(val value: Int) { UP(1), RIGHT(2), DOWN(4), LEFT(8) }

data class Beam(val position: Point2D, val direction: Direction) {
    fun getNextPosition(d: Direction): Point2D {
        return when (d) {
            Direction.UP -> Point2D(position.x, position.y - 1)
            Direction.RIGHT -> Point2D(position.x + 1, position.y)
            Direction.DOWN -> Point2D(position.x, position.y + 1)
            Direction.LEFT -> Point2D(position.x - 1, position.y)
        }
    }
}

class Day16(path: String) {
    private val map = File(path).readLines()
    private val width = map[0].length
    private val height = map.size
    private val initialBeam = Beam(Point2D(0, 0), Direction.RIGHT)
    private val energised = Array(height) { Array(width) { 0 } }

    private fun getMapAt(map: List<String>, pos: Point2D): Char {
        return map[pos.y][pos.x]
    }

    private fun isInBounds(pos: Point2D): Boolean {
        return pos.x in 0..<width && pos.y in 0..<height
    }

    private fun energise(pos: Point2D, direction: Direction) {
        energised[pos.y][pos.x] = energised[pos.y][pos.x] or direction.value
    }

    private fun isEnergised(pos: Point2D, direction: Direction): Boolean {
        return energised[pos.y][pos.x] and direction.value != 0
    }

    fun part1(): Int {
        val beams = mutableListOf<Beam>()
        beams.add(initialBeam)

        while (beams.isNotEmpty()) {
            val beam = beams.removeFirst()
            energise(beam.position, beam.direction)

            // Test map location
            val location = getMapAt(map, beam.position)
            if (location == '.') {
                // empty space
                val nextPosition = beam.getNextPosition(beam.direction)
                if (isInBounds(nextPosition) && !isEnergised(nextPosition, beam.direction)) {
                    beams.add(Beam(nextPosition, beam.direction))
                }
            } else if (location == '\\') {
                // mirror
                val nextDirection = when(beam.direction) {
                    Direction.UP -> Direction.LEFT
                    Direction.RIGHT -> Direction.DOWN
                    Direction.DOWN -> Direction.RIGHT
                    Direction.LEFT -> Direction.UP
                }
                val nextPosition = beam.getNextPosition(nextDirection)
                if (isInBounds(nextPosition) && !isEnergised(nextPosition, nextDirection)) {
                    beams.add(Beam(nextPosition, nextDirection))
                }
            } else if (location == '/') {
                // mirror
                val nextDirection = when(beam.direction) {
                    Direction.UP -> Direction.RIGHT
                    Direction.RIGHT -> Direction.UP
                    Direction.DOWN -> Direction.LEFT
                    Direction.LEFT -> Direction.DOWN
                }
                val nextPosition = beam.getNextPosition(nextDirection)
                if (isInBounds(nextPosition) && !isEnergised(nextPosition, nextDirection)) {
                    beams.add(Beam(nextPosition, nextDirection))
                }
            } else if (location == '-') {
                // splitter
                if (beam.direction == Direction.LEFT || beam.direction == Direction.RIGHT) {
                    // treat as empty space
                    val nextPosition = beam.getNextPosition(beam.direction)
                    if (isInBounds(nextPosition) && !isEnergised(nextPosition, beam.direction)) {
                        beams.add(Beam(nextPosition, beam.direction))
                    }
                } else {
                    val nextPositionLeft = beam.getNextPosition(Direction.LEFT)
                    if (isInBounds(nextPositionLeft) && !isEnergised(nextPositionLeft, Direction.LEFT)) {
                        beams.add(Beam(nextPositionLeft, Direction.LEFT))
                    }
                    val nextPositionRight = beam.getNextPosition(Direction.RIGHT)
                    if (isInBounds(nextPositionRight) && !isEnergised(nextPositionRight, Direction.RIGHT)) {
                        beams.add(Beam(nextPositionRight, Direction.RIGHT))
                    }
                }
            } else if (location == '|') {
                // splitter
                if (beam.direction == Direction.UP || beam.direction == Direction.DOWN) {
                    // treat as empty space
                    val nextPosition = beam.getNextPosition(beam.direction)
                    if (isInBounds(nextPosition) && !isEnergised(nextPosition, beam.direction)) {
                        beams.add(Beam(nextPosition, beam.direction))
                    }
                } else {
                    val nextPositionUp = beam.getNextPosition(Direction.UP)
                    if (isInBounds(nextPositionUp) && !isEnergised(nextPositionUp, Direction.UP)) {
                        beams.add(Beam(nextPositionUp, Direction.UP))
                    }
                    val nextPositionDown = beam.getNextPosition(Direction.DOWN)
                    if (isInBounds(nextPositionDown) && !isEnergised(nextPositionDown, Direction.DOWN)) {
                        beams.add(Beam(nextPositionDown, Direction.DOWN))
                    }
                }
            }
        }

        return energised.sumOf { row -> row.count { it != 0 } }
    }

    fun part2(): Int {
        return 0
    }
}

fun main() {
    val aoc = Day16("day16/input.txt")
    println(aoc.part1())
    println(aoc.part2())
}