package com.quinten.shame

class DayThree {
    val puzzleInput = javaClass.getResource("/day3/input.txt")
        .readText()
        .split("\n")

    fun a(): Long {
        var result = 0L
        for (batteryRow in puzzleInput) {
            val highestNumber = batteryRow.dropLast(1).maxBy { it }
            val highestNumberIndex = batteryRow.indexOf(highestNumber)
            val highestSecondNumber = batteryRow.substring(highestNumberIndex + 1).maxBy { it }
            val highest = "$highestNumber$highestSecondNumber".toLong()
            result += highest
        }
        return result
    }

    fun b(): Long {
        var result = 0L
        for (batteryRow in puzzleInput) {
            // 12 digits?
            val highestNumbers = mutableListOf<Char>()
            var lastIndex = -1
            for(i in 11 downTo 0) {
                val subString = batteryRow.substring(lastIndex + 1).dropLast(i)
                val highestNumber = subString.maxBy { it }
                highestNumbers.add(highestNumber)
                lastIndex = lastIndex + 1 + subString.indexOf(highestNumber)
            }
            val highest = highestNumbers.joinToString(separator = "").toLong()
            result += highest
        }
        return result
    }
}

fun main() {
    println(DayThree().a())
    println(DayThree().b())
}