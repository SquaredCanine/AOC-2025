package com.quinten.shame

import kotlin.system.measureTimeMillis

class DayEleven() {
    val puzzleInput = javaClass.getResource("/day11/input.txt")
        .readText()
        .split("\n")
        .associate {
            val key = it.split(":")
                .first()
            val children = it.split(":")
                .last()
                .split(" ")
                .filter { it.isNotBlank() }
            key to Node(name = key, children = children)
        }

    data class Node(
        val name: String,
        val children: List<String>
    )
    
    fun a(): Long {
        val start = puzzleInput["you"]!!
        val map = mutableMapOf<String, Long>()
        val result = start.traverse(map, "out")
        return result
    }
    
    fun Node.traverse(map: MutableMap<String, Long>, end: String = "out"): Long {
        val next = getNext(end)
        return when {
            next == null -> {
                return 0L
            }
            next.isEmpty() -> {
                return 1L
            }
            else -> {
                next.sumOf { nextNode ->
                    val key = "${nextNode.name}.$end"
                    if (map.containsKey(key)) {
                        map[key]!!
                    } else {
                        val result = nextNode.traverse(map, end)
                        map[key] = result
                        result
                    }
                }
            }
        }
    }
    
    fun Node.getNext(end: String = "out"): List<Node>? =
        if (this.children.contains(end)) {
            listOf()
        } else {
            val candidates = this.children.mapNotNull { puzzleInput[it] }
            candidates.ifEmpty {
                null
            }
        }
    
    fun b(): Long {
            val start1 = puzzleInput["svr"]!!
            val map1 = mutableMapOf<String, Long>()
            val result1 = start1.traverse(map1, "fft")
            val start2 = puzzleInput["fft"]!!
            val map2 = mutableMapOf<String, Long>()
            val result2 = start2.traverse(map2, "dac")
            val start3 = puzzleInput["dac"]!!
            val map3 = mutableMapOf<String, Long>()
            val result3 = start3.traverse(map3, "out")
            return result1 * result2 * result3
    }
}

fun main() {
    val day: DayEleven
    val result1: Long
    val result2: Long
    val initTime = measureTimeMillis {
        day = DayEleven()
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