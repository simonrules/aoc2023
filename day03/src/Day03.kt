import java.io.File

data class Part(val startX: Int, val endX: Int, val y: Int, val code: Int)

class Day03(filename: String) {
    val schematic = File(filename).readLines()
    val parts = mutableListOf<Part>()

    init {
        schematic.forEach { line ->
            line.forEachIndexed { index, c ->
                if (c.isDigit()) {

                }
            }
        }
    }

    fun part1(): Int {
        return 0
    }
}

fun main() {
    val day03 = Day03("day03/test1.txt")
    println(day03.part1())
}
