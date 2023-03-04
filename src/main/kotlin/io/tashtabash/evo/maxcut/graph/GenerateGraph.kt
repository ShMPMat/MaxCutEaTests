package io.tashtabash.evo.maxcut.graph

import shmp.random.singleton.chanceOf
import shmp.random.singleton.otherwise
import kotlin.random.Random


fun generateComplete(n: Int): Graph {
    val edges = (0 until n - 1).flatMap { x ->
        (x + 1 until n).map { y -> x to y }
    }

    return Graph(edges, n)
}

fun Random.generateRandom(n: Int, m: Int): Graph {
    val edges = (0 until n - 1).flatMap { x ->
        (x + 1 until n).map { y -> x to y }
    }

    return Graph(edges.asSequence().shuffled(this).take(m).toList(), n)
}

// Correct only for n = 2^m
fun generatePlanarTiled(n: Int): Graph {
    val addEdgeChance = 0.9
    val addDiagonalChance = addEdgeChance / 2
    var fieldWidth = n
    while (fieldWidth * fieldWidth > n)
        fieldWidth /= 2
    val fieldHeight = n / fieldWidth

    fun calcVertexIdx(i: Int, j: Int) = i * fieldHeight + j

    val edges = mutableListOf<Edge>()

    for (i in 0 until fieldWidth) {
        var prevDiagonalSet = true
        for (j in 0 until fieldHeight) {
            if (i != fieldWidth - 1)
                addEdgeChance.chanceOf {
                    edges += calcVertexIdx(i, j) to calcVertexIdx(i + 1, j)
                }
            if (j != fieldHeight - 1)
                addEdgeChance.chanceOf {
                    edges += calcVertexIdx(i, j) to calcVertexIdx(i, j + 1)
                }
            if (!prevDiagonalSet) {
                addDiagonalChance.chanceOf {
                    edges += calcVertexIdx(i, j) to calcVertexIdx(i + 1, j - 1)
                }
            }
            if (i != fieldWidth - 1 && j != fieldHeight - 1)
                addDiagonalChance.chanceOf {
                    edges += calcVertexIdx(i, j) to calcVertexIdx(i + 1, j + 1)
                    prevDiagonalSet = true
                } otherwise {
                    prevDiagonalSet = false
                }
        }
    }

    return Graph(edges, n)
}
