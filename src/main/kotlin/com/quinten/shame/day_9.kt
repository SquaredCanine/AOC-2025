package com.quinten.shame

import kotlin.Int.Companion.MAX_VALUE
import kotlin.Int.Companion.MIN_VALUE
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.system.measureTimeMillis

class DayNine() {
    val puzzleInput = javaClass.getResource("/day9/input.txt")
        .readText()
        .split("\n")
        .map { 
            val coordinates = it.split(",")
            Coordinate(
                x = coordinates[0].toInt(),
                y = coordinates[1].toInt(),
            )
        }
    
    val zeroPoint = Coordinate(-1, -1)
    
    fun a(): Long {
        val map: MutableMap<String, Long> = mutableMapOf()
        for (coord in puzzleInput) {
            for (otherCoord in puzzleInput) {
                val closest = listOf(coord, otherCoord).minBy { it.distanceTo(zeroPoint) }
                val furthest = listOf(coord, otherCoord).maxBy { it.distanceTo(zeroPoint) }
                if (coord == otherCoord) {
                    //ignore
                } else if (coord != otherCoord && closest == furthest) {
                    //doesn't happen
                } else {
                    val key = "$closest$furthest"
                    map.computeIfAbsent(key) { 
                        closest area furthest
                    }
                }
            }
        }
        map.entries.sortedBy { it.value }.forEach {
            //println("${it.key}: ${it.value}")
        }
        return map.values.max()
    }

    fun b(): Long {
        val grid = mutableListOf<String>()
        if (puzzleInput.size < 100) {
            for (i in 0..20) { grid.add(".".repeat(20)) }
            for (coord in puzzleInput) {
                grid[coord.y] = String(grid[coord.y].toCharArray().let { array -> array[coord.x] = 'X'; array })
            }
            grid.forEach { println(it) }
        }
        val map: MutableMap<Pair<Coordinate, Coordinate>, Long> = mutableMapOf()
        for (coord in puzzleInput) {
            for (otherCoord in puzzleInput) {
                val closest = listOf(coord, otherCoord).minBy { it.distanceTo(zeroPoint) }
                val furthest = listOf(coord, otherCoord).maxBy { it.distanceTo(zeroPoint) }
                if (coord == otherCoord) {
                    //ignore
                } else if (coord != otherCoord && closest == furthest) {
                    //doesn't happen
                } else {
                    val key = Pair(closest, furthest)
                    map.computeIfAbsent(key) {
                        closest area furthest
                    }
                }
            }
        }
        val sortedValues = map.entries.sortedByDescending { it.value }
        val edges = (puzzleInput + puzzleInput.first()).zipWithNext().map { Edge(it.first, it.second) }.toTypedArray()
        val (horizontalEdges, verticalEdges) = edges.partition { 
            it.e.y == it.s.y
        }

        //val figure = Figure(edges)
        //val edges = puzzleInput + puzzleInput.first()
        val selected = sortedValues.first {
            val (closest, furthest) = it.key
            val points = closest getRectangleCorners furthest
            //println("checking $points + ${it.value}")
            //println(points.map { point -> edges contains point })
            points.all { point -> edges contains point }  &&
            !Pair(points[0], points[1]).crossesVerticalEdge(verticalEdges) &&
            !Pair(points[1], points[2]).crossesHorizontalEdge(horizontalEdges) &&
            !Pair(points[3], points[2]).crossesVerticalEdge(verticalEdges) &&
            !Pair(points[0], points[3]).crossesHorizontalEdge(horizontalEdges)


        }
        println("candidate: ${selected.key} : ${selected.value}")
        return selected.value
    }

    fun isPointInsidePolygon(point: Coordinate, polygon: List<Coordinate>): Boolean {
        var inside = false
        val n = polygon.size
        var p1 = polygon[0]
        for (i in 1..n) {
            val p2 = polygon[i % n]
            // Ray casting logic (simplified)
            if (point.y > minOf(p1.y, p2.y) && point.y <= maxOf(p1.y, p2.y) &&
                point.x <= maxOf(p1.x, p2.x) && p1.y != p2.y) {
                val xIntersection = (point.y - p1.y) * (p2.x - p1.x) / (p2.y - p1.y) + p1.x
                if (p1.x == p2.x || point.x <= xIntersection) {
                    inside = !inside
                }
            }
            p1 = p2
        }
        return inside
    }


    data class Edge(val s: Coordinate, val e: Coordinate) {
        operator fun invoke(p: Coordinate) : Boolean = when {
            s.y > e.y -> Edge(e, s).invoke(p)
            p.y == s.y || p.y == e.y -> invoke(Coordinate(p.x, p.y + epsilon))
            p.y > e.y || p.y < s.y || p.x > max(s.x, e.x) -> false
            p.x < min(s.x, e.x) -> true
            else -> {
                val blue = if (abs(s.x - p.x) > MIN_VALUE) (p.y - s.y) / (p.x - s.x) else MAX_VALUE
                val red = if (abs(s.x - e.x) > MIN_VALUE) (e.y - s.y) / (e.x - s.x) else MAX_VALUE
                blue >= red
            }
        }

        val epsilon = 1
    }

    class Figure(val edges: Array<Edge>) {
        operator fun contains(p: Coordinate) = edges.count({ it(p) }) % 2 != 0
    }

    // should be from left to right
    fun Pair<Coordinate, Coordinate>.crossesVerticalEdge(verticalEdges: List<Edge>): Boolean {
        return verticalEdges.any {
            this.first.x < it.e.x && this.second.x > it.e.x &&
                this.first.y in it.e.y positiveRangeUntil it.s.y
        }
    }
    
    infix fun Int.positiveRangeUntil(other: Int): IntRange {
        return if (this > other) {
            other..this
        } else {
            this..other
        }
    }
    
    // should be from to bottom
    fun Pair<Coordinate, Coordinate>.crossesHorizontalEdge(horizontalEdges: List<Edge>): Boolean {
        return horizontalEdges.any {
            this.first.y < it.e.y && this.second.y > it.e.y &&
                this.first.x in it.e.x positiveRangeUntil it.s.x
        }
    }
    
    infix fun Array<Edge>.contains(point: Coordinate): Boolean {
        var crosses = 0

        this.forEach {
            val vertex1 = it.s
            val vertex2 = it.e

            if (vertex1.x == vertex2.x) {
                // vertical line
                if (point.x < vertex1.x) {
                    val highest = listOf(vertex1, vertex2).minBy { it.y }
                    val lowest = listOf(vertex1, vertex2).maxBy { it.y }
                    if (point.y in highest.y..lowest.y) {
                        crosses++
                    }
                }
            } else {
                //horizontal line
                if (vertex1.y == point.y && (point.x < vertex1.x || point.x < vertex2.x)) {
                    crosses++
                }
            }
        }

        return crosses % 2 == 1
    }
    
    data class Coordinate(val x: Int, val y: Int) {
        infix fun distanceTo(other: Coordinate): Double = sqrt(
            (this.x - other.x).toDouble().pow(2.0) + 
            (this.y - other.y).toDouble().pow(2.0) 
        )
        
        infix fun getRectangleCorners(other: Coordinate): List<Coordinate> {
            val left = min(this.x, other.x)
            val right = max(this.x, other.x)
            val top = min(this.y, other.y)
            val bottom = max(this.y, other.y)
            return listOf(
                Coordinate(left, top), // top left
                Coordinate(right, top), // top right
                Coordinate(right, bottom), // bottom right
                Coordinate(left, bottom), // bottom left
            )
        }
        
        infix fun area(other: Coordinate): Long {
            val left = min(this.x, other.x).toLong()
            val right = max(this.x, other.x).toLong()
            val top = min(this.y, other.y).toLong()
            val bottom = max(this.y, other.y).toLong()
            val width = (right - left) + 1L
            val height = (bottom - top) + 1L
            return (width * height)
        }
    }
}

fun main() {
    val day: DayNine
    val result1: Long
    val result2: Long
    val initTime = measureTimeMillis {
        day = DayNine()
    }
    val time1 = measureTimeMillis {
        result1 = day.a()
    }
    val time2 = measureTimeMillis {
        result2 = day.b()
    }
    println("Init time was $initTime ms")
    println("Part a($result1) in ${time1}ms")
    println("Part b($result2) in ${time2}ms")
}