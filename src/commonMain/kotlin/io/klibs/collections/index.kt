package io.klibs.collections

/**
 * Creates a new heap containing the given values, sorted by the given
 * [SortTest] function.
 *
 * Example:
 * ```
 * // Max heap
 * val heap = heapOf(1, 2, 3) { a, b -> a > b }
 *
 * heap.next() // 3
 * heap.next() // 2
 * heap.next() // 1
 * ```
 *
 * @param values Values to insert into the created [Heap]
 *
 * @param fn [SortTest] function to use to maintain the heap.
 *
 * @return A new [Heap] instance containing the given values.
 *
 * @author Elizabeth Paige Harper - https://github.com/foxcapades
 * @since 0.1.0
 */
fun <T> heapOf(vararg values: T, fn: SortTest<T>): Heap<T> =
  Heap(initialCapacity = values.size, sortTest = fn)
    .apply { values.forEach { insert(it) } }

/**
 * Creates a new heap containing the values in the given iterable, sorted by the
 * given [SortTest] function.
 *
 * Example:
 * ```
 * val items = listOf(1, 2, 3)
 * val heap  = heapOf(items) { a, b -> a > b }
 *
 * heap.next() // 3
 * heap.next() // 2
 * heap.next() // 1
 * ```
 *
 * @param values Values to insert into the created [Heap]
 *
 * @param fn [SortTest] function to use to maintain the heap.
 *
 * @return A new [Heap] instance containing the given values.
 *
 * @author Elizabeth Paige Harper - https://github.com/foxcapades
 * @since 0.1.0
 */
fun <T> heapOf(values: Iterable<T>, fn: SortTest<T>): Heap<T> =
  if (values is Collection)
    Heap(initialCapacity = values.size, sortTest = fn)
      .apply { values.forEach { insert(it) } }
  else
    Heap(sortTest = fn).apply { values.forEach { insert(it) } }

/**
 * Creates a new min-heap containing the given values.
 *
 * Example:
 * ```
 * val heap = minHeapOf(3, 2, 1)
 *
 * heap.next() // 1
 * heap.next() // 2
 * heap.next() // 3
 * ```
 *
 * @param values Values to insert into the created [Heap].
 *
 * @return A new [Heap] instance containing the given values.
 *
 * @author Elizabeth Paige Harper - https://github.com/foxcapades
 * @since 0.1.0
 */
fun <T : Comparable<T>> minHeapOf(vararg values: T): Heap<T> =
  Heap<T>(initialCapacity = values.size) { a, b -> a < b }
    .apply { values.forEach { insert(it) } }

/**
 * Creates a new min-heap containing the given values.
 *
 * Example:
 * ```
 * val items = listOf(3, 2, 1)
 * val heap  = minHeapOf(items)
 *
 * heap.next() // 1
 * heap.next() // 2
 * heap.next() // 3
 * ```
 *
 * @param values Values to insert into the created [Heap].
 *
 * @return A new [Heap] instance containing the given values.
 *
 * @author Elizabeth Paige Harper - https://github.com/foxcapades
 * @since 0.1.0
 */
fun <T : Comparable<T>> minHeapOf(values: Iterable<T>): Heap<T> =
  if (values is Collection)
    Heap<T>(initialCapacity = values.size) { a, b -> a < b }
      .apply { values.forEach { insert(it) } }
  else
    Heap<T> { a, b -> a < b }
      .apply { values.forEach { insert(it) } }

/**
 * Creates a new max-heap containing the given values.
 *
 * Example:
 * ```
 * val heap = maxHeapOf(1, 2, 3)
 *
 * heap.next() // 3
 * heap.next() // 2
 * heap.next() // 1
 * ```
 *
 * @param values Values to insert into the created [Heap].
 *
 * @return A new [Heap] instance containing the given values.
 *
 * @author Elizabeth Paige Harper - https://github.com/foxcapades
 * @since 0.1.0
 */
fun <T : Comparable<T>> maxHeapOf(vararg values: T): Heap<T> =
  Heap<T>(initialCapacity = values.size) { a, b -> a > b }
    .apply { values.forEach { insert(it) } }

/**
 * Creates a new min-heap containing the given values.
 *
 * Example:
 * ```
 * val items = listOf(1, 2, 3)
 * val heap  = maxHeapOf(items)
 *
 * heap.next() // 3
 * heap.next() // 2
 * heap.next() // 1
 * ```
 *
 * @param values Values to insert into the created [Heap].
 *
 * @return A new [Heap] instance containing the given values.
 *
 * @author Elizabeth Paige Harper - https://github.com/foxcapades
 * @since 0.1.0
 */
fun <T : Comparable<T>> maxHeapOf(values: Iterable<T>): Heap<T> =
  if (values is Collection)
    Heap<T>(initialCapacity = values.size) { a, b -> a > b }
      .apply { values.forEach { insert(it) } }
  else
    Heap<T> { a, b -> a > b }
      .apply { values.forEach { insert(it) } }