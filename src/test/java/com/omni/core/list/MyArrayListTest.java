package com.omni.core.list;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("MyArrayList Tests")
class MyArrayListTest {

  private MyArrayList<String> list;

  @BeforeEach
  void setUp() {
    list = new MyArrayList<>();
  }

  @Nested
  @DisplayName("Constructor Tests")
  class ConstructorTests {

    @Test
    @DisplayName("Default constructor creates empty list")
    void defaultConstructor_createsEmptyList() {
      MyArrayList<Integer> emptyList = new MyArrayList<>();
      assertTrue(emptyList.isEmpty());
      assertEquals(0, emptyList.size());
    }

    @Test
    @DisplayName("Constructor with initial capacity creates empty list")
    void constructorWithCapacity_createsEmptyList() {
      MyArrayList<Integer> list = new MyArrayList<>(20);
      assertTrue(list.isEmpty());
      assertEquals(0, list.size());
    }

    @Test
    @DisplayName("Constructor with negative capacity throws exception")
    void constructorWithNegativeCapacity_throwsException() {
      assertThrows(IllegalArgumentException.class, () -> new MyArrayList<>(-1));
    }
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
    @DisplayName("Add multiple elements")
    void add_multipleElements_addsInOrder() {
      list.add("first");
      list.add("second");
      list.add("third");

      assertEquals(3, list.size());
      assertEquals("first", list.get(0));
      assertEquals("second", list.get(1));
      assertEquals("third", list.get(2));
    }

    @Test
    @DisplayName("Add element at specific index")
    void addAtIndex_insertsElement() {
      list.add("first");
      list.add("third");
      list.add(1, "second");

      assertEquals(3, list.size());
      assertEquals("first", list.get(0));
      assertEquals("second", list.get(1));
      assertEquals("third", list.get(2));
    }

    @Test
    @DisplayName("Add at index 0 inserts at beginning")
    void addAtIndexZero_insertsAtBeginning() {
      list.add("second");
      list.add(0, "first");

      assertEquals(2, list.size());
      assertEquals("first", list.get(0));
      assertEquals("second", list.get(1));
    }

    @Test
    @DisplayName("Add at end using index")
    void addAtEndUsingIndex_appendsElement() {
      list.add("first");
      list.add(1, "second");

      assertEquals(2, list.size());
      assertEquals("first", list.get(0));
      assertEquals("second", list.get(1));
    }

    @Test
    @DisplayName("Get element by index")
    void get_returnsElementAtIndex() {
      list.add("first");
      list.add("second");
      list.add("third");

      assertEquals("first", list.get(0));
      assertEquals("second", list.get(1));
      assertEquals("third", list.get(2));
    }

    @Test
    @DisplayName("Remove element by index")
    void remove_removesAndReturnsElement() {
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
    @DisplayName("Remove first element")
    void removeFirst_shiftsElementsLeft() {
      list.add("first");
      list.add("second");
      list.add("third");

      list.remove(0);

      assertEquals(2, list.size());
      assertEquals("second", list.get(0));
      assertEquals("third", list.get(1));
    }

    @Test
    @DisplayName("Remove last element")
    void removeLast_decrementsSize() {
      list.add("first");
      list.add("second");

      list.remove(1);

      assertEquals(1, list.size());
      assertEquals("first", list.get(0));
    }

    @Test
    @DisplayName("Clear removes all elements")
    void clear_removesAllElements() {
      list.add("first");
      list.add("second");
      list.add("third");

      list.clear();

      assertTrue(list.isEmpty());
      assertEquals(0, list.size());
    }

    @Test
    @DisplayName("Size returns correct count")
    void size_returnsCorrectCount() {
      assertEquals(0, list.size());

      list.add("first");
      assertEquals(1, list.size());

      list.add("second");
      assertEquals(2, list.size());

      list.remove(0);
      assertEquals(1, list.size());
    }

    @Test
    @DisplayName("IsEmpty returns true for empty list")
    void isEmpty_returnsTrueForEmptyList() {
      assertTrue(list.isEmpty());

      list.add("first");
      assertFalse(list.isEmpty());

      list.remove(0);
      assertTrue(list.isEmpty());
    }

    @Test
    @DisplayName("Contains returns true for existing element")
    void contains_returnsTrueForExistingElement() {
      list.add("first");
      list.add("second");

      assertTrue(list.contains("first"));
      assertTrue(list.contains("second"));
      assertFalse(list.contains("third"));
    }

    @Test
    @DisplayName("Contains handles null elements")
    void contains_handlesNullElements() {
      list.add("first");
      list.add(null);
      list.add("third");

      assertTrue(list.contains(null));
      assertTrue(list.contains("first"));
    }
  }

  @Nested
  @DisplayName("Edge Cases")
  class EdgeCaseTests {

    @Test
    @DisplayName("Get with invalid negative index throws exception")
    void get_negativeIndex_throwsException() {
      list.add("first");
      assertThrows(IndexOutOfBoundsException.class, () -> list.get(-1));
    }

    @Test
    @DisplayName("Get with index >= size throws exception")
    void get_indexTooLarge_throwsException() {
      list.add("first");
      assertThrows(IndexOutOfBoundsException.class, () -> list.get(1));
      assertThrows(IndexOutOfBoundsException.class, () -> list.get(10));
    }

    @Test
    @DisplayName("Add with invalid negative index throws exception")
    void add_negativeIndex_throwsException() {
      list.add("first");
      assertThrows(IndexOutOfBoundsException.class, () -> list.add(-1, "element"));
    }

    @Test
    @DisplayName("Add with index > size throws exception")
    void add_indexTooLarge_throwsException() {
      list.add("first");
      assertThrows(IndexOutOfBoundsException.class, () -> list.add(3, "element"));
    }

    @Test
    @DisplayName("Remove with invalid index throws exception")
    void remove_invalidIndex_throwsException() {
      assertThrows(IndexOutOfBoundsException.class, () -> list.remove(0));
      list.add("first");
      assertThrows(IndexOutOfBoundsException.class, () -> list.remove(-1));
      assertThrows(IndexOutOfBoundsException.class, () -> list.remove(1));
    }

    @Test
    @DisplayName("Single element list operations work correctly")
    void singleElementList_operationsWork() {
      list.add("only");
      assertEquals(1, list.size());
      assertEquals("only", list.get(0));
      assertEquals("only", list.remove(0));
      assertTrue(list.isEmpty());
    }
  }

  @Nested
  @DisplayName("Dynamic Resizing")
  class ResizingTests {

    @Test
    @DisplayName("List resizes when capacity exceeded")
    void add_whenCapacityExceeded_resizesList() {
      MyArrayList<Integer> smallList = new MyArrayList<>(2);
      smallList.add(1);
      smallList.add(2);
      smallList.add(3); // Should trigger resize

      assertEquals(3, smallList.size());
      assertEquals(1, smallList.get(0));
      assertEquals(2, smallList.get(1));
      assertEquals(3, smallList.get(2));
    }

    @Test
    @DisplayName("List can grow to large size")
    void add_largeNumberOfElements_growsSuccessfully() {
      MyArrayList<Integer> largeList = new MyArrayList<>();
      int count = 1000;

      for (int i = 0; i < count; i++) {
        largeList.add(i);
      }

      assertEquals(count, largeList.size());
      assertEquals(0, largeList.get(0));
      assertEquals(count - 1, largeList.get(count - 1));
    }

    @Test
    @DisplayName("Mastery check: 1 million integers should work")
    void masteryCheck_oneMillionIntegers_worksCorrectly() {
      MyArrayList<Integer> massiveList = new MyArrayList<>();
      int count = 1_000_000;

      for (int i = 0; i < count; i++) {
        massiveList.add(i);
      }

      assertEquals(count, massiveList.size());
      // Verify first, middle, and last elements
      assertEquals(0, massiveList.get(0));
      assertEquals(500_000, massiveList.get(500_000));
      assertEquals(count - 1, massiveList.get(count - 1));
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
      assertTrue(it.hasNext());
      assertEquals("second", it.next());
      assertTrue(it.hasNext());
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
    @DisplayName("Iterator on empty list has no elements")
    void iterator_emptyList_hasNoElements() {
      Iterator<String> it = list.iterator();
      assertFalse(it.hasNext());
      assertThrows(NoSuchElementException.class, () -> it.next());
    }

    @Test
    @DisplayName("Iterator remove removes element")
    void iteratorRemove_removesElement() {
      list.add("first");
      list.add("second");
      list.add("third");

      Iterator<String> it = list.iterator();
      it.next(); // Skip first
      it.next(); // At second
      it.remove(); // Remove second

      assertEquals(2, list.size());
      assertEquals("first", list.get(0));
      assertEquals("third", list.get(1));
    }

    @Test
    @DisplayName("Iterator remove without next throws exception")
    void iteratorRemove_withoutNext_throwsException() {
      list.add("first");
      Iterator<String> it = list.iterator();

      assertThrows(IllegalStateException.class, () -> it.remove());
    }

    @Test
    @DisplayName("Iterator fail-fast on concurrent modification")
    void iterator_concurrentModification_throwsException() {
      list.add("first");
      list.add("second");
      list.add("third");

      Iterator<String> it = list.iterator();
      it.next();

      // Modify list while iterating
      list.add("fourth");

      assertThrows(ConcurrentModificationException.class, () -> it.next());
    }

    @Test
    @DisplayName("Iterator fail-fast on remove during iteration")
    void iterator_listModifiedDuringIteration_throwsException() {
      list.add("first");
      list.add("second");

      Iterator<String> it = list.iterator();
      it.next();

      list.remove(0); // Modify list

      assertThrows(ConcurrentModificationException.class, () -> it.next());
    }
  }
}
