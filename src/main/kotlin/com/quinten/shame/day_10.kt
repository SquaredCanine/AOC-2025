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
                joltages = splits.last().toNumbers(),
                buttons = splits.drop(1)
                    .dropLast(1)
                    .map { buttonEntry -> buttonEntry.toNumbers() })
        }

    fun String.toNumbers(): List<Int> = this.drop(1)
        .dropLast(1)
        .split(",")
        .map { it.toInt() }


    data class OperationA(
        val currState: String,
        val expectedState: String,
        val operation: List<Int>,
        val numberOfOps: Long,
        val done: Boolean = false,
    ) {
        fun fuckAround(): OperationA {
            val current = currState.toCharArray()
            for (op in operation) {
                val thing = current[op]
                current[op] = thing.flip()
            }
            val currentState = String(current)
            val newOp = OperationA(
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
    
    data class OperationB(
        val currState: List<Int>,
        val expectedState: List<Int>,
        val operation: Pair<Int, List<Int>>,
        val ops: List<Int>,
        val tooHigh: Boolean = false,
        val success: Boolean = false,
    ) {
        fun findOut(): OperationB {
            val state = currState.toMutableList()
            for (op in operation.second) {
                state[op] = state[op] + 1
            }
            var equality = true
            var yourHighness = false
            val newOp = OperationB(
                currState = state,
                expectedState = expectedState,
                operation = operation,
                ops = ops + operation.first,
            )
            state.forEachIndexed { index, value ->
                val expectedValue = expectedState[index]
                if (value != expectedValue) {
                    equality = false
                    if (value > expectedValue) {
                        yourHighness = true
                    }
                }
            }
            return if (equality) {
                // we are done baby
                newOp.copy(success = true)
            } else {
                newOp.copy(tooHigh = yourHighness)
            }
        }
    }

    fun a(): Long {
        val leastButtonPresses = puzzleInput.sumOf {
            var bestResult = -1L
            val operations = mutableListOf<OperationA>()
            with(it) {
                val currState = ".".repeat(expectedState.length)
                for (button in buttons) {
                    operations.add(
                        OperationA(
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
            val operations = mutableListOf<OperationB>()
            val pastOperations = mutableSetOf<String>()
            with(it) {
                val currState = List(expectedState.length) { 0 }
                buttons.forEachIndexed { index, button ->
                    operations.add(
                        OperationB(
                            currState = currState,
                            expectedState = joltages,
                            ops = listOf(),
                            operation = Pair(index, button),
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
                            val newOp = it.findOut()
                            if (newOp.success) {
                                searching = false
                                bestResult = newOp.ops.size.toLong()
                                listOf(it)
                            } else {
                                val newOperations = buttons.mapIndexed { index, buttonSeq ->
                                    val newOperationSequence = (newOp.ops + index).sorted().joinToString(separator = "")
                                    if (pastOperations.contains(newOperationSequence) || newOp.tooHigh) {
                                        // We don't have to do the same operation sequence again
                                        // 1 we have seen sequence before
                                        // 2 the sequence is already past the limit
                                        pastOperations.add(newOperationSequence) // in case of 2
                                        null
                                    } else {
                                        pastOperations.add(newOperationSequence)
                                        newOp.copy(operation = Pair(index, buttonSeq))
                                    }
                                }
                                //println("${newOperations.size} -> pruning -> ${newOperations.filterNotNull().size}")
                                newOperations.filterNotNull()
                            }
                        }
                    }.flatten()
                    operations.clear()
                    operations.addAll(newOps)
                }
            }
            println("=================")
            println("operation size: ${operations.size}")
            println("sequences seen: ${pastOperations.size}")
            println("best result: $bestResult")
            bestResult
        }
        return leastButtonPresses
    }
}

data class ButtonMash(
    val expectedState: String,
    val buttons: List<List<Int>>,
    val joltages: List<Int>,
)

fun main() {
    val day: DayTen
    val result1: Long
    val result2: Long
    val initTime = measureTimeMillis {
        day = DayTen()
    }
    val time1 = measureTimeMillis {
        //result1 = day.a()
        result1 = 1L
    }
    val time2 = measureTimeMillis {
        result2 = day.b()
    }
    println("=== Summary ===")
    println("Init time was $initTime ms")
    println("Part a($result1) in ${time1}ms")
    println("Part b($result2) in ${time2}ms")
}