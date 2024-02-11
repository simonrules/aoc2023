import java.io.File

class Day09(path: String) {
    val lists = mutableListOf<List<Long>>()
    init {
        File(path).forEachLine { line ->
            val values = line.split(" ").map { it.toLong() }
            lists.add(values)
        }
    }

    private fun diffBetweenElems(list: List<Long>): List<Long> {
        return list.zip(list.drop(1)) { a, b -> b - a }
    }

    private fun offsetToZero(list: List<Long>): List<Long> {
        return list.map { it - list[0] }
    }

    private fun getNext(list: List<Long>): Long {
        if (list.all { it == 0L }) {
            return 0
        }

        val next = list.last() + getNext(diffBetweenElems(list))
        return next
    }

    fun part1(): Long {
        var sum = 0L
        lists.forEach { list ->
            sum += getNext(list)
        }

        return sum
    }

    fun part2(): Int {
        return 0
    }
}

fun main() {
    val day09 = Day09("day09/input.txt")
    println(day09.part1())
    println(day09.part2())
}
