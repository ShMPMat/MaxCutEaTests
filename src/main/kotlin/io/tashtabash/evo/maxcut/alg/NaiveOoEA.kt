package io.tashtabash.evo.maxcut.alg

import shmp.random.testProbability
import java.io.PrintWriter
import kotlin.random.Random


class NaiveOoEA(val random: Random): EvolutionaryAlgorithm {
    override fun run(
        n: Int,
        startX: List<Boolean>,
        fitness: (List<Boolean>) -> Double,
        terminatePredicate: (List<Boolean>, Double) -> Boolean,
        log: PrintWriter?
    ): List<Boolean> {
        log?.println(n)

        val x = startX.toMutableList()
        var xFitness = fitness(startX)

        while (!terminatePredicate(startX, xFitness)) {
            for (i in x.indices)
                if (testProbability(1.0 / n, random))
                    x[i] = !x[i]

            xFitness = fitness(x)

            log?.println("$xFitness")
        }

        return x
    }
}
