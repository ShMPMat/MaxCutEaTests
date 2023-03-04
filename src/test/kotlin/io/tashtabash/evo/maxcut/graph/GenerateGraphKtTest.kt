package io.tashtabash.evo.maxcut.graph

import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import shmp.random.singleton.RandomSingleton
import kotlin.random.Random


class GenerateGraphTest {
    @Test
    fun `generateComplete generates the right amount of edges`() {
        val n = 32
        val graph = generateComplete(n)

        assertEquals(n * (n - 1) / 2, graph.edges.size)
    }

    @Test
    fun `generatePlanarTiled doesn't generate duplicate edges`() {
        RandomSingleton.safeRandom = Random(0)

        for (i in 0..1000) {
            val graph = generatePlanarTiled(8)

            assertEquals(graph.edges.size, graph.edges.toSet().size)
        }
    }

    @Test
    fun `generatePlanarTiled doesn't generate vertices out of range`() {
        RandomSingleton.safeRandom = Random(0)

        var n = 2
        for (p in (1..20)) {
            val graph = generatePlanarTiled(n)

            assert(graph.edges.flatMap { it.toList() }.minOrNull()!! >= 0)
            assert(graph.edges.flatMap { it.toList() }.maxOrNull()!! < n)

            n *= 2
        }
    }
}
