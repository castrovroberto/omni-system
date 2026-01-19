package com.omni.core.algorithm.challenge;

import com.omni.app.log.Interval;
import com.omni.app.log.SystemEvent;
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
      // Swap elements at left and right using set() for O(1) swap
      T temp = list.get(left);
      list.set(left, list.get(right));
      list.set(right, temp);
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
        // Move element to write position using set() for O(1) operation
        if (writeIndex != readIndex) {
          list.set(writeIndex, current);
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

    // Three-reversal technique for O(n) time, O(1) space rotation:
    // To rotate right by k:
    // 1. Reverse entire list
    // 2. Reverse first k elements
    // 3. Reverse remaining n-k elements
    //
    // Example: [1,2,3,4,5] rotate right by 2
    // Step 1: [5,4,3,2,1]
    // Step 2: [4,5,3,2,1]
    // Step 3: [4,5,1,2,3]
    int n = list.size();
    reverseRange(list, 0, n - 1);
    reverseRange(list, 0, k - 1);
    reverseRange(list, k, n - 1);
  }

  /**
   * Helper method to reverse a range of elements in a list.
   *
   * @param list the list to modify
   * @param start start index (inclusive)
   * @param end end index (inclusive)
   */
  private static <T> void reverseRange(MyList<T> list, int start, int end) {
    while (start < end) {
      T temp = list.get(start);
      list.set(start, list.get(end));
      list.set(end, temp);
      start++;
      end--;
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

  /**
   * Finds the longest consecutive streak of events without ERROR severity.
   *
   * <p><b>Difficulty:</b> {@link Difficulty#MEDIUM}
   *
   * <p><b>Technique:</b> Sliding Window (Single Pass)
   *
   * <pre>
   *   Events: [INFO] [DEBUG] [ERROR] [INFO] [INFO] [WARNING] [INFO] [ERROR]
   *              1      2       X       1      2       3        4       X
   *           ──────────      ─────────────────────────────────
   *           streak=2        streak=4 (max!)
   *
   *   Track currentLength and maxLength:
   *   - Non-ERROR: increment currentLength
   *   - ERROR: reset currentLength to 0
   *   - Update maxLength after each non-ERROR event
   *
   *   Result: 4 (longest streak without ERROR)
   * </pre>
   *
   * <p><b>Time:</b> O(n) | <b>Space:</b> O(1)
   *
   * @param events the list of system events to analyze
   * @return the length of the longest consecutive error-free streak
   * @throws IllegalArgumentException if events is null
   */
  public static int longestErrorFreeStreak(MyList<SystemEvent> events) {
    if (events == null) {
      throw new IllegalArgumentException("Events cannot be null");
    }
    if (events.isEmpty()) {
      return 0;
    }

    int maxLength = 0;
    int currentLength = 0;

    for (int i = 0; i < events.size(); i++) {
      SystemEvent event = events.get(i);
      if (event != null && event.getSeverity() == SystemEvent.Severity.ERROR) {
        currentLength = 0;
      } else {
        currentLength++;
        maxLength = Math.max(maxLength, currentLength);
      }
    }

    return maxLength;
  }

  /**
   * Finds the missing packet ID from a sequence of IDs expected to be [1, 2, ..., n].
   *
   * <p><b>Difficulty:</b> {@link Difficulty#MEDIUM}
   *
   * <p><b>Technique:</b> Cyclic Sort
   *
   * <pre>
   *   Input: [3, 1, 4, 2, 6]  (n=5, missing one from [1..6])
   *
   *   Cyclic Sort: Place each number n at index n-1
   *
   *   Step 1: i=0, value=3 → swap to index 2
   *           [4, 1, 3, 2, 6]
   *           value=4 → swap to index 3
   *           [2, 1, 3, 4, 6]
   *           value=2 → swap to index 1
   *           [1, 2, 3, 4, 6]
   *           value=1 → correct position!
   *
   *   After sort: [1, 2, 3, 4, 6]
   *                           ^ index 4 has value 6, not 5!
   *
   *   Scan: Find first index i where value != i+1
   *   Result: 5 (missing ID)
   * </pre>
   *
   * <p><b>Time:</b> O(n) | <b>Space:</b> O(1)
   *
   * @param packetIds list of packet IDs (values should be in range [1, n+1] with one missing)
   * @return the missing packet ID
   * @throws IllegalArgumentException if packetIds is null
   */
  public static int findMissingId(MyList<Integer> packetIds) {
    if (packetIds == null) {
      throw new IllegalArgumentException("Packet IDs cannot be null");
    }
    if (packetIds.isEmpty()) {
      return 1;
    }

    int n = packetIds.size();

    // Cyclic sort: place number x at index x-1 (if x is in valid range)
    int i = 0;
    while (i < n) {
      int value = packetIds.get(i);
      // value should be at index value-1
      // Only swap if value is in valid range [1, n] and not already in correct position
      if (value >= 1 && value <= n && value != packetIds.get(value - 1)) {
        // Swap elements at i and value-1
        int targetIndex = value - 1;
        int temp = packetIds.get(targetIndex);
        packetIds.set(targetIndex, value);
        packetIds.set(i, temp);
      } else {
        i++;
      }
    }

    // Find the first position where the value doesn't match the expected value
    for (int j = 0; j < n; j++) {
      if (packetIds.get(j) != j + 1) {
        return j + 1;
      }
    }

    // All positions [1..n] are filled correctly, so n+1 is missing
    return n + 1;
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
  public static <T> MyList<T> mergeKsSortedLists(
      MyList<MyList<T>> lists, Comparator<T> comparator) {
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
  public static <T> void reverseKsGroup(MyList<T> list, int k) {
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

      // Reverse elements from start to end using set() for O(1) swaps
      reverseRange(list, start, end);
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

  /**
   * Finds the number of positions to the next higher element for each position.
   *
   * <p><b>Difficulty:</b> {@link Difficulty#HARD}
   *
   * <p><b>Technique:</b> Monotonic Stack
   *
   * <pre>
   *   Input:  [73, 74, 75, 71, 69, 72, 76, 73]
   *   Output: [ 1,  1,  4,  2,  1,  1,  0,  0]
   *
   *   Stack-based approach (stores indices):
   *
   *   i=0: value=73, stack=[], push 0
   *        stack=[0]
   *
   *   i=1: value=74 > values[stack.top()]=73
   *        pop 0, result[0] = 1-0 = 1
   *        push 1, stack=[1]
   *
   *   i=2: value=75 > values[1]=74
   *        pop 1, result[1] = 2-1 = 1
   *        push 2, stack=[2]
   *
   *   i=3: value=71 < 75, push 3
   *        stack=[2,3]
   *
   *   i=4: value=69 < 71, push 4
   *        stack=[2,3,4]
   *
   *   i=5: value=72 > 69, pop 4, result[4]=1
   *        value=72 > 71, pop 3, result[3]=2
   *        push 5, stack=[2,5]
   *
   *   i=6: value=76 > 72, pop 5, result[5]=1
   *        value=76 > 75, pop 2, result[2]=4
   *        push 6, stack=[6]
   *
   *   i=7: value=73 < 76, push 7
   *        stack=[6,7]
   *
   *   Remaining indices in stack get 0 (no higher element to the right)
   * </pre>
   *
   * <p><b>Time:</b> O(n) | <b>Space:</b> O(n)
   *
   * @param values list of integer values
   * @return list where each position contains the distance to the next higher element (0 if none)
   * @throws IllegalArgumentException if values is null
   */
  public static MyList<Integer> nextHigherElement(MyList<Integer> values) {
    if (values == null) {
      throw new IllegalArgumentException("Values cannot be null");
    }

    int n = values.size();
    MyList<Integer> result = new MyArrayList<>();

    // Initialize result with zeros
    for (int i = 0; i < n; i++) {
      result.add(0);
    }

    if (n == 0) {
      return result;
    }

    // Stack stores indices (using MyArrayList as stack)
    MyArrayList<Integer> stack = new MyArrayList<>();

    for (int i = 0; i < n; i++) {
      int currentValue = values.get(i);

      // While stack is not empty and current value is greater than value at stack top
      while (!stack.isEmpty() && currentValue > values.get(stack.get(stack.size() - 1))) {
        int j = stack.remove(stack.size() - 1); // pop
        result.set(j, i - j); // distance to next higher element
      }

      stack.add(i); // push current index
    }

    // Remaining indices in stack have no higher element to the right (already 0)
    return result;
  }

  /**
   * Merges overlapping intervals into non-overlapping intervals.
   *
   * <p><b>Difficulty:</b> {@link Difficulty#MEDIUM}
   *
   * <p><b>Technique:</b> Sort + Linear Scan
   *
   * <pre>
   *   Input:  [[1,3], [2,6], [8,10], [15,18]]
   *
   *   Step 1: Sort by start time (already sorted in this example)
   *
   *   Step 2: Linear scan and merge overlapping
   *
   *   merged=[[1,3]]
   *   [2,6] overlaps [1,3] → merge to [1,6]
   *   merged=[[1,6]]
   *
   *   [8,10] doesn't overlap [1,6] → add
   *   merged=[[1,6], [8,10]]
   *
   *   [15,18] doesn't overlap [8,10] → add
   *   merged=[[1,6], [8,10], [15,18]]
   *
   *   Result: [[1,6], [8,10], [15,18]]
   * </pre>
   *
   * <p><b>Time:</b> O(n log n) | <b>Space:</b> O(n)
   *
   * @param intervals list of intervals to merge
   * @return new list with merged non-overlapping intervals
   * @throws IllegalArgumentException if intervals is null
   */
  public static MyList<Interval> mergeIntervals(MyList<Interval> intervals) {
    if (intervals == null) {
      throw new IllegalArgumentException("Intervals cannot be null");
    }

    MyList<Interval> result = new MyArrayList<>();

    if (intervals.isEmpty()) {
      return result;
    }

    if (intervals.size() == 1) {
      result.add(intervals.get(0));
      return result;
    }

    // Sort intervals by start time using existing sortList
    MyList<Interval> sorted = new MyArrayList<>();
    for (int i = 0; i < intervals.size(); i++) {
      sorted.add(intervals.get(i));
    }
    sortList(sorted, Comparator.comparingLong(Interval::getStart));

    // Initialize result with first interval
    result.add(sorted.get(0));

    // Process remaining intervals
    for (int i = 1; i < sorted.size(); i++) {
      Interval current = sorted.get(i);
      Interval lastMerged = result.get(result.size() - 1);

      if (lastMerged.overlaps(current)) {
        // Merge: replace last with merged interval
        result.set(result.size() - 1, lastMerged.merge(current));
      } else {
        // No overlap: add to result
        result.add(current);
      }
    }

    return result;
  }
}
