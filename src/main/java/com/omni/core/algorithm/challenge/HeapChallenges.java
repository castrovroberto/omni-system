package com.omni.core.algorithm.challenge;

import com.omni.core.list.MyArrayList;
import com.omni.core.list.MyList;
import com.omni.core.tree.MyHeap;
import java.util.Comparator;
import java.util.Iterator;

/**
 * Heap-based algorithm challenges for interview preparation.
 *
 * <p><b>Interview Context:</b> These problems demonstrate understanding of heaps and their
 * applications to database operations like external merge sort.
 */
public final class HeapChallenges {

  private HeapChallenges() {}

  /**
   * Merges k sorted lists into one sorted list using a min-heap.
   *
   * <p><b>Interview Question:</b> "Merge k Sorted Lists" (LeetCode #23) - commonly asked at
   * Snowflake because it mirrors how databases merge sorted runs during query processing.
   *
   * <p><b>Time Complexity:</b> O(n log k) where n is total elements and k is number of lists
   *
   * <p><b>Algorithm:</b>
   *
   * <ol>
   *   <li>Insert head of each list into min-heap (with list index)
   *   <li>Extract min, add to result
   *   <li>Insert next element from that list
   *   <li>Repeat until heap is empty
   * </ol>
   *
   * @param <T> the type of elements, must be Comparable
   * @param lists the k sorted lists to merge
   * @return a single sorted list containing all elements
   */
  public static <T extends Comparable<T>> MyList<T> mergeKSortedLists(MyList<MyList<T>> lists) {
    MyList<T> result = new MyArrayList<>();

    if (lists == null || lists.isEmpty()) {
      return result;
    }

    // Create iterators for each list
    @SuppressWarnings("unchecked")
    Iterator<T>[] iterators = new Iterator[lists.size()];
    for (int i = 0; i < lists.size(); i++) {
      iterators[i] = lists.get(i).iterator();
    }

    // Create min-heap of (value, listIndex)
    MyHeap<ListElement<T>> heap = new MyHeap<>(Comparator.comparing(ListElement::value));

    // Initialize heap with first element from each non-empty list
    for (int i = 0; i < iterators.length; i++) {
      if (iterators[i].hasNext()) {
        heap.insert(new ListElement<>(iterators[i].next(), i));
      }
    }

    // Extract min and insert next from same list
    while (!heap.isEmpty()) {
      ListElement<T> min = heap.extractRoot();
      result.add(min.value());

      int listIndex = min.listIndex();
      if (iterators[listIndex].hasNext()) {
        heap.insert(new ListElement<>(iterators[listIndex].next(), listIndex));
      }
    }

    return result;
  }

  /**
   * Finds the k largest elements in a list using a min-heap.
   *
   * <p><b>Interview Question:</b> "Kth Largest Element" variant - uses heap to maintain top k.
   *
   * <p><b>Time Complexity:</b> O(n log k)
   *
   * @param <T> the type of elements
   * @param list the input list
   * @param k the number of largest elements to find
   * @return a list of the k largest elements (may not be sorted)
   */
  public static <T extends Comparable<T>> MyList<T> findKLargest(MyList<T> list, int k) {
    if (k <= 0 || list == null || list.isEmpty()) {
      return new MyArrayList<>();
    }

    // Use min-heap of size k - smallest of the k largest is at root
    MyHeap<T> minHeap = MyHeap.natural();

    for (T element : list) {
      if (minHeap.size() < k) {
        minHeap.insert(element);
      } else if (element.compareTo(minHeap.peek()) > 0) {
        minHeap.extractRoot();
        minHeap.insert(element);
      }
    }

    MyList<T> result = new MyArrayList<>();
    while (!minHeap.isEmpty()) {
      result.add(minHeap.extractRoot());
    }
    return result;
  }

  /**
   * Record to hold a value and its source list index.
   *
   * @param <T> the type of value
   * @param value the element value
   * @param listIndex which list this element came from
   */
  private record ListElement<T>(T value, int listIndex) {}
}
