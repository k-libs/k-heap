package io.klibs.collections

/**
 * # Heap Sort Tester
 *
 * Defines a function that is used to verify the sorting of elements in a [Heap]
 * instance.
 *
 * This function takes two values in the order they currently appear and tests
 * whether that order is correct, returning `true` if the order _is_ correct, or
 * `false` if the order is incorrect.
 *
 * @param T Type of values that will be tested.
 *
 * @author Elizabeth Paige Harper - https://github.com/foxcapades
 * @since 0.1.0
 */
fun interface SortTest<T> {

  /**
   * Tests whether the given values are in the correct order.
   *
   * @param first The first element to test, this appears before [second] in the
   * heap.
   *
   * @param second The second element to test, this appears after [first] in the
   * heap.
   *
   * @return `true` if the current order is correct, or `false` if the order is
   * incorrect.
   */
  fun test(first: T, second: T): Boolean
}