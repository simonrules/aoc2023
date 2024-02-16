import java.io.File
import kotlin.math.min

class Day13(path: String) {
    private val blocks = mutableListOf<List<String>>()
    private val sizes = mutableListOf<Pair<Int, Int>>()
    private val originalReflection = mutableListOf<Pair<Int, Int>>()

    init {
        val sections = File(path).readText().trim().split("\n\n")
        sections.forEach {
            val b = it.split("\n")
            blocks.add(b)
        }
    }

    private fun stringHash(row: String): Long {
        var outputValue = 0L
        var bitValue = 1L
        row.forEach { c ->
            if (c == '#') {
                outputValue += bitValue
            }
            bitValue = bitValue.shl(1)
        }
        return outputValue
    }

    private fun doesReflect(list: List<Long>): Int {
        for (cut in 0 until list.size - 1) {
            val a = cut
            val b = cut + 1
            val size = min(list.size - cut - 1, cut + 1)
            var foundReflection = true
            for (i in 0 until size) {
                if (list[a - i] != list[b + i]) {
                    foundReflection = false
                }
            }
            if (foundReflection) {
                return cut + 1
            }
        }

        return -1
    }

    private fun reflectsHorizontally(block: List<String>): Int {
        val rows = mutableListOf<Long>()

        block.forEach {
            rows.add(stringHash(it))
        }

        return doesReflect(rows)
    }

    private fun reflectsVertically(block: List<String>): Int {
        val cols = mutableListOf<Long>()
        val width = block[0].length
        val height = block.size

        for (j in 0 until width) {
            var column = ""
            for (i in 0 until height) {
                column += block[i][j]
            }
            cols.add(stringHash(column))
        }

        return doesReflect(cols)
    }

    fun part1(): Long {
        var sum = 0L

        blocks.forEachIndexed { i, b ->
            val left = reflectsVertically(b)
            if (left != -1) {
                sum += left
            }
            val above = reflectsHorizontally(b)
            if (above != -1) {
                sum += above * 100L
            }

            val width = b[0].length
            val height = b.size
            sizes.add(Pair(width, height))
            originalReflection.add(Pair(left, above))
        }

        return sum
    }

    fun part2(): Long {
        // Try each block with an inverted # or .
        sizes.forEachIndexed { i, s ->
            for (x in 0 until s.first) {
                for (y in 0 until s.second) {
                    val b = blocks[i]
                    val originalChar = b[y][x]
                }
            }
        }
        return 0L
    }
}

fun main() {
    val day13 = Day13("day13/input.txt")
    println(day13.part1())
    println(day13.part2())
}
