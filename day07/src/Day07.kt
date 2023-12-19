import java.io.File

class Day07(filename: String) {
    private val rank = mapOf(
        'A' to 14,
        'K' to 13,
        'Q' to 12,
        'J' to 11,
        'T' to 10,
        '9' to 9,
        '8' to 8,
        '7' to 7,
        '6' to 6,
        '5' to 5,
        '4' to 4,
        '3' to 3,
        '2' to 2,
    )

    private val handBids = mutableMapOf<String, Int>()
    private val handStrengths = mutableListOf<Pair<String, Int>>()

    init {
        File(filename).forEachLine {
            val (hand, bid) = it.split(" ")
            handBids[hand] = bid.toInt()
            val strength = getHandStrength(countFrequency(hand))
            handStrengths.add(Pair(hand, strength))
        }
    }

    private fun countFrequency(hand: String): Map<Char, Int> {
        val frequency = mutableMapOf<Char, Int>()
        hand.forEach {
            frequency[it] = 1 + frequency.getOrDefault(it, 0)
        }
        return frequency.toMap()
    }

    private fun getHandStrength(frequency: Map<Char, Int>): Int {
        val values = frequency.values.sorted().reversed()

        // Five of a kind
        if (values.size == 1) {
            return 1
        }

        // Four of a kind
        if (values[0] == 4) {
            return 2
        }

        // Full house
        if ((values[0] == 3) && (values[1] == 2)) {
            return 3
        }

        // Three of a kind
        if (values[0] == 3) {
            return 4
        }

        // Two pair
        if ((values[0] == 2) && (values[1] == 2)) {
            return 5
        }

        // One pair
        if (values[0] == 2) {
            return 6
        }

        return 7
    }

    private fun compareHands(hand1: String, hand2: String): Int {
        hand1.forEachIndexed { i, value ->
            if (rank[value]!! > rank[hand2[i]]!!) {
                return 1
            } else if (rank[value]!! < rank[hand2[i]]!!) {
                return -1
            }
        }

        return 0
    }

    fun part1(): Int {
        handStrengths.sortBy { it.second }
        return 0
    }

    fun part2(): Int {
        return 0
    }
}

fun main() {
    val day07 = Day07("day07/test1.txt")
    println(day07.part1())
    println(day07.part2())
}
