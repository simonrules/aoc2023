import java.io.File

data class SchematicNumber(val startX: Int, val endX: Int, val y: Int, val code: Int)

class Day03(filename: String) {
    private val schematic = File(filename).readLines()
    private val schematicNumbers = mutableListOf<SchematicNumber>()
    private val symbols = mutableSetOf<Pair<Int, Int>>()
    private val possibleGears = mutableMapOf<Pair<Int, Int>, MutableList<Int>>()

    init {
        schematic.forEachIndexed { y, line ->
            var started = false
            var start = -1
            var end = -1
            line.forEachIndexed { x, c ->
                if (c.isDigit()) {
                    if (!started) {
                        start = x
                        started = true
                    }
                } else {
                    if (started) {
                        end = x - 1
                        started = false
                        schematicNumbers.add(SchematicNumber(start, end, y, line.substring(start, end + 1).toInt()))
                    }
                    if (c != '.') {
                        symbols.add(Pair(x, y))
                    }
                }
            }
            if (started) {
                end = line.length - 1
                schematicNumbers.add(SchematicNumber(start, end, y, line.substring(start, end + 1).toInt()))
            }
        }
    }

    private fun isPart(p: SchematicNumber): Boolean {
        val w = schematic[0].length
        val h = schematic.size

        for (y in p.y - 1..p.y + 1) {
            for (x in p.startX - 1..p.endX + 1) {
                if (y in 0 until h) {
                    if (x in 0 until w) {
                        val c = schematic[y][x]
                        if (!c.isDigit() && c != '.') {
                            if (c == '*') {
                                // Keep track of possible gears for part 2
                                if (!possibleGears.containsKey(Pair(x, y))) {
                                    possibleGears[Pair(x, y)] = mutableListOf()
                                }
                                possibleGears[Pair(x, y)]!!.add(p.code)
                            }
                            return true
                        }
                    }
                }
            }
        }
        return false
    }

    fun part1(): Int {
        var sum = 0
        schematicNumbers.forEach { p ->
            if (isPart(p)) {
                sum += p.code
            }
        }
        return sum
    }

    fun part2(): Long {
        var sum = 0L
        possibleGears.forEach {
            if (it.value.size == 2) {
                sum += it.value[0] * it.value[1]
            }
        }
        return sum
    }
}

fun main() {
    val day03 = Day03("day03/input.txt")
    println(day03.part1())
    println(day03.part2())
}
