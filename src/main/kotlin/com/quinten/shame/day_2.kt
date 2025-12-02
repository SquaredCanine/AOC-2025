package com.quinten.shame

class DayTwo {
    val puzzleInput = javaClass.getResource("/day2/input.txt")
        .readText()
        .split(",")
        .map {
            val range = it.split("-")
            Pair(range[0].toLong(), range[1].toLong())
        }

    fun a(): Long {
        var result = 0L
        for (ranges in puzzleInput) {
            print(ranges)
            for (i in ranges.first..ranges.second) {
                val string = i.toString()
                if (string.length % 2 == 0) {
                    //can split in half
                    val first = string.substring(0, string.length / 2)
                    val second = string.substring(string.length / 2)
                    if (first == second) {
                        println("invalid id $i")
                        result += i
                    }
                }
            }
        }
        return result
    }

    fun b(): Long {
        var result = 0L
        for (ranges in puzzleInput) {
            print(ranges)
            for (i in ranges.first..ranges.second) {
                val string = i.toString()
                val maxChunkSize = string.length / 2
                forEach@ for (chunkSize in 1..maxChunkSize) {
                    val chunks = string.chunked(chunkSize)
                    if (chunks.first() != chunks.last()) {
                        //Ignoring
                    } else {
                        if (chunks.all { it == chunks.first() }) {
                            println("invalid id $i")
                            result += i
                            break@forEach
                        }
                    }
                }
            }
        }
        return result
    }
}

fun main() {
    println(DayTwo().a())
    println(DayTwo().b())
}