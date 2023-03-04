package io.tashtabash.evo.maxcut

import io.tashtabash.evo.maxcut.alg.EvolutionaryAlgorithm
import io.tashtabash.evo.maxcut.alg.OllGA
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import io.tashtabash.evo.maxcut.graph.generateComplete
import shmp.random.singleton.RandomSingleton
import kotlin.math.pow
import kotlin.math.sqrt


private fun runAllCompleteGraphTests(ea: EvolutionaryAlgorithm) =
    runAllCompleteGraphTests(ea.javaClass.simpleName) {
        ea
    }

private fun runAllCompleteGraphTests(name: String, constructEa: (Int) -> EvolutionaryAlgorithm) {
    var log = openLogFile("completeGraph_$name")
    for (i in 5..11)
        repeat(ITERATIONS) {
            val n = 2.0.pow(i).toInt()
            val graph = generateComplete(n)
            val ea = constructEa(n)

            runMaxCut(
                ea,
                n,
                graph.edges.size,
                log,
                { _, xFitness -> xFitness >= graph.edges.size.toDouble() / 2 }
            ) { x ->//TODO calculate edgeCount
                val leftComponentSize = x.count { it }
                    .toDouble()

                leftComponentSize * (n - leftComponentSize)
            }

            log.flush()
            log.close()
            log = reopenLogFile("completeGraph_$name")
        }
    log.close()
}

suspend fun main(args: Array<String>) {
    RandomSingleton.safeRandom = RANDOM

    coroutineScope {
        val workers = (1..WORKERS).map {
            launch {
                for (ea in defaultEas) {
                    println("Running complete")
                    runAllCompleteGraphTests(ea)
                }
                runAllCompleteGraphTests("OllGASqrt") {
                    OllGA(RANDOM, sqrt(it.toDouble()).toInt())
                }
            }
        }

        for (worker in workers)
            worker.join()
    }
}
