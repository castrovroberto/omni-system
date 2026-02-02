package com.omni.core.algorithm.challenge;

import static org.junit.jupiter.api.Assertions.*;

import com.omni.core.list.MyArrayList;
import com.omni.core.list.MyList;
import org.junit.jupiter.api.Test;

class HeapChallengesTest {

  @Test
  void mergeKSortedLists_emptyInput_returnsEmpty() {
    MyList<MyList<Integer>> lists = new MyArrayList<>();
    MyList<Integer> result = HeapChallenges.mergeKSortedLists(lists);
    assertTrue(result.isEmpty());
  }

  @Test
  void mergeKSortedLists_singleList_returnsSameElements() {
    MyList<Integer> list1 = listOf(1, 3, 5);
    MyList<MyList<Integer>> lists = new MyArrayList<>();
    lists.add(list1);

    MyList<Integer> result = HeapChallenges.mergeKSortedLists(lists);

    assertEquals(3, result.size());
    assertEquals(1, result.get(0));
    assertEquals(3, result.get(1));
    assertEquals(5, result.get(2));
  }

  @Test
  void mergeKSortedLists_twoLists_mergesCorrectly() {
    MyList<Integer> list1 = listOf(1, 4, 7);
    MyList<Integer> list2 = listOf(2, 5, 8);
    MyList<MyList<Integer>> lists = new MyArrayList<>();
    lists.add(list1);
    lists.add(list2);

    MyList<Integer> result = HeapChallenges.mergeKSortedLists(lists);

    assertEquals(6, result.size());
    assertListEquals(new int[] {1, 2, 4, 5, 7, 8}, result);
  }

  @Test
  void mergeKSortedLists_threeLists_mergesCorrectly() {
    MyList<Integer> list1 = listOf(1, 6, 9);
    MyList<Integer> list2 = listOf(2, 4);
    MyList<Integer> list3 = listOf(3, 5, 7, 8);
    MyList<MyList<Integer>> lists = new MyArrayList<>();
    lists.add(list1);
    lists.add(list2);
    lists.add(list3);

    MyList<Integer> result = HeapChallenges.mergeKSortedLists(lists);

    assertEquals(9, result.size());
    assertListEquals(new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9}, result);
  }

  @Test
  void mergeKSortedLists_listsWithDuplicates_preservesDuplicates() {
    MyList<Integer> list1 = listOf(1, 3, 5);
    MyList<Integer> list2 = listOf(1, 3, 5);
    MyList<MyList<Integer>> lists = new MyArrayList<>();
    lists.add(list1);
    lists.add(list2);

    MyList<Integer> result = HeapChallenges.mergeKSortedLists(lists);

    assertEquals(6, result.size());
    assertListEquals(new int[] {1, 1, 3, 3, 5, 5}, result);
  }

  @Test
  void mergeKSortedLists_emptyListsIncluded_skipsEmpty() {
    MyList<Integer> list1 = listOf(1, 3);
    MyList<Integer> list2 = new MyArrayList<>();
    MyList<Integer> list3 = listOf(2);
    MyList<MyList<Integer>> lists = new MyArrayList<>();
    lists.add(list1);
    lists.add(list2);
    lists.add(list3);

    MyList<Integer> result = HeapChallenges.mergeKSortedLists(lists);

    assertEquals(3, result.size());
    assertListEquals(new int[] {1, 2, 3}, result);
  }

  @Test
  void findKLargest_findsTopK() {
    MyList<Integer> list = listOf(3, 1, 4, 1, 5, 9, 2, 6);

    MyList<Integer> result = HeapChallenges.findKLargest(list, 3);

    assertEquals(3, result.size());
    // Contains 9, 6, 5 in some order
    assertTrue(contains(result, 9));
    assertTrue(contains(result, 6));
    assertTrue(contains(result, 5));
  }

  @Test
  void findKLargest_kEqualsSize_returnsAll() {
    MyList<Integer> list = listOf(3, 1, 2);

    MyList<Integer> result = HeapChallenges.findKLargest(list, 3);

    assertEquals(3, result.size());
  }

  @Test
  void findKLargest_kGreaterThanSize_returnsAll() {
    MyList<Integer> list = listOf(3, 1);

    MyList<Integer> result = HeapChallenges.findKLargest(list, 5);

    assertEquals(2, result.size());
  }

  private MyList<Integer> listOf(int... values) {
    MyList<Integer> list = new MyArrayList<>();
    for (int v : values) {
      list.add(v);
    }
    return list;
  }

  private void assertListEquals(int[] expected, MyList<Integer> actual) {
    assertEquals(expected.length, actual.size());
    for (int i = 0; i < expected.length; i++) {
      assertEquals(expected[i], actual.get(i));
    }
  }

  private boolean contains(MyList<Integer> list, int value) {
    for (Integer i : list) {
      if (i == value) {
        return true;
      }
    }
    return false;
  }
}
