import java.io.File
import kotlin.math.max

data class CubeCount(val r: Int, val g: Int, val b: Int)

data class Game(val number: Int, val reveals: List<CubeCount>) {
    fun isPossible(r: Int, g: Int, b: Int): Boolean {
        reveals.forEach {
            if (it.r > r || it.g > g || it.b > b) {
                return false
            }
        }
        return true
    }

    fun power(): Int {
        var r = 0
        var g = 0
        var b = 0
        reveals.forEach {
            r = max(r, it.r)
            g = max(g, it.g)
            b = max(b, it.b)
        }

        return r * g * b
    }
}

class Day02(filename: String) {
    private val games = mutableListOf<Game>()

    init {
        File(filename).forEachLine { line ->
            val game = line.split(": ")
            val gameNumber = game[0].drop(5).toInt()
            val reveal = game[1].split("; ")
            val reveals = mutableListOf<CubeCount>()
            reveal.forEach { r ->
                val cube = r.split(", ")
                var red = 0
                var green = 0
                var blue = 0
                cube.forEach { c ->
                    val numberColour = c.split(" ")
                    if (numberColour[1] == "red") {
                        red = numberColour[0].toInt()
                    } else if (numberColour[1] == "green") {
                        green = numberColour[0].toInt()
                    } else {
                        blue = numberColour[0].toInt()
                    }
                    reveals.add(CubeCount(red, green, blue))
                }
            }
            games.add(Game(gameNumber, reveals.toList()))
        }
    }

    fun part1(): Int {
        var sum = 0
        games.forEach {
            if (it.isPossible(12, 13, 14)) {
                sum += it.number
            }
        }
        return sum
    }

    fun part2(): Long {
        var sum = 0L
        games.forEach {
            sum += it.power()
        }
        return sum
    }
}

fun main() {
    val day02 = Day02("day02/input.txt")
    println(day02.part1())
    println(day02.part2())
}
