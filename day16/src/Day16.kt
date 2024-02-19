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

    private fun getMapAt(map: List<String>, pos: Point2D): Char {
        return map[pos.y][pos.x]
    }

    private fun isInBounds(pos: Point2D): Boolean {
        return pos.x in 0..<width && pos.y in 0..<height
    }

    private fun energise(values: Array<Array<Int>>, pos: Point2D, direction: Direction) {
        values[pos.y][pos.x] = values[pos.y][pos.x] or direction.value
    }

    private fun isEnergised(values: Array<Array<Int>>, pos: Point2D, direction: Direction): Boolean {
        return values[pos.y][pos.x] and direction.value != 0
    }

    private fun checkAndMaybeAddBeam(
        beams: MutableList<Beam>,
        energised: Array<Array<Int>>,
        beam: Beam,
        nextDirection: Direction
    ) {
        val nextPosition = beam.getNextPosition(nextDirection)
        if (isInBounds(nextPosition) && !isEnergised(energised, nextPosition, nextDirection)) {
            beams.add(Beam(nextPosition, nextDirection))
        }
    }

    private fun mirrorUpToLeft(direction: Direction): Direction {
        return when (direction) {
            Direction.UP -> Direction.LEFT
            Direction.RIGHT -> Direction.DOWN
            Direction.DOWN -> Direction.RIGHT
            Direction.LEFT -> Direction.UP
        }
    }

    private fun mirrorUpToRight(direction: Direction): Direction {
        return when (direction) {
            Direction.UP -> Direction.RIGHT
            Direction.RIGHT -> Direction.UP
            Direction.DOWN -> Direction.LEFT
            Direction.LEFT -> Direction.DOWN
        }
    }

    private fun solve(start: Beam): Int {
        val energised = Array(height) { Array(width) { 0 } }
        val beams = mutableListOf<Beam>()
        beams.add(start)

        while (beams.isNotEmpty()) {
            val beam = beams.removeFirst()
            energise(energised, beam.position, beam.direction)

            // Test map location
            val location = getMapAt(map, beam.position)
            if (location == '.') {
                // empty space
                checkAndMaybeAddBeam(beams, energised, beam, beam.direction)
            } else if (location == '\\') {
                // mirror
                checkAndMaybeAddBeam(beams, energised, beam, mirrorUpToLeft(beam.direction))
            } else if (location == '/') {
                // mirror
                checkAndMaybeAddBeam(beams, energised, beam, mirrorUpToRight(beam.direction))
            } else if (location == '-') {
                // splitter
                if (beam.direction == Direction.LEFT || beam.direction == Direction.RIGHT) {
                    // treat as empty space
                    checkAndMaybeAddBeam(beams, energised, beam, beam.direction)
                } else {
                    checkAndMaybeAddBeam(beams, energised, beam, Direction.LEFT)
                    checkAndMaybeAddBeam(beams, energised, beam, Direction.RIGHT)
                }
            } else if (location == '|') {
                // splitter
                if (beam.direction == Direction.UP || beam.direction == Direction.DOWN) {
                    // treat as empty space
                    checkAndMaybeAddBeam(beams, energised, beam, beam.direction)
                } else {
                    checkAndMaybeAddBeam(beams, energised, beam, Direction.UP)
                    checkAndMaybeAddBeam(beams, energised, beam, Direction.DOWN)
                }
            }
        }

        return energised.sumOf { row -> row.count { it != 0 } }
    }

    fun part1(): Int {
        return solve(initialBeam)
    }

    fun part2(): Int {
        var mostEnergised = 0

        for (x in 0 until width) {
            var beam = Beam(Point2D(x, 0), Direction.DOWN)
            var count = solve(beam)
            if (count > mostEnergised) {
                mostEnergised = count
            }

            beam = Beam(Point2D(x, height - 1), Direction.UP)
            count = solve(beam)
            if (count > mostEnergised) {
                mostEnergised = count
            }
        }

        for (y in 0 until height) {
            var beam = Beam(Point2D(0, y), Direction.RIGHT)
            var count = solve(beam)
            if (count > mostEnergised) {
                mostEnergised = count
            }

            beam = Beam(Point2D(width - 1, y), Direction.LEFT)
            count = solve(beam)
            if (count > mostEnergised) {
                mostEnergised = count
            }
        }

        return mostEnergised
    }
}

fun main() {
    val aoc = Day16("day16/input.txt")
    println(aoc.part1())
    println(aoc.part2())
}