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

    data class PulseState(val from: String, val dest: String, val pulse: Boolean)

    private fun countPulses(): Pair<Int, Int> {
        var pulseState = listOf(PulseState("button", "broadcaster", false))
        var lowPulses = 1
        var highPulses = 0

        while (pulseState.isNotEmpty()) {
            val newPulseState = mutableListOf<PulseState>()
            pulseState.forEach { s ->
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
                            newPulseState.add(PulseState(s.dest, d, result))
                        }
                    }
                }
            }
            pulseState = newPulseState.toList()
        }

        //println()

        return Pair(lowPulses, highPulses)
    }

    private fun reset() {
        modules.values.forEach { m ->
            m.state = false
            m.inputs.keys.forEach {
                m.inputs[it] = false
            }
        }
    }

    private fun countPushesToLevel(name: String, level: Boolean): Int {
        var pushes = 0
        do {
            pushes++
            var pulseState = listOf(PulseState("button", "broadcaster", false))
            while (pulseState.isNotEmpty()) {
                val newPulseState = mutableListOf<PulseState>()
                pulseState.forEach { s ->
                    val module = modules[s.dest]
                    if (module != null) {
                        val result = process(s.from, s.dest, s.pulse)
                        if (result != null) {
                            if (s.dest == name && result == level) {
                                return pushes
                            }
                            module.dests.forEach { d ->
                                newPulseState.add(PulseState(s.dest, d, result))
                            }
                        }
                    }
                }
                pulseState = newPulseState.toList()
            }
        } while (true)
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

    fun part2(): Long {
        // broadcaster -> pj, fg, bh, br
        //  %pj -> zj, zq
        //  %fg -> nt, gt
        //  %bh -> qd, vv
        //  %br -> vn, jz
        //    &vv -> dm, bl, sb, nb, qd, bh
        //    &nt -> rq, fg, ft, nd, gt, xz
        //    &vn -> br, jz, ht, ps, zc, pp, ds
        //    &zq -> fs, gr, ff, hf, ln, zj, pj
        //      &sb -> zp
        //      &nd -> zp
        //      &ds -> zp
        //      &hf -> zp
        //        &zp -> rx

        // Count the number of pushes for important conjunctions to go low
        reset()
        val vv = countPushesToLevel("vv", false)
        reset()
        val nt = countPushesToLevel("nt", false)
        reset()
        val vn = countPushesToLevel("vn", false)
        reset()
        val zq = countPushesToLevel("zq", false)

        // vv = 3797
        // nt = 3917
        // vn = 3733
        // zq = 3877

        return vv.toLong() * nt.toLong() * vn.toLong() * zq.toLong()
    }
}

fun main() {
    val day20 = Day20("day20/input.txt")
    println(day20.part1())
    println(day20.part2())
}
