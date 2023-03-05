package io.tashtabash.evo.maxcut

import io.tashtabash.evo.maxcut.alg.EvolutionaryAlgorithm
import io.tashtabash.evo.maxcut.alg.OllGA
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import shmp.random.singleton.RandomSingleton
import kotlin.math.pow
import kotlin.math.sqrt


private fun runAllCompleteBipartiteGraphTests(ea: EvolutionaryAlgorithm) {
    return runAllCompleteBipartiteGraphTests(ea.javaClass.simpleName) {
        ea
    }
//    var log = openLogFile("completeBipartiteGraph_${ea.javaClass.simpleName}")
//    for (i in 5..11)
//        repeat(ITERATIONS) {
//            val n = 2.0.pow(i).toInt()
//            val leftSetSize = n / 2
//            val rightSetSize = n - leftSetSize
//
//            runMaxCut(ea, n, leftSetSize * (n - leftSetSize), log) { x ->
//                val leftComponent = x.mapIndexedNotNull { i, b -> if (b) i else null }
//                val leftSetOnLeftSize = leftComponent.filter { it < leftSetSize }
//                    .size
//                val rightSetOnLeftSize = leftComponent.size - leftSetOnLeftSize
//                val rightSetOnRightSize = rightSetSize - rightSetOnLeftSize
//                val leftSetOnRightSize = (n - leftComponent.size) - rightSetOnRightSize
//
//                leftSetOnLeftSize * rightSetOnRightSize + rightSetOnLeftSize * leftSetOnRightSize.toDouble()
//            }
//
//            log.flush()
//            log.close()
//            log = reopenLogFile("completeBipartiteGraph_${ea.javaClass.simpleName}")
//        }
//    log.close()
}

private fun runAllCompleteBipartiteGraphTests(name: String, constructEa: (Int) -> EvolutionaryAlgorithm) {
    var log = openLogFile("completeBipartiteGraph_$name")
    for (i in 5..11)
        repeat(ITERATIONS) {
            val n = 2.0.pow(i).toInt()
            val leftSetSize = n / 2
            val rightSetSize = n - leftSetSize

            val ea = constructEa(n)

            runMaxCut(ea, n, leftSetSize * (n - leftSetSize), log) { x ->
                val leftComponent = x.mapIndexedNotNull { i, b -> if (b) i else null }
                val leftSetOnLeftSize = leftComponent.filter { it < leftSetSize }
                    .size
                val rightSetOnLeftSize = leftComponent.size - leftSetOnLeftSize
                val rightSetOnRightSize = rightSetSize - rightSetOnLeftSize
                val leftSetOnRightSize = (n - leftComponent.size) - rightSetOnRightSize

                leftSetOnLeftSize * rightSetOnRightSize + rightSetOnLeftSize * leftSetOnRightSize.toDouble()
            }

            log.flush()
            log.close()
            log = reopenLogFile("completeBipartiteGraph_$name")
        }
    log.close()
}

suspend fun main(args: Array<String>) {//TODO more threads?
    RandomSingleton.safeRandom = RANDOM

    coroutineScope {
        val workers = (1..WORKERS).map {
            launch {
                for (ea in defaultEas) {
                    println("Running bipartite complete")
                    runAllCompleteBipartiteGraphTests(ea)
                }
                runAllCompleteBipartiteGraphTests("OllGASqrt") {
                    OllGA(RANDOM, sqrt(it.toDouble()).toInt())
                }
            }
        }

        for (worker in workers)
            worker.join()
    }
}
