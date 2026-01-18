package com.omni.core.algorithm.challenge;

import static org.junit.jupiter.api.Assertions.*;

import com.omni.core.list.MyArrayList;
import com.omni.core.list.MyLinkedList;
import com.omni.core.list.MyList;
import java.util.Comparator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("List Challenges Tests")
class ListChallengesTest {

  // ==================== EASY CHALLENGES ====================

  @Nested
  @DisplayName("Reverse List Tests (LeetCode #206)")
  class ReverseListTests {

    @Nested
    @DisplayName("ArrayList Implementation")
    class ArrayListTests {

      @Test
      @DisplayName("Reverses a list with multiple elements")
      void reverse_multipleElements_reversesCorrectly() {
        MyList<Integer> list = new MyArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);

        ListChallenges.reverse(list);

        assertEquals(5, list.get(0));
        assertEquals(4, list.get(1));
        assertEquals(3, list.get(2));
        assertEquals(2, list.get(3));
        assertEquals(1, list.get(4));
      }

      @Test
      @DisplayName("Reverses a single element list")
      void reverse_singleElement_unchanged() {
        MyList<Integer> list = new MyArrayList<>();
        list.add(42);

        ListChallenges.reverse(list);

        assertEquals(1, list.size());
        assertEquals(42, list.get(0));
      }

      @Test
      @DisplayName("Reverses an empty list")
      void reverse_emptyList_unchanged() {
        MyList<Integer> list = new MyArrayList<>();

        ListChallenges.reverse(list);

        assertTrue(list.isEmpty());
      }

      @Test
      @DisplayName("Throws exception for null list")
      void reverse_nullList_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> ListChallenges.reverse(null));
      }

      @Test
      @DisplayName("Reverses a two element list")
      void reverse_twoElements_swaps() {
        MyList<String> list = new MyArrayList<>();
        list.add("A");
        list.add("B");

        ListChallenges.reverse(list);

        assertEquals("B", list.get(0));
        assertEquals("A", list.get(1));
      }
    }

    @Nested
    @DisplayName("LinkedList Implementation")
    class LinkedListTests {

      @Test
      @DisplayName("Reverses a linked list with multiple elements")
      void reverse_multipleElements_reversesCorrectly() {
        MyList<Integer> list = new MyLinkedList<>();
        list.add(1);
        list.add(2);
        list.add(3);

        ListChallenges.reverse(list);

        assertEquals(3, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(1, list.get(2));
      }
    }
  }

  @Nested
  @DisplayName("Find Middle Tests (LeetCode #876)")
  class FindMiddleTests {

    @Nested
    @DisplayName("ArrayList Implementation")
    class ArrayListTests {

      @Test
      @DisplayName("Finds middle of odd-length list")
      void findMiddle_oddLength_returnsMiddle() {
        MyList<Integer> list = new MyArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);

        Integer middle = ListChallenges.findMiddle(list);

        assertEquals(3, middle);
      }

      @Test
      @DisplayName("Finds middle of even-length list (returns second middle)")
      void findMiddle_evenLength_returnsSecondMiddle() {
        MyList<Integer> list = new MyArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);

        Integer middle = ListChallenges.findMiddle(list);

        assertEquals(3, middle);
      }

      @Test
      @DisplayName("Returns single element for single-element list")
      void findMiddle_singleElement_returnsThatElement() {
        MyList<Integer> list = new MyArrayList<>();
        list.add(42);

        Integer middle = ListChallenges.findMiddle(list);

        assertEquals(42, middle);
      }

      @Test
      @DisplayName("Returns null for empty list")
      void findMiddle_emptyList_returnsNull() {
        MyList<Integer> list = new MyArrayList<>();

        Integer middle = ListChallenges.findMiddle(list);

        assertNull(middle);
      }

      @Test
      @DisplayName("Throws exception for null list")
      void findMiddle_nullList_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> ListChallenges.findMiddle(null));
      }
    }

    @Nested
    @DisplayName("LinkedList Implementation")
    class LinkedListTests {

      @Test
      @DisplayName("Finds middle of linked list")
      void findMiddle_linkedList_returnsMiddle() {
        MyList<String> list = new MyLinkedList<>();
        list.add("A");
        list.add("B");
        list.add("C");

        String middle = ListChallenges.findMiddle(list);

        assertEquals("B", middle);
      }
    }
  }

  @Nested
  @DisplayName("Remove Duplicates Tests (LeetCode #83)")
  class RemoveDuplicatesTests {

    @Nested
    @DisplayName("ArrayList Implementation")
    class ArrayListTests {

      @Test
      @DisplayName("Removes consecutive duplicates from sorted list")
      void removeDuplicates_sortedListWithDuplicates_removesAll() {
        MyList<Integer> list = new MyArrayList<>();
        list.add(1);
        list.add(1);
        list.add(2);
        list.add(2);
        list.add(2);
        list.add(3);

        ListChallenges.removeDuplicates(list);

        assertEquals(3, list.size());
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(3, list.get(2));
      }

      @Test
      @DisplayName("Handles list with no duplicates")
      void removeDuplicates_noDuplicates_unchanged() {
        MyList<Integer> list = new MyArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);

        ListChallenges.removeDuplicates(list);

        assertEquals(3, list.size());
      }

      @Test
      @DisplayName("Handles single element list")
      void removeDuplicates_singleElement_unchanged() {
        MyList<Integer> list = new MyArrayList<>();
        list.add(1);

        ListChallenges.removeDuplicates(list);

        assertEquals(1, list.size());
        assertEquals(1, list.get(0));
      }

      @Test
      @DisplayName("Handles empty list")
      void removeDuplicates_emptyList_unchanged() {
        MyList<Integer> list = new MyArrayList<>();

        ListChallenges.removeDuplicates(list);

        assertTrue(list.isEmpty());
      }

      @Test
      @DisplayName("Handles all same elements")
      void removeDuplicates_allSame_leavesOne() {
        MyList<Integer> list = new MyArrayList<>();
        list.add(5);
        list.add(5);
        list.add(5);
        list.add(5);

        ListChallenges.removeDuplicates(list);

        assertEquals(1, list.size());
        assertEquals(5, list.get(0));
      }

      @Test
      @DisplayName("Throws exception for null list")
      void removeDuplicates_nullList_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> ListChallenges.removeDuplicates(null));
      }
    }

    @Nested
    @DisplayName("LinkedList Implementation")
    class LinkedListTests {

      @Test
      @DisplayName("Removes duplicates from linked list")
      void removeDuplicates_linkedList_works() {
        MyList<Integer> list = new MyLinkedList<>();
        list.add(1);
        list.add(1);
        list.add(2);

        ListChallenges.removeDuplicates(list);

        assertEquals(2, list.size());
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
      }
    }
  }

  @Nested
  @DisplayName("Merge Sorted Lists Tests (LeetCode #21)")
  class MergeSortedListsTests {

    @Nested
    @DisplayName("ArrayList Implementation")
    class ArrayListTests {

      @Test
      @DisplayName("Merges two sorted lists")
      void mergeSortedLists_twoSortedLists_mergesCorrectly() {
        MyList<Integer> list1 = new MyArrayList<>();
        list1.add(1);
        list1.add(3);
        list1.add(5);

        MyList<Integer> list2 = new MyArrayList<>();
        list2.add(2);
        list2.add(4);
        list2.add(6);

        MyList<Integer> merged = ListChallenges.mergeSortedLists(list1, list2, null);

        assertEquals(6, merged.size());
        assertEquals(1, merged.get(0));
        assertEquals(2, merged.get(1));
        assertEquals(3, merged.get(2));
        assertEquals(4, merged.get(3));
        assertEquals(5, merged.get(4));
        assertEquals(6, merged.get(5));
      }

      @Test
      @DisplayName("Merges when one list is empty")
      void mergeSortedLists_oneEmpty_returnsOther() {
        MyList<Integer> list1 = new MyArrayList<>();
        list1.add(1);
        list1.add(2);

        MyList<Integer> list2 = new MyArrayList<>();

        MyList<Integer> merged = ListChallenges.mergeSortedLists(list1, list2, null);

        assertEquals(2, merged.size());
        assertEquals(1, merged.get(0));
        assertEquals(2, merged.get(1));
      }

      @Test
      @DisplayName("Merges two empty lists")
      void mergeSortedLists_bothEmpty_returnsEmpty() {
        MyList<Integer> list1 = new MyArrayList<>();
        MyList<Integer> list2 = new MyArrayList<>();

        MyList<Integer> merged = ListChallenges.mergeSortedLists(list1, list2, null);

        assertTrue(merged.isEmpty());
      }

      @Test
      @DisplayName("Merges with custom comparator")
      void mergeSortedLists_withComparator_works() {
        MyList<Integer> list1 = new MyArrayList<>();
        list1.add(5);
        list1.add(3);
        list1.add(1);

        MyList<Integer> list2 = new MyArrayList<>();
        list2.add(6);
        list2.add(4);
        list2.add(2);

        Comparator<Integer> reverseOrder = Comparator.reverseOrder();
        MyList<Integer> merged = ListChallenges.mergeSortedLists(list1, list2, reverseOrder);

        assertEquals(6, merged.size());
        assertEquals(6, merged.get(0));
        assertEquals(5, merged.get(1));
      }

      @Test
      @DisplayName("Throws exception for null lists")
      void mergeSortedLists_nullList_throwsException() {
        MyList<Integer> list = new MyArrayList<>();
        assertThrows(
            IllegalArgumentException.class,
            () -> ListChallenges.mergeSortedLists(null, list, null));
        assertThrows(
            IllegalArgumentException.class,
            () -> ListChallenges.mergeSortedLists(list, null, null));
      }
    }

    @Nested
    @DisplayName("LinkedList Implementation")
    class LinkedListTests {

      @Test
      @DisplayName("Merges linked lists")
      void mergeSortedLists_linkedLists_works() {
        MyList<Integer> list1 = new MyLinkedList<>();
        list1.add(1);
        list1.add(3);

        MyList<Integer> list2 = new MyLinkedList<>();
        list2.add(2);

        MyList<Integer> merged = ListChallenges.mergeSortedLists(list1, list2, null);

        assertEquals(3, merged.size());
        assertEquals(1, merged.get(0));
        assertEquals(2, merged.get(1));
        assertEquals(3, merged.get(2));
      }
    }
  }

  @Nested
  @DisplayName("Is Palindrome Tests (LeetCode #234)")
  class IsPalindromeTests {

    @Nested
    @DisplayName("ArrayList Implementation")
    class ArrayListTests {

      @Test
      @DisplayName("Detects palindrome with odd length")
      void isPalindrome_oddLengthPalindrome_returnsTrue() {
        MyList<Integer> list = new MyArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(2);
        list.add(1);

        assertTrue(ListChallenges.isPalindrome(list));
      }

      @Test
      @DisplayName("Detects palindrome with even length")
      void isPalindrome_evenLengthPalindrome_returnsTrue() {
        MyList<Integer> list = new MyArrayList<>();
        list.add(1);
        list.add(2);
        list.add(2);
        list.add(1);

        assertTrue(ListChallenges.isPalindrome(list));
      }

      @Test
      @DisplayName("Detects non-palindrome")
      void isPalindrome_notPalindrome_returnsFalse() {
        MyList<Integer> list = new MyArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);

        assertFalse(ListChallenges.isPalindrome(list));
      }

      @Test
      @DisplayName("Single element is palindrome")
      void isPalindrome_singleElement_returnsTrue() {
        MyList<Integer> list = new MyArrayList<>();
        list.add(1);

        assertTrue(ListChallenges.isPalindrome(list));
      }

      @Test
      @DisplayName("Empty list is palindrome")
      void isPalindrome_emptyList_returnsTrue() {
        MyList<Integer> list = new MyArrayList<>();

        assertTrue(ListChallenges.isPalindrome(list));
      }

      @Test
      @DisplayName("Throws exception for null list")
      void isPalindrome_nullList_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> ListChallenges.isPalindrome(null));
      }
    }

    @Nested
    @DisplayName("LinkedList Implementation")
    class LinkedListTests {

      @Test
      @DisplayName("Works with linked list")
      void isPalindrome_linkedList_works() {
        MyList<String> list = new MyLinkedList<>();
        list.add("A");
        list.add("B");
        list.add("A");

        assertTrue(ListChallenges.isPalindrome(list));
      }
    }
  }

  @Nested
  @DisplayName("Get Nth From End Tests")
  class GetNthFromEndTests {

    @Nested
    @DisplayName("ArrayList Implementation")
    class ArrayListTests {

      @Test
      @DisplayName("Gets last element (n=1)")
      void getNthFromEnd_last_returnsLastElement() {
        MyList<Integer> list = new MyArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);

        Integer result = ListChallenges.getNthFromEnd(list, 1);

        assertEquals(5, result);
      }

      @Test
      @DisplayName("Gets second from end (n=2)")
      void getNthFromEnd_secondFromEnd_returnsCorrect() {
        MyList<Integer> list = new MyArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);

        Integer result = ListChallenges.getNthFromEnd(list, 2);

        assertEquals(4, result);
      }

      @Test
      @DisplayName("Gets first element when n equals size")
      void getNthFromEnd_nEqualsSize_returnsFirst() {
        MyList<Integer> list = new MyArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);

        Integer result = ListChallenges.getNthFromEnd(list, 3);

        assertEquals(1, result);
      }

      @Test
      @DisplayName("Throws exception for n=0")
      void getNthFromEnd_nZero_throwsException() {
        MyList<Integer> list = new MyArrayList<>();
        list.add(1);

        assertThrows(IllegalArgumentException.class, () -> ListChallenges.getNthFromEnd(list, 0));
      }

      @Test
      @DisplayName("Throws exception for n greater than size")
      void getNthFromEnd_nTooLarge_throwsException() {
        MyList<Integer> list = new MyArrayList<>();
        list.add(1);
        list.add(2);

        assertThrows(IllegalArgumentException.class, () -> ListChallenges.getNthFromEnd(list, 3));
      }

      @Test
      @DisplayName("Throws exception for null list")
      void getNthFromEnd_nullList_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> ListChallenges.getNthFromEnd(null, 1));
      }
    }

    @Nested
    @DisplayName("LinkedList Implementation")
    class LinkedListTests {

      @Test
      @DisplayName("Works with linked list")
      void getNthFromEnd_linkedList_works() {
        MyList<String> list = new MyLinkedList<>();
        list.add("A");
        list.add("B");
        list.add("C");

        String result = ListChallenges.getNthFromEnd(list, 2);

        assertEquals("B", result);
      }
    }
  }

  // ==================== MEDIUM CHALLENGES ====================

  @Nested
  @DisplayName("Remove Nth From End Tests (LeetCode #19)")
  class RemoveNthFromEndTests {

    @Nested
    @DisplayName("ArrayList Implementation")
    class ArrayListTests {

      @Test
      @DisplayName("Removes last element (n=1)")
      void removeNthFromEnd_last_removesCorrectly() {
        MyList<Integer> list = new MyArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);

        Integer removed = ListChallenges.removeNthFromEnd(list, 1);

        assertEquals(5, removed);
        assertEquals(4, list.size());
        assertEquals(4, list.get(3));
      }

      @Test
      @DisplayName("Removes second from end (n=2)")
      void removeNthFromEnd_secondFromEnd_removesCorrectly() {
        MyList<Integer> list = new MyArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);

        Integer removed = ListChallenges.removeNthFromEnd(list, 2);

        assertEquals(4, removed);
        assertEquals(4, list.size());
        assertEquals(5, list.get(3));
      }

      @Test
      @DisplayName("Removes first element when n equals size")
      void removeNthFromEnd_nEqualsSize_removesFirst() {
        MyList<Integer> list = new MyArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);

        Integer removed = ListChallenges.removeNthFromEnd(list, 3);

        assertEquals(1, removed);
        assertEquals(2, list.size());
        assertEquals(2, list.get(0));
      }

      @Test
      @DisplayName("Removes only element")
      void removeNthFromEnd_singleElement_removesIt() {
        MyList<Integer> list = new MyArrayList<>();
        list.add(42);

        Integer removed = ListChallenges.removeNthFromEnd(list, 1);

        assertEquals(42, removed);
        assertTrue(list.isEmpty());
      }

      @Test
      @DisplayName("Throws exception for invalid n")
      void removeNthFromEnd_invalidN_throwsException() {
        MyList<Integer> list = new MyArrayList<>();
        list.add(1);

        assertThrows(
            IllegalArgumentException.class, () -> ListChallenges.removeNthFromEnd(list, 0));
        assertThrows(
            IllegalArgumentException.class, () -> ListChallenges.removeNthFromEnd(list, 2));
      }

      @Test
      @DisplayName("Throws exception for null list")
      void removeNthFromEnd_nullList_throwsException() {
        assertThrows(
            IllegalArgumentException.class, () -> ListChallenges.removeNthFromEnd(null, 1));
      }
    }

    @Nested
    @DisplayName("LinkedList Implementation")
    class LinkedListTests {

      @Test
      @DisplayName("Works with linked list")
      void removeNthFromEnd_linkedList_works() {
        MyList<String> list = new MyLinkedList<>();
        list.add("A");
        list.add("B");
        list.add("C");

        String removed = ListChallenges.removeNthFromEnd(list, 2);

        assertEquals("B", removed);
        assertEquals(2, list.size());
      }
    }
  }

  @Nested
  @DisplayName("Rotate Right Tests (LeetCode #61)")
  class RotateRightTests {

    @Nested
    @DisplayName("ArrayList Implementation")
    class ArrayListTests {

      @Test
      @DisplayName("Rotates list by k=2")
      void rotateRight_byTwo_rotatesCorrectly() {
        MyList<Integer> list = new MyArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);

        ListChallenges.rotateRight(list, 2);

        assertEquals(4, list.get(0));
        assertEquals(5, list.get(1));
        assertEquals(1, list.get(2));
        assertEquals(2, list.get(3));
        assertEquals(3, list.get(4));
      }

      @Test
      @DisplayName("Rotates by k greater than size")
      void rotateRight_kGreaterThanSize_wrapsAround() {
        MyList<Integer> list = new MyArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);

        ListChallenges.rotateRight(list, 4); // Same as rotating by 1

        assertEquals(3, list.get(0));
        assertEquals(1, list.get(1));
        assertEquals(2, list.get(2));
      }

      @Test
      @DisplayName("Rotation by 0 leaves list unchanged")
      void rotateRight_byZero_unchanged() {
        MyList<Integer> list = new MyArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);

        ListChallenges.rotateRight(list, 0);

        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(3, list.get(2));
      }

      @Test
      @DisplayName("Rotation by size leaves list unchanged")
      void rotateRight_bySize_unchanged() {
        MyList<Integer> list = new MyArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);

        ListChallenges.rotateRight(list, 3);

        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(3, list.get(2));
      }

      @Test
      @DisplayName("Empty list remains empty")
      void rotateRight_emptyList_unchanged() {
        MyList<Integer> list = new MyArrayList<>();

        ListChallenges.rotateRight(list, 2);

        assertTrue(list.isEmpty());
      }

      @Test
      @DisplayName("Single element unchanged")
      void rotateRight_singleElement_unchanged() {
        MyList<Integer> list = new MyArrayList<>();
        list.add(1);

        ListChallenges.rotateRight(list, 5);

        assertEquals(1, list.size());
        assertEquals(1, list.get(0));
      }

      @Test
      @DisplayName("Throws exception for negative k")
      void rotateRight_negativeK_throwsException() {
        MyList<Integer> list = new MyArrayList<>();
        list.add(1);

        assertThrows(IllegalArgumentException.class, () -> ListChallenges.rotateRight(list, -1));
      }

      @Test
      @DisplayName("Throws exception for null list")
      void rotateRight_nullList_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> ListChallenges.rotateRight(null, 1));
      }
    }

    @Nested
    @DisplayName("LinkedList Implementation")
    class LinkedListTests {

      @Test
      @DisplayName("Works with linked list")
      void rotateRight_linkedList_works() {
        MyList<String> list = new MyLinkedList<>();
        list.add("A");
        list.add("B");
        list.add("C");

        ListChallenges.rotateRight(list, 1);

        assertEquals("C", list.get(0));
        assertEquals("A", list.get(1));
        assertEquals("B", list.get(2));
      }
    }
  }

  @Nested
  @DisplayName("Add Two Numbers Tests (LeetCode #2)")
  class AddTwoNumbersTests {

    @Nested
    @DisplayName("ArrayList Implementation")
    class ArrayListTests {

      @Test
      @DisplayName("Adds 342 + 465 = 807")
      void addTwoNumbers_342plus465_returns807() {
        // 342 stored as [2,4,3]
        MyList<Integer> list1 = new MyArrayList<>();
        list1.add(2);
        list1.add(4);
        list1.add(3);

        // 465 stored as [5,6,4]
        MyList<Integer> list2 = new MyArrayList<>();
        list2.add(5);
        list2.add(6);
        list2.add(4);

        // Result 807 stored as [7,0,8]
        MyList<Integer> result = ListChallenges.addTwoNumbers(list1, list2);

        assertEquals(3, result.size());
        assertEquals(7, result.get(0));
        assertEquals(0, result.get(1));
        assertEquals(8, result.get(2));
      }

      @Test
      @DisplayName("Adds with carry propagation")
      void addTwoNumbers_withCarry_handlesCorrectly() {
        // 99 stored as [9,9]
        MyList<Integer> list1 = new MyArrayList<>();
        list1.add(9);
        list1.add(9);

        // 1 stored as [1]
        MyList<Integer> list2 = new MyArrayList<>();
        list2.add(1);

        // Result 100 stored as [0,0,1]
        MyList<Integer> result = ListChallenges.addTwoNumbers(list1, list2);

        assertEquals(3, result.size());
        assertEquals(0, result.get(0));
        assertEquals(0, result.get(1));
        assertEquals(1, result.get(2));
      }

      @Test
      @DisplayName("Adds zero to number")
      void addTwoNumbers_addZero_returnsOriginal() {
        MyList<Integer> list1 = new MyArrayList<>();
        list1.add(1);
        list1.add(2);
        list1.add(3);

        MyList<Integer> list2 = new MyArrayList<>();
        list2.add(0);

        MyList<Integer> result = ListChallenges.addTwoNumbers(list1, list2);

        assertEquals(3, result.size());
        assertEquals(1, result.get(0));
        assertEquals(2, result.get(1));
        assertEquals(3, result.get(2));
      }

      @Test
      @DisplayName("Adds two empty lists")
      void addTwoNumbers_bothEmpty_returnsEmpty() {
        MyList<Integer> list1 = new MyArrayList<>();
        MyList<Integer> list2 = new MyArrayList<>();

        MyList<Integer> result = ListChallenges.addTwoNumbers(list1, list2);

        assertTrue(result.isEmpty());
      }

      @Test
      @DisplayName("Throws exception for null lists")
      void addTwoNumbers_nullList_throwsException() {
        MyList<Integer> list = new MyArrayList<>();
        assertThrows(
            IllegalArgumentException.class, () -> ListChallenges.addTwoNumbers(null, list));
        assertThrows(
            IllegalArgumentException.class, () -> ListChallenges.addTwoNumbers(list, null));
      }
    }

    @Nested
    @DisplayName("LinkedList Implementation")
    class LinkedListTests {

      @Test
      @DisplayName("Works with linked lists")
      void addTwoNumbers_linkedLists_works() {
        MyList<Integer> list1 = new MyLinkedList<>();
        list1.add(1);
        list1.add(2);

        MyList<Integer> list2 = new MyLinkedList<>();
        list2.add(3);
        list2.add(4);

        // 21 + 43 = 64 stored as [4,6]
        MyList<Integer> result = ListChallenges.addTwoNumbers(list1, list2);

        assertEquals(2, result.size());
        assertEquals(4, result.get(0));
        assertEquals(6, result.get(1));
      }
    }
  }

  @Nested
  @DisplayName("Partition Tests (LeetCode #86)")
  class PartitionTests {

    @Nested
    @DisplayName("ArrayList Implementation")
    class ArrayListTests {

      @Test
      @DisplayName("Partitions list around pivot")
      void partition_aroundPivot_separatesCorrectly() {
        MyList<Integer> list = new MyArrayList<>();
        list.add(1);
        list.add(4);
        list.add(3);
        list.add(2);
        list.add(5);
        list.add(2);

        ListChallenges.partition(list, 3, null);

        // All elements < 3 should come before elements >= 3
        // Expected: [1, 2, 2, 4, 3, 5]
        assertEquals(6, list.size());

        // First three should be < 3
        assertTrue(list.get(0) < 3);
        assertTrue(list.get(1) < 3);
        assertTrue(list.get(2) < 3);

        // Last three should be >= 3
        assertTrue(list.get(3) >= 3);
        assertTrue(list.get(4) >= 3);
        assertTrue(list.get(5) >= 3);
      }

      @Test
      @DisplayName("Handles all elements less than pivot")
      void partition_allLessThanPivot_unchanged() {
        MyList<Integer> list = new MyArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);

        ListChallenges.partition(list, 10, null);

        assertEquals(3, list.size());
      }

      @Test
      @DisplayName("Handles all elements greater than pivot")
      void partition_allGreaterThanPivot_unchanged() {
        MyList<Integer> list = new MyArrayList<>();
        list.add(5);
        list.add(6);
        list.add(7);

        ListChallenges.partition(list, 1, null);

        assertEquals(3, list.size());
      }

      @Test
      @DisplayName("Empty list remains empty")
      void partition_emptyList_unchanged() {
        MyList<Integer> list = new MyArrayList<>();

        ListChallenges.partition(list, 5, null);

        assertTrue(list.isEmpty());
      }

      @Test
      @DisplayName("Single element list unchanged")
      void partition_singleElement_unchanged() {
        MyList<Integer> list = new MyArrayList<>();
        list.add(3);

        ListChallenges.partition(list, 2, null);

        assertEquals(1, list.size());
        assertEquals(3, list.get(0));
      }

      @Test
      @DisplayName("Works with custom comparator")
      void partition_withComparator_works() {
        MyList<String> list = new MyArrayList<>();
        list.add("apple");
        list.add("zebra");
        list.add("banana");

        ListChallenges.partition(list, "m", null);

        // "apple" and "banana" < "m", "zebra" >= "m"
        assertEquals(3, list.size());
        assertEquals("apple", list.get(0));
        assertEquals("banana", list.get(1));
        assertEquals("zebra", list.get(2));
      }

      @Test
      @DisplayName("Throws exception for null list")
      void partition_nullList_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> ListChallenges.partition(null, 3, null));
      }
    }

    @Nested
    @DisplayName("LinkedList Implementation")
    class LinkedListTests {

      @Test
      @DisplayName("Works with linked list")
      void partition_linkedList_works() {
        MyList<Integer> list = new MyLinkedList<>();
        list.add(3);
        list.add(1);
        list.add(2);

        ListChallenges.partition(list, 2, null);

        assertEquals(3, list.size());
        assertEquals(1, list.get(0)); // Only element < 2
      }
    }
  }

  // ==================== HARD CHALLENGES ====================

  @Nested
  @DisplayName("Merge K Sorted Lists Tests (LeetCode #23)")
  class MergeKSortedListsTests {

    @Nested
    @DisplayName("ArrayList Implementation")
    class ArrayListTests {

      @Test
      @DisplayName("Merges three sorted lists")
      void mergeKSortedLists_threeLists_mergesCorrectly() {
        MyList<MyList<Integer>> lists = new MyArrayList<>();

        MyList<Integer> list1 = new MyArrayList<>();
        list1.add(1);
        list1.add(4);
        list1.add(5);

        MyList<Integer> list2 = new MyArrayList<>();
        list2.add(1);
        list2.add(3);
        list2.add(4);

        MyList<Integer> list3 = new MyArrayList<>();
        list3.add(2);
        list3.add(6);

        lists.add(list1);
        lists.add(list2);
        lists.add(list3);

        MyList<Integer> merged = ListChallenges.mergeKSortedLists(lists, null);

        assertEquals(8, merged.size());
        assertEquals(1, merged.get(0));
        assertEquals(1, merged.get(1));
        assertEquals(2, merged.get(2));
        assertEquals(3, merged.get(3));
        assertEquals(4, merged.get(4));
        assertEquals(4, merged.get(5));
        assertEquals(5, merged.get(6));
        assertEquals(6, merged.get(7));
      }

      @Test
      @DisplayName("Handles empty list of lists")
      void mergeKSortedLists_emptyInput_returnsEmpty() {
        MyList<MyList<Integer>> lists = new MyArrayList<>();

        MyList<Integer> merged = ListChallenges.mergeKSortedLists(lists, null);

        assertTrue(merged.isEmpty());
      }

      @Test
      @DisplayName("Handles single list")
      void mergeKSortedLists_singleList_returnsCopy() {
        MyList<MyList<Integer>> lists = new MyArrayList<>();

        MyList<Integer> list1 = new MyArrayList<>();
        list1.add(1);
        list1.add(2);
        list1.add(3);

        lists.add(list1);

        MyList<Integer> merged = ListChallenges.mergeKSortedLists(lists, null);

        assertEquals(3, merged.size());
        assertEquals(1, merged.get(0));
        assertEquals(2, merged.get(1));
        assertEquals(3, merged.get(2));
      }

      @Test
      @DisplayName("Handles lists with empty sublists")
      void mergeKSortedLists_withEmptySublists_ignoresThem() {
        MyList<MyList<Integer>> lists = new MyArrayList<>();

        MyList<Integer> list1 = new MyArrayList<>();
        list1.add(1);

        MyList<Integer> list2 = new MyArrayList<>();
        // Empty

        MyList<Integer> list3 = new MyArrayList<>();
        list3.add(2);

        lists.add(list1);
        lists.add(list2);
        lists.add(list3);

        MyList<Integer> merged = ListChallenges.mergeKSortedLists(lists, null);

        assertEquals(2, merged.size());
        assertEquals(1, merged.get(0));
        assertEquals(2, merged.get(1));
      }

      @Test
      @DisplayName("Works with custom comparator")
      void mergeKSortedLists_withComparator_works() {
        MyList<MyList<Integer>> lists = new MyArrayList<>();

        MyList<Integer> list1 = new MyArrayList<>();
        list1.add(5);
        list1.add(3);
        list1.add(1);

        MyList<Integer> list2 = new MyArrayList<>();
        list2.add(4);
        list2.add(2);

        lists.add(list1);
        lists.add(list2);

        Comparator<Integer> reverseOrder = Comparator.reverseOrder();
        MyList<Integer> merged = ListChallenges.mergeKSortedLists(lists, reverseOrder);

        assertEquals(5, merged.size());
        assertEquals(5, merged.get(0));
        assertEquals(4, merged.get(1));
      }

      @Test
      @DisplayName("Throws exception for null input")
      void mergeKSortedLists_nullInput_throwsException() {
        assertThrows(
            IllegalArgumentException.class, () -> ListChallenges.mergeKSortedLists(null, null));
      }
    }

    @Nested
    @DisplayName("LinkedList Implementation")
    class LinkedListTests {

      @Test
      @DisplayName("Works with linked lists")
      void mergeKSortedLists_linkedLists_works() {
        MyList<MyList<Integer>> lists = new MyArrayList<>();

        MyList<Integer> list1 = new MyLinkedList<>();
        list1.add(1);
        list1.add(3);

        MyList<Integer> list2 = new MyLinkedList<>();
        list2.add(2);

        lists.add(list1);
        lists.add(list2);

        MyList<Integer> merged = ListChallenges.mergeKSortedLists(lists, null);

        assertEquals(3, merged.size());
      }
    }
  }

  @Nested
  @DisplayName("Reverse K Group Tests (LeetCode #25)")
  class ReverseKGroupTests {

    @Nested
    @DisplayName("ArrayList Implementation")
    class ArrayListTests {

      @Test
      @DisplayName("Reverses in groups of 3")
      void reverseKGroup_k3_reversesGroups() {
        MyList<Integer> list = new MyArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);

        ListChallenges.reverseKGroup(list, 3);

        // First group [1,2,3] reversed to [3,2,1]
        // Second group [4,5] has < k elements, keep as is
        assertEquals(3, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(1, list.get(2));
        assertEquals(4, list.get(3));
        assertEquals(5, list.get(4));
      }

      @Test
      @DisplayName("Reverses in groups of 2")
      void reverseKGroup_k2_reversesGroups() {
        MyList<Integer> list = new MyArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);

        ListChallenges.reverseKGroup(list, 2);

        // [1,2] -> [2,1], [3,4] -> [4,3], [5] stays
        assertEquals(2, list.get(0));
        assertEquals(1, list.get(1));
        assertEquals(4, list.get(2));
        assertEquals(3, list.get(3));
        assertEquals(5, list.get(4));
      }

      @Test
      @DisplayName("k=1 leaves list unchanged")
      void reverseKGroup_k1_unchanged() {
        MyList<Integer> list = new MyArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);

        ListChallenges.reverseKGroup(list, 1);

        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(3, list.get(2));
      }

      @Test
      @DisplayName("k equal to size reverses whole list")
      void reverseKGroup_kEqualsSize_reversesAll() {
        MyList<Integer> list = new MyArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);

        ListChallenges.reverseKGroup(list, 3);

        assertEquals(3, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(1, list.get(2));
      }

      @Test
      @DisplayName("k greater than size leaves list unchanged")
      void reverseKGroup_kGreaterThanSize_unchanged() {
        MyList<Integer> list = new MyArrayList<>();
        list.add(1);
        list.add(2);

        ListChallenges.reverseKGroup(list, 5);

        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
      }

      @Test
      @DisplayName("Empty list unchanged")
      void reverseKGroup_emptyList_unchanged() {
        MyList<Integer> list = new MyArrayList<>();

        ListChallenges.reverseKGroup(list, 2);

        assertTrue(list.isEmpty());
      }

      @Test
      @DisplayName("Throws exception for k < 1")
      void reverseKGroup_invalidK_throwsException() {
        MyList<Integer> list = new MyArrayList<>();
        list.add(1);

        assertThrows(IllegalArgumentException.class, () -> ListChallenges.reverseKGroup(list, 0));
        assertThrows(IllegalArgumentException.class, () -> ListChallenges.reverseKGroup(list, -1));
      }

      @Test
      @DisplayName("Throws exception for null list")
      void reverseKGroup_nullList_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> ListChallenges.reverseKGroup(null, 2));
      }
    }

    @Nested
    @DisplayName("LinkedList Implementation")
    class LinkedListTests {

      @Test
      @DisplayName("Works with linked list")
      void reverseKGroup_linkedList_works() {
        MyList<String> list = new MyLinkedList<>();
        list.add("A");
        list.add("B");
        list.add("C");
        list.add("D");

        ListChallenges.reverseKGroup(list, 2);

        assertEquals("B", list.get(0));
        assertEquals("A", list.get(1));
        assertEquals("D", list.get(2));
        assertEquals("C", list.get(3));
      }
    }
  }

  @Nested
  @DisplayName("Sort List Tests (LeetCode #148)")
  class SortListTests {

    @Nested
    @DisplayName("ArrayList Implementation")
    class ArrayListTests {

      @Test
      @DisplayName("Sorts unsorted list")
      void sortList_unsorted_sortedCorrectly() {
        MyList<Integer> list = new MyArrayList<>();
        list.add(4);
        list.add(2);
        list.add(1);
        list.add(3);

        ListChallenges.sortList(list, null);

        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(3, list.get(2));
        assertEquals(4, list.get(3));
      }

      @Test
      @DisplayName("Handles already sorted list")
      void sortList_alreadySorted_unchanged() {
        MyList<Integer> list = new MyArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);

        ListChallenges.sortList(list, null);

        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(3, list.get(2));
      }

      @Test
      @DisplayName("Handles reverse sorted list")
      void sortList_reverseSorted_sortsCorrectly() {
        MyList<Integer> list = new MyArrayList<>();
        list.add(5);
        list.add(4);
        list.add(3);
        list.add(2);
        list.add(1);

        ListChallenges.sortList(list, null);

        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(3, list.get(2));
        assertEquals(4, list.get(3));
        assertEquals(5, list.get(4));
      }

      @Test
      @DisplayName("Handles single element")
      void sortList_singleElement_unchanged() {
        MyList<Integer> list = new MyArrayList<>();
        list.add(42);

        ListChallenges.sortList(list, null);

        assertEquals(1, list.size());
        assertEquals(42, list.get(0));
      }

      @Test
      @DisplayName("Handles empty list")
      void sortList_emptyList_unchanged() {
        MyList<Integer> list = new MyArrayList<>();

        ListChallenges.sortList(list, null);

        assertTrue(list.isEmpty());
      }

      @Test
      @DisplayName("Handles duplicates")
      void sortList_withDuplicates_sortsCorrectly() {
        MyList<Integer> list = new MyArrayList<>();
        list.add(3);
        list.add(1);
        list.add(2);
        list.add(1);
        list.add(3);

        ListChallenges.sortList(list, null);

        assertEquals(1, list.get(0));
        assertEquals(1, list.get(1));
        assertEquals(2, list.get(2));
        assertEquals(3, list.get(3));
        assertEquals(3, list.get(4));
      }

      @Test
      @DisplayName("Works with custom comparator")
      void sortList_withComparator_sortsCorrectly() {
        MyList<Integer> list = new MyArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);

        Comparator<Integer> reverseOrder = Comparator.reverseOrder();
        ListChallenges.sortList(list, reverseOrder);

        assertEquals(3, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(1, list.get(2));
      }

      @Test
      @DisplayName("Throws exception for null list")
      void sortList_nullList_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> ListChallenges.sortList(null, null));
      }
    }

    @Nested
    @DisplayName("LinkedList Implementation")
    class LinkedListTests {

      @Test
      @DisplayName("Sorts linked list")
      void sortList_linkedList_works() {
        MyList<Integer> list = new MyLinkedList<>();
        list.add(3);
        list.add(1);
        list.add(2);

        ListChallenges.sortList(list, null);

        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(3, list.get(2));
      }
    }
  }

  // ==================== MASTERY CHECK TESTS ====================

  @Nested
  @DisplayName("Mastery Check Tests")
  class MasteryCheckTests {

    @Test
    @DisplayName("Merge sort performance on large list")
    void masteryCheck_mergeSortLargeList() {
      MyList<Integer> list = new MyArrayList<>();
      int count = 10_000;

      // Create reverse-sorted list (worst case for many sorts)
      for (int i = count; i > 0; i--) {
        list.add(i);
      }

      long startTime = System.nanoTime();
      ListChallenges.sortList(list, null);
      long endTime = System.nanoTime();

      // Verify sorted
      for (int i = 0; i < count - 1; i++) {
        assertTrue(list.get(i) <= list.get(i + 1));
      }

      // Merge sort should be reasonably fast (O(n log n))
      assertTrue(endTime - startTime < 5_000_000_000L, "Sort should complete in reasonable time");
    }

    @Test
    @DisplayName("Merge K sorted lists performance")
    void masteryCheck_mergeKSortedLists() {
      MyList<MyList<Integer>> lists = new MyArrayList<>();
      int numLists = 100;
      int elementsPerList = 100;

      // Create k sorted lists
      for (int i = 0; i < numLists; i++) {
        MyList<Integer> sortedList = new MyArrayList<>();
        for (int j = 0; j < elementsPerList; j++) {
          sortedList.add(i * elementsPerList + j);
        }
        lists.add(sortedList);
      }

      long startTime = System.nanoTime();
      MyList<Integer> merged = ListChallenges.mergeKSortedLists(lists, null);
      long endTime = System.nanoTime();

      // Verify size
      assertEquals(numLists * elementsPerList, merged.size());

      // Verify sorted
      for (int i = 0; i < merged.size() - 1; i++) {
        assertTrue(merged.get(i) <= merged.get(i + 1));
      }

      // Should complete in reasonable time
      assertTrue(endTime - startTime < 5_000_000_000L, "Merge should complete in reasonable time");
    }

    @Test
    @DisplayName("Rotation maintains all elements")
    void masteryCheck_rotationMaintainsElements() {
      MyList<Integer> list = new MyArrayList<>();
      int count = 1000;

      for (int i = 0; i < count; i++) {
        list.add(i);
      }

      // Rotate multiple times
      ListChallenges.rotateRight(list, 333);
      ListChallenges.rotateRight(list, 333);
      ListChallenges.rotateRight(list, 334);

      // After rotating by total of 1000 (== size), should be back to original
      for (int i = 0; i < count; i++) {
        assertEquals(i, list.get(i));
      }
    }
  }
}
