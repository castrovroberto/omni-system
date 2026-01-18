package com.omni.core.list;

import static org.junit.jupiter.api.Assertions.*;

import java.util.NoSuchElementException;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Parameterized tests that run against both MyArrayList and MyLinkedList implementations.
 *
 * <p>This reduces test duplication by testing the MyList interface contract with both
 * implementations using the same test cases.
 */
@DisplayName("MyList Parameterized Tests")
class MyListParameterizedTest {

  static Stream<MyList<String>> stringListImplementations() {
    return Stream.of(new MyArrayList<>(), new MyLinkedList<>());
  }

  static Stream<MyList<Integer>> integerListImplementations() {
    return Stream.of(new MyArrayList<>(), new MyLinkedList<>());
  }

  @Nested
  @DisplayName("Basic Operations")
  class BasicOperationsTests {

    @ParameterizedTest(name = "{0}")
    @MethodSource(
        "com.omni.core.list.MyListParameterizedTest#stringListImplementations")
    @DisplayName("Add element to empty list")
    void add_toEmptyList_addsElement(MyList<String> list) {
      list.add("first");
      assertEquals(1, list.size());
      assertEquals("first", list.get(0));
      assertFalse(list.isEmpty());
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource(
        "com.omni.core.list.MyListParameterizedTest#stringListImplementations")
    @DisplayName("Add multiple elements maintains order")
    void add_multipleElements_maintainsOrder(MyList<String> list) {
      list.add("first");
      list.add("second");
      list.add("third");

      assertEquals(3, list.size());
      assertEquals("first", list.get(0));
      assertEquals("second", list.get(1));
      assertEquals("third", list.get(2));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource(
        "com.omni.core.list.MyListParameterizedTest#stringListImplementations")
    @DisplayName("Add at index inserts element correctly")
    void addAtIndex_insertsElement(MyList<String> list) {
      list.add("first");
      list.add("third");
      list.add(1, "second");

      assertEquals(3, list.size());
      assertEquals("first", list.get(0));
      assertEquals("second", list.get(1));
      assertEquals("third", list.get(2));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource(
        "com.omni.core.list.MyListParameterizedTest#stringListImplementations")
    @DisplayName("Remove by index removes and returns element")
    void remove_removesAndReturnsElement(MyList<String> list) {
      list.add("first");
      list.add("second");
      list.add("third");

      String removed = list.remove(1);

      assertEquals("second", removed);
      assertEquals(2, list.size());
      assertEquals("first", list.get(0));
      assertEquals("third", list.get(1));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource(
        "com.omni.core.list.MyListParameterizedTest#stringListImplementations")
    @DisplayName("Clear removes all elements")
    void clear_removesAllElements(MyList<String> list) {
      list.add("first");
      list.add("second");

      list.clear();

      assertTrue(list.isEmpty());
      assertEquals(0, list.size());
    }
  }

  @Nested
  @DisplayName("New Interface Methods")
  class NewInterfaceMethodsTests {

    @ParameterizedTest(name = "{0}")
    @MethodSource(
        "com.omni.core.list.MyListParameterizedTest#stringListImplementations")
    @DisplayName("Contains returns true for existing element")
    void contains_existingElement_returnsTrue(MyList<String> list) {
      list.add("first");
      list.add("second");
      list.add("third");

      assertTrue(list.contains("first"));
      assertTrue(list.contains("second"));
      assertTrue(list.contains("third"));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource(
        "com.omni.core.list.MyListParameterizedTest#stringListImplementations")
    @DisplayName("Contains returns false for missing element")
    void contains_missingElement_returnsFalse(MyList<String> list) {
      list.add("first");
      list.add("second");

      assertFalse(list.contains("third"));
      assertFalse(list.contains("missing"));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource(
        "com.omni.core.list.MyListParameterizedTest#stringListImplementations")
    @DisplayName("Contains handles null elements")
    void contains_nullElement_works(MyList<String> list) {
      list.add("first");
      list.add(null);
      list.add("third");

      assertTrue(list.contains(null));
      assertTrue(list.contains("first"));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource(
        "com.omni.core.list.MyListParameterizedTest#stringListImplementations")
    @DisplayName("IndexOf returns correct index")
    void indexOf_existingElement_returnsIndex(MyList<String> list) {
      list.add("first");
      list.add("second");
      list.add("third");

      assertEquals(0, list.indexOf("first"));
      assertEquals(1, list.indexOf("second"));
      assertEquals(2, list.indexOf("third"));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource(
        "com.omni.core.list.MyListParameterizedTest#stringListImplementations")
    @DisplayName("IndexOf returns -1 for missing element")
    void indexOf_missingElement_returnsMinusOne(MyList<String> list) {
      list.add("first");
      list.add("second");

      assertEquals(-1, list.indexOf("third"));
      assertEquals(-1, list.indexOf("missing"));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource(
        "com.omni.core.list.MyListParameterizedTest#stringListImplementations")
    @DisplayName("IndexOf returns first occurrence for duplicates")
    void indexOf_duplicates_returnsFirstIndex(MyList<String> list) {
      list.add("first");
      list.add("second");
      list.add("first"); // duplicate

      assertEquals(0, list.indexOf("first"));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource(
        "com.omni.core.list.MyListParameterizedTest#stringListImplementations")
    @DisplayName("Set replaces element and returns old value")
    void set_replacesElement_returnsOldValue(MyList<String> list) {
      list.add("first");
      list.add("second");
      list.add("third");

      String oldValue = list.set(1, "new-second");

      assertEquals("second", oldValue);
      assertEquals("new-second", list.get(1));
      assertEquals(3, list.size());
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource(
        "com.omni.core.list.MyListParameterizedTest#stringListImplementations")
    @DisplayName("Set throws exception for invalid index")
    void set_invalidIndex_throwsException(MyList<String> list) {
      list.add("first");

      assertThrows(IndexOutOfBoundsException.class, () -> list.set(-1, "value"));
      assertThrows(IndexOutOfBoundsException.class, () -> list.set(1, "value"));
      assertThrows(IndexOutOfBoundsException.class, () -> list.set(10, "value"));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource(
        "com.omni.core.list.MyListParameterizedTest#stringListImplementations")
    @DisplayName("ToArray returns correct array")
    void toArray_returnsCorrectArray(MyList<String> list) {
      list.add("first");
      list.add("second");
      list.add("third");

      Object[] array = list.toArray();

      assertEquals(3, array.length);
      assertEquals("first", array[0]);
      assertEquals("second", array[1]);
      assertEquals("third", array[2]);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource(
        "com.omni.core.list.MyListParameterizedTest#stringListImplementations")
    @DisplayName("ToArray on empty list returns empty array")
    void toArray_emptyList_returnsEmptyArray(MyList<String> list) {
      Object[] array = list.toArray();

      assertEquals(0, array.length);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource(
        "com.omni.core.list.MyListParameterizedTest#stringListImplementations")
    @DisplayName("ToArray returns independent copy")
    void toArray_returnsIndependentCopy(MyList<String> list) {
      list.add("first");
      list.add("second");

      Object[] array = list.toArray();
      array[0] = "modified";

      // Original list should be unchanged
      assertEquals("first", list.get(0));
    }
  }

  @Nested
  @DisplayName("Edge Cases")
  class EdgeCaseTests {

    @ParameterizedTest(name = "{0}")
    @MethodSource(
        "com.omni.core.list.MyListParameterizedTest#stringListImplementations")
    @DisplayName("Get with invalid index throws exception")
    void get_invalidIndex_throwsException(MyList<String> list) {
      list.add("first");

      assertThrows(IndexOutOfBoundsException.class, () -> list.get(-1));
      assertThrows(IndexOutOfBoundsException.class, () -> list.get(1));
      assertThrows(IndexOutOfBoundsException.class, () -> list.get(10));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource(
        "com.omni.core.list.MyListParameterizedTest#stringListImplementations")
    @DisplayName("Remove with invalid index throws exception")
    void remove_invalidIndex_throwsException(MyList<String> list) {
      assertThrows(IndexOutOfBoundsException.class, () -> list.remove(0));
      list.add("first");
      assertThrows(IndexOutOfBoundsException.class, () -> list.remove(-1));
      assertThrows(IndexOutOfBoundsException.class, () -> list.remove(1));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource(
        "com.omni.core.list.MyListParameterizedTest#stringListImplementations")
    @DisplayName("Single element list operations work")
    void singleElementList_operationsWork(MyList<String> list) {
      list.add("only");

      assertEquals(1, list.size());
      assertEquals("only", list.get(0));
      assertTrue(list.contains("only"));
      assertEquals(0, list.indexOf("only"));

      String removed = list.remove(0);
      assertEquals("only", removed);
      assertTrue(list.isEmpty());
    }
  }

  @Nested
  @DisplayName("Performance Characteristics")
  class PerformanceTests {

    @ParameterizedTest(name = "{0}")
    @MethodSource(
        "com.omni.core.list.MyListParameterizedTest#integerListImplementations")
    @DisplayName("Large list operations work correctly")
    void largeList_operationsWork(MyList<Integer> list) {
      int count = 10_000;

      // Add elements
      for (int i = 0; i < count; i++) {
        list.add(i);
      }

      assertEquals(count, list.size());

      // Verify first, middle, and last
      assertEquals(0, list.get(0));
      assertEquals(count / 2, list.get(count / 2));
      assertEquals(count - 1, list.get(count - 1));

      // Verify contains
      assertTrue(list.contains(0));
      assertTrue(list.contains(count / 2));
      assertTrue(list.contains(count - 1));
      assertFalse(list.contains(count)); // Out of range

      // Verify indexOf
      assertEquals(0, list.indexOf(0));
      assertEquals(count / 2, list.indexOf(count / 2));

      // Verify set
      list.set(0, -1);
      assertEquals(-1, list.get(0));

      // Verify toArray
      Object[] array = list.toArray();
      assertEquals(count, array.length);
    }
  }

  @Nested
  @DisplayName("LinkedList-Specific Methods")
  class LinkedListSpecificTests {

    @org.junit.jupiter.api.Test
    @DisplayName("getFirst returns first element")
    void getFirst_returnsFirstElement() {
      MyLinkedList<String> list = new MyLinkedList<>();
      list.add("first");
      list.add("second");
      list.add("third");

      assertEquals("first", list.getFirst());
    }

    @org.junit.jupiter.api.Test
    @DisplayName("getLast returns last element")
    void getLast_returnsLastElement() {
      MyLinkedList<String> list = new MyLinkedList<>();
      list.add("first");
      list.add("second");
      list.add("third");

      assertEquals("third", list.getLast());
    }

    @org.junit.jupiter.api.Test
    @DisplayName("getFirst on empty list throws exception")
    void getFirst_emptyList_throwsException() {
      MyLinkedList<String> list = new MyLinkedList<>();

      assertThrows(NoSuchElementException.class, list::getFirst);
    }

    @org.junit.jupiter.api.Test
    @DisplayName("getLast on empty list throws exception")
    void getLast_emptyList_throwsException() {
      MyLinkedList<String> list = new MyLinkedList<>();

      assertThrows(NoSuchElementException.class, list::getLast);
    }

    @org.junit.jupiter.api.Test
    @DisplayName("getFirst and getLast work with single element")
    void getFirstAndLast_singleElement_returnsSameElement() {
      MyLinkedList<String> list = new MyLinkedList<>();
      list.add("only");

      assertEquals("only", list.getFirst());
      assertEquals("only", list.getLast());
    }
  }
}
