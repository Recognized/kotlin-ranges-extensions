package com.github.recognized.kotlin.ranges.extensions

import kotlin.Comparator
import kotlin.math.max
import kotlin.math.min

val IntRange.Companion.INTERSECTS_CMP get() = INTERSECTS_CMP_INT

inline val IntRange.length: Int
  get() = max(endInclusive - start + 1, 0)

fun IntRange.mapLong(fn: (Int) -> Long): LongRange = fn(start)..fn(endInclusive)

fun IntRange.mapInt(fn: (Int) -> Int): IntRange = fn(start)..fn(endInclusive)

fun IntRange.shift(delta: Int) = (start + delta)..(endInclusive + delta)

fun IntRange.intersects(other: IntRange): Boolean {
  if (other.isEmpty() || isEmpty()) return false
  return !(start > other.endInclusive || other.start > endInclusive)
}

fun IntRange.inside(x: Int) = when {
  x in start..endInclusive -> x
  x < start -> start
  else -> endInclusive
}

fun IntRange.padded(padding: Int) = ((start + padding)..(endInclusive - padding))
  .let { if (it.isEmpty()) IntRange.EMPTY else it }

fun IntRange.copy(start: Int = this.start, endInclusive: Int = this.endInclusive): IntRange {
  return start..endInclusive
}

fun IntRange.Companion.from(startOffsetMs: Int, length: Int): IntRange {
  return IntRange(startOffsetMs, startOffsetMs + length - 1)
}

infix fun IntRange.intersectWith(other: IntRange): IntRange {
  return if (intersects(other)) IntRange(max(start, other.start), min(endInclusive, other.endInclusive))
  else IntRange.EMPTY
}

operator fun IntRange.component1(): Int = start

operator fun IntRange.component2(): Int = endInclusive

operator fun IntRange.compareTo(other: IntRange): Int {
  return start - other.start
}

operator fun IntRange.plus(other: IntRange): IntRange {
  if (isEmpty() || this in other) return other
  if (other.isEmpty() || other in this) return this
  return IntRange(min(start, other.start), max(endInclusive, other.endInclusive))
}

operator fun IntRange.contains(other: IntRange): Boolean {
  return start <= other.start && other.endInclusive <= endInclusive
}

private val INTERSECTS_CMP_INT = Comparator<IntRange> { a, b ->
  return@Comparator if (a.intersects(b)) 0 else a.start - b.start
}
