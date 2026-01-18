package com.omni.core.list;

/**
 * Base interface for all list implementations in the Omni-System.
 *
 * <p>This interface defines the contract for linear data structures that support index-based access
 * and iteration.
 *
 * <p>Guarantees: null elements are permitted unless implementation specifies otherwise.
 *
 * @param <T> the type of elements in this list
 */
public interface MyList<T> extends Iterable<T> {

  /**
   * Appends element to the end of the list.
   *
   * @param element the element to add (might be null)
   * @throws IllegalStateException if list is at capacity and cannot grow
   */
  void add(T element);

  /**
   * Inserts element at specified index.
   *
   * @param index position to insert (0 <= index <= size)
   * @param element the element to insert
   * @throws IndexOutOfBoundsException if index < 0 or index > size
   */
  void add(int index, T element);

  /**
   * Returns element at specified index.
   *
   * @param index position to retrieve (0 <= index < size)
   * @return the element at that position
   * @throws IndexOutOfBoundsException if index < 0 or index >= size
   */
  T get(int index);

  /**
   * Removes and returns element at specified index.
   *
   * @param index position to remove (0 <= index < size)
   * @return the removed element
   * @throws IndexOutOfBoundsException if index < 0 or index >= size
   */
  T remove(int index);

  /**
   * Returns the number of elements.
   *
   * @return element count (always >= 0)
   */
  int size();

  /**
   * Returns true if list contains no elements.
   *
   * @return size() == 0
   */
  boolean isEmpty();

  /** Removes all elements from the list. After this call, size() returns 0. */
  void clear();
}
