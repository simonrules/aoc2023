import java.io.File

data class Hand(val value: String): Comparable<Hand> {
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

    override fun compareTo(other: Hand): Int {
        value.forEachIndexed { i, value ->
            if (rank[value]!! > rank[other.value[i]]!!) {
                return 1
            } else if (rank[value]!! < rank[other.value[i]]!!) {
                return -1
            }
        }

        return 0
    }
}

class Day07(filename: String) {
    private val handBids = mutableMapOf<Hand, Int>()
    private val handStrengths = mutableListOf<Pair<Hand, Int>>()

    init {
        File(filename).forEachLine {
            val (hand, bid) = it.split(" ")
            handBids[Hand(hand)] = bid.toInt()
            val strength = getHandStrength(countFrequency(hand))
            handStrengths.add(Pair(Hand(hand), strength))
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
            return 7
        }

        // Four of a kind
        if (values[0] == 4) {
            return 6
        }

        // Full house
        if ((values[0] == 3) && (values[1] == 2)) {
            return 5
        }

        // Three of a kind
        if (values[0] == 3) {
            return 4
        }

        // Two pair
        if ((values[0] == 2) && (values[1] == 2)) {
            return 3
        }

        // One pair
        if (values[0] == 2) {
            return 2
        }

        return 1
    }

    fun part1(): Int {
        var sum = 0
        var rank = 1

        // I could simplify this by using sort to sort by two things (rank, strength)
        for (strength in 1..7) {
            val hands = handStrengths.filter { it.second == strength }.map { it.first }
            if (hands.isNotEmpty()) {
                val sortedHands = hands.sorted()
                sortedHands.forEach {
                    sum += rank * handBids[Hand(it.value)]!!
                    rank++
                }
            }
        }

        return sum
    }

    fun part2(): Int {
        return 0
    }
}

fun main() {
    val day07 = Day07("day07/input.txt")
    println(day07.part1())
    println(day07.part2())
}
