import java.io.File

class Day08(path: String) {
    private val instructions: String
    private val network = mutableMapOf<String, Pair<String, String>>()

    init {
        val regex = "(.+) = \\((.+), (.+)\\)".toRegex()
        val block = File(path).readText().split("\n\n")
        instructions = block[0]
        val lines = block[1].trim().split("\n")
        lines.forEach { line ->
            val (d, l, r) = regex.matchEntire(line)!!.destructured
            network[d] = Pair(l, r)
        }
    }

    private fun getNextInstruction(step: Int): Char {
        val next = instructions[step % instructions.length]

        return next
    }

    private fun countFrom(start: String): Int {
        var next = start
        var count = 0
        do {
            val goLeft = getNextInstruction(count) == 'L'
            next = if (goLeft) network[next]!!.first else network[next]!!.second
            count++
        } while (next.last() != 'Z')

        return count
    }

    fun part1(): Int {
        return countFrom("AAA")
    }

    fun part2Simultaneous(): Int {
        val aNodes = network.keys.filter { it.last() == 'A' }

        var next = aNodes.toList()
        var count = 0
        do {
            val goLeft = getNextInstruction(count) == 'L'
            next = next.map { if (goLeft) network[it]!!.first else network[it]!!.second }
            count++
        } while (!next.all { it.last() == 'Z' })

        return count
    }

    fun part2(): Long {
        val aNodes = network.keys.filter { it.last() == 'A' }

        var product = 1L
        //aNodes.forEach { product *= countFrom(it) }
        aNodes.forEach { println(countFrom(it)) }

        return product
    }
}

fun main() {
    val day08 = Day08("day08/input.txt")
    println(day08.part1())
    // 11188774513823 is the LCM of the output
    println(day08.part2())
}
