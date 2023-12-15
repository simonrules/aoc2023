import java.io.File

data class Range(val destStart: Long, val sourceStart: Long, val length: Long)

class Day05(filename: String) {
    private val seeds: List<Long>
    private val soilRanges: List<Range>
    private val fertilizerRanges: List<Range>
    private val waterRanges: List<Range>
    private val lightRanges: List<Range>
    private val tempRanges: List<Range>
    private val humidityRanges: List<Range>
    private val locationRanges: List<Range>

    init {
        val sections = File(filename).readText().split("\n\n").map { it.trim() }

        seeds = sections[0].drop(7).split(" ").map { it.toLong() }

        soilRanges = readRange(sections[1])
        fertilizerRanges = readRange(sections[2])
        waterRanges = readRange(sections[3])
        lightRanges = readRange(sections[4])
        tempRanges = readRange(sections[5])
        humidityRanges = readRange(sections[6])
        locationRanges = readRange(sections[7])
    }

    private fun readRange(block: String): List<Range> {
        val ranges = mutableListOf<Range>()
        val lines = block.split("\n")

        for (i in 1 until lines.size) {
            val values = lines[i].split(" ")
            ranges.add(Range(values[0].toLong(), values[1].toLong(), values[2].toLong()))
        }

        return ranges.sortedBy { it.destStart }.toList()
    }

    private fun mapNumber(source: Long, ranges: List<Range>): Long {
        ranges.forEach { r ->
            if (source in r.sourceStart until r.sourceStart + r.length) {
                return source - r.sourceStart + r.destStart
            }
        }
        return source
    }

    private fun mapNumberReverse(dest: Long, ranges: List<Range>): Long {
        ranges.forEach { r ->
            if (dest in r.destStart until r.destStart + r.length) {
                return dest - r.destStart + r.sourceStart
            }
        }
        return dest
    }

    private fun mapAll(source: Long): Long {
        return mapNumber(
            mapNumber(
                mapNumber(
                    mapNumber(
                        mapNumber(
                            mapNumber(
                                mapNumber(source, soilRanges),
                                fertilizerRanges
                            ), waterRanges
                        ), lightRanges
                    ), tempRanges
                ), humidityRanges
            ), locationRanges
        )
    }

    private fun mapAllReverse(dest: Long): Long {
        return mapNumberReverse(
            mapNumberReverse(
                mapNumberReverse(
                    mapNumberReverse(
                        mapNumberReverse(
                            mapNumberReverse(
                                mapNumberReverse(dest, locationRanges),
                                humidityRanges
                            ), tempRanges
                        ), lightRanges
                    ), waterRanges
                ), fertilizerRanges
            ), soilRanges
        )
    }

    fun part1(): Long {
        val locations = seeds.map { mapAll(it) }

        return locations.min()
    }

    private fun inRanges(ranges: List<Pair<Long, Long>>, value: Long): Boolean {
        ranges.forEach { r ->
            if (value in (r.first until r.second)) {
                return true
            }
        }

        return false
    }

    fun part2(): Long {
        val seedRanges = mutableListOf<Pair<Long, Long>>()
        for (i in 0 until seeds.size / 2) {
            val start = seeds[i * 2]
            val end = start + seeds[i * 2 + 1]
            seedRanges.add(Pair(start, end))
            seedRanges.sortBy { it.first }
        }
        var min = 0L
        var max = 5000000000L

        println(inRanges(seedRanges, mapAllReverse(46L)))

        // binary search



        return 0L
    }
}

fun main() {
    val day05 = Day05("day05/test1.txt")
    //println(day05.part1())
    println(day05.part2())
}
