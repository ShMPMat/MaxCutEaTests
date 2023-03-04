package io.tashtabash.evo.maxcut

import io.tashtabash.evo.maxcut.alg.EvolutionaryAlgorithm
import io.tashtabash.evo.maxcut.alg.OllGA
import io.tashtabash.evo.maxcut.graph.Graph
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import io.tashtabash.evo.maxcut.graph.generateComplete
import io.tashtabash.evo.maxcut.graph.generatePlanarTiled
import shmp.random.singleton.RandomSingleton
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.sqrt


fun findMaxCut(graph: Graph): Int {
    var maxCutN = 0
    val cut = (1..graph.n).map { false }
        .toMutableList()

    while (!cut[0]) {
        var i = cut.lastIndex
        while (cut[i]) {
            cut[i] = false
            i--
        }
        cut[i] = true

        maxCutN = max(maxCutN, graph.toFitness(cut).toInt())
    }

    return maxCutN
}

private fun runAllPlanarTiledGraphTests(ea: EvolutionaryAlgorithm) =
    runAllPlanarTiledGraphTests(ea.javaClass.simpleName) {
        ea
    }

private fun runAllPlanarTiledGraphTests(name: String, constructEa: (Int) -> EvolutionaryAlgorithm) {
    var log = openLogFile("planarTiledGraph_$name")
    for (i in 5..11)
        repeat(ITERATIONS) {
            val n = 2.0.pow(i).toInt()
            val graph = generatePlanarTiled(n)
            val maxCutN = findMaxCut(graph)

            val ea = constructEa(n)

            runMaxCut(
                ea,
                n,
                graph.edges.size,
                log,
                { _, xFitness -> xFitness >= graph.edges.size.toDouble() / 2 },
                graph::toFitness
            )
            log.flush()
            log.close()
            log = reopenLogFile("planarTiledGraph_$name")
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
                    runAllPlanarTiledGraphTests(ea)
                }
                runAllPlanarTiledGraphTests("OllGASqrt") {
                    OllGA(RANDOM, sqrt(it.toDouble()).toInt())
                }
            }
        }

        for (worker in workers)
            worker.join()
    }
}
