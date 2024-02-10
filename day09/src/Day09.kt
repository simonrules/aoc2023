import java.io.File

class Day09(path: String) {
    val list = mutableListOf<List<Long>>()
    init {
        File(path).forEachLine { line ->
            val values = line.split(" ").map { it.toLong() }
            list.add(values)
        }
    }

    fun part1(): Int {
        list.forEach { println(it) }
        return 0
    }
}

fun main() {
    val day09 = Day09("day09/test1.txt")
    println(day09.part1())
}
