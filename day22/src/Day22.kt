package com.simonrules.aoc2023.day22

import com.simonrules.aoc2023.utils.Point2D
import com.simonrules.aoc2023.utils.Point3D
import java.io.File
import kotlin.math.absoluteValue

data class Brick(val p0: Point3D, val p1: Point3D): Comparable<Brick> {
    val volume = ((p0.x - p1.x).absoluteValue + 1) *
            ((p0.y - p1.y).absoluteValue + 1) *
            ((p0.z - p1.z).absoluteValue + 1)

    val maxX = if (p0.x > p1.x) p0.x else p1.x
    val maxY = if (p0.y > p1.y) p0.y else p1.y
    val maxZ = if (p0.z > p1.z) p0.z else p1.z

    fun setPosition(p: Point3D): Brick {
        val dx = p1.x - p0.x
        val dy = p1.y - p0.y
        val dz = p1.z - p0.z
        return Brick(p, Point3D(p.x + dx, p.y + dy, p.z + dz))
    }

    fun setZ(z: Int): Brick {
        val dz = p1.z - p0.z
        return Brick(Point3D(p0.x, p0.y, z), Point3D(p1.x, p1.y, z + dz))
    }

    fun crossSection(): Set<Point2D> {
        val cubes = mutableSetOf<Point2D>()
        for (x in p0.x..p1.x) {
            for (y in p0.y..p1.y) {
                cubes.add(Point2D(x, y))
            }
        }
        return cubes.toSet()
    }

    operator fun contains(p: Point3D): Boolean {
        return p.x in p0.x..p1.x && p.y in p0.y..p1.y && p.z in p0.z..p1.z
    }

    override fun compareTo(other: Brick): Int {
        // sort by p0.z ascending. Next sort by block height?
        //return if (p0.z > other.p0.z) 1 else -1
        // sort by p0.z ascending, then by block height
        return if (p0.z > other.p0.z) {
            1
        } else if (p0.z < other.p0.z) {
            -1
        } else {
            if ((p1.z - p0.z) > (other.p1.z - other.p0.z)) 1 else -1
        }
    }
}

class Stack(private val bricks: List<Brick>) {
    private val maxX: Int = bricks.maxOf { it.maxX }
    private val maxY: Int = bricks.maxOf { it.maxY }
    private val maxZ: Int = bricks.maxOf { it.maxZ }

    fun printXZ() {
        for (z in maxZ downTo 1) {
            for (x in 0..maxX) {
                var brickChar = '.'
                // Print the char for the brick closest to the observer
                for (y in maxY downTo 0) {
                    var c = 'A'
                    for (brick in bricks) {
                        if (Point3D(x, y, z) in brick) {
                            brickChar = c
                        }
                        c++
                    }
                }
                print(brickChar)
            }
            println()
        }
        // Print floor
        repeat(maxX + 1) {
            print('-')
        }
        println()
    }

    fun printYZ() {
        for (z in maxZ downTo 1) {
            for (y in 0..maxY) {
                var brickChar = '.'
                // Print the char for the brick closest to the observer
                for (x in maxX downTo 0) {
                    var c = 'A'
                    for (brick in bricks) {
                        if (Point3D(x, y, z) in brick) {
                            brickChar = c
                        }
                        c++
                    }
                }
                print(brickChar)
            }
            println()
        }
        // Print floor
        repeat(maxY + 1) {
            print('-')
        }
        println()
    }

    fun anyUpsideDown(): Boolean {
        return bricks.any { it.p0.z > it.p1.z }
    }

    // bricks fall until they hit the bottom or a brick below them.
    fun fall(): Stack {
        val bricksSorted = bricks.sorted()

        val bricksFallen = mutableListOf<Brick>()

        var brickChar = 'A'
        for (b in bricksSorted) {
            // drop a brick
            val fallen = drop(b, bricksFallen.reversed())
            println("brick $brickChar fell from ${b.p0.z} to ${fallen.p0.z}")
            brickChar++
            bricksFallen.add(fallen)
        }

        return Stack(bricksFallen)
    }

    // Assumes that the list of bricks is already sorted with the first at the bottom
    fun canDisintegrate(above: Brick): Boolean {
        return false
    }

    private fun drop(above: Brick, belowList: List<Brick>): Brick {
        // drop until it hits something

        // given a set of cubes from the bottom of this brick
        // and a set of cubes from the top of the brick below
        // check for intersection

        val aboveCrossSection = above.crossSection()

        belowList.forEach { below ->
            val belowCrossSection = below.crossSection()

            if (aboveCrossSection.intersect(belowCrossSection).isNotEmpty()) {
                // fall to right above this brick

                return above.setZ(below.p1.z + 1)
            }
        }

        // nothing below, drop to the floor
        return above.setZ(1)
    }
}

class Day22(filename: String) {
    private val bricks = File(filename).readLines().map {
        val (left, right) = it.split("~")
        val (x0, y0, z0) = left.split((","))
        val (x1, y1, z1) = right.split((","))
        Brick(Point3D(x0.toInt(), y0.toInt(), z0.toInt()), Point3D(x1.toInt(), y1.toInt(), z1.toInt()))
    }
    private val stack = Stack(bricks)

    fun part1(): Int {
        if (stack.anyUpsideDown()) {
            println("block found with p0.z > p1.z, exiting")
            return 0
        }

        //stack.printXZ()
        //println()
        //stack.printYZ()
        //println()

        val newStack = stack.fall()

        newStack.printXZ()
        println()
        newStack.printYZ()

        return 0
    }
}

fun main() {
    val aoc = Day22("day22/test1.txt")
    println(aoc.part1())
}
