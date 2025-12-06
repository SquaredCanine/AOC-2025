package com.quinten.shame

class DaySix() {
    val puzzleInputA = javaClass.getResource("/day6/input.txt")
        .readText()
        .split("\n")
        .map { it.split(Regex("\\s+"))}

    fun a(): Long {
        var result = 0L
        val numbers = puzzleInputA.dropLast(1)
        val operations = puzzleInputA.takeLast(1)[0]
        puzzleInputA.first().forEachIndexed { i, _ ->
            val values = numbers.map { it[i].toInt() }
            val thingy = operations[i].toOperation()
            result += values.drop(1).fold(values[0].toLong()) { acc, next -> acc.thingy(next.toLong()) }
        }
        return result
    }

    fun String.toOperation(): Long.(Long) -> Long = when(this) {
        "+" -> Long::plus
        else -> Long::times
    }

    val puzzleInputB = javaClass.getResource("/day6/input.txt")
        .readText()
        .split("\n")

    fun b(): Long {
        //Parsing the input based on operator position
        val numbers = puzzleInputB.dropLast(1)
        val queue = mutableListOf<Pair<Long.(Long) -> Long, List<Long>>>()
        puzzleInputB.last().forEachIndexed { i, operator ->
            if (!operator.isWhitespace()) {
                //get numbers until no numbers
                var numberScanLine = i
                var isLooping = true
                val numberList = mutableListOf<Long>()
                while (isLooping) {
                    var number = ""
                    numbers.forEach {
                        number += it[numberScanLine]
                    }
                    if (number.isNotBlank()) {
                        numberList.add(number.trim().toLong())
                    } else {
                        isLooping = false
                    }
                    numberScanLine++
                    if (numberScanLine >= numbers.first().length) {
                        isLooping = false
                    }
                }
                queue.add(Pair("$operator".toOperation(), numberList))
            }
        }
        var result = 0L
        queue.forEach { (operation, values) ->
            result += values.drop(1).fold(values[0]) { acc, next -> acc.operation(next) }
        }
        return result
    }
}

fun main() {
    println(DaySix().a())
    println(DaySix().b())
}