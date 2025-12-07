package com.quinten.shame

import kotlin.system.measureTimeMillis

class DaySeven() {
    val puzzleInput = javaClass.getResource("/day7/input.txt")
        .readText()
        .split("\n")

    
    fun a(): Long {
        var result = 0L
        val beams = mutableSetOf(puzzleInput.first().indexOf("S"))
        puzzleInput.drop(1).forEach { row ->
            val beamsToSplit = beams.mapNotNull { beam ->
                if (row[beam] == '^') {
                    result++
                    beam
                } else {
                    null
                }
            }
            beamsToSplit.forEach { 
                beams.remove(it)
                beams.addAll(listOf(it + 1, it - 1))
            }
            for (i in 0..puzzleInput.first().length) {
                if (i in beamsToSplit) {
                    print("^")
                } else if (i in beams) {
                    print("|")
                } else {
                    print(".")
                }
            }
            println()
        }
        return result
    }

    fun b(): Long {
        val startX = puzzleInput.first().indexOf("S")
        return puzzleInput.beams(0, startX)
    }
    
    val cache = mutableMapOf<String, Long>()
    
    fun List<String>.beams(row: Int, x: Int): Long {
        val key = "$row:$x"
        return if (cache.containsKey(key)) {
            cache[key]!!
        } else {
            var output = 0L
            for (nextRow in (row + 1) until size) {
                if (this[nextRow][x] == '^') {
                    output += beams(nextRow, x - 1)
                    output += beams(nextRow, x + 1)
                    break
                }
            }
            if (output == 0L) output++
            cache[key] = output
            output
        }
    }
}

fun main() {
    val day = DaySeven()
    val result1: Long
    val result2: Long
    val time1 = measureTimeMillis {
        result1 = day.a()
    }
    val time2 = measureTimeMillis {
        result2 = day.b()
    }
    println("Part a($result1) in ${time1}ms")
    println("Part b($result2) in ${time2}ms")
}