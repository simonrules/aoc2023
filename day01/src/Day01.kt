import java.io.File

data class DigitWithIndex(val digit: Char, val index: Int)

class Day01(private val filename: String) {
    private val numberWords =
        listOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")

    private fun getFirstDigit(line: String): DigitWithIndex {
        val firstIndex = line.indexOfFirst { it.isDigit() }
        val firstDigit = if (firstIndex == -1) 'x' else line[firstIndex]

        return DigitWithIndex(firstDigit, firstIndex)
    }

    private fun getLastDigit(line: String): DigitWithIndex {
        val lastIndex = line.indexOfLast { it.isDigit() }
        val lastDigit = if (lastIndex == -1) 'x' else line[lastIndex]

        return DigitWithIndex(lastDigit, lastIndex)
    }

    fun part1(): Int {
        val calibrationList = mutableListOf<Int>()

        File(filename).forEachLine { line ->
            val first = getFirstDigit(line).digit
            val last = getLastDigit(line).digit

            calibrationList.add("${first}${last}".toInt())
        }

        return calibrationList.sum()
    }

    private fun getFirstDigitWord(line: String): DigitWithIndex {
        var firstDigit = 'x'
        var firstIndex = -1
        numberWords.forEachIndexed { n, word ->
            val digit = (n + 1).digitToChar()
            val index = line.indexOf(word)
            if (index != -1) {
                if (firstIndex == -1 || index < firstIndex) {
                    firstDigit = digit
                    firstIndex = index
                }
            }
        }

        return DigitWithIndex(firstDigit, firstIndex)
    }

    private fun getLastDigitWord(line: String): DigitWithIndex {
        var lastDigit = 'x'
        var lastIndex = -1
        numberWords.forEachIndexed { n, word ->
            val digit = (n + 1).digitToChar()
            val index = line.lastIndexOf(word)
            if (index != -1) {
                if (lastIndex == -1 || index > lastIndex) {
                    lastDigit = digit
                    lastIndex = index
                }
            }
        }

        return DigitWithIndex(lastDigit, lastIndex)
    }

    fun part2(): Int {
        val calibrationList = mutableListOf<Int>()

        File(filename).forEachLine { line ->
            val firstDigit = getFirstDigit(line)
            val lastDigit = getLastDigit(line)
            val firstDigitWord = getFirstDigitWord(line)
            val lastDigitWord = getLastDigitWord(line)

            val first = if (firstDigit.index == -1) {
                firstDigitWord.digit
            } else if (firstDigitWord.index == -1) {
                firstDigit.digit
            } else {
                if (firstDigit.index < firstDigitWord.index) firstDigit.digit else firstDigitWord.digit
            }

            val last = if (lastDigit.index == -1) {
                lastDigitWord.digit
            } else if (lastDigitWord.index == -1) {
                lastDigit.digit
            } else {
                if (lastDigit.index > lastDigitWord.index) lastDigit.digit else lastDigitWord.digit
            }

            calibrationList.add("${first}${last}".toInt())
        }

        return calibrationList.sum()
    }
}

fun main() {
    val day01 = Day01("day01/input.txt")
    println(day01.part1())
    println(day01.part2())
}
