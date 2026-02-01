package com.omni.core.tree;

import static org.junit.jupiter.api.Assertions.*;

import com.omni.core.list.MyArrayList;
import com.omni.core.list.MyList;
import java.util.Comparator;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("MyHeap Tests")
class MyHeapTest {

  // ==================== Min-Heap Tests ====================

  @Nested
  @DisplayName("Min-Heap Tests")
  class MinHeapTests {

    @Test
    @DisplayName("Extract returns elements in ascending order")
    void minHeap_extractOrder_ascending() {
      MyHeap<Integer> heap = MyHeap.natural();
      heap.insert(5);
      heap.insert(3);
      heap.insert(8);
      heap.insert(1);
      heap.insert(4);

      assertEquals(1, heap.extractRoot());
      assertEquals(3, heap.extractRoot());
      assertEquals(4, heap.extractRoot());
      assertEquals(5, heap.extractRoot());
      assertEquals(8, heap.extractRoot());
    }

    @Test
    @DisplayName("Peek returns minimum without removing")
    void minHeap_peek_returnsMinimum() {
      MyHeap<Integer> heap = MyHeap.natural();
      heap.insert(5);
      heap.insert(3);
      heap.insert(8);
      assertEquals(3, heap.peek());
      assertEquals(3, heap.size());
    }

    @Test
    @DisplayName("Size tracks insertions and extractions")
    void minHeap_size_tracksOperations() {
      MyHeap<Integer> heap = MyHeap.natural();
      assertTrue(heap.isEmpty());
      heap.insert(1);
      heap.insert(2);
      assertEquals(2, heap.size());
      heap.extractRoot();
      assertEquals(1, heap.size());
    }
  }

  // ==================== Max-Heap Tests ====================

  @Nested
  @DisplayName("Max-Heap Tests")
  class MaxHeapTests {

    @Test
    @DisplayName("Extract returns elements in descending order")
    void maxHeap_extractOrder_descending() {
      Comparator<Integer> reversed = Comparator.reverseOrder();
      MyHeap<Integer> heap = new MyHeap<>(reversed);
      heap.insert(5);
      heap.insert(3);
      heap.insert(8);
      heap.insert(1);
      heap.insert(4);

      assertEquals(8, heap.extractRoot());
      assertEquals(5, heap.extractRoot());
      assertEquals(4, heap.extractRoot());
      assertEquals(3, heap.extractRoot());
      assertEquals(1, heap.extractRoot());
    }
  }

  // ==================== Heapify Tests ====================

  @Nested
  @DisplayName("Heapify Tests")
  class HeapifyTests {

    @Test
    @DisplayName("Heapify builds valid min-heap")
    void heapify_minHeap_correctOrder() {
      MyList<Integer> list = new MyArrayList<>();
      list.add(5);
      list.add(3);
      list.add(8);
      list.add(1);
      list.add(4);

      MyHeap<Integer> heap = MyHeap.heapify(list, Comparator.naturalOrder());

      assertEquals(1, heap.extractRoot());
      assertEquals(3, heap.extractRoot());
      assertEquals(4, heap.extractRoot());
      assertEquals(5, heap.extractRoot());
      assertEquals(8, heap.extractRoot());
    }

    @Test
    @DisplayName("Heapify builds valid max-heap")
    void heapify_maxHeap_correctOrder() {
      MyList<Integer> list = new MyArrayList<>();
      list.add(5);
      list.add(3);
      list.add(8);
      list.add(1);

      MyHeap<Integer> heap = MyHeap.heapify(list, Comparator.reverseOrder());

      assertEquals(8, heap.extractRoot());
      assertEquals(5, heap.extractRoot());
      assertEquals(3, heap.extractRoot());
      assertEquals(1, heap.extractRoot());
    }

    @Test
    @DisplayName("Heapify empty list produces empty heap")
    void heapify_emptyList_emptyHeap() {
      MyList<Integer> list = new MyArrayList<>();
      MyHeap<Integer> heap = MyHeap.heapify(list, Comparator.naturalOrder());
      assertTrue(heap.isEmpty());
    }
  }

  // ==================== Edge Case Tests ====================

  @Nested
  @DisplayName("Edge Case Tests")
  class EdgeCaseTests {

    @Test
    @DisplayName("Extract from empty heap throws exception")
    void extractRoot_emptyHeap_throwsException() {
      MyHeap<Integer> heap = MyHeap.natural();
      assertThrows(NoSuchElementException.class, heap::extractRoot);
    }

    @Test
    @DisplayName("Peek on empty heap throws exception")
    void peek_emptyHeap_throwsException() {
      MyHeap<Integer> heap = MyHeap.natural();
      assertThrows(NoSuchElementException.class, heap::peek);
    }

    @Test
    @DisplayName("Single element heap")
    void singleElement_insertAndExtract() {
      MyHeap<Integer> heap = MyHeap.natural();
      heap.insert(42);
      assertEquals(42, heap.extractRoot());
      assertTrue(heap.isEmpty());
    }

    @Test
    @DisplayName("Duplicate elements are preserved")
    void duplicates_allPreserved() {
      MyHeap<Integer> heap = MyHeap.natural();
      heap.insert(5);
      heap.insert(5);
      heap.insert(5);
      assertEquals(3, heap.size());
      assertEquals(5, heap.extractRoot());
      assertEquals(5, heap.extractRoot());
      assertEquals(5, heap.extractRoot());
    }

    @Test
    @DisplayName("Large heap maintains correct order")
    void largeHeap_correctOrder() {
      MyHeap<Integer> heap = MyHeap.natural();
      for (int i = 1000; i >= 1; i--) {
        heap.insert(i);
      }
      for (int i = 1; i <= 1000; i++) {
        assertEquals(i, heap.extractRoot());
      }
    }
  }
}
