package com.omni.core.concurrent;

import static org.junit.jupiter.api.Assertions.*;

import com.omni.core.list.MyArrayList;
import java.util.concurrent.CountDownLatch;
import org.junit.jupiter.api.Test;

class SynchronizedListTest {

  @Test
  void add_singleThread_works() {
    SynchronizedList<String> list = new SynchronizedList<>(new MyArrayList<>());

    list.add("a");
    list.add("b");
    list.add("c");

    assertEquals(3, list.size());
    assertEquals("a", list.get(0));
    assertEquals("b", list.get(1));
    assertEquals("c", list.get(2));
  }

  @Test
  void multipleThreads_addConcurrently_noDataLoss() throws InterruptedException {
    SynchronizedList<Integer> list = new SynchronizedList<>(new MyArrayList<>());
    int threadCount = 10;
    int addsPerThread = 100;
    CountDownLatch latch = new CountDownLatch(threadCount);

    for (int t = 0; t < threadCount; t++) {
      new Thread(
              () -> {
                for (int i = 0; i < addsPerThread; i++) {
                  list.add(i);
                }
                latch.countDown();
              })
          .start();
    }

    latch.await();
    assertEquals(threadCount * addsPerThread, list.size());
  }

  @Test
  void getLock_allowsCompoundOperations() {
    SynchronizedList<String> list = new SynchronizedList<>(new MyArrayList<>());
    list.add("first");

    synchronized (list.getLock()) {
      if (!list.isEmpty()) {
        String first = list.get(0);
        assertEquals("first", first);
        list.remove(0);
      }
    }

    assertTrue(list.isEmpty());
  }

  @Test
  void remove_works() {
    SynchronizedList<String> list = new SynchronizedList<>(new MyArrayList<>());
    list.add("a");
    list.add("b");
    list.add("c");

    String removed = list.remove(1);

    assertEquals("b", removed);
    assertEquals(2, list.size());
    assertEquals("a", list.get(0));
    assertEquals("c", list.get(1));
  }

  @Test
  void contains_works() {
    SynchronizedList<String> list = new SynchronizedList<>(new MyArrayList<>());
    list.add("hello");

    assertTrue(list.contains("hello"));
    assertFalse(list.contains("world"));
  }

  @Test
  void indexOf_works() {
    SynchronizedList<String> list = new SynchronizedList<>(new MyArrayList<>());
    list.add("a");
    list.add("b");
    list.add("c");

    assertEquals(1, list.indexOf("b"));
    assertEquals(-1, list.indexOf("x"));
  }

  @Test
  void clear_emptiesList() {
    SynchronizedList<String> list = new SynchronizedList<>(new MyArrayList<>());
    list.add("a");
    list.add("b");

    list.clear();

    assertTrue(list.isEmpty());
    assertEquals(0, list.size());
  }

  @Test
  void addAtIndex_insertsElement() {
    SynchronizedList<String> list = new SynchronizedList<>(new MyArrayList<>());
    list.add("a");
    list.add("c");

    list.add(1, "b");

    assertEquals(3, list.size());
    assertEquals("a", list.get(0));
    assertEquals("b", list.get(1));
    assertEquals("c", list.get(2));
  }

  @Test
  void set_replacesElement() {
    SynchronizedList<String> list = new SynchronizedList<>(new MyArrayList<>());
    list.add("old");

    String previous = list.set(0, "new");

    assertEquals("old", previous);
    assertEquals("new", list.get(0));
  }

  @Test
  void toArray_returnsAllElements() {
    SynchronizedList<String> list = new SynchronizedList<>(new MyArrayList<>());
    list.add("a");
    list.add("b");
    list.add("c");

    Object[] array = list.toArray();

    assertEquals(3, array.length);
    assertEquals("a", array[0]);
    assertEquals("b", array[1]);
    assertEquals("c", array[2]);
  }

  @Test
  void iterator_traversesAllElements() {
    SynchronizedList<String> list = new SynchronizedList<>(new MyArrayList<>());
    list.add("a");
    list.add("b");
    list.add("c");

    int count = 0;
    synchronized (list.getLock()) {
      for (String s : list) {
        assertNotNull(s);
        count++;
      }
    }
    assertEquals(3, count);
  }

  @Test
  void removeElement_removesFirstOccurrence() {
    SynchronizedList<String> list = new SynchronizedList<>(new MyArrayList<>());
    list.add("a");
    list.add("b");
    list.add("c");

    boolean removed = list.removeElement("b");

    assertTrue(removed);
    assertEquals(2, list.size());
    assertEquals("a", list.get(0));
    assertEquals("c", list.get(1));
  }

  @Test
  void removeElement_returnsFalseIfNotFound() {
    SynchronizedList<String> list = new SynchronizedList<>(new MyArrayList<>());
    list.add("a");

    boolean removed = list.removeElement("x");

    assertFalse(removed);
    assertEquals(1, list.size());
  }
}
