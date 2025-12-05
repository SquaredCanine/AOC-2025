package com.quinten.shame

import kotlin.math.max
import kotlin.math.min

class DayFive() {
    val puzzleInput = javaClass.getResource("/day5/input.txt")
        .readText()
        .split("===")
        .let {
            it[0].split("\n").filter { it.isNotEmpty() } to it[1].split("\n").filter { it.isNotEmpty() }
        }

    fun a(): Long {
        var result = 0L
        val ranges = puzzleInput.first.map {
            val range = it.split("-")
            range[0].toLong()..range[1].toLong()
        }
        for (id in puzzleInput.second.map { it.toLong() }) {
            val inRange = ranges.any { id in it }
            if (inRange) result++
        }
        return result
    }

    fun b(): Long {
        val ranges = puzzleInput.first.map {
            val range = it.split("-")
            range[0].toLong()..range[1].toLong()
        }
        val newRanges = mutableListOf<LongRange>()
        val sorted = ranges.sortedBy { it.first() }
        newRanges.add(sorted.first())
        sorted.forEach { range ->
            var relevantId: Int? = null
            var rangeToMerge: LongRange? = null
            newRanges.forEachIndexed { id, existingRange ->
                if (range.first in existingRange) {
                    relevantId = id
                    rangeToMerge = existingRange
                    return@forEachIndexed
                }
            }
            if (relevantId != null) {
                rangeToMerge!!
                newRanges.remove(rangeToMerge)
                newRanges.add(relevantId, min(range.first, rangeToMerge.first)..max(range.last, rangeToMerge.last))
            } else {
                newRanges.add(range)
            }
        }
        var result: Long = 0
        for (range in newRanges) {
            result += range.last - range.first + 1
        }
        return result
    }
}

fun main() {
    println(DayFive().a())
    println(DayFive().b())
}