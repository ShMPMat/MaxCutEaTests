package io.tashtabash.evo.maxcut.alg

import java.io.PrintWriter


interface EvolutionaryAlgorithm {
    fun run(
        n: Int,
        startX: List<Boolean>,
        fitness: (List<Boolean>) -> Double = { y -> y.sumBy { if (it) 1 else 0 }.toDouble() },
        terminatePredicate: (List<Boolean>, Double) -> Boolean = { _, f -> f == n.toDouble() },
        log: PrintWriter? = null
    ): List<Boolean>
}
