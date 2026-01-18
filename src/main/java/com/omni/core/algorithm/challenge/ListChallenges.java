package com.omni.core.algorithm.challenge;

import com.omni.core.list.MyArrayList;
import com.omni.core.list.MyList;
import java.util.Comparator;

/**
 * Utility class containing LeetCode-style list manipulation challenges.
 *
 * <p>All challenges work with the {@link MyList} interface and are designed to practice common
 * algorithmic techniques such as two-pointer, slow/fast pointer, and divide-and-conquer.
 *
 * <p>Each method includes:
 *
 * <ul>
 *   <li>Problem description and difficulty level
 *   <li>Time and space complexity analysis
 *   <li>ASCII art diagram showing the algorithm
 *   <li>LeetCode reference number (where applicable)
 * </ul>
 */
public final class ListChallenges {

  private ListChallenges() {
    // Utility class - prevent instantiation
  }

  // ==================== EASY CHALLENGES ====================

  /**
   * Reverses the elements of a list in-place.
   *
   * <p><b>Difficulty:</b> {@link Difficulty#EASY} | <b>LeetCode #206</b>
   *
   * <p><b>Technique:</b> Two Pointers (swap from ends)
   *
   * <pre>
   *   Before:  [1] [2] [3] [4] [5]
   *             ^               ^
   *           left           right
   *
   *   Step 1:  [5] [2] [3] [4] [1]   (swap 1 and 5)
   *                 ^       ^
   *               left   right
   *
   *   Step 2:  [5] [4] [3] [2] [1]   (swap 2 and 4)
   *                     ^
   *                 left=right       (done when pointers meet)
   *
   *   After:   [5] [4] [3] [2] [1]
   * </pre>
   *
   * <p><b>Time:</b> O(n) | <b>Space:</b> O(1)
   *
   * @param list the list to reverse (modified in-place)
   * @param <T> the type of elements in the list
   * @throws IllegalArgumentException if list is null
   */
  public static <T> void reverse(MyList<T> list) {
    if (list == null) {
      throw new IllegalArgumentException("List cannot be null");
    }

    int left = 0;
    int right = list.size() - 1;

    while (left < right) {
      // Swap elements at left and right
      T temp = list.get(left);
      list.remove(left);
      list.add(left, list.get(right - 1));
      list.remove(right);
      list.add(right, temp);
      left++;
      right--;
    }
  }

  /**
   * Finds the middle element of a list using slow/fast pointer technique.
   *
   * <p><b>Difficulty:</b> {@link Difficulty#EASY} | <b>LeetCode #876</b>
   *
   * <p><b>Technique:</b> Slow/Fast Pointers (tortoise and hare)
   *
   * <pre>
   *   List:    [1] [2] [3] [4] [5]
   *             ^
   *           slow (moves 1 step)
   *             ^
   *           fast (moves 2 steps)
   *
   *   Step 1:  [1] [2] [3] [4] [5]
   *                 ^
   *               slow
   *                     ^
   *                   fast
   *
   *   Step 2:  [1] [2] [3] [4] [5]
   *                     ^
   *                   slow          (middle found!)
   *                             ^
   *                           fast
   *
   *   For even length [1] [2] [3] [4]:
   *   Returns second middle element (index 2, value 3)
   * </pre>
   *
   * <p><b>Time:</b> O(n) | <b>Space:</b> O(1)
   *
   * @param list the list to search
   * @param <T> the type of elements in the list
   * @return the middle element, or null if list is empty
   * @throws IllegalArgumentException if list is null
   */
  public static <T> T findMiddle(MyList<T> list) {
    if (list == null) {
      throw new IllegalArgumentException("List cannot be null");
    }
    if (list.isEmpty()) {
      return null;
    }

    int slow = 0;
    int fast = 0;

    // Fast pointer moves 2 steps, slow moves 1 step
    while (fast < list.size() - 1 && fast + 1 < list.size()) {
      slow++;
      fast += 2;
    }

    return list.get(slow);
  }

  /**
   * Removes duplicate consecutive elements from a sorted list.
   *
   * <p><b>Difficulty:</b> {@link Difficulty#EASY} | <b>LeetCode #83</b>
   *
   * <p><b>Technique:</b> Read/Write Pointers
   *
   * <pre>
   *   Sorted:  [1] [1] [2] [2] [2] [3]
   *             ^
   *           write (position to place next unique)
   *             ^
   *            read (scanning through list)
   *
   *   Step 1:  read=1 equals write position, skip
   *   Step 2:  read=2 is new, write++ then place
   *   Step 3:  read=2 duplicate, skip
   *   Step 4:  read=2 duplicate, skip
   *   Step 5:  read=3 is new, write++ then place
   *
   *   After:   [1] [2] [3]
   * </pre>
   *
   * <p><b>Time:</b> O(n) | <b>Space:</b> O(1)
   *
   * @param list the sorted list to deduplicate (modified in-place)
   * @param <T> the type of elements in the list
   * @throws IllegalArgumentException if list is null
   */
  public static <T> void removeDuplicates(MyList<T> list) {
    if (list == null) {
      throw new IllegalArgumentException("List cannot be null");
    }
    if (list.size() <= 1) {
      return;
    }

    int writeIndex = 0;

    for (int readIndex = 1; readIndex < list.size(); readIndex++) {
      T current = list.get(readIndex);
      T lastUnique = list.get(writeIndex);

      // If current element is different from last unique, keep it
      boolean isDifferent;
      if (current == null || lastUnique == null) {
        isDifferent = current != lastUnique;
      } else {
        isDifferent = !current.equals(lastUnique);
      }

      if (isDifferent) {
        writeIndex++;
        // Move element to write position
        if (writeIndex != readIndex) {
          list.remove(writeIndex);
          list.add(writeIndex, current);
        }
      }
    }

    // Remove remaining duplicates from the end
    while (list.size() > writeIndex + 1) {
      list.remove(list.size() - 1);
    }
  }

  /**
   * Merges two sorted lists into a single sorted list.
   *
   * <p><b>Difficulty:</b> {@link Difficulty#EASY} | <b>LeetCode #21</b>
   *
   * <p><b>Technique:</b> Two-Pointer Merge
   *
   * <pre>
   *   List1:   [1] [3] [5]
   *             ^
   *            p1
   *
   *   List2:   [2] [4] [6]
   *             ^
   *            p2
   *
   *   Compare: 1 < 2, take 1, advance p1
   *   Result:  [1]
   *
   *   Compare: 3 > 2, take 2, advance p2
   *   Result:  [1] [2]
   *
   *   Continue until both exhausted...
   *   Final:   [1] [2] [3] [4] [5] [6]
   * </pre>
   *
   * <p><b>Time:</b> O(n + m) | <b>Space:</b> O(n + m)
   *
   * @param list1 first sorted list
   * @param list2 second sorted list
   * @param comparator comparator to compare elements (null for natural ordering)
   * @param <T> the type of elements in the lists
   * @return new merged sorted list
   * @throws IllegalArgumentException if either list is null
   */
  @SuppressWarnings("unchecked")
  public static <T> MyList<T> mergeSortedLists(
      MyList<T> list1, MyList<T> list2, Comparator<T> comparator) {
    if (list1 == null || list2 == null) {
      throw new IllegalArgumentException("Lists cannot be null");
    }

    MyList<T> result = new MyArrayList<>();
    int p1 = 0;
    int p2 = 0;

    while (p1 < list1.size() && p2 < list2.size()) {
      T elem1 = list1.get(p1);
      T elem2 = list2.get(p2);

      int cmp;
      if (comparator != null) {
        cmp = comparator.compare(elem1, elem2);
      } else {
        cmp = ((Comparable<T>) elem1).compareTo(elem2);
      }

      if (cmp <= 0) {
        result.add(elem1);
        p1++;
      } else {
        result.add(elem2);
        p2++;
      }
    }

    // Add remaining elements from list1
    while (p1 < list1.size()) {
      result.add(list1.get(p1++));
    }

    // Add remaining elements from list2
    while (p2 < list2.size()) {
      result.add(list2.get(p2++));
    }

    return result;
  }

  /**
   * Checks if a list reads the same forwards and backwards.
   *
   * <p><b>Difficulty:</b> {@link Difficulty#EASY} | <b>LeetCode #234</b>
   *
   * <p><b>Technique:</b> Compare from Ends
   *
   * <pre>
   *   List:    [1] [2] [3] [2] [1]
   *             ^               ^
   *           left           right
   *
   *   Step 1:  1 == 1 ✓, move pointers inward
   *   Step 2:  2 == 2 ✓, move pointers inward
   *   Step 3:  left >= right, done → Palindrome!
   *
   *   Non-palindrome: [1] [2] [3]
   *   Step 1:  1 != 3 ✗ → Not a palindrome
   * </pre>
   *
   * <p><b>Time:</b> O(n) | <b>Space:</b> O(1)
   *
   * @param list the list to check
   * @param <T> the type of elements in the list
   * @return true if the list is a palindrome
   * @throws IllegalArgumentException if list is null
   */
  public static <T> boolean isPalindrome(MyList<T> list) {
    if (list == null) {
      throw new IllegalArgumentException("List cannot be null");
    }
    if (list.size() <= 1) {
      return true;
    }

    int left = 0;
    int right = list.size() - 1;

    while (left < right) {
      T leftElem = list.get(left);
      T rightElem = list.get(right);

      boolean areEqual;
      if (leftElem == null || rightElem == null) {
        areEqual = leftElem == rightElem;
      } else {
        areEqual = leftElem.equals(rightElem);
      }

      if (!areEqual) {
        return false;
      }
      left++;
      right--;
    }

    return true;
  }

  /**
   * Returns the Nth element from the end of the list.
   *
   * <p><b>Difficulty:</b> {@link Difficulty#EASY}
   *
   * <p><b>Technique:</b> Two Pointers with Gap
   *
   * <pre>
   *   List:    [1] [2] [3] [4] [5]   n=2 (2nd from end)
   *             ^
   *           lead (advance n steps first)
   *             ^
   *           trail
   *
   *   After n steps:
   *            [1] [2] [3] [4] [5]
   *                     ^
   *                   lead
   *             ^
   *           trail
   *
   *   Move both until lead hits end:
   *            [1] [2] [3] [4] [5]
   *                             ^
   *                           lead
   *                     ^
   *                   trail    → returns 4 (2nd from end)
   * </pre>
   *
   * <p><b>Time:</b> O(n) | <b>Space:</b> O(1)
   *
   * @param list the list to search
   * @param n position from end (1-indexed: 1 = last element)
   * @param <T> the type of elements in the list
   * @return the Nth element from the end
   * @throws IllegalArgumentException if list is null or n is invalid
   */
  public static <T> T getNthFromEnd(MyList<T> list, int n) {
    if (list == null) {
      throw new IllegalArgumentException("List cannot be null");
    }
    if (n <= 0 || n > list.size()) {
      throw new IllegalArgumentException("n must be between 1 and list size");
    }

    int lead = 0;
    int trail = 0;

    // Advance lead pointer n steps
    for (int i = 0; i < n; i++) {
      lead++;
    }

    // Move both pointers until lead reaches the end
    while (lead < list.size()) {
      lead++;
      trail++;
    }

    return list.get(trail);
  }

  // ==================== MEDIUM CHALLENGES ====================

  /**
   * Removes the Nth node from the end of the list.
   *
   * <p><b>Difficulty:</b> {@link Difficulty#MEDIUM} | <b>LeetCode #19</b>
   *
   * <p><b>Technique:</b> Two Pointers with Gap
   *
   * <pre>
   *   List:    [1] [2] [3] [4] [5]   n=2 (remove 2nd from end)
   *
   *   Setup lead pointer n+1 ahead:
   *            [1] [2] [3] [4] [5]
   *                         ^
   *                       lead
   *             ^
   *           trail (points to node BEFORE target)
   *
   *   Move both until lead reaches end:
   *            [1] [2] [3] [4] [5]
   *                             ^
   *                           lead
   *                 ^
   *               trail
   *
   *   Remove trail.next (node 4):
   *   Result:  [1] [2] [3] [5]
   * </pre>
   *
   * <p><b>Time:</b> O(n) | <b>Space:</b> O(1)
   *
   * @param list the list to modify
   * @param n position from end to remove (1-indexed)
   * @param <T> the type of elements in the list
   * @return the removed element
   * @throws IllegalArgumentException if list is null or n is invalid
   */
  public static <T> T removeNthFromEnd(MyList<T> list, int n) {
    if (list == null) {
      throw new IllegalArgumentException("List cannot be null");
    }
    if (n <= 0 || n > list.size()) {
      throw new IllegalArgumentException("n must be between 1 and list size");
    }

    // Calculate the index from the start
    int indexToRemove = list.size() - n;
    return list.remove(indexToRemove);
  }

  /**
   * Rotates the list to the right by k positions.
   *
   * <p><b>Difficulty:</b> {@link Difficulty#MEDIUM} | <b>LeetCode #61</b>
   *
   * <p><b>Technique:</b> Find Pivot, Rearrange
   *
   * <pre>
   *   List:    [1] [2] [3] [4] [5]   k=2
   *
   *   Effective rotation: k % size = 2 % 5 = 2
   *   Pivot point: size - k = 5 - 2 = 3
   *
   *   Split at pivot:
   *   First:   [1] [2] [3]
   *   Second:  [4] [5]
   *
   *   Rearrange (second + first):
   *   Result:  [4] [5] [1] [2] [3]
   * </pre>
   *
   * <p><b>Time:</b> O(n) | <b>Space:</b> O(1)
   *
   * @param list the list to rotate
   * @param k number of positions to rotate right
   * @param <T> the type of elements in the list
   * @throws IllegalArgumentException if list is null or k is negative
   */
  public static <T> void rotateRight(MyList<T> list, int k) {
    if (list == null) {
      throw new IllegalArgumentException("List cannot be null");
    }
    if (k < 0) {
      throw new IllegalArgumentException("k cannot be negative");
    }
    if (list.size() <= 1 || k == 0) {
      return;
    }

    // Normalize k to avoid unnecessary rotations
    k = k % list.size();
    if (k == 0) {
      return;
    }

    // Collect elements that will move to front
    MyList<T> tail = new MyArrayList<>();
    int pivotIndex = list.size() - k;

    for (int i = pivotIndex; i < list.size(); i++) {
      tail.add(list.get(i));
    }

    // Remove elements from end
    for (int i = 0; i < k; i++) {
      list.remove(list.size() - 1);
    }

    // Insert at beginning
    for (int i = tail.size() - 1; i >= 0; i--) {
      list.add(0, tail.get(i));
    }
  }

  /**
   * Adds two numbers represented as lists (digits in reverse order).
   *
   * <p><b>Difficulty:</b> {@link Difficulty#MEDIUM} | <b>LeetCode #2</b>
   *
   * <p><b>Technique:</b> Digit-by-Digit with Carry
   *
   * <pre>
   *   Number 342 stored as: [2] [4] [3]  (reverse order)
   *   Number 465 stored as: [5] [6] [4]
   *
   *   Addition:
   *     2 + 5 = 7, carry = 0  → [7]
   *     4 + 6 = 10, carry = 1 → [7] [0]
   *     3 + 4 + 1 = 8         → [7] [0] [8]
   *
   *   Result: [7] [0] [8] represents 807
   *   Verify: 342 + 465 = 807 ✓
   * </pre>
   *
   * <p><b>Time:</b> O(max(n, m)) | <b>Space:</b> O(max(n, m))
   *
   * @param list1 first number as reversed digit list
   * @param list2 second number as reversed digit list
   * @return sum as reversed digit list
   * @throws IllegalArgumentException if either list is null
   */
  public static MyList<Integer> addTwoNumbers(MyList<Integer> list1, MyList<Integer> list2) {
    if (list1 == null || list2 == null) {
      throw new IllegalArgumentException("Lists cannot be null");
    }

    MyList<Integer> result = new MyArrayList<>();
    int carry = 0;
    int i = 0;
    int j = 0;

    while (i < list1.size() || j < list2.size() || carry > 0) {
      int sum = carry;

      if (i < list1.size()) {
        sum += list1.get(i);
        i++;
      }

      if (j < list2.size()) {
        sum += list2.get(j);
        j++;
      }

      result.add(sum % 10);
      carry = sum / 10;
    }

    return result;
  }

  /**
   * Partitions list so all elements less than x come before elements >= x.
   *
   * <p><b>Difficulty:</b> {@link Difficulty#MEDIUM} | <b>LeetCode #86</b>
   *
   * <p><b>Technique:</b> Two-Region Traversal
   *
   * <pre>
   *   List:    [1] [4] [3] [2] [5] [2]   pivot=3
   *
   *   Create two regions:
   *   Less:    [1] [2] [2]      (elements < 3)
   *   GreaterEq: [4] [3] [5]    (elements >= 3)
   *
   *   Concatenate:
   *   Result:  [1] [2] [2] [4] [3] [5]
   *
   *   Note: Relative order within each partition is preserved.
   * </pre>
   *
   * <p><b>Time:</b> O(n) | <b>Space:</b> O(n)
   *
   * @param list the list to partition
   * @param x the pivot element
   * @param comparator comparator to compare elements (null for natural ordering)
   * @param <T> the type of elements in the list
   * @throws IllegalArgumentException if list is null
   */
  @SuppressWarnings("unchecked")
  public static <T> void partition(MyList<T> list, T x, Comparator<T> comparator) {
    if (list == null) {
      throw new IllegalArgumentException("List cannot be null");
    }
    if (list.size() <= 1) {
      return;
    }

    MyList<T> less = new MyArrayList<>();
    MyList<T> greaterOrEqual = new MyArrayList<>();

    for (int i = 0; i < list.size(); i++) {
      T elem = list.get(i);
      int cmp;
      if (comparator != null) {
        cmp = comparator.compare(elem, x);
      } else {
        cmp = ((Comparable<T>) elem).compareTo(x);
      }

      if (cmp < 0) {
        less.add(elem);
      } else {
        greaterOrEqual.add(elem);
      }
    }

    // Rebuild the list
    list.clear();
    for (int i = 0; i < less.size(); i++) {
      list.add(less.get(i));
    }
    for (int i = 0; i < greaterOrEqual.size(); i++) {
      list.add(greaterOrEqual.get(i));
    }
  }

  // ==================== HARD CHALLENGES ====================

  /**
   * Merges k sorted lists into one sorted list.
   *
   * <p><b>Difficulty:</b> {@link Difficulty#HARD} | <b>LeetCode #23</b>
   *
   * <p><b>Technique:</b> Divide and Conquer
   *
   * <pre>
   *   Input: [[1,4,5], [1,3,4], [2,6]]
   *
   *   Divide and Conquer approach:
   *   Round 1: Merge pairs
   *     merge([1,4,5], [1,3,4]) → [1,1,3,4,4,5]
   *     [2,6] remains
   *
   *   Round 2: Merge remaining
   *     merge([1,1,3,4,4,5], [2,6]) → [1,1,2,3,4,4,5,6]
   *
   *   Result: [1,1,2,3,4,4,5,6]
   * </pre>
   *
   * <p><b>Time:</b> O(N log k) where N = total elements, k = number of lists
   *
   * <p><b>Space:</b> O(N)
   *
   * @param lists list of sorted lists to merge
   * @param comparator comparator to compare elements (null for natural ordering)
   * @param <T> the type of elements in the lists
   * @return single merged sorted list
   * @throws IllegalArgumentException if lists is null
   */
  public static <T> MyList<T> mergeKSortedLists(MyList<MyList<T>> lists, Comparator<T> comparator) {
    if (lists == null) {
      throw new IllegalArgumentException("Lists cannot be null");
    }
    if (lists.isEmpty()) {
      return new MyArrayList<>();
    }
    if (lists.size() == 1) {
      // Return a copy
      MyList<T> result = new MyArrayList<>();
      MyList<T> source = lists.get(0);
      if (source != null) {
        for (int i = 0; i < source.size(); i++) {
          result.add(source.get(i));
        }
      }
      return result;
    }

    // Divide and conquer: merge pairs iteratively
    MyList<MyList<T>> current = lists;

    while (current.size() > 1) {
      MyList<MyList<T>> merged = new MyArrayList<>();

      for (int i = 0; i < current.size(); i += 2) {
        if (i + 1 < current.size()) {
          MyList<T> list1 = current.get(i);
          MyList<T> list2 = current.get(i + 1);
          if (list1 == null) {
            list1 = new MyArrayList<>();
          }
          if (list2 == null) {
            list2 = new MyArrayList<>();
          }
          merged.add(mergeSortedLists(list1, list2, comparator));
        } else {
          // Odd one out, carry forward
          MyList<T> remaining = current.get(i);
          if (remaining == null) {
            remaining = new MyArrayList<>();
          }
          merged.add(remaining);
        }
      }

      current = merged;
    }

    return current.get(0);
  }

  /**
   * Reverses nodes in groups of k.
   *
   * <p><b>Difficulty:</b> {@link Difficulty#HARD} | <b>LeetCode #25</b>
   *
   * <p><b>Technique:</b> Iterative K-Reversal
   *
   * <pre>
   *   List:    [1] [2] [3] [4] [5]   k=3
   *
   *   Group 1 (indices 0-2): Reverse [1,2,3] → [3,2,1]
   *   Group 2 (indices 3-4): Only 2 elements, keep as [4,5]
   *
   *   Result:  [3] [2] [1] [4] [5]
   *
   *   Another example with k=2:
   *   List:    [1] [2] [3] [4] [5]
   *   Group 1: [2,1]
   *   Group 2: [4,3]
   *   Group 3: [5] (only 1, keep)
   *   Result:  [2] [1] [4] [3] [5]
   * </pre>
   *
   * <p><b>Time:</b> O(n) | <b>Space:</b> O(1)
   *
   * @param list the list to reverse in groups
   * @param k the group size
   * @param <T> the type of elements in the list
   * @throws IllegalArgumentException if list is null or k is less than 1
   */
  public static <T> void reverseKGroup(MyList<T> list, int k) {
    if (list == null) {
      throw new IllegalArgumentException("List cannot be null");
    }
    if (k < 1) {
      throw new IllegalArgumentException("k must be at least 1");
    }
    if (k == 1 || list.size() < k) {
      return;
    }

    int n = list.size();
    int fullGroups = n / k;

    for (int group = 0; group < fullGroups; group++) {
      int start = group * k;
      int end = start + k - 1;

      // Reverse elements from start to end
      while (start < end) {
        T temp = list.get(start);
        T endVal = list.get(end);

        list.remove(start);
        list.add(start, endVal);
        list.remove(end);
        list.add(end, temp);

        start++;
        end--;
      }
    }
  }

  /**
   * Sorts a list using merge sort algorithm.
   *
   * <p><b>Difficulty:</b> {@link Difficulty#HARD} | <b>LeetCode #148</b>
   *
   * <p><b>Technique:</b> Merge Sort (Divide and Conquer)
   *
   * <pre>
   *   List:    [4] [2] [1] [3]
   *
   *   Divide:
   *            [4] [2]       [1] [3]
   *           /      \       /      \
   *         [4]     [2]    [1]     [3]
   *
   *   Conquer (merge sorted halves):
   *         [4]     [2]    [1]     [3]
   *           \      /       \      /
   *           [2] [4]       [1] [3]
   *               \            /
   *            [1] [2] [3] [4]
   *
   *   Result: [1] [2] [3] [4]
   * </pre>
   *
   * <p><b>Time:</b> O(n log n) | <b>Space:</b> O(n)
   *
   * @param list the list to sort (modified in-place)
   * @param comparator comparator to compare elements (null for natural ordering)
   * @param <T> the type of elements in the list
   * @throws IllegalArgumentException if list is null
   */
  public static <T> void sortList(MyList<T> list, Comparator<T> comparator) {
    if (list == null) {
      throw new IllegalArgumentException("List cannot be null");
    }
    if (list.size() <= 1) {
      return;
    }

    MyList<T> sorted = mergeSort(list, comparator);

    // Copy sorted elements back to original list
    list.clear();
    for (int i = 0; i < sorted.size(); i++) {
      list.add(sorted.get(i));
    }
  }

  @SuppressWarnings("unchecked")
  private static <T> MyList<T> mergeSort(MyList<T> list, Comparator<T> comparator) {
    if (list.size() <= 1) {
      MyList<T> result = new MyArrayList<>();
      if (list.size() == 1) {
        result.add(list.get(0));
      }
      return result;
    }

    int mid = list.size() / 2;

    // Split into two halves
    MyList<T> left = new MyArrayList<>();
    MyList<T> right = new MyArrayList<>();

    for (int i = 0; i < mid; i++) {
      left.add(list.get(i));
    }
    for (int i = mid; i < list.size(); i++) {
      right.add(list.get(i));
    }

    // Recursively sort both halves
    MyList<T> sortedLeft = mergeSort(left, comparator);
    MyList<T> sortedRight = mergeSort(right, comparator);

    // Merge sorted halves
    return mergeSortedLists(sortedLeft, sortedRight, comparator);
  }
}
