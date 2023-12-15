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
        // Easier just to hard code these from input.txt
        val time = 49787980L
        val distance = 298118510661181L

        // We need to efficiently know how many possible distances are longer than distance.
        // Since the distances up to the longest are sorted, use binary search to find where we
        // start beating the record.
        var low = 1L
        var high = time / 2L

        while (low < high) {
            val guess = (low + high) / 2L
            val product = guess * (time - guess)
            if (product <= distance) {
                low = guess + 1
            } else {
                high = guess
            }
        }
        val resultTime = high - 1

        // If time is even, subtract 1 due to duplicate distance
        val even = if (time % 2L == 0L) 1L else 0L
        val ways = ((time / 2L) - resultTime) * 2L - even

        return ways
    }
}

fun main() {
    val day06 = Day06("day06/input.txt")
    println(day06.part1())
    println(day06.part2())
}
