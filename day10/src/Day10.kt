import java.io.File

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
}

class Day10(path: String) {
    private val map = File(path).readLines()
    private val width = map[0].length
    private val height = map.size

    private fun locateStart(): Point2D {
        map.forEachIndexed { y, row ->
            val x = row.indexOf('S')
            if (x != -1) {
                return Point2D(x, y)
            }
        }

        return Point2D(-1, -1)
    }

    private fun getMapAt(pos: Point2D): Char {
        if (!isInBounds(pos)) {
            println("out of bounds: $pos")
        }
        return map[pos.y][pos.x]
    }

    private fun getValueAt(valueMap: List<MutableList<Int>>, pos: Point2D): Int {
        if (!isInBounds(pos)) {
            println("out of bounds: $pos")
        }
        return valueMap[pos.y][pos.x]
    }

    private fun setValueAt(valueMap: List<MutableList<Int>>, pos: Point2D, value: Int) {
        if (!isInBounds(pos)) {
            println("out of bounds: $pos")
        }
        valueMap[pos.y][pos.x] = value
    }

    private fun allocateDistanceMap(): List<MutableList<Int>> {
        val distanceMap = mutableListOf<MutableList<Int>>()
        repeat(height) {
            val row = mutableListOf<Int>()
            repeat(width) { row.add(-1) }
            distanceMap.add(row)
        }
        return distanceMap.toList()
    }

    private fun canMoveToNorth(pos: Point2D): Boolean {
        return getMapAt(pos) in "SLJ|" && isInBounds(pos.getNorth())
    }

    private fun canMoveToSouth(pos: Point2D): Boolean {
        return getMapAt(pos) in "SF7|" && isInBounds(pos.getSouth())
    }

    private fun canMoveToEast(pos: Point2D): Boolean {
        return getMapAt(pos) in "SFL-" && isInBounds(pos.getEast())
    }

    private fun canMoveToWest(pos: Point2D): Boolean {
        return getMapAt(pos) in "S7J-" && isInBounds(pos.getWest())
    }

    private fun isInBounds(pos: Point2D): Boolean {
        return pos.x in 0..<width && pos.y in 0..<height
    }

    // consider 4 NSEW neighbours to get first step
    // - and |
    // top-left corner    - F 7 - top-right corner
    // bottom-left corner - L J - bottom-right corner
    private fun doDistanceWalk(): Int {
        val start = locateStart()
        val distanceMap = allocateDistanceMap()
        var steps = 0
        var direction = 'S'
        var position = start

        while (getValueAt(distanceMap, position) == -1) {
            setValueAt(distanceMap, position, steps)

            // Move
            position = when (direction) {
                'N' -> position.getNorth()
                'S' -> position.getSouth()
                'E' -> position.getEast()
                'W' -> position.getWest()
                else -> Point2D(-1, -1)
            }
            steps++

            // Choose next direction, can't be reverse of what we just did
            direction = when (direction) {
                'N' -> {
                    if (canMoveToNorth(position)) {
                        'N'
                    } else if (canMoveToEast(position)) {
                        'E'
                    } else if (canMoveToWest(position)) {
                        'W'
                    } else {
                        ' '
                    }
                }

                'S' -> {
                    if (canMoveToSouth(position)) {
                        'S'
                    } else if (canMoveToEast(position)) {
                        'E'
                    } else if (canMoveToWest(position)) {
                        'W'
                    } else {
                        ' '
                    }
                }

                'E' -> {
                    if (canMoveToEast(position)) {
                        'E'
                    } else if (canMoveToNorth(position)) {
                        'N'
                    } else if (canMoveToSouth(position)) {
                        'S'
                    } else {
                        ' '
                    }
                }

                'W' -> {
                    if (canMoveToWest(position)) {
                        'W'
                    } else if (canMoveToNorth(position)) {
                        'N'
                    } else if (canMoveToSouth(position)) {
                        'S'
                    } else {
                        ' '
                    }
                }

                else -> ' '
            }
        }

        return steps / 2
    }

    fun part1(): Int {
        return doDistanceWalk()
    }
}

fun main() {
    val day10 = Day10("day10/input.txt")
    println(day10.part1())
}
