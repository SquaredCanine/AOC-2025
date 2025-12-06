package com.quinten.shame

import kotlin.system.measureTimeMillis

class DaySix() {
    val puzzleInput = javaClass.getResource("/day6/input.txt")
        .readText()
        .split("\n")
    
    val puzzleInputA = puzzleInput
        .map { it.split(Regex("\\s+"))}

    fun a(): Long {
        var result = 0L
        val numbers = puzzleInputA.dropLast(1)
        val operations = puzzleInputA.last() // operation line
        puzzleInputA.first().forEachIndexed { i, _ ->
            val values = numbers.map { it[i].toLong() }
            val operation = operations[i].toOperation()
            result += values.sumOperation(operation)
        }
        return result
    }

    fun b(): Long {
        val numbers = puzzleInput.dropLast(1)
        val queue = mutableListOf<Pair<Long.(Long) -> Long, List<Long>>>()
        val operatorLine = puzzleInput.last()
        operatorLine.forEachIndexed { i, operator ->
            if (!operator.isWhitespace()) {
                var numberScanLine = i // column to scan for numbers
                var numbersToParse = true
                val numberList = mutableListOf<Long>() // numbers parsed for the operation
                do {
                    val number = numbers.getColumn(numberScanLine).toLongOrNull()
                    if (number != null) {
                        numberList.add(number)
                    } else {
                        numbersToParse = false
                    }
                    numberScanLine++
                } while (numbersToParse && numberScanLine < numbers.first().length)
                queue.add(Pair("$operator".toOperation(), numberList))
            }
        }
        return queue.fold(0L) { acc, (operation, values) ->
            acc + values.sumOperation(operation)
        }
    }
    
    fun String.toLongOrNull(): Long? = if (this.isNotBlank()) this.trim().toLong() else null
    
    fun List<String>.getColumn(index: Int): String = this.map { it[index] }.joinToString("")

    fun String.toOperation(): Long.(Long) -> Long = when(this) {
        "+" -> Long::plus
        else -> Long::times
    }

    fun List<Long>.sumOperation(operation: Long.(Long) -> Long): Long = this.drop(1).fold(this[0]) { acc, next -> acc.operation(next) }
}

fun main() {
    val day = DaySix()
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