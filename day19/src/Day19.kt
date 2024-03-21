import java.io.File

class Day19(path: String) {
    private val workflows = mutableMapOf<String, List<String>>()
    private val parts = mutableListOf<Map<Char, Int>>()
    
    init {
        val sections = File(path).readText().trim().split("\n\n")
        val firstSection = sections[0].split("\n")
        firstSection.forEach {
            val openIndex = it.indexOf("{")
            val closeIndex = it.indexOf("}")

            val name = it.substring(0, openIndex)
            workflows[name] = it.substring(openIndex + 1, closeIndex).split(",")

        }
        val secondSection = sections[1].split("\n")
        secondSection.forEach { s ->
            val ratingList = s.drop(1).dropLast(1).split(",")
            // converts lists items like x=123 to a map of [x]=123
            val ratingMap = ratingList.associateBy({ it[0] }, { it.substring(2).toInt() })
            parts.add(ratingMap)
        }
    }

    private fun processWorkflow(name: String, x: Int, m: Int, a: Int, s: Int): Boolean {
        val rules = workflows[name]!!

        rules.forEach { r ->
            if (r.length == 1) {
                return r[0] == 'A' // accepted (or 'R' == false)
            }

            if (r[1] == '>' || r[1] == '<') {
                val operand = when (r[0]) {
                    'x' -> x
                    'm' -> m
                    'a' -> a
                    's' -> s
                    else -> -1
                }
                val operator = r[1]
                val index = r.indexOf(':')
                val number = r.substring(2, index).toInt()
                val then = r.substring(index + 1)
                val result = if (operator == '<') operand < number else operand > number

                if (result) {
                    if (then.length == 1) {
                        return then[0] == 'A' // accepted (or 'R' == false)
                    }

                    return processWorkflow(then, x, m, a, s)
                }
            } else {
                return processWorkflow(r, x, m, a, s)
            }
        }
        return false
    }

    fun part1(): Int {
        var sum = 0
        parts.forEach { r ->
            val x = r['x']!!
            val m = r['m']!!
            val a = r['a']!!
            val s = r['s']!!
            if (processWorkflow("in", x, m, a, s)) {
                sum += x + m + a + s
            }
        }
        return sum
    }
}

fun main() {
    val day19 = Day19("day19/input.txt")
    println(day19.part1())
}