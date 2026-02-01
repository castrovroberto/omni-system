package com.omni.core.algorithm.sort;

import com.omni.core.list.MyArrayList;
import com.omni.core.list.MyList;
import com.omni.core.tree.MyHeap;
import java.util.Comparator;

/**
 * Utility class providing sorting algorithms for {@link MyList} implementations.
 *
 * <p>This class provides two classic comparison-based sorting algorithms:
 *
 * <ul>
 *   <li><b>Merge Sort</b>: Stable, O(n log n) guaranteed, O(n) space
 *   <li><b>Quick Sort</b>: Unstable, O(n log n) average, O(log n) space (in-place)
 * </ul>
 */
public final class SortAlgorithms {

  private SortAlgorithms() {
    // Utility class - prevent instantiation
  }

  // ==================== Merge Sort ====================

  /**
   * Sorts the given list using merge sort algorithm.
   *
   * <p>Merge sort is a divide-and-conquer algorithm that:
   *
   * <ol>
   *   <li>Divides the list into two halves
   *   <li>Recursively sorts each half
   *   <li>Merges the sorted halves
   * </ol>
   *
   * <p><b>Complexity:</b>
   *
   * <ul>
   *   <li>Time: O(n log n) - guaranteed for all cases
   *   <li>Space: O(n) - requires auxiliary space for merging
   * </ul>
   *
   * <p><b>Stability:</b> Stable - equal elements maintain their relative order.
   *
   * @param <T> the type of elements in the list
   * @param list the list to sort (modified in-place)
   * @param comparator the comparator to determine order; if null, elements must be Comparable
   * @throws ClassCastException if elements are not Comparable and comparator is null
   */
  public static <T> void mergeSort(MyList<T> list, Comparator<T> comparator) {
    if (list == null || list.size() <= 1) {
      return;
    }

    MyList<T> sorted = mergeSortRecursive(list, comparator);

    // Copy sorted elements back to original list
    for (int i = 0; i < list.size(); i++) {
      list.set(i, sorted.get(i));
    }
  }

  /**
   * Sorts using natural ordering (elements must implement Comparable).
   *
   * @param <T> the type of elements, must be Comparable
   * @param list the list to sort
   */
  public static <T extends Comparable<T>> void mergeSort(MyList<T> list) {
    mergeSort(list, null);
  }

  private static <T> MyList<T> mergeSortRecursive(MyList<T> list, Comparator<T> comparator) {
    if (list.size() <= 1) {
      MyList<T> result = new MyArrayList<>();
      if (list.size() == 1) {
        result.add(list.get(0));
      }
      return result;
    }

    int mid = list.size() / 2;

    // Split into left and right halves
    MyList<T> left = new MyArrayList<>();
    MyList<T> right = new MyArrayList<>();

    for (int i = 0; i < mid; i++) {
      left.add(list.get(i));
    }
    for (int i = mid; i < list.size(); i++) {
      right.add(list.get(i));
    }

    // Recursively sort halves
    left = mergeSortRecursive(left, comparator);
    right = mergeSortRecursive(right, comparator);

    // Merge sorted halves
    return merge(left, right, comparator);
  }

  private static <T> MyList<T> merge(MyList<T> left, MyList<T> right, Comparator<T> comparator) {
    MyList<T> result = new MyArrayList<>();
    int i = 0;
    int j = 0;

    while (i < left.size() && j < right.size()) {
      if (compare(left.get(i), right.get(j), comparator) <= 0) {
        result.add(left.get(i));
        i++;
      } else {
        result.add(right.get(j));
        j++;
      }
    }

    // Add remaining elements
    while (i < left.size()) {
      result.add(left.get(i));
      i++;
    }
    while (j < right.size()) {
      result.add(right.get(j));
      j++;
    }

    return result;
  }

  // ==================== Quick Sort ====================

  /**
   * Sorts the given list using quick sort algorithm with median-of-three pivot selection.
   *
   * <p>Quick sort is a divide-and-conquer algorithm that:
   *
   * <ol>
   *   <li>Selects a pivot element (using median-of-three)
   *   <li>Partitions the list around the pivot
   *   <li>Recursively sorts the partitions
   * </ol>
   *
   * <p><b>Complexity:</b>
   *
   * <ul>
   *   <li>Time: O(n log n) average, O(n²) worst case (rare with median-of-three)
   *   <li>Space: O(log n) - for recursion stack (in-place partitioning)
   * </ul>
   *
   * <p><b>Stability:</b> Unstable - equal elements may be reordered.
   *
   * <p><b>Pivot Selection:</b> Uses median-of-three (first, middle, last) to avoid O(n²)
   * degradation on sorted or reverse-sorted input.
   *
   * @param <T> the type of elements in the list
   * @param list the list to sort (modified in-place)
   * @param comparator the comparator to determine order; if null, elements must be Comparable
   * @throws ClassCastException if elements are not Comparable and comparator is null
   */
  public static <T> void quickSort(MyList<T> list, Comparator<T> comparator) {
    if (list == null || list.size() <= 1) {
      return;
    }

    quickSortRecursive(list, 0, list.size() - 1, comparator);
  }

  /**
   * Sorts using natural ordering (elements must implement Comparable).
   *
   * @param <T> the type of elements, must be Comparable
   * @param list the list to sort
   */
  public static <T extends Comparable<T>> void quickSort(MyList<T> list) {
    quickSort(list, null);
  }

  private static <T> void quickSortRecursive(
      MyList<T> list, int low, int high, Comparator<T> comparator) {
    if (low < high) {
      // Use insertion sort for small subarrays (optimization)
      if (high - low < 10) {
        insertionSort(list, low, high, comparator);
        return;
      }

      int pivotIndex = partition(list, low, high, comparator);
      quickSortRecursive(list, low, pivotIndex - 1, comparator);
      quickSortRecursive(list, pivotIndex + 1, high, comparator);
    }
  }

  /**
   * Partitions the list around a pivot using median-of-three selection.
   *
   * @return the final position of the pivot
   */
  private static <T> int partition(MyList<T> list, int low, int high, Comparator<T> comparator) {
    // Median-of-three pivot selection
    int mid = low + (high - low) / 2;
    medianOfThree(list, low, mid, high, comparator);

    // Pivot is now at 'mid', move it to 'high-1' for Hoare-style partition
    swap(list, mid, high);
    T pivot = list.get(high);

    int i = low - 1;

    for (int j = low; j < high; j++) {
      if (compare(list.get(j), pivot, comparator) <= 0) {
        i++;
        swap(list, i, j);
      }
    }

    // Place pivot in its final position
    swap(list, i + 1, high);
    return i + 1;
  }

  /**
   * Sorts three elements and places median at 'mid' position.
   *
   * <p>After this call:
   *
   * <ul>
   *   <li>list[low] <= list[mid] <= list[high]
   * </ul>
   */
  private static <T> void medianOfThree(
      MyList<T> list, int low, int mid, int high, Comparator<T> comparator) {
    // Sort low, mid, high
    if (compare(list.get(low), list.get(mid), comparator) > 0) {
      swap(list, low, mid);
    }
    if (compare(list.get(low), list.get(high), comparator) > 0) {
      swap(list, low, high);
    }
    if (compare(list.get(mid), list.get(high), comparator) > 0) {
      swap(list, mid, high);
    }
    // Now: list[low] <= list[mid] <= list[high]
    // Median is at 'mid'
  }

  /** Insertion sort for small subarrays - faster than quicksort for n < 10. */
  private static <T> void insertionSort(
      MyList<T> list, int low, int high, Comparator<T> comparator) {
    for (int i = low + 1; i <= high; i++) {
      T key = list.get(i);
      int j = i - 1;

      while (j >= low && compare(list.get(j), key, comparator) > 0) {
        list.set(j + 1, list.get(j));
        j--;
      }
      list.set(j + 1, key);
    }
  }

  // ==================== Helper Methods ====================

  /** Compares two elements using the comparator, or natural ordering if comparator is null. */
  @SuppressWarnings("unchecked")
  private static <T> int compare(T a, T b, Comparator<T> comparator) {
    if (comparator != null) {
      return comparator.compare(a, b);
    }
    return ((Comparable<T>) a).compareTo(b);
  }

  /** Swaps two elements in the list. */
  private static <T> void swap(MyList<T> list, int i, int j) {
    if (i != j) {
      T temp = list.get(i);
      list.set(i, list.get(j));
      list.set(j, temp);
    }
  }

  // ==================== Heap Sort ====================

  /**
   * Sorts the given list using heap sort algorithm.
   *
   * <p>Builds a min-heap from the list, then repeatedly extracts the root to produce a sorted
   * sequence.
   *
   * <p><b>Complexity:</b>
   *
   * <ul>
   *   <li>Time: O(n log n) - guaranteed for all cases
   *   <li>Space: O(n) - for the heap structure
   * </ul>
   *
   * <p><b>Stability:</b> Unstable - equal elements may be reordered.
   *
   * @param <T> the type of elements in the list
   * @param list the list to sort (modified in-place)
   * @param comparator the comparator to determine order
   */
  public static <T> void heapSort(MyList<T> list, Comparator<T> comparator) {
    if (list == null || list.size() <= 1) {
      return;
    }
    MyHeap<T> heap = MyHeap.heapify(list, comparator);
    for (int i = 0; i < list.size(); i++) {
      list.set(i, heap.extractRoot());
    }
  }

  /**
   * Sorts using natural ordering with heap sort.
   *
   * @param <T> the type of elements, must be Comparable
   * @param list the list to sort
   */
  @SuppressWarnings("unchecked")
  public static <T extends Comparable<T>> void heapSort(MyList<T> list) {
    heapSort(list, (a, b) -> ((Comparable<T>) a).compareTo(b));
  }

  // ==================== Analysis Methods ====================

  /**
   * Checks if the list is sorted according to the comparator.
   *
   * @param <T> the type of elements
   * @param list the list to check
   * @param comparator the comparator; if null, uses natural ordering
   * @return true if the list is sorted in ascending order
   */
  public static <T> boolean isSorted(MyList<T> list, Comparator<T> comparator) {
    if (list == null || list.size() <= 1) {
      return true;
    }

    for (int i = 1; i < list.size(); i++) {
      if (compare(list.get(i - 1), list.get(i), comparator) > 0) {
        return false;
      }
    }
    return true;
  }

  /**
   * Checks if the list is sorted using natural ordering.
   *
   * @param <T> the type of elements, must be Comparable
   * @param list the list to check
   * @return true if the list is sorted
   */
  public static <T extends Comparable<T>> boolean isSorted(MyList<T> list) {
    return isSorted(list, null);
  }
}
