package com.omni.core.algorithm.sort;

import static org.junit.jupiter.api.Assertions.*;

import com.omni.core.list.MyArrayList;
import com.omni.core.list.MyList;
import java.util.Comparator;
import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("SortAlgorithms Tests")
class SortAlgorithmsTest {

  private MyList<Integer> list;
  private Random random;

  @BeforeEach
  void setUp() {
    list = new MyArrayList<>();
    random = new Random(42); // Fixed seed for reproducibility
  }

  // ==================== Helper Methods ====================

  private MyList<Integer> createList(int... values) {
    MyList<Integer> result = new MyArrayList<>();
    for (int v : values) {
      result.add(v);
    }
    return result;
  }

  private MyList<Integer> createRandomList(int size) {
    MyList<Integer> result = new MyArrayList<>();
    for (int i = 0; i < size; i++) {
      result.add(random.nextInt(10000));
    }
    return result;
  }

  private MyList<Integer> createSortedList(int size) {
    MyList<Integer> result = new MyArrayList<>();
    for (int i = 0; i < size; i++) {
      result.add(i);
    }
    return result;
  }

  private MyList<Integer> createReverseSortedList(int size) {
    MyList<Integer> result = new MyArrayList<>();
    for (int i = size - 1; i >= 0; i--) {
      result.add(i);
    }
    return result;
  }

  private MyList<Integer> createListWithDuplicates(int size) {
    MyList<Integer> result = new MyArrayList<>();
    for (int i = 0; i < size; i++) {
      result.add(i % 10); // Values 0-9 repeated
    }
    return result;
  }

  // ==================== Merge Sort Tests ====================

  @Nested
  @DisplayName("Merge Sort Tests")
  class MergeSortTests {

    @Test
    @DisplayName("Sorts empty list")
    void mergeSort_emptyList_unchanged() {
      SortAlgorithms.mergeSort(list, null);
      assertTrue(list.isEmpty());
    }

    @Test
    @DisplayName("Sorts single element")
    void mergeSort_singleElement_unchanged() {
      list.add(42);
      SortAlgorithms.mergeSort(list, null);
      assertEquals(1, list.size());
      assertEquals(42, list.get(0));
    }

    @Test
    @DisplayName("Sorts two elements")
    void mergeSort_twoElements_sorted() {
      list = createList(5, 3);
      SortAlgorithms.mergeSort(list, null);
      assertEquals(3, list.get(0));
      assertEquals(5, list.get(1));
    }

    @Test
    @DisplayName("Sorts already sorted list")
    void mergeSort_alreadySorted_unchanged() {
      list = createList(1, 2, 3, 4, 5);
      SortAlgorithms.mergeSort(list, null);
      assertTrue(SortAlgorithms.isSorted(list, null));
    }

    @Test
    @DisplayName("Sorts reverse sorted list")
    void mergeSort_reverseSorted_sorted() {
      list = createList(5, 4, 3, 2, 1);
      SortAlgorithms.mergeSort(list, null);
      assertTrue(SortAlgorithms.isSorted(list, null));
      assertEquals(1, list.get(0));
      assertEquals(5, list.get(4));
    }

    @Test
    @DisplayName("Sorts random list")
    void mergeSort_randomList_sorted() {
      list = createList(3, 1, 4, 1, 5, 9, 2, 6, 5, 3);
      SortAlgorithms.mergeSort(list, null);
      assertTrue(SortAlgorithms.isSorted(list, null));
    }

    @Test
    @DisplayName("Handles duplicates")
    void mergeSort_withDuplicates_sorted() {
      list = createList(3, 1, 2, 1, 3, 2);
      SortAlgorithms.mergeSort(list, null);
      assertTrue(SortAlgorithms.isSorted(list, null));
      assertEquals(6, list.size());
    }

    @Test
    @DisplayName("Uses custom comparator")
    void mergeSort_customComparator_sorted() {
      list = createList(1, 5, 3, 2, 4);
      SortAlgorithms.mergeSort(list, Comparator.reverseOrder());
      assertEquals(5, list.get(0));
      assertEquals(1, list.get(4));
    }

    @Test
    @DisplayName("Is stable - maintains relative order of equal elements")
    void mergeSort_isStable() {
      // Use a wrapper class to track original positions
      MyList<int[]> pairs = new MyArrayList<>();
      pairs.add(new int[] {3, 0}); // value, original position
      pairs.add(new int[] {1, 1});
      pairs.add(new int[] {3, 2});
      pairs.add(new int[] {2, 3});
      pairs.add(new int[] {3, 4});

      SortAlgorithms.mergeSort(pairs, Comparator.comparingInt(a -> a[0]));

      // All 3s should maintain their relative order: positions 0, 2, 4
      int[] first3 = null;
      int[] second3 = null;
      int[] third3 = null;
      for (int i = 0; i < pairs.size(); i++) {
        if (pairs.get(i)[0] == 3) {
          if (first3 == null) {
            first3 = pairs.get(i);
          } else if (second3 == null) {
            second3 = pairs.get(i);
          } else {
            third3 = pairs.get(i);
          }
        }
      }

      assertEquals(0, first3[1]);
      assertEquals(2, second3[1]);
      assertEquals(4, third3[1]);
    }

    @Test
    @DisplayName("Sorts large list")
    void mergeSort_largeList_sorted() {
      list = createRandomList(10000);
      SortAlgorithms.mergeSort(list, null);
      assertTrue(SortAlgorithms.isSorted(list, null));
    }

    @Test
    @DisplayName("Works with natural ordering")
    void mergeSort_naturalOrdering_works() {
      MyList<String> strings = new MyArrayList<>();
      strings.add("banana");
      strings.add("apple");
      strings.add("cherry");

      SortAlgorithms.mergeSort(strings);

      assertEquals("apple", strings.get(0));
      assertEquals("banana", strings.get(1));
      assertEquals("cherry", strings.get(2));
    }
  }

  // ==================== Quick Sort Tests ====================

  @Nested
  @DisplayName("Quick Sort Tests")
  class QuickSortTests {

    @Test
    @DisplayName("Sorts empty list")
    void quickSort_emptyList_unchanged() {
      SortAlgorithms.quickSort(list, null);
      assertTrue(list.isEmpty());
    }

    @Test
    @DisplayName("Sorts single element")
    void quickSort_singleElement_unchanged() {
      list.add(42);
      SortAlgorithms.quickSort(list, null);
      assertEquals(1, list.size());
      assertEquals(42, list.get(0));
    }

    @Test
    @DisplayName("Sorts two elements")
    void quickSort_twoElements_sorted() {
      list = createList(5, 3);
      SortAlgorithms.quickSort(list, null);
      assertEquals(3, list.get(0));
      assertEquals(5, list.get(1));
    }

    @Test
    @DisplayName("Sorts already sorted list")
    void quickSort_alreadySorted_sorted() {
      list = createList(1, 2, 3, 4, 5);
      SortAlgorithms.quickSort(list, null);
      assertTrue(SortAlgorithms.isSorted(list, null));
    }

    @Test
    @DisplayName("Sorts reverse sorted list")
    void quickSort_reverseSorted_sorted() {
      list = createList(5, 4, 3, 2, 1);
      SortAlgorithms.quickSort(list, null);
      assertTrue(SortAlgorithms.isSorted(list, null));
      assertEquals(1, list.get(0));
      assertEquals(5, list.get(4));
    }

    @Test
    @DisplayName("Sorts random list")
    void quickSort_randomList_sorted() {
      list = createList(3, 1, 4, 1, 5, 9, 2, 6, 5, 3);
      SortAlgorithms.quickSort(list, null);
      assertTrue(SortAlgorithms.isSorted(list, null));
    }

    @Test
    @DisplayName("Handles duplicates")
    void quickSort_withDuplicates_sorted() {
      list = createList(3, 1, 2, 1, 3, 2);
      SortAlgorithms.quickSort(list, null);
      assertTrue(SortAlgorithms.isSorted(list, null));
      assertEquals(6, list.size());
    }

    @Test
    @DisplayName("Uses custom comparator")
    void quickSort_customComparator_sorted() {
      list = createList(1, 5, 3, 2, 4);
      SortAlgorithms.quickSort(list, Comparator.reverseOrder());
      assertEquals(5, list.get(0));
      assertEquals(1, list.get(4));
    }

    @Test
    @DisplayName("Handles all equal elements")
    void quickSort_allEqual_sorted() {
      list = createList(5, 5, 5, 5, 5);
      SortAlgorithms.quickSort(list, null);
      assertTrue(SortAlgorithms.isSorted(list, null));
      for (int i = 0; i < 5; i++) {
        assertEquals(5, list.get(i));
      }
    }

    @Test
    @DisplayName("Sorts large list")
    void quickSort_largeList_sorted() {
      list = createRandomList(10000);
      SortAlgorithms.quickSort(list, null);
      assertTrue(SortAlgorithms.isSorted(list, null));
    }

    @Test
    @DisplayName("Works with natural ordering")
    void quickSort_naturalOrdering_works() {
      MyList<String> strings = new MyArrayList<>();
      strings.add("banana");
      strings.add("apple");
      strings.add("cherry");

      SortAlgorithms.quickSort(strings);

      assertEquals("apple", strings.get(0));
      assertEquals("banana", strings.get(1));
      assertEquals("cherry", strings.get(2));
    }

    @Test
    @DisplayName("Median-of-three handles sorted input efficiently")
    void quickSort_sortedInput_efficientWithMedianOfThree() {
      // This would be O(n²) with naive pivot, but median-of-three should handle it
      list = createSortedList(1000);
      long start = System.nanoTime();
      SortAlgorithms.quickSort(list, null);
      long duration = System.nanoTime() - start;

      assertTrue(SortAlgorithms.isSorted(list, null));
      // Should complete quickly (not O(n²) behavior)
      assertTrue(duration < 100_000_000L, "Should not degrade to O(n²)");
    }

    @Test
    @DisplayName("Median-of-three handles reverse sorted input efficiently")
    void quickSort_reverseSortedInput_efficientWithMedianOfThree() {
      list = createReverseSortedList(1000);
      long start = System.nanoTime();
      SortAlgorithms.quickSort(list, null);
      long duration = System.nanoTime() - start;

      assertTrue(SortAlgorithms.isSorted(list, null));
      assertTrue(duration < 100_000_000L, "Should not degrade to O(n²)");
    }
  }

  // ==================== IsSorted Tests ====================

  @Nested
  @DisplayName("IsSorted Tests")
  class IsSortedTests {

    @Test
    @DisplayName("Empty list is sorted")
    void isSorted_emptyList_returnsTrue() {
      assertTrue(SortAlgorithms.isSorted(list, null));
    }

    @Test
    @DisplayName("Single element is sorted")
    void isSorted_singleElement_returnsTrue() {
      list.add(42);
      assertTrue(SortAlgorithms.isSorted(list, null));
    }

    @Test
    @DisplayName("Sorted list returns true")
    void isSorted_sortedList_returnsTrue() {
      list = createList(1, 2, 3, 4, 5);
      assertTrue(SortAlgorithms.isSorted(list, null));
    }

    @Test
    @DisplayName("Unsorted list returns false")
    void isSorted_unsortedList_returnsFalse() {
      list = createList(1, 3, 2, 4, 5);
      assertFalse(SortAlgorithms.isSorted(list, null));
    }

    @Test
    @DisplayName("Equal elements is sorted")
    void isSorted_equalElements_returnsTrue() {
      list = createList(3, 3, 3, 3);
      assertTrue(SortAlgorithms.isSorted(list, null));
    }

    @Test
    @DisplayName("Works with custom comparator")
    void isSorted_customComparator_works() {
      list = createList(5, 4, 3, 2, 1);
      assertTrue(SortAlgorithms.isSorted(list, Comparator.reverseOrder()));
    }
  }

  // ==================== Heap Sort Tests ====================

  @Nested
  @DisplayName("Heap Sort Tests")
  class HeapSortTests {

    @Test
    @DisplayName("Sorts empty list")
    void heapSort_emptyList_unchanged() {
      SortAlgorithms.heapSort(list, null);
      assertTrue(list.isEmpty());
    }

    @Test
    @DisplayName("Sorts single element")
    void heapSort_singleElement_unchanged() {
      list.add(42);
      SortAlgorithms.heapSort(list, null);
      assertEquals(1, list.size());
      assertEquals(42, list.get(0));
    }

    @Test
    @DisplayName("Sorts random list")
    void heapSort_randomList_sorted() {
      list = createList(3, 1, 4, 1, 5, 9, 2, 6, 5, 3);
      SortAlgorithms.heapSort(list, Comparator.naturalOrder());
      assertTrue(SortAlgorithms.isSorted(list, null));
    }

    @Test
    @DisplayName("Sorts reverse sorted list")
    void heapSort_reverseSorted_sorted() {
      list = createList(5, 4, 3, 2, 1);
      SortAlgorithms.heapSort(list, Comparator.naturalOrder());
      assertTrue(SortAlgorithms.isSorted(list, null));
      assertEquals(1, list.get(0));
      assertEquals(5, list.get(4));
    }

    @Test
    @DisplayName("Handles duplicates")
    void heapSort_withDuplicates_sorted() {
      list = createList(3, 1, 2, 1, 3, 2);
      SortAlgorithms.heapSort(list, Comparator.naturalOrder());
      assertTrue(SortAlgorithms.isSorted(list, null));
      assertEquals(6, list.size());
    }

    @Test
    @DisplayName("Uses custom comparator for reverse order")
    void heapSort_customComparator_sorted() {
      list = createList(1, 5, 3, 2, 4);
      SortAlgorithms.heapSort(list, Comparator.reverseOrder());
      assertEquals(5, list.get(0));
      assertEquals(1, list.get(4));
    }

    @Test
    @DisplayName("Works with natural ordering")
    void heapSort_naturalOrdering_works() {
      MyList<String> strings = new MyArrayList<>();
      strings.add("banana");
      strings.add("apple");
      strings.add("cherry");

      SortAlgorithms.heapSort(strings);

      assertEquals("apple", strings.get(0));
      assertEquals("banana", strings.get(1));
      assertEquals("cherry", strings.get(2));
    }

    @Test
    @DisplayName("Sorts large list")
    void heapSort_largeList_sorted() {
      list = createRandomList(10000);
      SortAlgorithms.heapSort(list, Comparator.naturalOrder());
      assertTrue(SortAlgorithms.isSorted(list, null));
    }
  }

  // ==================== Comparison Tests ====================

  @Nested
  @DisplayName("Algorithm Comparison Tests")
  class ComparisonTests {

    @Test
    @DisplayName("All algorithms produce same result")
    void allAlgorithms_sameResult() {
      MyList<Integer> list1 = createList(3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5);
      MyList<Integer> list2 = createList(3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5);
      MyList<Integer> list3 = createList(3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5);

      SortAlgorithms.mergeSort(list1, null);
      SortAlgorithms.quickSort(list2, null);
      SortAlgorithms.heapSort(list3, Comparator.naturalOrder());

      assertEquals(list1.size(), list2.size());
      assertEquals(list1.size(), list3.size());
      for (int i = 0; i < list1.size(); i++) {
        assertEquals(list1.get(i), list2.get(i));
        assertEquals(list1.get(i), list3.get(i));
      }
    }

    @Test
    @DisplayName("All handle null list")
    void allAlgorithms_handleNullList() {
      assertDoesNotThrow(() -> SortAlgorithms.mergeSort(null, null));
      assertDoesNotThrow(() -> SortAlgorithms.quickSort(null, null));
      assertDoesNotThrow(() -> SortAlgorithms.heapSort(null, null));
    }
  }

  // ==================== Mastery Check Tests ====================

  @Nested
  @DisplayName("Mastery Check Tests")
  class MasteryCheckTests {

    @Test
    @DisplayName("Mastery check: Sort 1,000,000 random integers")
    void masteryCheck_sortOneMillionIntegers() {
      // Use smaller random seed for deterministic test
      Random r = new Random(12345);
      MyList<Integer> largeList = new MyArrayList<>();
      int count = 1_000_000;

      for (int i = 0; i < count; i++) {
        largeList.add(r.nextInt());
      }

      long mergeStart = System.nanoTime();
      SortAlgorithms.mergeSort(largeList, null);
      long mergeDuration = System.nanoTime() - mergeStart;

      assertTrue(SortAlgorithms.isSorted(largeList, null));
      System.out.println("MergeSort 1M elements: " + (mergeDuration / 1_000_000) + " ms");
    }

    @Test
    @DisplayName("Mastery check: QuickSort on already sorted data")
    void masteryCheck_quickSortAlreadySorted() {
      int count = 100_000;
      list = createSortedList(count);

      long start = System.nanoTime();
      SortAlgorithms.quickSort(list, null);
      long duration = System.nanoTime() - start;

      assertTrue(SortAlgorithms.isSorted(list, null));

      // With naive pivot, this would be O(n²) = 10^10 operations
      // With median-of-three, should be O(n log n) = ~1.6 * 10^6 operations
      // Should complete in reasonable time (< 1 second)
      assertTrue(
          duration < 1_000_000_000L,
          "QuickSort with median-of-three should not degrade on sorted input. Took: "
              + (duration / 1_000_000)
              + " ms");

      System.out.println("QuickSort sorted 100K: " + (duration / 1_000_000) + " ms");
    }

    @Test
    @DisplayName("Mastery check: Median-of-three performance recovery")
    void masteryCheck_medianOfThreePerformanceRecovery() {
      int count = 50_000;

      // Test 1: Already sorted
      MyList<Integer> sorted = createSortedList(count);
      long sortedStart = System.nanoTime();
      SortAlgorithms.quickSort(sorted, null);
      long sortedDuration = System.nanoTime() - sortedStart;

      // Test 2: Reverse sorted
      MyList<Integer> reversed = createReverseSortedList(count);
      long reversedStart = System.nanoTime();
      SortAlgorithms.quickSort(reversed, null);
      long reversedDuration = System.nanoTime() - reversedStart;

      // Test 3: Random
      MyList<Integer> randomList = createRandomList(count);
      long randomStart = System.nanoTime();
      SortAlgorithms.quickSort(randomList, null);
      long randomDuration = System.nanoTime() - randomStart;

      // All should be sorted
      assertTrue(SortAlgorithms.isSorted(sorted, null));
      assertTrue(SortAlgorithms.isSorted(reversed, null));
      assertTrue(SortAlgorithms.isSorted(randomList, null));

      // Sorted/reversed should not be drastically slower than random
      // (With naive pivot, sorted would be ~50x slower)
      long maxDuration = Math.max(sortedDuration, Math.max(reversedDuration, randomDuration));
      long minDuration = Math.min(sortedDuration, Math.min(reversedDuration, randomDuration));

      // Max should not be more than 10x min (generous margin)
      assertTrue(
          maxDuration < minDuration * 10,
          String.format(
              "Median-of-three should prevent degradation. Sorted: %dms, Reversed: %dms, Random:"
                  + " %dms",
              sortedDuration / 1_000_000,
              reversedDuration / 1_000_000,
              randomDuration / 1_000_000));
    }

    @Test
    @DisplayName("Mastery check: Compare MergeSort vs QuickSort performance")
    void masteryCheck_compareMergeSortVsQuickSort() {
      int count = 100_000;

      // Create identical lists
      Random r = new Random(999);
      MyList<Integer> mergeList = new MyArrayList<>();
      MyList<Integer> quickList = new MyArrayList<>();
      for (int i = 0; i < count; i++) {
        int val = r.nextInt();
        mergeList.add(val);
        quickList.add(val);
      }

      long mergeStart = System.nanoTime();
      SortAlgorithms.mergeSort(mergeList, null);
      long mergeDuration = System.nanoTime() - mergeStart;

      long quickStart = System.nanoTime();
      SortAlgorithms.quickSort(quickList, null);
      long quickDuration = System.nanoTime() - quickStart;

      assertTrue(SortAlgorithms.isSorted(mergeList, null));
      assertTrue(SortAlgorithms.isSorted(quickList, null));

      System.out.println(
          String.format(
              "100K elements - MergeSort: %dms, QuickSort: %dms",
              mergeDuration / 1_000_000, quickDuration / 1_000_000));

      // Both should complete in reasonable time
      assertTrue(mergeDuration < 5_000_000_000L, "MergeSort took too long");
      assertTrue(quickDuration < 5_000_000_000L, "QuickSort took too long");
    }
  }
}
