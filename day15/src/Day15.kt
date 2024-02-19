import java.io.File

class Day15(path: String) {
    val input = File(path).readText().trim().split(",")

    fun getHash(string: String): Int {
        var value = 0
        string.forEach {
            value = (value + it.code) * 17 % 256
        }
        return value
    }

    fun part1(): Long {
        var sum = 0L
        input.forEach {
            sum += getHash(it)
        }
        return sum
    }
}

fun main() {
    val day15 = Day15("day15/input.txt")
    println(day15.part1())
}
