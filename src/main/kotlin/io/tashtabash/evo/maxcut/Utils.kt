package io.tashtabash.evo.maxcut

import io.tashtabash.evo.maxcut.alg.EvolutionaryAlgorithm
import io.tashtabash.evo.maxcut.alg.OllGA
import io.tashtabash.evo.maxcut.alg.OllGAFifth
import io.tashtabash.evo.maxcut.alg.OoEA
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import io.tashtabash.evo.maxcut.graph.generateComplete
import io.tashtabash.evo.maxcut.graph.generateRandom
import java.io.*
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.random.Random


val RANDOM = Random(12345)
val defaultEas = listOf(
    OllGAFifth(RANDOM, 10, 1.5),
    OllGA(RANDOM, 10),
    OoEA(RANDOM)
)
const val PREFIX_PATH = ".\\out"
const val ITERATIONS = 60
const val WORKERS = 4


fun openLogFile(name: String): PrintWriter {
    val threadId = Thread.currentThread().id
    return PrintWriter(BufferedOutputStream(FileOutputStream("$PREFIX_PATH\\${name}_$threadId.txt", false)))
}

fun reopenLogFile(name: String): PrintWriter {
    val threadId = Thread.currentThread().id
    return PrintWriter(BufferedOutputStream(FileOutputStream("$PREFIX_PATH\\${name}_$threadId.txt", true)))
}


fun List<Boolean>.toSpecimenString() = joinToString("") { if (it) "1" else "0" }


fun runMaxCut(
    ea: EvolutionaryAlgorithm,
    n: Int,
    edgeCount: Int,
    log: PrintWriter?,
    terminatePredicate: (List<Boolean>, Double) -> Boolean = { _, xFitness -> xFitness >= edgeCount.toDouble() },
    fitness: (List<Boolean>) -> Double,
) {
    val cut = ea.run(
        n,
        startX = (1..n).map { true },
        terminatePredicate = terminatePredicate,
        fitness = fitness,
        log = log
    )

    println(n)
//    println(cut.toSpecimenString())
}

//private fun runRandomGraphTests(ea: EvolutionaryAlgorithm, logName: String, mGenerator: (Int) -> Int) {
//    var log = openLogFile(logName)
//    for (i in 5..12)
//        repeat(ITERATIONS) {
//            val n = 2.0.pow(i).toInt()
//            val graph = RANDOM.generateRandom(n, mGenerator(n))
//
    //            runMaxCut(ea, n, graph.edges.size, log, graph::toFitness)
//
//            log.flush()
//            log.close()
//            log = reopenLogFile(logName)
//        }
//    log.close()
//}


suspend fun main(args: Array<String>) {//TODO more threads?
//    RandomSingleton.safeRandom = RANDOM

    coroutineScope {
        val workers = (1..WORKERS).map {
            launch {
//                for (ea in listOf(OllGA(RANDOM, 10), OoEA(RANDOM), NaiveOoEA(RANDOM))) {
//                for (ea in listOf(OllGA(RANDOM, 10))) {
                for (ea in listOf(OllGAFifth(RANDOM, 10, 1.5), OllGA(RANDOM, 10), OoEA(RANDOM))) {
//                    println("Running random n")
//                    runRandomGraphTests(ea, "nGraph_${ea.javaClass.simpleName}") {
//                        it
//                    }

//                    println("Running random n logn")
//                    runRandomGraphTests(ea, "nlognGraph_${ea.javaClass.simpleName}") {
//                        (it * log2(it.toDouble())).toInt()
//                    }

//                    println("Running random n^1.5")
//                    runRandomGraphTests(ea, "nsqrtn_${ea.javaClass.simpleName}") {
//                        it.toDouble().pow(1.5).toInt()
//                    }
                }
            }
        }

        for (worker in workers)
            worker.join()
    }
}
