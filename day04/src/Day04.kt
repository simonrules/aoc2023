import java.io.File
import kotlin.math.pow

data class Card(val number: Int, val winning: Set<Int>, val numbers: Set<Int>) {
    fun numWinning(): Int {
        return numbers.intersect(winning).size
    }
}

class Day04(filename: String) {
    private val cards = mutableListOf<Card>()

    init {
        File(filename).forEachLine { line ->
            val card = line.split(": ")
            val number = card[0].drop(5).trim().toInt()
            val part = card[1].split(" | ")
            val winning = part[0].trim().split("\\s+".toRegex()).map { it.toInt() }.toSet()
            val numbers = part[1].trim().split("\\s+".toRegex()).map { it.toInt() }.toSet()
            cards.add(Card(number, winning, numbers))
        }
    }
    fun part1(): Int {
        var sum = 0
        cards.forEach {
            sum += 2.0.pow(it.numWinning() - 1).toInt()
        }
        return sum
    }

    fun part2(): Int {
        val processed = mutableMapOf<Int, Int>()
        cards.forEach { processed[it.number] = 1 }

        for (c in 1..cards.last().number) {
            repeat(processed[c]!!) {
                val numWinning = cards[c - 1].numWinning()
                for (copy in c + 1..c + numWinning) {
                    processed[copy] = processed[copy]!! + 1
                }
            }
        }

        var sum = 0
        processed.forEach { sum += it.value }
        return sum
    }
}

fun main() {
    val day04 = Day04("day04/input.txt")
    println(day04.part1())
    println(day04.part2())
}
