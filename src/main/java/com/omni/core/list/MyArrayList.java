package com.omni.core.list;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A dynamic array implementation of the MyList interface.
 *
 * <p>Provides O(1) access to elements by index and amortized O(1) appending. Supports dynamic
 * resizing by doubling capacity when full.
 *
 * <pre>
 *   Structure:
 *
 *   Index:   0   1   2   3   4   ...   Capacity-1
 *          +---+---+---+---+---+-----+---+
 *   Array: | A | B | C |   |   | ... |   |
 *          +---+---+---+---+---+-----+---+
 *                    ^                   ^
 *                    |                   |
 *                  Size=3             Capacity
 * </pre>
 *
 * @param <T> the type of elements in this list
 */
public class MyArrayList<T> implements MyList<T> {

  private static final int DEFAULT_CAPACITY = 10;

  private T[] elements;
  private int size;
  private int modCount; // For fail-fast iterators

  /** Constructs an empty list with default initial capacity. */
  @SuppressWarnings("unchecked")
  public MyArrayList() {
    this(DEFAULT_CAPACITY);
  }

  /**
   * Constructs an empty list with the specified initial capacity.
   *
   * @param initialCapacity the initial capacity of the list
   * @throws IllegalArgumentException if initialCapacity is negative
   */
  @SuppressWarnings("unchecked")
  public MyArrayList(int initialCapacity) {
    if (initialCapacity < 0) {
      throw new IllegalArgumentException("Initial capacity cannot be negative: " + initialCapacity);
    }
    this.elements = (T[]) new Object[initialCapacity];
    this.size = 0;
    this.modCount = 0;
  }

  @Override
  public void add(T element) {
    ensureCapacity(size + 1);
    elements[size++] = element;
    modCount++;
  }

  @Override
  public void add(int index, T element) {
    if (index < 0 || index > size) {
      throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }
    ensureCapacity(size + 1);

    // Shift elements right to make space
    //
    // Before: [A] [B] [C] [D] [ ]
    //          0   1   2   3   4
    //              ^ Insert 'X' at index 1
    //
    // Shift:  [A] [ ] [B] [C] [D]
    //          0   1   2   3   4
    //
    // After:  [A] [X] [B] [C] [D]
    //
    System.arraycopy(elements, index, elements, index + 1, size - index);
    elements[index] = element;
    size++;
    modCount++;
  }

  @Override
  public T get(int index) {
    if (index < 0 || index >= size) {
      throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }
    return elements[index];
  }

  @Override
  public T remove(int index) {
    if (index < 0 || index >= size) {
      throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }
    final T removed = elements[index];

    // Shift elements left to fill the gap
    //
    // Before: [A] [B] [C] [D]
    //          0   1   2   3
    //              ^ Remove index 1 ('B')
    //
    // Shift:  [A] [C] [D] [D]
    //          0   1   2   3
    //
    // Null:   [A] [C] [D] [null]
    //          0   1   2   3
    //
    System.arraycopy(elements, index + 1, elements, index, size - index - 1);
    elements[--size] = null; // Help GC
    modCount++;
    return removed;
  }

  @Override
  public int size() {
    return size;
  }

  @Override
  public boolean isEmpty() {
    return size == 0;
  }

  @Override
  @SuppressWarnings("unchecked")
  public void clear() {
    // Null out all references to help GC
    //
    // [A] [B] [C]  --->  [null] [null] [null]
    //
    for (int i = 0; i < size; i++) {
      elements[i] = null;
    }
    size = 0;
    modCount++;
  }

  /**
   * Returns true if this list contains the specified element. Performs linear search - O(n) time
   * complexity.
   *
   * @param element the element to search for (may be null)
   * @return true if element is present, false otherwise
   */
  @Override
  public boolean contains(T element) {
    return indexOf(element) >= 0;
  }

  @Override
  public int indexOf(T element) {
    for (int i = 0; i < size; i++) {
      if (element == null ? elements[i] == null : element.equals(elements[i])) {
        return i;
      }
    }
    return -1;
  }

  @Override
  public T set(int index, T element) {
    if (index < 0 || index >= size) {
      throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }
    T oldValue = elements[index];
    elements[index] = element;
    return oldValue;
  }

  @Override
  public Object[] toArray() {
    Object[] result = new Object[size];
    System.arraycopy(elements, 0, result, 0, size);
    return result;
  }

  /**
   * Ensures the list has capacity for at least minCapacity elements. Doubles capacity when full.
   *
   * @param minCapacity the minimum required capacity
   */
  @SuppressWarnings("unchecked")
  private void ensureCapacity(int minCapacity) {
    if (minCapacity > elements.length) {
      // Resize Strategy: Double the capacity
      //
      // Old Array: [A] [B] [C] (Full)
      //             +---+---+---+
      //
      // New Array: [A] [B] [C] [ ] [ ] [ ]
      //             +---+---+---+---+---+---+
      //             (Capacity * 2)
      //
      int newCapacity = elements.length == 0 ? DEFAULT_CAPACITY : elements.length * 2;
      if (newCapacity < minCapacity) {
        newCapacity = minCapacity;
      }
      T[] newElements = (T[]) new Object[newCapacity];
      System.arraycopy(elements, 0, newElements, 0, size);
      elements = newElements;
    }
  }

  @Override
  public Iterator<T> iterator() {
    return new MyArrayListIterator();
  }

  private class MyArrayListIterator implements Iterator<T> {
    private int currentIndex = 0;
    private int lastReturnedIndex = -1;
    private int expectedModCount = modCount;

    @Override
    public boolean hasNext() {
      return currentIndex < size;
    }

    @Override
    public T next() {
      checkForComodification();
      if (!hasNext()) {
        throw new NoSuchElementException();
      }
      lastReturnedIndex = currentIndex;
      return elements[currentIndex++];
    }

    @Override
    public void remove() {
      checkForComodification();
      if (lastReturnedIndex < 0) {
        throw new IllegalStateException("remove() called without next()");
      }
      MyArrayList.this.remove(lastReturnedIndex);
      currentIndex = lastReturnedIndex;
      lastReturnedIndex = -1;
      expectedModCount = modCount;
    }

    private void checkForComodification() {
      if (modCount != expectedModCount) {
        throw new ConcurrentModificationException();
      }
    }
  }
}
