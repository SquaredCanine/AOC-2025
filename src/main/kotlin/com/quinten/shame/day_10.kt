package com.quinten.shame

import kotlin.system.measureTimeMillis

class DayTen() {
    val puzzleInput = javaClass.getResource("/day10/input.txt")
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
    
    
}

fun main() {
    val day: DayTen
    val result1: Long
    val result2: Long
    val initTime = measureTimeMillis {
        day = DayTen()
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