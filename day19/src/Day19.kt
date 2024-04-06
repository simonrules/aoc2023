import java.io.File

sealed class Node() {
    data class Comparison(
        val category: Char,
        val operator: Char,
        val operand: Int,
        val thenNode: Node,
        val elseNode: Node
    ) : Node() {
        override fun toString() = "$category $operator $operand"
    }

    data class Result(val accepted: Boolean) : Node() {
        override fun toString() = if (accepted) "A" else "R"
    }
}

class Day19(path: String) {
    private val workflows = mutableMapOf<String, List<String>>()
    private val workflowStrings = mutableMapOf<String, String>()
    private val parts = mutableListOf<Map<Char, Int>>()

    init {
        val sections = File(path).readText().trim().split("\n\n")
        val firstSection = sections[0].split("\n")
        firstSection.forEach {
            val openIndex = it.indexOf("{")
            val closeIndex = it.indexOf("}")

            val name = it.substring(0, openIndex)
            workflows[name] = it.substring(openIndex + 1, closeIndex).split(",")
            workflowStrings[name] = it.substring(openIndex + 1, closeIndex)

        }
        val secondSection = sections[1].split("\n")
        secondSection.forEach { s ->
            val ratingList = s.drop(1).dropLast(1).split(",")
            // converts lists items like x=123 to a map of [x]=123
            val ratingMap = ratingList.associateBy({ it[0] }, { it.substring(2).toInt() })
            parts.add(ratingMap)
        }
    }

    // name: the workflow name (first is always "in")
    // part: the part ratings, e.g. x=123, m=456, a=987, s=654
    // returns: true if accepted (A), false if rejected (R)
    private fun isAccepted(name: String, part: Map<Char, Int>): Boolean {
        val rules = workflows[name]!!

        rules.forEach { r ->
            if (r.length == 1) {
                return r[0] == 'A' // accepted (or 'R' == false)
            }

            if (r[1] == '>' || r[1] == '<') {
                val value = part[r[0]]!!
                val operator = r[1]
                val index = r.indexOf(':')
                val operand = r.substring(2, index).toInt()
                val then = r.substring(index + 1)
                val result = if (operator == '<') value < operand else value > operand

                if (result) {
                    if (then.length == 1) {
                        return then[0] == 'A' // accepted (or 'R' == false)
                    }

                    return isAccepted(then, part)
                }
            } else {
                return isAccepted(r, part)
            }
        }
        return false
    }

    fun part1(): Int {
        var sum = 0
        parts.forEach { p ->
            if (isAccepted("in", p)) {
                sum += p['x']!! + p['m']!! + p['a']!! + p['s']!!
            }
        }
        return sum
    }

    private fun parseRule(input: String): Node {
        if (input.length == 1) {
            return Node.Result(input[0] == 'A')
        }

        val rule = if (input[1] == '<' || input[1] == '>') input else workflowStrings[input]!!

        val category = rule[0]
        val operator = rule[1]
        val thenIndex = rule.indexOf(':')
        val elseIndex = rule.indexOf(',')
        val operand = rule.substring(2, thenIndex).toInt()
        val thenRule = rule.substring(thenIndex + 1, elseIndex)
        val elseRule = rule.substring(elseIndex + 1)

        val thenNode = parseRule(thenRule)
        val elseNode = parseRule(elseRule)
        val comparisonNode = Node.Comparison(category, operator, operand, thenNode, elseNode)

        return comparisonNode
    }

    private fun rangeProduct(ranges: Map<Char, IntRange>): Long {
        var product = 1L
        ranges.forEach { (_, value) ->
            product *= value.last.toLong() - value.first.toLong() + 1L
        }
        return product
    }

    private fun calculate(node: Node, ranges: Map<Char, IntRange>): Long {
        var sum = 0L

        if (node is Node.Result) {
            if (node.accepted) {
                sum += rangeProduct(ranges)
            }
        } else if (node is Node.Comparison) {
            val r = ranges[node.category]!!

            val rangesA = ranges.toMutableMap()
            val rangesB = ranges.toMutableMap()
            if (node.operator == '<') {
                // x < 1234, so new ranges are 1..1233 and 1234..4000
                rangesA[node.category] = IntRange(r.first, node.operand - 1)
                rangesB[node.category] = IntRange(node.operand, r.last)
                sum += calculate(node.thenNode, rangesA)
                sum += calculate(node.elseNode, rangesB)
            } else {
                // x > 1234, so new ranges are 1..1234 and 1235..4000
                rangesA[node.category] = IntRange(r.first, node.operand)
                rangesB[node.category] = IntRange(node.operand + 1, r.last)
                sum += calculate(node.thenNode, rangesB)
                sum += calculate(node.elseNode, rangesA)
            }
        }

        return sum
    }

    fun part2(): Long {
        // Generate a tree of nodes
        val node = parseRule("in")

        val ranges = mapOf(
            'x' to IntRange(1, 4000),
            'm' to IntRange(1, 4000),
            'a' to IntRange(1, 4000),
            's' to IntRange(1, 4000)
        )

        return calculate(node, ranges)
    }
}

fun main() {
    val day19 = Day19("day19/input.txt")
    println(day19.part1())
    println(day19.part2())
}