import java.io.File

data class Lens(val label: String, val focal: Int)

data class Box(val lenses: MutableList<Lens>) {
    fun getPower(box: Int, slot: Int): Int {
        var sum = 0
        lenses.forEachIndexed { index, lens ->
            sum += (box + 1) * (index + 1) * lens.focal
        }
        return sum
    }
}

class Day15(path: String) {
    private val input = File(path).readText().trim().split(",")
    private val boxes = Array(256) { Box(mutableListOf()) }

    private fun getHash(string: String): Int {
        var value = 0
        string.forEach {
            value = (value + it.code) * 17 % 256
        }
        return value
    }

    private fun getLabel(string: String): String {
        val regex = "(\\w+)[=-]\\d*".toRegex()
        return regex.matchEntire(string)!!.groupValues[1]
    }

    private fun getFocal(string: String): Int {
        val index = string.indexOf("=") + 1
        return string.substring(index).toInt()
    }

    private fun getOperation(string: String): Char {
        return if (string.last() == '-') '-' else '='
    }

    private fun printState() {
        boxes.forEachIndexed { index, box ->
            if (box.lenses.isNotEmpty()) {
                print("Box $index: ")
                println(box.lenses)
            }
        }
        println()
    }

    private fun getPower(): Long {
        var sum = 0L
        boxes.forEachIndexed { boxIndex, box ->
            box.lenses.forEachIndexed { lensIndex, lens ->
                sum += (boxIndex + 1) * (lensIndex + 1) * lens.focal
            }
        }
        return sum
    }

    fun part1(): Long {
        var sum = 0L
        input.forEach {
            sum += getHash(it)
        }
        return sum
    }

    fun part2(): Long {
        //val lenses = mutableSetOf<String>()

        input.forEach { s ->
            //println(s)
            val label = getLabel(s)
            val box = boxes[getHash(label)]
            //lenses.add(label)

            // The label will be immediately followed by a character that indicates the operation to perform:
            // either an equals sign (=) or a dash (-).
            if (getOperation(s) == '-') {
                // If the operation character is a dash (-), go to the relevant box and remove the lens with the
                // given label if it is present in the box. Then, move any remaining lenses as far forward in the
                // box as they can go without changing their order, filling any space made by removing the
                // indicated lens. (If no lens in that box has the given label, nothing happens.)

                val lens = box.lenses.find { it.label == label }
                if (lens != null) {
                    box.lenses.remove(lens)
                }
            } else { // =
                // If the operation character is an equals sign (=), it will be followed by a number indicating
                // the focal length of the lens that needs to go into the relevant box; be sure to use the label
                // maker to mark the lens with the label given in the beginning of the step so you can find it
                // later.
                val newLens = Lens(label, getFocal(s))

                val lens = box.lenses.find { it.label == label }
                if (lens != null) {
                    // If there is already a lens in the box with the same label, replace the old lens with the new
                    // lens: remove the old lens and put the new lens in its place, not moving any other lenses in
                    // the box.
                    val index = box.lenses.indexOf(lens)
                    box.lenses[index] = newLens
                } else {
                    // If there is not already a lens in the box with the same label, add the lens to the box
                    // immediately behind any lenses already in the box. Don't move any of the other lenses when
                    // you do this. If there aren't any lenses in the box, the new lens goes all the way to the
                    // front of the box.
                    box.lenses.addLast(newLens)
                }
            }

            //printState()
        }
        return getPower()
    }
}

fun main() {
    val day15 = Day15("day15/input.txt")
    println(day15.part1())
    println(day15.part2())
}
