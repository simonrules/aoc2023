package com.simonrules.aoc2023.utils

abstract class Plan2D<T>(val min: Point2D, val max: Point2D, val initial: T) {
    val width: Int
        get() = max.x - min.x + 1

    val height: Int
        get() = max.y - min.y + 1

    fun inBounds(p: Point2D): Boolean {
        return (p.x in min.x..max.x) && (p.y in min.y..max.y)
    }

    abstract fun getAt(point: Point2D): T
    abstract fun setAt(point: Point2D, value: T)
    abstract fun count(value: T): Int
    abstract fun floodFill(start: Point2D, value: T)
    abstract fun print()
}

class ListPlan2D<T>(min: Point2D, max: Point2D, initial: T) : Plan2D<T>(min, max, initial) {
    private val data = MutableList(height) { MutableList(width) { initial } }

    private val offsetX: Int
        get() = -min.x

    private val offsetY: Int
        get() = -min.y


    override fun getAt(point: Point2D): T {
        return data[point.y + offsetY][point.x + offsetX]
    }

    override fun setAt(point: Point2D, value: T) {
        data[point.y + offsetY][point.x + offsetX] = value
    }

    override fun count(value: T): Int {
        var sum = 0
        data.forEach { row ->
            sum += row.count { it == value }
        }
        return sum
    }

    override fun floodFill(start: Point2D, value: T) {
        val visited = HashSet<Point2D>()
        val queue = ArrayDeque<Point2D>()

        // first point
        queue.addLast(start)

        while (queue.isNotEmpty()) {
            val p = queue.removeFirst()

            if (p in visited) {
                continue
            }
            visited.add(p)
            setAt(p, value)

            for (n in -1..1 step 2) {
                val horizontal = Point2D(p.x + n, p.y)
                if (horizontal.x in min.x..max.x && getAt(horizontal) == initial) {
                    queue.addLast(horizontal)
                }
                val vertical = Point2D(p.x, p.y + n)
                if (vertical.y in min.y..max.y && getAt(vertical) == initial) {
                    queue.addLast(vertical)
                }
            }
        }
    }

    override fun print() {
        data.forEach { row ->
            row.forEach { col ->
                print(col)
            }
            println()
        }
    }
}
