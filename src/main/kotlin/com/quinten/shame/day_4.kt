package com.quinten.shame

class DayFour() {
    val puzzleInput = javaClass.getResource("/day4/input.txt")
        .readText()
        .split("\n")
        .map { it.split("").filter { it.isNotEmpty() } }

    fun a(): Long {
        var result = 0L
        puzzleInput.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { colIndex, value ->
                if (value == "@") {
                    val neighbours = puzzleInput.getNeighbours(rowIndex, colIndex)
                    if (neighbours.size < 4) {
                        result++
                    }
                }
            }
        }
        return result
    }

    fun b(): Long {
        var result = 0L
        var intermediaryResult: Long
        val theForkingPaperRolls = puzzleInput.map { it.toMutableList() }.toMutableList()
        val rollsToBeRemoved = mutableListOf<Pair<Int, Int>>()
        println("===start===")
        println(theForkingPaperRolls.joinToString("\n") { it.joinToString(separator = "") })
        println("===LETSGO===")
        do {
            intermediaryResult = 0L
            theForkingPaperRolls.forEachIndexed { rowIndex, row ->
                row.forEachIndexed { colIndex, value ->
                    if (value == "@") {
                        val neighbours = theForkingPaperRolls.getNeighbours(rowIndex, colIndex)
                        if (neighbours.size < 4) {
                            rollsToBeRemoved.add(Pair(rowIndex, colIndex))
                            intermediaryResult++
                        }
                    }
                }
            }
            // Finish up
            result += intermediaryResult
            // remove rolls
            println("removing $rollsToBeRemoved")
            for (roll in rollsToBeRemoved) {
                theForkingPaperRolls[roll.first][roll.second] = "."
            }
            rollsToBeRemoved.clear()
            println(theForkingPaperRolls.joinToString("\n") { it.joinToString(separator = "") })
            println("==iteration done==")
        } while (intermediaryResult > 0L)

        return result
    }

    fun List<List<String>>.getNeighbours(x: Int, y: Int): List<String> {
        val combinations = mutableListOf(
            Pair(x - 1, y - 1),
            Pair(x - 1, y),
            Pair(x - 1, y + 1),
            Pair(x, y - 1),
            Pair(x, y + 1),
            Pair(x + 1, y - 1),
            Pair(x + 1, y),
            Pair(x + 1, y + 1),
        )
//        println("showing neighbours of $x, $y")
//        println("""
//            ${combinations[0]}${combinations[3]}${combinations[5]}
//            ${combinations[1]}$x,$y${combinations[6]}
//            ${combinations[2]}${combinations[4]}${combinations[7]}
//        """.trimIndent())
//        println("""
//            ${this.getValueOrElse(combinations[0])}${this.getValueOrElse(combinations[3])}${this.getValueOrElse(combinations[5])}
//            ${this.getValueOrElse(combinations[1])}${this.getValueOrElse(x, y)}${this.getValueOrElse(combinations[6])}
//            ${this.getValueOrElse(combinations[2])}${this.getValueOrElse(combinations[4])}${this.getValueOrElse(combinations[7])}
//        """.trimIndent())
        return combinations.mapNotNull { (xx, yy) ->
            val item = this.getValueOrElse(xx, yy, null)
            if (item == "@") {
                item
            } else {
                null
            }
        }
    }

    fun List<List<String>>.getValueOrElse(x: Int, y: Int, defaultValue: String? = " "): String? {
        val row = this.getOrNull(x)
        val item: String? = row?.getOrNull(y)
        return item ?: defaultValue
    }

    fun List<List<String>>.getValueOrElse(coordinates: Pair<Int, Int>): String? = this.getValueOrElse(coordinates.first, coordinates.second)
}

fun main() {
    println(DayFour().a())
    println(DayFour().b())
}