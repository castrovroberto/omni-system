package com.omni.core.algorithm.search;

import com.omni.core.list.MyList;
import java.util.Comparator;

/**
 * Utility class containing search algorithms that work with MyList implementations.
 *
 * <p>All algorithms are generic and work with any list implementation.
 */
public final class SearchAlgorithms {

  private SearchAlgorithms() {
    // Utility class - prevent instantiation
  }

  /**
   * Performs linear search on a list to find the first occurrence of target.
   *
   * <p>Time complexity: O(n) where n is the list size Works on sorted or unsorted lists.
   *
   * @param list the list to search (may be unsorted)
   * @param target the element to search for
   * @param comparator comparator to compare elements (may be null for Comparable)
   * @param <T> the type of elements in the list
   * @return the index of the first match, or -1 if not found
   */
  @SuppressWarnings("unchecked")
  public static <T> int linearSearch(MyList<T> list, T target, Comparator<T> comparator) {
    if (list == null || target == null) {
      return -1;
    }

    // If comparator is null, assume T implements Comparable
    if (comparator == null) {
      for (int i = 0; i < list.size(); i++) {
        T element = list.get(i);
        if (element != null && ((Comparable<T>) element).compareTo(target) == 0) {
          return i;
        }
      }
    } else {
      for (int i = 0; i < list.size(); i++) {
        if (comparator.compare(list.get(i), target) == 0) {
          return i;
        }
      }
    }
    return -1;
  }

  /**
   * Performs binary search on a sorted list to find target.
   *
   * <p>Time complexity: O(log n) where n is the list size Precondition: the list must be sorted
   * according to the comparator.
   *
   * <p>Returns index if found, or -(insertion_point + 1) if not found (following Java's
   * Collections.binarySearch convention).
   *
   * @param sortedList the sorted list to search (must be sorted)
   * @param target the element to search for
   * @param comparator comparator to compare elements (may be null for Comparable)
   * @param <T> the type of elements in the list
   * @return the index if found, or -(insertion_point + 1) if not found
   * @throws IllegalArgumentException if list is null
   */
  @SuppressWarnings("unchecked")
  public static <T> int binarySearch(MyList<T> sortedList, T target, Comparator<T> comparator) {
    if (sortedList == null) {
      throw new IllegalArgumentException("List cannot be null");
    }
    if (target == null) {
      throw new IllegalArgumentException("Target cannot be null");
    }

    int low = 0;
    int high = sortedList.size() - 1;

    // If comparator is null, assume T implements Comparable
    if (comparator == null) {
      while (low <= high) {
        int mid = low + (high - low) / 2;
        T midElement = sortedList.get(mid);
        int comparison = ((Comparable<T>) midElement).compareTo(target);

        if (comparison == 0) {
          return mid; // Found
        } else if (comparison < 0) {
          low = mid + 1; // Search right half
        } else {
          high = mid - 1; // Search left half
        }
      }
    } else {
      while (low <= high) {
        int mid = low + (high - low) / 2;
        int comparison = comparator.compare(sortedList.get(mid), target);

        if (comparison == 0) {
          return mid; // Found
        } else if (comparison < 0) {
          low = mid + 1; // Search right half
        } else {
          high = mid - 1; // Search left half
        }
      }
    }

    // Not found - return insertion point as negative value
    return -(low + 1);
  }
}
