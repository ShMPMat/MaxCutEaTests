package io.tashtabash.evo.maxcut

import org.apache.commons.math3.distribution.HypergeometricDistribution

fun main() {
    val n = readLine()!!.toInt()
    for (f in 1 until n) {//for all fitness levels
        val d = n - f
        println("LEFT=$d")
        val es = mutableListOf(0.0)
        for (k in 1..n) { //find max k
            val dist = HypergeometricDistribution(n, d, k)
            val expectations = (0..k).map { i -> //all possible amounts of success
                val delta = (2 * i - k)
                val p = dist.probability(i)
                delta * p
            }
//            if (d == 50)
//            println("k=" + k + ", " + expectations.sum() + ", " + expectations)
            es += expectations.sum()
        }
        val mx = es.maxOrNull()!!
        println(es.indexOf(mx))
    }
}