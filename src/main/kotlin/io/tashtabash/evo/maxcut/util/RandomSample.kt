package io.tashtabash.evo.maxcut.util

import kotlin.random.Random


fun Random.randomX(n: Int) = (1..n).map { this.nextBoolean() }

fun <E> Random.sample(lst: Iterable<E>, n: Int) = lst.asSequence()
    .shuffled(this)
    .take(n)
