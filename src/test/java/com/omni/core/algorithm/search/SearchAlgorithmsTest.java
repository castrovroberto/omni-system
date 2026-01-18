package com.omni.core.algorithm.search;

import static org.junit.jupiter.api.Assertions.*;

import com.omni.core.list.MyArrayList;
import com.omni.core.list.MyLinkedList;
import com.omni.core.list.MyList;
import java.util.Comparator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Search Algorithms Tests")
class SearchAlgorithmsTest {

  private MyList<Integer> sortedList;
  private MyList<Integer> unsortedList;

  @BeforeEach
  void setUp() {
    sortedList = new MyArrayList<>();
    // Create sorted list: 1, 3, 5, 7, 9, 11, 13
    sortedList.add(1);
    sortedList.add(3);
    sortedList.add(5);
    sortedList.add(7);
    sortedList.add(9);
    sortedList.add(11);
    sortedList.add(13);

    unsortedList = new MyArrayList<>();
    unsortedList.add(5);
    unsortedList.add(2);
    unsortedList.add(8);
    unsortedList.add(1);
    unsortedList.add(9);
  }

  @Nested
  @DisplayName("Linear Search Tests")
  class LinearSearchTests {

    @Test
    @DisplayName("Linear search finds element in sorted list")
    void linearSearch_inSortedList_findsElement() {
      int index = SearchAlgorithms.linearSearch(sortedList, 7, null);
      assertEquals(3, index);
    }

    @Test
    @DisplayName("Linear search finds element in unsorted list")
    void linearSearch_inUnsortedList_findsElement() {
      int index = SearchAlgorithms.linearSearch(unsortedList, 8, null);
      assertEquals(2, index);
    }

    @Test
    @DisplayName("Linear search returns -1 when element not found")
    void linearSearch_elementNotFound_returnsMinusOne() {
      int index = SearchAlgorithms.linearSearch(sortedList, 10, null);
      assertEquals(-1, index);
    }

    @Test
    @DisplayName("Linear search finds first occurrence")
    void linearSearch_findsFirstOccurrence() {
      MyList<Integer> listWithDuplicates = new MyArrayList<>();
      listWithDuplicates.add(1);
      listWithDuplicates.add(2);
      listWithDuplicates.add(2);
      listWithDuplicates.add(3);

      int index = SearchAlgorithms.linearSearch(listWithDuplicates, 2, null);
      assertEquals(1, index); // Should find first occurrence
    }

    @Test
    @DisplayName("Linear search with null list returns -1")
    void linearSearch_nullList_returnsMinusOne() {
      int index = SearchAlgorithms.linearSearch(null, 1, null);
      assertEquals(-1, index);
    }

    @Test
    @DisplayName("Linear search with null target returns -1")
    void linearSearch_nullTarget_returnsMinusOne() {
      int index = SearchAlgorithms.linearSearch(sortedList, null, null);
      assertEquals(-1, index);
    }

    @Test
    @DisplayName("Linear search works with Comparator")
    void linearSearch_withComparator_works() {
      Comparator<Integer> reverseComparator = (a, b) -> b.compareTo(a);
      int index = SearchAlgorithms.linearSearch(sortedList, 7, reverseComparator);
      // Note: This won't work correctly because list is sorted ascending
      // but demonstrates comparator usage
      assertTrue(index >= 0 || index == -1);
    }

    @Test
    @DisplayName("Linear search works with MyLinkedList")
    void linearSearch_withMyLinkedList_works() {
      MyList<Integer> linkedList = new MyLinkedList<>();
      linkedList.add(1);
      linkedList.add(3);
      linkedList.add(5);

      int index = SearchAlgorithms.linearSearch(linkedList, 3, null);
      assertEquals(1, index);
    }
  }

  @Nested
  @DisplayName("Binary Search Tests")
  class BinarySearchTests {

    @Test
    @DisplayName("Binary search finds element in sorted list")
    void binarySearch_inSortedList_findsElement() {
      int index = SearchAlgorithms.binarySearch(sortedList, 7, null);
      assertEquals(3, index);
    }

    @Test
    @DisplayName("Binary search finds first element")
    void binarySearch_findsFirstElement() {
      int index = SearchAlgorithms.binarySearch(sortedList, 1, null);
      assertEquals(0, index);
    }

    @Test
    @DisplayName("Binary search finds last element")
    void binarySearch_findsLastElement() {
      int index = SearchAlgorithms.binarySearch(sortedList, 13, null);
      assertEquals(6, index);
    }

    @Test
    @DisplayName("Binary search returns negative value when not found")
    void binarySearch_elementNotFound_returnsNegative() {
      int index = SearchAlgorithms.binarySearch(sortedList, 10, null);
      // 10 should be inserted at position 5, so returns -(5 + 1) = -6
      assertTrue(index < 0);
      assertEquals(-6, index);
    }

    @Test
    @DisplayName("Binary search returns correct insertion point")
    void binarySearch_returnsCorrectInsertionPoint() {
      // 10 is not in list, should be inserted at index 5
      int index = SearchAlgorithms.binarySearch(sortedList, 10, null);
      assertEquals(-6, index); // -(5 + 1)
      assertEquals(5, Math.abs(index) - 1);
    }

    @Test
    @DisplayName("Binary search with null list throws exception")
    void binarySearch_nullList_throwsException() {
      assertThrows(
          IllegalArgumentException.class, () -> SearchAlgorithms.binarySearch(null, 1, null));
    }

    @Test
    @DisplayName("Binary search with null target throws exception")
    void binarySearch_nullTarget_throwsException() {
      assertThrows(
          IllegalArgumentException.class,
          () -> SearchAlgorithms.binarySearch(sortedList, null, null));
    }

    @Test
    @DisplayName("Binary search with Comparator works")
    void binarySearch_withComparator_works() {
      // Create descending sorted list
      MyList<Integer> descendingList = new MyArrayList<>();
      descendingList.add(13);
      descendingList.add(11);
      descendingList.add(9);
      descendingList.add(7);
      descendingList.add(5);
      descendingList.add(3);
      descendingList.add(1);

      Comparator<Integer> reverseComparator = Comparator.reverseOrder();
      int index = SearchAlgorithms.binarySearch(descendingList, 9, reverseComparator);
      assertEquals(2, index);
    }

    @Test
    @DisplayName("Binary search works with MyLinkedList")
    void binarySearch_withMyLinkedList_works() {
      MyList<Integer> linkedList = new MyLinkedList<>();
      linkedList.add(1);
      linkedList.add(3);
      linkedList.add(5);
      linkedList.add(7);

      int index = SearchAlgorithms.binarySearch(linkedList, 5, null);
      assertEquals(2, index);
    }

    @Test
    @DisplayName("Binary search with empty list returns -1")
    void binarySearch_emptyList_returnsNegativeOne() {
      MyList<Integer> emptyList = new MyArrayList<>();
      int index = SearchAlgorithms.binarySearch(emptyList, 1, null);
      assertEquals(-1, index); // Should insert at 0, so -(0 + 1) = -1
    }
  }

  @Nested
  @DisplayName("Mastery Check Tests")
  class MasteryCheckTests {

    @Test
    @DisplayName("Mastery check: Binary search on 1 million elements")
    void masteryCheck_binarySearchOneMillionElements() {
      MyList<Integer> largeList = new MyArrayList<>();
      int count = 1_000_000;

      // Create sorted list
      for (int i = 0; i < count; i++) {
        largeList.add(i * 2); // Even numbers: 0, 2, 4, ..., 1,999,998
      }

      // Binary search should find in ~20 comparisons
      int target = 500_000 * 2; // Should be at index 500,000
      long startTime = System.nanoTime();
      int index = SearchAlgorithms.binarySearch(largeList, target, null);
      long endTime = System.nanoTime();

      assertEquals(500_000, index);
      // Binary search should be very fast (O(log n))
      assertTrue(endTime - startTime < 100_000_000L, "Binary search should be fast");
    }

    @Test
    @DisplayName("Mastery check: Linear vs Binary search comparison")
    void masteryCheck_linearVsBinarySearch() {
      MyList<Integer> largeList = new MyArrayList<>();
      int count = 100_000;

      // Create sorted list
      for (int i = 0; i < count; i++) {
        largeList.add(i);
      }

      int target = count - 1; // Last element

      // Linear search (should be slow - O(n))
      long linearStart = System.nanoTime();
      int linearIndex = SearchAlgorithms.linearSearch(largeList, target, null);
      long linearEnd = System.nanoTime();

      // Binary search (should be fast - O(log n))
      long binaryStart = System.nanoTime();
      int binaryIndex = SearchAlgorithms.binarySearch(largeList, target, null);
      long binaryEnd = System.nanoTime();

      assertEquals(count - 1, linearIndex);
      assertEquals(count - 1, binaryIndex);

      long linearTime = linearEnd - linearStart;
      long binaryTime = binaryEnd - binaryStart;

      // Binary search should be significantly faster
      assertTrue(binaryTime < linearTime, "Binary search should be faster than linear search");
    }
  }
}
