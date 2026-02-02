package com.omni.core.concurrent;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.CountDownLatch;
import org.junit.jupiter.api.Test;

class ConcurrentHashMapTest {

  @Test
  void putAndGet_singleThread_works() {
    ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();

    map.put("one", 1);
    map.put("two", 2);
    map.put("three", 3);

    assertEquals(1, map.get("one").orElse(null));
    assertEquals(2, map.get("two").orElse(null));
    assertEquals(3, map.get("three").orElse(null));
    assertTrue(map.get("four").isEmpty());
  }

  @Test
  void multipleThreads_putConcurrently_noDataLoss() throws InterruptedException {
    ConcurrentHashMap<Integer, Integer> map = new ConcurrentHashMap<>();
    int threadCount = 8;
    int putsPerThread = 100;
    CountDownLatch latch = new CountDownLatch(threadCount);

    for (int t = 0; t < threadCount; t++) {
      final int threadId = t;
      new Thread(
              () -> {
                for (int i = 0; i < putsPerThread; i++) {
                  int key = threadId * putsPerThread + i;
                  map.put(key, key);
                }
                latch.countDown();
              })
          .start();
    }

    latch.await();

    // Verify all values are present
    for (int t = 0; t < threadCount; t++) {
      for (int i = 0; i < putsPerThread; i++) {
        int key = t * putsPerThread + i;
        assertTrue(map.containsKey(key), "Missing key: " + key);
        assertEquals(key, map.get(key).orElse(-1));
      }
    }
  }

  @Test
  void remove_works() {
    ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
    map.put("key", 42);

    assertEquals(42, map.remove("key").orElse(null));
    assertFalse(map.containsKey("key"));
    assertTrue(map.remove("key").isEmpty());
  }

  @Test
  void putIfAbsent_onlyPutsIfMissing() {
    ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();

    assertTrue(map.putIfAbsent("key", 1).isEmpty()); // Not present, put succeeds
    assertEquals(1, map.get("key").orElse(null));

    assertEquals(1, map.putIfAbsent("key", 2).orElse(null)); // Present, returns existing
    assertEquals(1, map.get("key").orElse(null)); // Value unchanged
  }

  @Test
  void removeConditional_onlyRemovesIfValueMatches() {
    ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
    map.put("key", 1);

    assertFalse(map.remove("key", 2)); // Wrong value
    assertTrue(map.containsKey("key"));

    assertTrue(map.remove("key", 1)); // Correct value
    assertFalse(map.containsKey("key"));
  }

  @Test
  void size_returnsCorrectCount() {
    ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();

    assertEquals(0, map.size());
    assertTrue(map.isEmpty());

    map.put("a", 1);
    map.put("b", 2);

    assertEquals(2, map.size());
    assertFalse(map.isEmpty());
  }

  @Test
  void clear_removesAllEntries() {
    ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
    map.put("a", 1);
    map.put("b", 2);

    map.clear();

    assertTrue(map.isEmpty());
    assertEquals(0, map.size());
  }

  @Test
  void keys_returnsAllKeys() {
    ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
    map.put("a", 1);
    map.put("b", 2);
    map.put("c", 3);

    int count = 0;
    for (String ignored : map.keys()) {
      count++;
    }
    assertEquals(3, count);
  }

  @Test
  void values_returnsAllValues() {
    ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
    map.put("a", 1);
    map.put("b", 2);

    int count = 0;
    for (Integer ignored : map.values()) {
      count++;
    }
    assertEquals(2, count);
  }

  @Test
  void entries_returnsAllEntries() {
    ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
    map.put("a", 1);
    map.put("b", 2);

    int count = 0;
    for (var entry : map.entries()) {
      assertNotNull(entry.getKey());
      assertNotNull(entry.getValue());
      count++;
    }
    assertEquals(2, count);
  }

  @Test
  void containsValue_findsValue() {
    ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
    map.put("key", 42);

    assertTrue(map.containsValue(42));
    assertFalse(map.containsValue(99));
  }

  @Test
  void put_returnsOldValue() {
    ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();

    assertTrue(map.put("key", 1).isEmpty());
    assertEquals(1, map.put("key", 2).orElse(-1));
    assertEquals(2, map.get("key").orElse(-1));
  }
}
