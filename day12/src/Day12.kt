import java.io.File

class Day12(private val path: String) {
    private val operational = '.'
    private val damaged = '#'
    private val unknown = '?'

    // #.#.### 1,1,3
    private fun isValid(input: String, damagedGroups: List<Int>): Boolean {
        var start = 0

        // Skip over any undamaged springs
        while (start < input.length && input[start] != damaged) {
            start++
        }

        damagedGroups.forEach { length ->
            val end = start + length
            val subString = input.subSequence(start, end)
            // Stop if any of the springs are not damaged
            if (subString.any { it != damaged } ) {
                return false
            }
            start = end

            // Skip over any undamaged springs
            while (start < input.length && input[start] != damaged) {
                start++
            }
        }

        // Ensure that everything is matched
        return start == input.length
    }

    data class State(val start: Int, val length: Int)

    private fun solve(springs: String, damagedGroups: List<Int>) {
        // dfs

    }

    fun part1(): Int {
        // Parse input file
        File(path).forEachLine { line ->
            val parts = line.split(" ")
            val groups = parts[1].split(",").map { it.toInt() }
            solve(parts[0], groups)
        }



        return 0
    }
}

fun main() {
    val day12 = Day12("day12/test2.txt")
    println(day12.part1())
}
