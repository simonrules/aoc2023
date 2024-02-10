import java.io.File

data class Hand(val value: String, private val usesJoker: Boolean = false): Comparable<Hand> {
    private val rank = if (usesJoker) mapOf(
        'A' to 14,
        'K' to 13,
        'Q' to 12,
        'T' to 10,
        '9' to 9,
        '8' to 8,
        '7' to 7,
        '6' to 6,
        '5' to 5,
        '4' to 4,
        '3' to 3,
        '2' to 2,
        'J' to 1,
    ) else mapOf(
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

    private val cardFrequency: Map<Char, Int>

    init {
        val frequency = mutableMapOf<Char, Int>()
        value.forEach {
            frequency[it] = 1 + frequency.getOrDefault(it, 0)
        }
        cardFrequency = frequency.toMap()
    }

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

    val numJokers: Int
        get() = value.count { it == 'J' }

    fun getStrength(): Int {
        val values = cardFrequency.values.sorted().reversed()

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
}

class Day07(filename: String) {
    private val inputList: List<Pair<String, Int>>

    init {
        val list = mutableListOf<Pair<String, Int>>()
        File(filename).forEachLine {
            val (cards, bid) = it.split(" ")
            list.add(Pair(cards, bid.toInt()))
        }
        inputList = list.toList()
    }

    fun part1(): Int {
        val handBids = mutableMapOf<Hand, Int>()
        val handStrengths = mutableListOf<Pair<Hand, Int>>()
        var sum = 0
        var rank = 1

        // Pre-process input list
        inputList.forEach {
            val hand = Hand(it.first)
            handBids[hand] = it.second
            handStrengths.add(Pair(hand, hand.getStrength()))
        }

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

    private fun upgradeHand(hand: Hand): Hand {
        //max-hand-strength = hand-strength
        //for each non-joker in hand:
        //  swap each joker with non-joker
        //  if new-hand-strength > max-hand-strength:
        //      max-hand-strength = new-hand-strength
        //return max-hand-strength
        //val maxHandStrength = hand.
        return hand
    }

    fun part2(): Int {
        val hands = listOf(Hand("T55J5", true), Hand("KTJJT", true), Hand("QQQJA", true))
        val upgradedHands = hands.map { if (it.numJokers > 0) upgradeHand(it) else it }
        //hands.sorted().forEach { println(it.value) }
        return 0
    }
}

fun main() {
    val day07 = Day07("day07/input.txt")
    println(day07.part1())
    //println(day07.part2())
}
