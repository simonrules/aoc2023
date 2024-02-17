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

    private fun doesReflect(list: List<Long>, exclude: Int): Int {
        for (cut in 0 until list.size - 1) {
            // Row or column to skip, -1 if none
            if (exclude != -1 && exclude == cut + 1) {
                continue
            }

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

    private fun reflectsHorizontally(block: List<String>, exclude: Int = -1): Int {
        val rows = mutableListOf<Long>()

        block.forEach {
            rows.add(stringHash(it))
        }

        return doesReflect(rows, exclude)
    }

    private fun reflectsVertically(block: List<String>, exclude: Int = -1): Int {
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

        return doesReflect(cols, exclude)
    }

    private fun locateMirrorSmudge(blockIndex: Int, size: Pair<Int, Int>): Int {
        val block = blocks[blockIndex]
        val excludeLeft = originalReflection[blockIndex].first
        val excludeAbove = originalReflection[blockIndex].second

        // Try every combo
        for (y in 0 until size.second) {
            for (x in 0 until size.first) {
                val inverse = when (block[y][x]) {
                    '#' -> "."
                    else -> "#"
                }

                val newBlock = block.toMutableList()
                newBlock[y] = newBlock[y].replaceRange(x, x + 1, inverse)

                val left = reflectsVertically(newBlock, excludeLeft)
                if (left != -1) {
                    // Stop when found
                    return left
                }
                val above = reflectsHorizontally(newBlock, excludeAbove)
                if (above != -1) {
                    return above * 100
                }
            }
        }

        // None found
        return -1
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

            // Stash these for part 2
            sizes.add(Pair(width, height))
            originalReflection.add(Pair(left, above))
        }

        return sum
    }

    fun part2(): Long {
        var sum = 0L

        // Try each block with an inverted # or .
        blocks.indices.forEach { i ->
            val ret = locateMirrorSmudge(i, sizes[i])
            if (ret != -1) {
                sum += ret
            }
        }

        return sum
    }
}

fun main() {
    val day13 = Day13("day13/input.txt")
    println(day13.part1())
    println(day13.part2())
}
