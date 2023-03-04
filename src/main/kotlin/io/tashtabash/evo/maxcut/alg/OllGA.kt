package io.tashtabash.evo.maxcut.alg

import org.apache.commons.math3.distribution.BinomialDistribution
import shmp.random.testProbability
import io.tashtabash.evo.maxcut.util.sample
import java.io.PrintWriter
import kotlin.random.Random


class OllGA(val random: Random, val lmb: Int): EvolutionaryAlgorithm {
    override fun run(
        n: Int,
        startX: List<Boolean>,
        fitness: (List<Boolean>) -> Double,
        terminatePredicate: (List<Boolean>, Double) -> Boolean,
        log: PrintWriter?
    ) = run(n, startX, p = lmb.toDouble() / n, c = 1.0 / lmb, fitness, terminatePredicate, log)

    fun run(
        n: Int,
        startX: List<Boolean>,
        p: Double = lmb.toDouble() / n,
        c: Double = 1.0 / lmb,
        fitness: (List<Boolean>) -> Double = { y -> y.sumBy { if (it) 1 else 0 }.toDouble() },
        terminatePredicate: (List<Boolean>, Double) -> Boolean = { _, f -> f == n.toDouble() },
        log: PrintWriter? = null
    ): List<Boolean> {
        log?.println("n lmb")
        log?.println("$n $lmb")
        log?.println("xPrimeFitness yFitness")

        val distribution = BinomialDistribution(n, p)
        var x = startX
        var xFitness = fitness(startX)

        while (!terminatePredicate(startX, xFitness)) {
            val l = distribution.sample()
            val mutants = (1..lmb).map {
                val flipPositions = random.sample(0 until n, l).toSet()
                val mutant = x.mapIndexed { i, b -> if (i in flipPositions) !b else b }

                mutant to fitness(mutant)
            }
            val (xPrime, xPrimeFitness) = mutants.maxByOrNull { it.second }
                ?: error("Lambda cannot be 0")

            val crossoverOffspring = (1..lmb).map {
                val offspring = x.mapIndexed { i, b -> if (testProbability(c, random)) xPrime[i] else b }

                offspring to fitness(offspring)
            }
            val (y, yFitness) = crossoverOffspring.maxByOrNull { it.second }
                ?: error("Lambda cannot be 0")

            if (yFitness >= xFitness) {
                x = y
                xFitness = yFitness
            }

            log?.println("$xPrimeFitness $yFitness")
        }

        return x
    }
}
