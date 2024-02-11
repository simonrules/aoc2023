import java.io.File

class Day12(val path: String) {
    private fun solve(springs: String, brokenGroups: List<Int>) {
        println(brokenGroups)
    }

    fun part1(): Int {
        File(path).forEachLine { line ->
            val parts = line.split(" ")
            val groups = parts[1].split(",").map { it.toInt() }
            solve(parts[0], groups)
        }
        return 0
    }
}

fun main() {
    val day12 = Day12("day12/test1.txt")
    println(day12.part1())
}
