package com.omni.core.list;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("MyLinkedList Tests")
class MyLinkedListTest {

  private MyLinkedList<String> list;

  @BeforeEach
  void setUp() {
    list = new MyLinkedList<>();
  }

  @Nested
  @DisplayName("Basic Operations")
  class BasicOperationsTests {

    @Test
    @DisplayName("Add element to empty list")
    void add_toEmptyList_addsElement() {
      list.add("first");
      assertEquals(1, list.size());
      assertEquals("first", list.get(0));
      assertFalse(list.isEmpty());
    }

    @Test
    @DisplayName("AddFirst adds element at beginning")
    void addFirst_addsAtBeginning() {
      list.add("second");
      list.addFirst("first");

      assertEquals(2, list.size());
      assertEquals("first", list.get(0));
      assertEquals("second", list.get(1));
    }

    @Test
    @DisplayName("AddLast appends element")
    void addLast_appendsElement() {
      list.addFirst("first");
      list.addLast("second");

      assertEquals(2, list.size());
      assertEquals("first", list.get(0));
      assertEquals("second", list.get(1));
    }

    @Test
    @DisplayName("RemoveFirst removes and returns first element")
    void removeFirst_removesFirstElement() {
      list.add("first");
      list.add("second");

      String removed = list.removeFirst();

      assertEquals("first", removed);
      assertEquals(1, list.size());
      assertEquals("second", list.get(0));
    }

    @Test
    @DisplayName("RemoveLast removes and returns last element")
    void removeLast_removesLastElement() {
      list.add("first");
      list.add("second");

      String removed = list.removeLast();

      assertEquals("second", removed);
      assertEquals(1, list.size());
      assertEquals("first", list.get(0));
    }

    @Test
    @DisplayName("Get by index returns correct element")
    void get_byIndex_returnsCorrectElement() {
      list.add("first");
      list.add("second");
      list.add("third");

      assertEquals("first", list.get(0));
      assertEquals("second", list.get(1));
      assertEquals("third", list.get(2));
    }

    @Test
    @DisplayName("Remove by index removes correct element")
    void remove_byIndex_removesCorrectElement() {
      list.add("first");
      list.add("second");
      list.add("third");

      String removed = list.remove(1);

      assertEquals("second", removed);
      assertEquals(2, list.size());
      assertEquals("first", list.get(0));
      assertEquals("third", list.get(1));
    }

    @Test
    @DisplayName("Clear removes all elements")
    void clear_removesAllElements() {
      list.add("first");
      list.add("second");
      list.clear();

      assertTrue(list.isEmpty());
      assertEquals(0, list.size());
    }
  }

  @Nested
  @DisplayName("Edge Cases")
  class EdgeCaseTests {

    @Test
    @DisplayName("RemoveFirst on empty list throws exception")
    void removeFirst_emptyList_throwsException() {
      assertThrows(NoSuchElementException.class, () -> list.removeFirst());
    }

    @Test
    @DisplayName("RemoveLast on empty list throws exception")
    void removeLast_emptyList_throwsException() {
      assertThrows(NoSuchElementException.class, () -> list.removeLast());
    }

    @Test
    @DisplayName("Single element list operations work")
    void singleElementList_operationsWork() {
      list.add("only");
      assertEquals("only", list.removeFirst());
      assertTrue(list.isEmpty());

      list.add("only");
      assertEquals("only", list.removeLast());
      assertTrue(list.isEmpty());
    }
  }

  @Nested
  @DisplayName("Iterator Tests")
  class IteratorTests {

    @Test
    @DisplayName("Iterator traverses all elements")
    void iterator_traversesAllElements() {
      list.add("first");
      list.add("second");
      list.add("third");

      Iterator<String> it = list.iterator();
      assertTrue(it.hasNext());
      assertEquals("first", it.next());
      assertEquals("second", it.next());
      assertEquals("third", it.next());
      assertFalse(it.hasNext());
    }

    @Test
    @DisplayName("Enhanced for loop works")
    void enhancedForLoop_works() {
      list.add("first");
      list.add("second");
      list.add("third");

      int count = 0;
      for (String element : list) {
        assertNotNull(element);
        count++;
      }

      assertEquals(3, count);
    }

    @Test
    @DisplayName("Iterator remove removes element")
    void iteratorRemove_removesElement() {
      list.add("first");
      list.add("second");
      list.add("third");

      Iterator<String> it = list.iterator();
      it.next();
      it.next();
      it.remove();

      assertEquals(2, list.size());
      assertEquals("first", list.get(0));
      assertEquals("third", list.get(1));
    }

    @Test
    @DisplayName("Iterator fail-fast on concurrent modification")
    void iterator_concurrentModification_throwsException() {
      list.add("first");
      list.add("second");

      Iterator<String> it = list.iterator();
      it.next();

      list.add("third"); // Modify list

      assertThrows(ConcurrentModificationException.class, () -> it.next());
    }
  }

  @Nested
  @DisplayName("Performance Tests")
  class PerformanceTests {

    @Test
    @DisplayName("Mastery check: addFirst is O(1) - 100000 insertions")
    void masteryCheck_addFirst_isO1() {
      // Measure time for 100,000 addFirst operations
      long startTime = System.nanoTime();

      for (int i = 0; i < 100_000; i++) {
        list.addFirst("element" + i);
      }

      long endTime = System.nanoTime();
      long duration = endTime - startTime;

      assertEquals(100_000, list.size());
      // Verify first and last elements
      assertEquals("element99999", list.get(0));
      assertEquals("element0", list.get(99999));

      // This should complete very quickly (O(1) per operation)
      // If it takes more than a second, something is wrong
      assertTrue(duration < 1_000_000_000L, "addFirst should be O(1), took too long");
    }

    @Test
    @DisplayName("AddLast is O(1)")
    void addLast_isO1() {
      for (int i = 0; i < 100_000; i++) {
        list.addLast("element" + i);
      }

      assertEquals(100_000, list.size());
      assertEquals("element0", list.get(0));
      assertEquals("element99999", list.get(99999));
    }
  }
}
