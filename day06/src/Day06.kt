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

    private fun computePossibilities(time: Int): List<Int> {
        val possibilities = mutableListOf<Int>()

        for (t in 1 until time) {
            possibilities.add(t * (time - t))
        }

        return possibilities.toList()
    }

    fun part1(): Int {
        var product = 1
        for (r in race) {
            val possibilities = computePossibilities(r.time)
            product *= possibilities.count { it > r.distance }
        }

        return product
    }

    fun part2(): Long {
        // For odd times, sequence from largest two down is diff 2, 4, 6, 8...
        // For even, sequence from largest one down is diff 1, 3, 5, 7...
        // Both test and input are even.
        val time = 30L
        val distance = 200L

        // The longest distance is in the middle
        val longest = (time / 2L) * (time - time / 2L)

        // Now we need to know how many possible distances are longer than distance
        // Try binary search
        var low = 1L
        var high = time / 2L

        while (low != high) {
            val guess = low + (high - low) / 2L
            val product = guess * (time - guess)
            if (product < distance) {
                low = guess
            } else if (product > distance) {
                high = guess
            }
        }

        return 0
    }
}

fun main() {
    val day06 = Day06("day06/test1.txt")
    println(day06.part1())
    println(day06.part2())
}
