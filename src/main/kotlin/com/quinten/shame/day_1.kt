package com.quinten.shame

import kotlin.math.abs

class DayOne {
    val puzzleInput = javaClass.getResource("/day1/input.txt").readText().split("\n")

    fun a(input: List<String> = puzzleInput): String {
        var output = 0
        var position = 50
        input.forEach { rotation ->
            val (_, normalizedRotations) = rotation.splitToValues()
            position += normalizedRotations
            position = position.toDialNumber()
            if (position == 0) {
                output++
            }
        }
        return output.toString()
    }

    fun b(input: List<String> = puzzleInput): String {
        var output = 0
        var position = 50
        input.forEach { rotation ->
            val prevPos = position
            val (rotations, normalizedRotations) = rotation.splitToValues()
            position += normalizedRotations

            // How many times fully past 0
            val fullRotations = abs(rotations / 100)
            output += fullRotations

            // How many times partially past 0 (less than a 100)
            if ((prevPos != 0 && position < 0) || (position > 100)) {
                output++
            }

            position = position.toDialNumber()
            // How many times ended on 0
            if (position == 0) {
                output++
            }
        }
        return output.toString()
    }

    fun Int.toDialNumber(): Int {
        val modulo = this % 100
        return if (modulo < 0) {
            modulo + 100
        } else {
            modulo
        }
    }

    fun String.splitToValues(): Pair<Int, Int> {
        val rotationValue = this.drop(1).toInt()
        val isLeft = this.first() == 'L'
        val rotation = if (isLeft) rotationValue * -1 else rotationValue
        val normalizedRotation = rotation % 100
        return Pair(rotation, normalizedRotation)
    }
}



fun main() {
    println(DayOne().a())
    println(DayOne().b())
}