package io.klibs.collections

/**
 * # Heap
 *
 * A simple, generic heap implementation that uses a caller provided function to
 * enable heaps of custom types.
 *
 * The basic heap types, min-heap and max-heap, can be implemented by using a
 * simple lambda such as:
 * ```
 * // min heap
 * { a, b -> a < b }
 *
 * // max heap
 * { a, b -> a > b }
 * ```
 *
 * @param T Type of values contained in this heap.
 *
 * @author Elizabeth Paige Harper - https://github.com/foxcapades
 * @since 0.1.0
 */
@Suppress("UNCHECKED_CAST")
class Heap<T> {
  private var buffer: Array<Any?>
  private val scale: Float
  private val sortTest: SortTest<T>

  /**
   * The current number of items in this heap.
   */
  var size: Int
    private set

  /**
   * The maximum allowed number of items in this heap.
   */
  val maxSize: Int

  /**
   * The current capacity of the heap.
   */
  val capacity: Int
    get() = buffer.size

  /**
   * Constructs a new heap instance.
   *
   * @param initialCapacity Initial capacity for the data container underlying
   * the new heap.
   *
   * If this value is greater than [maxSize], an exception will be thrown.
   *
   * Default: `8`
   *
   * @param scaleFactor How quickly the capacity will scale.
   *
   * Default: `1.5`
   *
   * @param maxSize Maximum allowed size for this heap.
   *
   * Default: `2147483647`
   *
   * @param sortTest Function used to test and verify the ordering of the values
   * in this heap.
   *
   * @throws IllegalArgumentException If the given [initialCapacity] value is
   * greater than the given [maxSize] value.
   */
  constructor(
    initialCapacity: Int   = 8,
    scaleFactor:     Float = 1.5F,
    maxSize:         Int   = Int.MAX_VALUE,
    sortTest:        SortTest<T>
  ) {
    if (initialCapacity > maxSize)
      throw IllegalArgumentException()

    this.buffer   = arrayOfNulls(initialCapacity)
    this.scale    = scaleFactor
    this.sortTest = sortTest
    this.maxSize  = maxSize
    this.size     = 0
  }

  /**
   * Tests whether this heap contains zero items.
   *
   * @return `true` if this heap contains zero items, `false` if this heap
   * contains one or more items.
   */
  fun isEmpty() = size == 0

  /**
   * Tests whether this heap contains one or more items.
   *
   * @return `true` if this heap contains one or more items, `false` if this
   * heap contains zero items.
   */
  fun isNotEmpty() = size > 0

  /**
   * Destructively iterates over the elements popped from this heap and calls
   * the given function on each value.
   *
   * Example:
   * ```
   * val heap = heapOf(5, 6, 7) { a, b -> a > b }
   *
   * heap.forEach { println(it) }
   *
   * require(heap.isEmpty())
   * ```
   *
   * @param fn Function to call on each value popped from this heap.
   */
  inline fun forEach(fn: (T) -> Unit) {
    for (i in this)
      fn(i)
  }

  /**
   * Destructively iterates over the elements popped from this heap and calls
   * the given function on each value.
   *
   * Additionally, the function will be passed an incrementing index value for
   * each element.
   *
   * Example:
   * ```
   * val heap = heapOf(1, 2, 3) { a, b -> a > b }
   *
   * heap.forEachIndexed { i, it -> println("Item $i = $it") }
   *
   * require(heap.isEmpty())
   * ```
   *
   * @param fn Function to call on each value popped from this heap.
   */
  inline fun forEachIndexed(fn: (i: Int, it: T) -> Unit) {
    var i = 0
    for (v in this)
      fn(i++, v)
  }

  /**
   * Returns the value at the top of this heap without removing it.
   *
   * If the heap is empty, this method will throw an exception.
   *
   * Example:
   * ```
   * val heap = heapOf(1, 2, 3) { a, b -> a > b }
   *
   * require(heap.peekNext() == 3)
   * require(heap.peekNext() == 3)
   * ```
   *
   * @return The value at the top of this heap.
   *
   * @throws NoSuchElementException If this method was called on an empty heap.
   */
  fun peekNext(): T {
    if (size < 1)
      throw NoSuchElementException()

    return buffer[0] as T
  }

  /**
   * Removes and returns the value at the top of this heap.
   *
   * If the heap is empty, this method will throw an exception.
   *
   * Example:
   * ```
   * val heap = heapOf(1, 2, 3) { a, b -> a > b }
   *
   * require(heap.next() == 3)
   * require(heap.next() == 2)
   * require(heap.next() == 1)
   * ```
   *
   * @return The value removed from the top of this heap.
   *
   * @throws NoSuchElementException If this method was called on an empty heap.
   */
  operator fun next(): T {
    // If the heap is empty, bail here.
    if (size < 1)
      throw NoSuchElementException()

    // Get the current top of the heap.
    val out = buffer[0]

    // Swap the value at the root with the last element in the tree
    buffer[0] = buffer[--size]
    // Clear out the last element in the tree's spot
    buffer[size] = null
    // Fix the tree
    heapify(0)

    return out as T
  }

  /**
   * Tests whether this heap contains more items.
   *
   * @return `true` if this heap contains at least one more item, `false` if
   * this heap contains zero items.
   */
  operator fun hasNext() = size > 0

  /**
   * Inserts a new value into the heap.
   *
   * If the new value would push the size of the heap to be greater than
   * [maxSize] an exception will be thrown.
   *
   * Example:
   * ```
   * val heap = heapOf(1, 2, 3) { a, b -> a > b }
   *
   * require(heap.peekNext() == 3)
   *
   * heap.insert(4)
   *
   * require(heap.peekNext() == 4)
   * ```
   *
   * @param value Value to insert into the heap.
   *
   * @throws IllegalStateException If inserting a new value would increase the
   * size of the heap to be greater than [maxSize].
   */
  fun insert(value: T) {
    ensureSize(size+1)
    var i = size
    buffer[size++] = value

    while (i > 0) {
      // Get the parent index.
      val pi = parent(i)

      // If the sorting is already okay, then we can bail as the heap property
      // is already satisfied.
      if (sortTest.test(buffer[pi] as T, buffer[i] as T))
        break

      // If we made it here, then the sorting is not okay, and we need to swap
      // the child node with the parent node
      swap(pi, i)

      // Move up the tree to the parent
      i = pi
    }
  }

  inline operator fun plusAssign(value: T) = insert(value)

  /**
   * Returns a destructive iterator over the contents of this heap.
   *
   * Example:
   * ```
   * val heap = heapOf(1, 2, 3) { a, b -> a > b }
   *
   * for (item in heap)
   *   println(item)
   * ```
   *
   * @return This heap instance as an iterator.
   */
  operator fun iterator() = this

  /**
   * Tests whether the current sorting between two elements is okay.
   *
   * @param pi Parent value index
   *
   * @param ci Child value index
   *
   * @return `true` if the current sorting is okay, `false` if the values should
   * be swapped.
   */
  private inline fun sortOkay(pi: Int, ci: Int) = sortTest.test(buffer[pi] as T, buffer[ci] as T)

  private fun heapify(i: Int) {
    val li  = leftChild(i)
    val ri  = rightChild(i)
    var top = i

    // If there is a left child, and it needs to be swapped with the current top
    // value to maintain the heap property, record the new top of the heap index
    // as being the left child.
    if (li < size && !sortOkay(top, li))
      top = li

    // If the there is a right child, and it needs to be swapped with the
    // current top value (which may be the value of the left sibling), record
    // the new top of the heap index as being the right child.
    if (ri < size && !sortOkay(top, ri))
      top = ri

    // If the top index has changed, swap the elements around to fix the tree.
    if (top != i) {
      swap(i, top)
      heapify(top)
    }
  }

  private fun swap(a: Int, b: Int) {
    val tmp = buffer[a]
    buffer[a] = buffer[b]
    buffer[b] = tmp
  }

  private fun ensureSize(minSize: Int) {
    // If the buffer size is less than the min size required, then we should
    // attempt to resize the buffer to accommodate the new value(s).
    if (buffer.size < minSize) {

      // If the min required size for the new value(s) is greater than the max
      // allowed size for the heap, bail here.
      if (minSize > maxSize)
        throw IllegalStateException("Attempted to grow heap beyond max size of $maxSize")

      // Copy the buffer to a new array of a size that is at least `minSize` and
      // at most `maxSize`, preferring to resize at a rate of
      // `currentSize * scale`.
      buffer = buffer.copyOf(min(maxSize, max(minSize, (buffer.size * scale).toInt())))
    }
  }

}

private inline fun parent(i: Int) = (i-1)/2

private inline fun leftChild(i: Int) = 2*i + 1

private inline fun rightChild(i: Int) = 2*i + 2

private inline fun min(a: Int, b: Int) = if (a > b) b else a

private inline fun max(a: Int, b: Int) = if (a > b) a else b
