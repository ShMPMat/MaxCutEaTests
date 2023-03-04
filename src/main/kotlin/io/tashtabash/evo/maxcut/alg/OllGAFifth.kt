package io.tashtabash.evo.maxcut.alg

import org.apache.commons.math3.distribution.BinomialDistribution
import org.apache.commons.math3.exception.OutOfRangeException
import shmp.random.testProbability
import io.tashtabash.evo.maxcut.util.sample
import java.io.PrintWriter
import kotlin.math.ceil
import kotlin.math.min
import kotlin.math.pow
import kotlin.random.Random


class OllGAFifth(val random: Random, lmb: Int, val f: Double): EvolutionaryAlgorithm {
    val softLmb = lmb.toDouble()

    override fun run(
        n: Int,
        startX: List<Boolean>,
        fitness: (List<Boolean>) -> Double,
        terminatePredicate: (List<Boolean>, Double) -> Boolean,
        log: PrintWriter?
    ): List<Boolean> {
        var currentLmb = softLmb
        log?.println("n lmb")
        log?.println("$n $softLmb")
        log?.println("xPrimeFitness yFitness previousLmb")

        var x = startX
        var xFitness = fitness(startX)

        while (!terminatePredicate(startX, xFitness)) {
            val lmb = ceil(currentLmb).toInt()
            val p: Double = currentLmb / n
            val c: Double = 1.0 / currentLmb
            val distribution = BinomialDistribution(n, p)

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


            val previousLmb = currentLmb
            currentLmb = if (yFitness > xFitness)
                currentLmb / f
            else min(currentLmb * f.pow(0.25), n.toDouble())

            if (yFitness >= xFitness) {
                x = y
                xFitness = yFitness
            }

            log?.println("$xPrimeFitness $yFitness $previousLmb")
        }

        return x
    }
}
