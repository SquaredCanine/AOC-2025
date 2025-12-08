package com.quinten.shame

import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.system.measureTimeMillis

class DayEight() {
    val puzzleInput = javaClass.getResource("/day8/test.txt")
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
    
    fun noLongerFun(): Long {
        val combinations = puzzleInput.mapIndexed { index, target ->
            ((index + 1) until puzzleInput.size).map { j ->
                val coordinate = puzzleInput[j]
                Triple(target, coordinate, target.distanceTo(coordinate))
            }
        }.flatten()
            .sortedBy { it.third }
        
        val circuits = mutableListOf<Set<Coordinate3D>>()
        val map = mutableMapOf<Coordinate3D, MutableSet<Coordinate3D>>()
        
        var connectionsMade = 0
        combinations.take(10).forEach { (coordinate, neighbor, distance) ->
            println("$coordinate <-> $neighbor with $distance")
            val coordinateSet = map.getOrElse(coordinate) { null }
            val neighborSet = map.getOrElse(neighbor) { null }

            if (coordinateSet == null && neighborSet == null) {
                println("initializing set")
                val set = mutableSetOf(coordinate, neighbor)
                map[coordinate] = set
                map[neighbor] = set
                circuits.add(set)
                connectionsMade++
            } else if (coordinateSet == null && neighborSet != null) {
                println("adding coordinate to neighborSet and setting for original")
                neighborSet.add(coordinate)
                map[coordinate] = neighborSet
                connectionsMade++
            } else if (coordinateSet != null && neighborSet == null) {
                println("adding coordinate to coordinateSet and setting for neighbor")
                coordinateSet.add(coordinate)
                map[neighbor] = coordinateSet
                connectionsMade++
            } else {
                // merge?
                if (neighborSet == coordinateSet) {
                    println("same set")
                    coordinateSet!!.add(coordinate)
                    neighborSet!!.add(coordinate)
                    connectionsMade++
                } else {
                    println("not same set 🚨🚨🚨🚨🚨🚨🚨🚨")
                    val sameSet = mutableSetOf(*coordinateSet!!.toTypedArray(), *neighborSet!!.toTypedArray())
                    circuits.remove(neighborSet)
                    circuits.remove(coordinateSet)
                    circuits.add(sameSet)
                    sameSet.forEach {
                        map[it] = sameSet
                    }
                    connectionsMade++
                }
            }
        }
        val sorted = circuits.sortedByDescending { it.size }
        val first = sorted[0]
        val second = sorted[1]
        val third = sorted[2]
        val result = (first.size * second.size * third.size).toLong()
        println("${first.size} * ${second.size} * ${third.size} = $result")
        return result
    }
    
    fun a(): Long {
        val circuits = mutableListOf<Set<Coordinate3D>>()
        val map = mutableMapOf<Coordinate3D, MutableSet<Coordinate3D>>()
        puzzleInput.forEach { coordinate ->
            val neighbor = puzzleInput.closestNeighbor(coordinate)
            val coordinateSet = map.getOrElse(coordinate) { null }
            val neighborSet = map.getOrElse(neighbor.first) { null }
            
            if (coordinateSet == null && neighborSet == null) {
                println("initializing set")
                val set = mutableSetOf(coordinate, neighbor.first)
                map[coordinate] = set
                map[neighbor.first] = set
                circuits.add(set)
            } else if (coordinateSet == null && neighborSet != null) {
                println("adding coordinate to neighborSet and setting for original")
                neighborSet.add(coordinate)
                map[coordinate] = neighborSet
            } else if (coordinateSet != null && neighborSet == null) {
                println("adding coordinate to coordinateSet and setting for neighbor")
                coordinateSet.add(coordinate)
                map[neighbor.first] = coordinateSet
            } else {
                // merge?
                if (neighborSet == coordinateSet) {
                    println("same set")
                    coordinateSet!!.add(coordinate)
                    neighborSet!!.add(coordinate)
                } else {
                    println("not same set 🚨🚨🚨🚨🚨🚨🚨🚨")
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
        val allElements = circuits.fold(listOf<Coordinate3D>()) { acc, set ->
            acc + set.toList()
        }
        println(allElements.size)
        println(map)
        val sorted = circuits.sortedByDescending { it.size }
        sorted.forEach {
            println(it)
        }
        val first = sorted[0]
        val second = sorted[1]
        val third = sorted[2]
        println("${first.size} * ${second.size} * ${third.size}")
        return (first.size * second.size * third.size).toLong()
    }

    fun b(): Long {
        return 0L
    }

    data class Coordinate3D(val x: Int, val y: Int, val z: Int) {
        infix fun distanceTo(other: Coordinate3D): Double = sqrt(
            (this.x - other.x).toDouble().pow(2.0) + 
            (this.y - other.y).toDouble().pow(2.0) + 
            (this.z - other.z).toDouble().pow(2.0)
        )
    }
    
    fun List<Coordinate3D>.closestNeighbor(target: Coordinate3D): Pair<Coordinate3D, Double> {
        val allValues = this.map {
            Pair(it, target.distanceTo(it))
        }.sortedBy { it.second }
        return allValues[1]
    }
}

fun main() {
    val day = DayEight()
    val result1: Long
    val result2: Long
    println(day.noLongerFun())
    val time1 = measureTimeMillis {
        result1 = day.a()
    }
    val time2 = measureTimeMillis {
        result2 = day.b()
    }
    println("Part a($result1) in ${time1}ms")
    println("Part b($result2) in ${time2}ms")
}