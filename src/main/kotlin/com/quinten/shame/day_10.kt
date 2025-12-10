package com.quinten.shame

import kotlin.system.measureTimeMillis

class DayTen() {
    val puzzleInput = javaClass.getResource("/day10/input.txt")
        .readText()
        .split("\n")
        .map {
            val splits = it.split(" ")
            ButtonMash(
                expectedState = splits[0].drop(1)
                    .dropLast(1),
                joltages = splits.last(),
                buttons = splits.drop(1)
                    .dropLast(1)
                    .map { buttonEntry -> buttonEntry.toButtonIndexes() })
        }

    fun String.toButtonIndexes(): List<Int> = this.drop(1)
        .dropLast(1)
        .split(",")
        .map { it.toInt() }


    data class Operation(
        val currState: String,
        val expectedState: String,
        val operation: List<Int>,
        val numberOfOps: Long,
        val done: Boolean = false,
    ) {
        fun fuckAround(): Operation {
            val current = currState.toCharArray()
            for (op in operation) {
                val thing = current[op]
                current[op] = thing.flip()
            }
            val currentState = String(current)
            val newOp = Operation(
                currState = currentState,
                expectedState = expectedState,
                operation = listOf(),
                numberOfOps = numberOfOps + 1,
            )
            return if (currentState == expectedState) {
                newOp.copy(done = true)
            } else {
                newOp
            }
        }

        fun Char.flip() = if (this == '.') '#' else '.'
    }

    fun a(): Long {
        val leastButtonPresses = puzzleInput.sumOf {
            var bestResult = -1L
            val operations = mutableListOf<Operation>()
            with(it) {
                val currState = ".".repeat(expectedState.length)
                for (button in buttons) {
                    operations.add(
                        Operation(
                            currState = currState,
                            expectedState = expectedState,
                            operation = button,
                            numberOfOps = 0L,
                        )
                    )
                }
                var searching = true
                while (searching) {
                    val newOps = operations.map {
                        if (!searching) {
                            // we can stop
                            listOf(it)
                        } else {
                            val newOp = it.fuckAround()
                            if (newOp.done) {
                                searching = false
                                bestResult = newOp.numberOfOps
                                listOf(it)
                            } else {
                                buttons.map {
                                    newOp.copy(operation = it)
                                }
                            }
                        }
                    }.flatten()
                    operations.clear()
                    operations.addAll(newOps)
                }
            }
            bestResult
        }
        return leastButtonPresses
    }

    fun b(): Long {
        val leastButtonPresses = puzzleInput.sumOf {
            var bestResult = -1L
            val operations = mutableListOf<Operation>()
            with(it) {
                val currState = ".".repeat(expectedState.length)
                for (button in buttons) {
                    operations.add(
                        Operation(
                            currState = currState,
                            expectedState = expectedState,
                            operation = button,
                            numberOfOps = 0L,
                        )
                    )
                }
                var searching = true
                while (searching) {
                    val newOps = operations.map {
                        if (!searching) {
                            // we can stop
                            listOf(it)
                        } else {
                            val newOp = it.fuckAround()
                            if (newOp.done) {
                                searching = false
                                bestResult = newOp.numberOfOps
                                listOf(it)
                            } else {
                                buttons.map {
                                    newOp.copy(operation = it)
                                }
                            }
                        }
                    }.flatten()
                    operations.clear()
                    operations.addAll(newOps)
                }
            }
            bestResult
        }
        return leastButtonPresses
    }
}

data class ButtonMash(
    val expectedState: String,
    val buttons: List<List<Int>>,
    val joltages: String,
)

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