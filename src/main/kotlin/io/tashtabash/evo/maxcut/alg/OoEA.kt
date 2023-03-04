package io.tashtabash.evo.maxcut.alg

import shmp.random.testProbability
import java.io.PrintWriter
import kotlin.random.Random


class OoEA(val random: Random): EvolutionaryAlgorithm {
    override fun run(
        n: Int,
        startX: List<Boolean>,
        fitness: (List<Boolean>) -> Double,
        terminatePredicate: (List<Boolean>, Double) -> Boolean,
        log: PrintWriter?
    ): List<Boolean> {
        log?.println("n")
        log?.println(n)
        log?.println("yFitness")

        var x = startX.toMutableList()
        var xFitness = fitness(startX)

        while (!terminatePredicate(startX, xFitness)) {
            val y = x.map {
                if (testProbability(1.0 / n, random))
                    !it
                else
                    it
            }.toMutableList()
            val yFitness = fitness(y)

            if (yFitness >= xFitness) {
                x = y
                xFitness = yFitness
            }

            log?.println("$yFitness")
        }

        return x
    }
}
