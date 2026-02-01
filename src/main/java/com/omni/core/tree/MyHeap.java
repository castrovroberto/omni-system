package com.omni.core.tree;

import com.omni.core.list.MyArrayList;
import com.omni.core.list.MyList;
import java.util.Comparator;
import java.util.NoSuchElementException;

/**
 * A binary heap backed by {@link MyArrayList}.
 *
 * <p>Supports both min-heap (default, natural ordering) and max-heap (via custom comparator).
 *
 * <pre>
 *   Min-Heap (array representation):
 *
 *   Index:  0   1   2   3   4   5
 *         [ 1 | 3 | 2 | 7 | 5 | 4 ]
 *
 *   Tree view:
 *           1
 *          / \
 *         3   2
 *        / \ /
 *       7  5 4
 * </pre>
 *
 * @param <T> the type of elements
 */
public class MyHeap<T> {

  private final MyArrayList<T> data;
  private final Comparator<T> comparator;

  /**
   * Constructs an empty heap with the given comparator.
   *
   * @param comparator the comparator to determine element ordering
   */
  public MyHeap(Comparator<T> comparator) {
    this.data = new MyArrayList<>();
    this.comparator = comparator;
  }

  /**
   * Constructs an empty min-heap using natural ordering.
   *
   * @param <T> the type of elements, must be Comparable
   * @return a new min-heap
   */
  @SuppressWarnings("unchecked")
  public static <T extends Comparable<T>> MyHeap<T> natural() {
    return new MyHeap<>((a, b) -> ((Comparable<T>) a).compareTo(b));
  }

  /**
   * Inserts a value into the heap.
   *
   * @param value the value to insert
   */
  public void insert(T value) {
    data.add(value);
    siftUp(data.size() - 1);
  }

  /**
   * Removes and returns the root element (min or max depending on comparator).
   *
   * @return the root element
   * @throws NoSuchElementException if the heap is empty
   */
  public T extractRoot() {
    if (data.isEmpty()) {
      throw new NoSuchElementException("Heap is empty");
    }
    T root = data.get(0);
    int lastIndex = data.size() - 1;
    data.set(0, data.get(lastIndex));
    data.remove(lastIndex);
    if (!data.isEmpty()) {
      siftDown(0);
    }
    return root;
  }

  /**
   * Returns the root element without removing it.
   *
   * @return the root element
   * @throws NoSuchElementException if the heap is empty
   */
  public T peek() {
    if (data.isEmpty()) {
      throw new NoSuchElementException("Heap is empty");
    }
    return data.get(0);
  }

  /**
   * Returns the number of elements in the heap.
   *
   * @return the size
   */
  public int size() {
    return data.size();
  }

  /**
   * Returns true if the heap is empty.
   *
   * @return true if empty
   */
  public boolean isEmpty() {
    return data.isEmpty();
  }

  /**
   * Builds a heap from the given list using Floyd's O(n) algorithm.
   *
   * @param <T> the type of elements
   * @param list the source list
   * @param comparator the comparator for ordering
   * @return a new heap containing all elements
   */
  public static <T> MyHeap<T> heapify(MyList<T> list, Comparator<T> comparator) {
    MyHeap<T> heap = new MyHeap<>(comparator);
    for (int i = 0; i < list.size(); i++) {
      heap.data.add(list.get(i));
    }
    // Floyd's algorithm: sift down from last non-leaf to root
    for (int i = (heap.data.size() / 2) - 1; i >= 0; i--) {
      heap.siftDown(i);
    }
    return heap;
  }

  private void siftUp(int index) {
    while (index > 0) {
      int parentIndex = (index - 1) / 2;
      if (comparator.compare(data.get(index), data.get(parentIndex)) < 0) {
        swap(index, parentIndex);
        index = parentIndex;
      } else {
        break;
      }
    }
  }

  private void siftDown(int index) {
    int size = data.size();
    while (true) {
      int smallest = index;
      int left = 2 * index + 1;
      int right = 2 * index + 2;
      if (left < size && comparator.compare(data.get(left), data.get(smallest)) < 0) {
        smallest = left;
      }
      if (right < size && comparator.compare(data.get(right), data.get(smallest)) < 0) {
        smallest = right;
      }
      if (smallest == index) {
        break;
      }
      swap(index, smallest);
      index = smallest;
    }
  }

  private void swap(int i, int j) {
    T temp = data.get(i);
    data.set(i, data.get(j));
    data.set(j, temp);
  }
}
