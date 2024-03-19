import java.io.File

class Day19(path: String) {
    private val rules = mutableMapOf<String, List<String>>()
    private val ratings = mutableListOf<Map<Char, Int>>()
    
    init {
        val sections = File(path).readText().trim().split("\n\n")
        val workflows = sections[0].split("\n")
        workflows.forEach { workflow ->
            val openIndex = workflow.indexOf("{")
            val closeIndex = workflow.indexOf("}")

            val name = workflow.substring(0, openIndex)
            rules[name] = workflow.substring(openIndex + 1, closeIndex).split(",")

        }
        val parts = sections[1].split("\n")
        parts.forEach { part ->
            val ratingList = part.drop(1).dropLast(1).split(",")
            // converts lists items like x=123 to a map of [x]=123
            val ratingMap = ratingList.associateBy({ it[0] }, { it.substring(2) })
            ratings.add(ratingMap)
        }
    }
    fun part1(): Int {
        return 0
    }
}

fun main() {
    val day19 = Day19("day19/test1.txt")
    println(day19.part1())
}