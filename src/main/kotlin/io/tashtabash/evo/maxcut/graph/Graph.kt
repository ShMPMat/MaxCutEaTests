package io.tashtabash.evo.maxcut.graph


data class Graph(val edges: List<Edge>, val n: Int, val useFitnessHash: Boolean = false) {
    private val fitnessHash = mutableMapOf<List<Boolean>, Double>()

    fun toFitness(x: List<Boolean>): Double {
        if (useFitnessHash)
            fitnessHash[x]?.let {
                return it
            }

        val leftComponent = x.mapIndexedNotNull { i, b -> if (b) i else null }
            .toSet()

        val res = edges.count { (x, y) ->
            val xIn = x in leftComponent
            val yIn = y in leftComponent

            xIn && !yIn || !xIn && yIn
        }.toDouble()

        if (useFitnessHash)
            fitnessHash[x] = res

        return res
    }
}

typealias Edge = Pair<Int, Int>
