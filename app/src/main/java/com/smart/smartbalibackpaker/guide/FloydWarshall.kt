package com.smart.smartbalibackpaker.guide

import com.smart.smartbalibackpaker.core.data.source.local.entity.GuideMapsEntity
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

object FloydWarshall {
    private const val INFINITE = 999999
    private var k = 0
    private val listPlace = ArrayList<GuideMapsEntity?>()
    private var list = ArrayList<Int?>()
    private val anestedLoopIndex = ArrayList<Int?>()

    fun getDistance(valueList : ArrayList<Int?>, listPlace: ArrayList<GuideMapsEntity>, node: HashMap<Any, Int?>){
        k = listPlace.size
        this.listPlace.clear()
        this.listPlace.addAll(listPlace)

        val sortedNode : MutableMap<Any, Int?> = TreeMap(node)

        list.clear()
        list = ArrayList(sortedNode.values)

        if (list.size == k){
            list.forEachIndexed { index, _ ->
                if (index == list.size - 1){
                    createTable()
                }
            }
        }

    }

    private fun createTable() {
        val table = Array(k) { IntArray(k) }
        var indexTable = 0

        for(row in 0 until k){
            for (column in 0 until k){
                if(row == column){
                    table[row][column] = INFINITE
                } else {
                    table[row][column] = list[indexTable]!!
                }

                if(indexTable == list.size - 1){
                    indexTable = 0
                } else {
                    if(row != column){
                        indexTable++
                    }
                }
            }
        }

        anestedLoopIndex.add(1)
        val tableValues = ArrayList<Int>()
        val tableValuesSorted = ArrayList<Int>()
        var tableRow = 0
        var tableColumn = 0
        var idx = 0
        val columnIteration = ArrayList<Int>()

        while(idx < k) {
            tableValues.add(table[tableRow][tableColumn])
            tableColumn++
            if (tableValues.size != k) continue
            tableColumn = 0
            tableValuesSorted.addAll(tableValues)

            for(i in 0 until tableValuesSorted.size - 1){
                for(j in 0 until tableValuesSorted.size-i - 1){
                    if(tableValuesSorted[j] > tableValuesSorted[j+1]){
                        val tmp = tableValuesSorted[j]
                        tableValuesSorted[j] = tableValuesSorted[j+1]
                        tableValuesSorted[j+1] = tmp
                    }
                }
            }

            var minValue = 0
            if(tableValuesSorted[0] < INFINITE){
                minValue = tableValuesSorted[0]
            } else {
                minValue = tableValuesSorted.find { item -> item > INFINITE } ?: 0
            }

            val minValueIndex = tableValues.indexOf(minValue)
            anestedLoopIndex.add(minValueIndex+1)

            tableValuesSorted.clear()
            tableValues.clear()

            for(column in 0 until k){
                if (table[tableRow][column] == minValue) {
                    table[tableRow][column] = INFINITE
                    tableRow = column
                    columnIteration.add(column)
                    columnIteration.forEachIndexed { index,_ ->
                        if(index == 0){
                            table[columnIteration[index]][0] = INFINITE
                        } else {
                            table[columnIteration[index]][0] = INFINITE
                            table[columnIteration[index]][columnIteration[index-1]] = INFINITE
                        }
                    }
                    tableColumn = 0
                    idx++
                }
            }
        }

        defineRoutes()
    }

    fun defineRoutes(): MutableList<GuideMapsEntity?> {
        val orderByIndex = listPlace.associateBy { it?.id }
        val sortedNode = mutableListOf<GuideMapsEntity?>()
        anestedLoopIndex.toSet().toList().forEachIndexed { index, value ->
            if (index < k) {
                sortedNode.add(orderByIndex[value])
            }
        }
        return sortedNode.toSet().toMutableList()
    }

    //      print table
//    for(column in table){
//        for(value in column){
//            print(value)
//            print(" ")
//        }
//        println("")
//    }
//    println("test")
}