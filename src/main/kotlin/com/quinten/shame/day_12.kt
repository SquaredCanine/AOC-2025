package com.quinten.shame

import kotlin.system.measureTimeMillis

class DayTwelve() {
    val puzzleInput = javaClass.getResource("/day12/input.txt")
        .readText()
        .split("\n")
        .map {
            val (surface, shapes) = it.split(":").let { Pair(it.first(), it.last()) }
            val factors = surface.split("x")
            val width = factors.first().toLong()
            val height = factors.last().toLong()
            Pair(width * height, shapes.split(" ").mapNotNull { if (it.isBlank()) null else it.trim().toLong() })
        }
    
    val shapes = listOf(
        7, 7, 7, 5, 7, 6
    )
    
    fun a(): Long {
        return puzzleInput.sumOf { (area, blocks) -> 
            val spaceNeeded = blocks.mapIndexed { index, block ->
                block * shapes[index]
            }.sum()
            println("$area <> $spaceNeeded")
            if (area < spaceNeeded) (
                0L
            ) else {
                1L
            }
        }
    }
    
    fun b(): Long {
            return 0L
    }
}

fun main() {
    val day: DayTwelve
    val result1: Long
    val result2: Long
    val initTime = measureTimeMillis {
        day = DayTwelve()
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