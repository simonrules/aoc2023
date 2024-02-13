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
    private val frequencyCard: List<Pair<Int, Char>>

    init {
        val frequency = mutableMapOf<Char, Int>()
        value.forEach {
            frequency[it] = 1 + frequency.getOrDefault(it, 0)
        }
        cardFrequency = frequency.toMap()
        frequencyCard = cardFrequency.map { Pair(it.value, it.key) }.sortedByDescending { it.first }
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

    private val numJokers: Int
        get() = value.count { it == 'J' }

    private fun getOriginalStrength(): Int {
        // Five of a kind
        if (frequencyCard.size == 1) {
            return 7
        }

        // Four of a kind
        if (frequencyCard[0].first == 4) {
            return 6
        }

        // Full house
        if ((frequencyCard[0].first == 3) && (frequencyCard[1].first == 2)) {
            return 5
        }

        // Three of a kind
        if (frequencyCard[0].first == 3) {
            return 4
        }

        // Two pair
        if ((frequencyCard[0].first == 2) && (frequencyCard[1].first == 2)) {
            return 3
        }

        // One pair
        if (frequencyCard[0].first == 2) {
            return 2
        }

        return 1
    }

    fun getStrength(): Int {
        val originalStrength = getOriginalStrength()

        if (!usesJoker) {
            return originalStrength
        }

        when (numJokers) {
            0 -> {
                // no change
                return originalStrength
            }
            1 -> {
                return when (originalStrength) {
                    1 -> 2 // high card to pair
                    2 -> 4 // one pair to three of a kind
                    3 -> 5 // two pair to full house
                    4 -> 6 // three to four of a kind
                    6 -> 7 // four to five of a kind
                    else -> originalStrength
                }
            }
            2 -> {
                return when (originalStrength) {
                    2 -> 4 // pair to three of a kind
                    3 -> 6 // two pair to four of a kind
                    5 -> 7 // full house to five of a kind
                    else -> originalStrength
                }
            }
            3 -> {
                return when (originalStrength) {
                    4 -> 6 // three to four of a kind
                    5 -> 7 // full house to five of a kind
                    else -> originalStrength
                }
            }
            4 -> {
                return when (originalStrength) {
                    6 -> 7 // four to five of a kind
                    else -> originalStrength
                }
            }
            else -> return originalStrength
        }
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

    fun part2(): Int {
        val handBids = mutableMapOf<Hand, Int>()
        val handStrengths = mutableListOf<Pair<Hand, Int>>()
        var sum = 0
        var rank = 1

        // Pre-process input list
        inputList.forEach {
            val hand = Hand(it.first, usesJoker = true)
            handBids[hand] = it.second
            handStrengths.add(Pair(hand, hand.getStrength()))
        }

        for (strength in 1..7) {
            val hands = handStrengths.filter { it.second == strength }.map { it.first }
            if (hands.isNotEmpty()) {
                val sortedHands = hands.sorted()
                sortedHands.forEach {
                    sum += rank * handBids[Hand(it.value, usesJoker = true)]!!
                    rank++
                }
            }
        }

        return sum
    }
}

fun main() {
    val day07 = Day07("day07/input.txt")
    println(day07.part1())
    println(day07.part2())
}
