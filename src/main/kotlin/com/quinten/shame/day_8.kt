package com.quinten.shame

import kotlin.collections.MutableMap
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.system.measureTimeMillis

class DayEight() {
    val puzzleInput = javaClass.getResource("/day8/input.txt")
        .readText()
        .split("\n")
        .map { 
            val coordinates = it.split(",")
            Coordinate3D(
                x = coordinates[0].toInt(),
                y = coordinates[1].toInt(),
                z = coordinates[2].toInt(),
            )
        }

    val combinations = puzzleInput.mapIndexed { index, target ->
        ((index + 1) until puzzleInput.size).map { j ->
            val coordinate = puzzleInput[j]
            Triple(target, coordinate, target.distanceTo(coordinate))
        }
    }.flatten()
        .sortedBy { it.third }
    
    val zero = Coordinate3D(-1, -1, -1)
    val uniqueEdges = combinations.distinctBy {
        val coords = listOf(it.first, it.second).sortedBy { whateverTheFuck -> whateverTheFuck.distanceTo(zero) }
        "${coords[0]}-${coords[1]}"
    }.sortedBy { it.third }
    
    fun a(): Long {
        val (circuits, map) = puzzleInput.toCircuitMap()
        uniqueEdges.take(1000).forEach { (coordinate, neighbor, distance) ->
            val coordinateSet = map.getOrElse(coordinate) { null }
            val neighborSet = map.getOrElse(neighbor) { null }

            if (coordinateSet == null && neighborSet == null) {
                val set = mutableSetOf(coordinate, neighbor)
                map[coordinate] = set
                map[neighbor] = set
                circuits.add(set)
            } else if (coordinateSet == null && neighborSet != null) {
                neighborSet.add(coordinate)
                map[coordinate] = neighborSet
            } else if (coordinateSet != null && neighborSet == null) {
                coordinateSet.add(coordinate)
                map[neighbor] = coordinateSet
            } else {
                // merge?
                if (neighborSet == coordinateSet) {
                    coordinateSet!!.add(coordinate)
                    neighborSet!!.add(coordinate)
                } else {
                    val sameSet = mutableSetOf(*coordinateSet!!.toTypedArray(), *neighborSet!!.toTypedArray())
                    circuits.remove(neighborSet)
                    circuits.remove(coordinateSet)
                    circuits.add(sameSet)
                    sameSet.forEach {
                        map[it] = sameSet
                    }
                }
            }
        }
        val sorted = circuits.sortedByDescending { it.size }
        val first = sorted[0]
        val second = sorted[1]
        val third = sorted[2]
        val result = (first.size * second.size * third.size).toLong()
        return result
    }

    fun b(): Long {
        val (circuits, map) = puzzleInput.toCircuitMap()
        uniqueEdges.forEach { (coordinate, neighbor, _) ->
            val coordinateSet = map.getOrElse(coordinate) { null }
            val neighborSet = map.getOrElse(neighbor) { null }

            if (coordinateSet == null && neighborSet == null) {
                val set = mutableSetOf(coordinate, neighbor)
                map[coordinate] = set
                map[neighbor] = set
                circuits.add(set)
            } else if (coordinateSet == null && neighborSet != null) {
                neighborSet.add(coordinate)
                map[coordinate] = neighborSet
            } else if (coordinateSet != null && neighborSet == null) {
                coordinateSet.add(coordinate)
                map[neighbor] = coordinateSet
            } else {
                // merge?
                if (neighborSet == coordinateSet) {
                    coordinateSet!!.add(coordinate)
                    neighborSet!!.add(coordinate)
                } else {
                    if (circuits.size == 2) { // We are done baby
                        return neighbor.x.toLong() * coordinate.x
                    } else {
                        val sameSet = mutableSetOf(*coordinateSet!!.toTypedArray(), *neighborSet!!.toTypedArray())
                        circuits.remove(neighborSet)
                        circuits.remove(coordinateSet)
                        circuits.add(sameSet)
                        sameSet.forEach {
                            map[it] = sameSet
                        }    
                    }
                }
            }
        }
        return -1L // Shouldn't happen
    }
    
    fun List<Coordinate3D>.toCircuitMap(): Pair<MutableList<Set<Coordinate3D>>, MutableMap<Coordinate3D, MutableSet<Coordinate3D>>> {
        val circuits = mutableListOf<Set<Coordinate3D>>()
        val map: MutableMap<Coordinate3D, MutableSet<Coordinate3D>> = this
            .associateWith { point -> mutableSetOf(point).also { set -> circuits.add(set) } }
            .toMutableMap()
        return Pair(circuits, map)
    } 
    
    data class Coordinate3D(val x: Int, val y: Int, val z: Int) {
        infix fun distanceTo(other: Coordinate3D): Double = sqrt(
            (this.x - other.x).toDouble().pow(2.0) + 
            (this.y - other.y).toDouble().pow(2.0) + 
            (this.z - other.z).toDouble().pow(2.0)
        )
    }
}

fun main() {
    val day: DayEight
    val result1: Long
    val result2: Long
    val initTime = measureTimeMillis {
        day = DayEight()
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