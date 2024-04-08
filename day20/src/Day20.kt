import java.io.File

data class Module(
    val type: Type,
    val dests: List<String>,
    var state: Boolean,
    val inputs: MutableMap<String, Boolean>
) {
    enum class Type { FLIPFLOP, CONJUNCTION, BROADCAST }
}

class Day20(filename: String) {
    private val modules = mutableMapOf<String, Module>()

    init {
        val moduleInputsMap = mutableMapOf<String, MutableSet<String>>()

        File(filename).forEachLine { line ->
            val parts = line.split(" -> ")
            val type = when(parts[0][0]) {
                '%' -> Module.Type.FLIPFLOP
                '&' -> Module.Type.CONJUNCTION
                else -> Module.Type.BROADCAST
            }
            val name = if (type == Module.Type.BROADCAST) {
                parts[0]
            } else {
                parts[0].drop(1)
            }
            val destinations = parts[1].split(", ")
            modules[name] = Module(type, destinations, false, mutableMapOf())

            // collect inputs
            destinations.forEach { d ->
                if (!moduleInputsMap.containsKey(d)) {
                    moduleInputsMap[d] = mutableSetOf()
                }
                moduleInputsMap[d]?.add(name)
            }
        }

        moduleInputsMap.forEach { (name, input) ->
            input.forEach {
                modules[name]?.inputs?.set(it, false) // initial input state
            }
        }
    }

    private fun process(from: String, dest: String, pulse: Boolean): Boolean? {
        val module = modules[dest]!!

        when (module.type) {
            Module.Type.CONJUNCTION -> {
                module.inputs[from] = pulse
                return !module.inputs.all { it.value }
            }
            Module.Type.FLIPFLOP -> {
                if (!pulse) {
                    module.state = !module.state
                    return module.state
                }

                return null
            }
            else -> {
                return pulse
            }
        }
    }

    data class State(val from: String, val dest: String, val pulse: Boolean)

    private fun countPulses(): Pair<Int, Int> {
        var state = listOf(State("button", "broadcaster", false))
        var lowPulses = 1
        var highPulses = 0

        while (state.isNotEmpty()) {
            val newState = mutableListOf<State>()
            state.forEach { s ->
                val module = modules[s.dest]
                if (module != null) {
                    val result = process(s.from, s.dest, s.pulse)
                    if (result != null) {
                        if (result) {
                            highPulses += module.dests.size
                        } else {
                            lowPulses += module.dests.size
                        }
                        module.dests.forEach { d ->
                            //val highLow = if (result) "high" else "low"
                            //println("${s.dest} -$highLow-> $d")
                            newState.add(State(s.dest, d, result))
                        }
                    }
                }
            }
            state = newState.toList()
        }

        //println()

        return Pair(lowPulses, highPulses)
    }

    fun part1(): Int {
        var lowSum = 0
        var highSum = 0

        repeat(1000) {
            val (lowPulses, highPulses) = countPulses()
            lowSum += lowPulses
            highSum += highPulses
        }

        return lowSum * highSum
    }
}

fun main() {
    val day20 = Day20("day20/input.txt")
    println(day20.part1())
}
