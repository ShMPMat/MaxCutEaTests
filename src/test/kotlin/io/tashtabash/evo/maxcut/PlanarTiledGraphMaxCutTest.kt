package io.tashtabash.evo.maxcut

import io.tashtabash.evo.maxcut.graph.Graph
import io.tashtabash.evo.maxcut.graph.generatePlanarTiled
import org.junit.Test

import org.junit.jupiter.api.Assertions.*
import shmp.random.singleton.RandomSingleton
import kotlin.random.Random


class PlanarTiledGraphMaxCutTest {
    @Test
    fun `findMaxCut finds max cut of a line graph`() {
        // 0-1-2-3
        // |
        // 4-5-6-7
        val graph = Graph(
            listOf(0 to 1, 1 to 2, 2 to 3, 4 to 5, 5 to 6, 6 to 7, 0 to 4),
            8
        )
        val maxCutN = findMaxCut(graph)

        assertEquals(7, maxCutN)
    }

    @Test
    fun `findMaxCut finds max cut with a tile`() {
        // 0-1-2-3
        // | |
        // 4-5-6-7
        val graph = Graph(
            listOf(0 to 1, 1 to 2, 2 to 3, 4 to 5, 5 to 6, 6 to 7, 0 to 4, 1 to 5),
            8
        )
        val maxCutN = findMaxCut(graph)

        assertEquals(8, maxCutN)
    }

    @Test
    fun `findMaxCut finds max cut which is M - 1`() {
        // 0-1-2-3
        // |/
        // 4-5-6-7
        val graph = Graph(
            listOf(0 to 1, 1 to 2, 2 to 3, 4 to 5, 5 to 6, 6 to 7, 0 to 4, 1 to 4),
            8
        )
        val maxCutN = findMaxCut(graph)

        assertEquals(7, maxCutN)
    }

    @Test
    fun `findMaxCut handles big graphs`() {
        RandomSingleton.safeRandom = Random(0)
        val graph = generatePlanarTiled(128)
//        val graph = generatePlanarTiled(2048)
        val maxCutN = findMaxCut(graph)
    }
}
