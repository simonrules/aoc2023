import java.io.File

data class Race(val time: Int, val distance: Int)

class Day06(filename: String) {
    private val race = mutableListOf<Race>()

    init {
        val line = File(filename).readLines()
        val time = line[0].drop(9).trim().split("\\s+".toRegex()).map { it.toInt() }
        val distance = line[1].drop(9).trim().split("\\s+".toRegex()).map { it.toInt() }

        time.forEachIndexed { i, t ->
            race.add(Race(t, distance[i]))
        }
    }

    fun doesWin(): Boolean {
        return false
    }

    fun part1(): Long {
        race.forEach {
            for (t in 1 until it.time) {
                
            }
        }
        return 0L
    }


    fun part2(): Long {

        return 0L
    }
}

fun main() {
    val day06 = Day06("day06/input.txt")
    println(day06.part1())
    println(day06.part2())
}
