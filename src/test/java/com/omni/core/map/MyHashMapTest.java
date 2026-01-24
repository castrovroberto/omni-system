package com.omni.core.map;

import static org.junit.jupiter.api.Assertions.*;

import com.omni.core.map.hash.DJB2HashStrategy;
import com.omni.core.map.hash.HashStrategy;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("MyHashMap Tests")
class MyHashMapTest {

  private MyHashMap<String, Integer> map;

  @BeforeEach
  void setUp() {
    map = new MyHashMap<>();
  }

  @Nested
  @DisplayName("Constructor Tests")
  class ConstructorTests {

    @Test
    @DisplayName("Default constructor creates empty map")
    void defaultConstructor_createsEmptyMap() {
      MyHashMap<String, String> newMap = new MyHashMap<>();
      assertTrue(newMap.isEmpty());
      assertEquals(0, newMap.size());
      assertEquals(16, newMap.capacity()); // Default capacity
    }

    @Test
    @DisplayName("Constructor with initial capacity")
    void constructorWithCapacity_setsCorrectCapacity() {
      MyHashMap<String, String> newMap = new MyHashMap<>(32);
      assertEquals(32, newMap.capacity());
    }

    @Test
    @DisplayName("Constructor rounds capacity to power of 2")
    void constructorWithCapacity_roundsToPowerOfTwo() {
      MyHashMap<String, String> newMap = new MyHashMap<>(17);
      assertEquals(32, newMap.capacity()); // Next power of 2
    }

    @Test
    @DisplayName("Constructor with custom load factor")
    void constructorWithLoadFactor_setsCorrectLoadFactor() {
      MyHashMap<String, String> newMap = new MyHashMap<>(16, 0.5f);
      assertEquals(0.5f, newMap.getLoadFactor());
    }

    @Test
    @DisplayName("Constructor rejects negative capacity")
    void constructorWithNegativeCapacity_throwsException() {
      assertThrows(IllegalArgumentException.class, () -> new MyHashMap<>(-1));
    }

    @Test
    @DisplayName("Constructor rejects invalid load factor")
    void constructorWithInvalidLoadFactor_throwsException() {
      assertThrows(IllegalArgumentException.class, () -> new MyHashMap<>(16, 0f));
      assertThrows(IllegalArgumentException.class, () -> new MyHashMap<>(16, -0.5f));
      assertThrows(IllegalArgumentException.class, () -> new MyHashMap<>(16, Float.NaN));
    }
  }

  @Nested
  @DisplayName("Basic Operations Tests")
  class BasicOperationsTests {

    @Test
    @DisplayName("Put and get single entry")
    void putAndGet_singleEntry_works() {
      map.put("key", 42);
      assertEquals(Optional.of(42), map.get("key"));
    }

    @Test
    @DisplayName("Put returns empty for new key")
    void put_newKey_returnsEmpty() {
      Optional<Integer> result = map.put("key", 42);
      assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Put returns old value when updating")
    void put_existingKey_returnsOldValue() {
      map.put("key", 42);
      Optional<Integer> result = map.put("key", 100);
      assertEquals(Optional.of(42), result);
      assertEquals(Optional.of(100), map.get("key"));
    }

    @Test
    @DisplayName("Get returns empty for missing key")
    void get_missingKey_returnsEmpty() {
      assertTrue(map.get("nonexistent").isEmpty());
    }

    @Test
    @DisplayName("Size increases on put")
    void put_increasesSize() {
      assertEquals(0, map.size());
      map.put("a", 1);
      assertEquals(1, map.size());
      map.put("b", 2);
      assertEquals(2, map.size());
    }

    @Test
    @DisplayName("Size unchanged on update")
    void put_existingKey_sizeUnchanged() {
      map.put("key", 1);
      assertEquals(1, map.size());
      map.put("key", 2);
      assertEquals(1, map.size());
    }

    @Test
    @DisplayName("Remove returns value and decreases size")
    void remove_existingKey_returnsValueAndDecreasesSize() {
      map.put("key", 42);
      assertEquals(1, map.size());

      Optional<Integer> removed = map.remove("key");
      assertEquals(Optional.of(42), removed);
      assertEquals(0, map.size());
      assertTrue(map.get("key").isEmpty());
    }

    @Test
    @DisplayName("Remove returns empty for missing key")
    void remove_missingKey_returnsEmpty() {
      Optional<Integer> removed = map.remove("nonexistent");
      assertTrue(removed.isEmpty());
    }

    @Test
    @DisplayName("ContainsKey returns true for existing key")
    void containsKey_existingKey_returnsTrue() {
      map.put("key", 42);
      assertTrue(map.containsKey("key"));
    }

    @Test
    @DisplayName("ContainsKey returns false for missing key")
    void containsKey_missingKey_returnsFalse() {
      assertFalse(map.containsKey("nonexistent"));
    }

    @Test
    @DisplayName("ContainsValue returns true for existing value")
    void containsValue_existingValue_returnsTrue() {
      map.put("key", 42);
      assertTrue(map.containsValue(42));
    }

    @Test
    @DisplayName("ContainsValue returns false for missing value")
    void containsValue_missingValue_returnsFalse() {
      map.put("key", 42);
      assertFalse(map.containsValue(100));
    }

    @Test
    @DisplayName("Clear removes all entries")
    void clear_removesAllEntries() {
      map.put("a", 1);
      map.put("b", 2);
      map.put("c", 3);
      assertEquals(3, map.size());

      map.clear();
      assertEquals(0, map.size());
      assertTrue(map.isEmpty());
      assertTrue(map.get("a").isEmpty());
    }
  }

  @Nested
  @DisplayName("Null Handling Tests")
  class NullHandlingTests {

    @Test
    @DisplayName("Null key can be stored and retrieved")
    void nullKey_canBeStoredAndRetrieved() {
      map.put(null, 42);
      assertEquals(Optional.of(42), map.get(null));
      assertTrue(map.containsKey(null));
    }

    @Test
    @DisplayName("Null value can be stored and retrieved")
    void nullValue_canBeStoredAndRetrieved() {
      map.put("key", null);
      // get returns Optional.empty() for null value, but containsKey should return true
      assertTrue(map.containsKey("key"));
    }

    @Test
    @DisplayName("Null key can be removed")
    void nullKey_canBeRemoved() {
      map.put(null, 42);
      Optional<Integer> removed = map.remove(null);
      assertEquals(Optional.of(42), removed);
      assertFalse(map.containsKey(null));
    }

    @Test
    @DisplayName("ContainsValue works with null value")
    void containsValue_nullValue_works() {
      map.put("key", null);
      assertTrue(map.containsValue(null));
    }
  }

  @Nested
  @DisplayName("Collision Handling Tests")
  class CollisionHandlingTests {

    @Test
    @DisplayName("Handles collisions correctly")
    void collisions_handledCorrectly() {
      // Use a small capacity to force collisions
      MyHashMap<Integer, String> smallMap = new MyHashMap<>(4);

      // These keys will likely collide in a 4-bucket map
      smallMap.put(0, "zero");
      smallMap.put(4, "four");
      smallMap.put(8, "eight");
      smallMap.put(12, "twelve");

      assertEquals(Optional.of("zero"), smallMap.get(0));
      assertEquals(Optional.of("four"), smallMap.get(4));
      assertEquals(Optional.of("eight"), smallMap.get(8));
      assertEquals(Optional.of("twelve"), smallMap.get(12));
    }

    @Test
    @DisplayName("Remove works with collisions")
    void remove_withCollisions_works() {
      MyHashMap<Integer, String> smallMap = new MyHashMap<>(4);

      smallMap.put(0, "zero");
      smallMap.put(4, "four");
      smallMap.put(8, "eight");

      // Remove middle element from chain
      smallMap.remove(4);
      assertEquals(Optional.of("zero"), smallMap.get(0));
      assertTrue(smallMap.get(4).isEmpty());
      assertEquals(Optional.of("eight"), smallMap.get(8));
    }

    @Test
    @DisplayName("Update works with collisions")
    void update_withCollisions_works() {
      MyHashMap<Integer, String> smallMap = new MyHashMap<>(4);

      smallMap.put(0, "zero");
      smallMap.put(4, "four");

      smallMap.put(4, "FOUR");
      assertEquals(Optional.of("FOUR"), smallMap.get(4));
      assertEquals(2, smallMap.size());
    }
  }

  @Nested
  @DisplayName("Resizing Tests")
  class ResizingTests {

    @Test
    @DisplayName("Map resizes when load factor exceeded")
    void resize_whenLoadFactorExceeded() {
      MyHashMap<Integer, Integer> smallMap = new MyHashMap<>(4, 0.75f);
      assertEquals(4, smallMap.capacity());

      // Add entries until resize is triggered (4 * 0.75 = 3 threshold)
      smallMap.put(1, 1);
      smallMap.put(2, 2);
      smallMap.put(3, 3);
      assertEquals(4, smallMap.capacity()); // Still 4

      smallMap.put(4, 4); // This should trigger resize
      assertEquals(8, smallMap.capacity()); // Doubled

      // Verify all entries still accessible
      assertEquals(Optional.of(1), smallMap.get(1));
      assertEquals(Optional.of(2), smallMap.get(2));
      assertEquals(Optional.of(3), smallMap.get(3));
      assertEquals(Optional.of(4), smallMap.get(4));
    }

    @Test
    @DisplayName("Multiple resizes maintain data integrity")
    void multipleResizes_maintainDataIntegrity() {
      MyHashMap<Integer, Integer> smallMap = new MyHashMap<>(2);

      for (int i = 0; i < 100; i++) {
        smallMap.put(i, i * 10);
      }

      assertEquals(100, smallMap.size());

      for (int i = 0; i < 100; i++) {
        assertEquals(Optional.of(i * 10), smallMap.get(i));
      }
    }
  }

  @Nested
  @DisplayName("Iterator Tests")
  class IteratorTests {

    @Test
    @DisplayName("Keys iterator returns all keys")
    void keysIterator_returnsAllKeys() {
      map.put("a", 1);
      map.put("b", 2);
      map.put("c", 3);

      Set<String> keys = new HashSet<>();
      for (String key : map.keys()) {
        keys.add(key);
      }

      assertEquals(Set.of("a", "b", "c"), keys);
    }

    @Test
    @DisplayName("Values iterator returns all values")
    void valuesIterator_returnsAllValues() {
      map.put("a", 1);
      map.put("b", 2);
      map.put("c", 3);

      Set<Integer> values = new HashSet<>();
      for (Integer value : map.values()) {
        values.add(value);
      }

      assertEquals(Set.of(1, 2, 3), values);
    }

    @Test
    @DisplayName("Entries iterator returns all entries")
    void entriesIterator_returnsAllEntries() {
      map.put("a", 1);
      map.put("b", 2);

      List<String> entries = new ArrayList<>();
      for (MyMap.Entry<String, Integer> entry : map.entries()) {
        entries.add(entry.getKey() + "=" + entry.getValue());
      }

      assertEquals(2, entries.size());
      assertTrue(entries.contains("a=1"));
      assertTrue(entries.contains("b=2"));
    }

    @Test
    @DisplayName("Empty map iterators are empty")
    void emptyMap_iteratorsAreEmpty() {
      assertFalse(map.keys().iterator().hasNext());
      assertFalse(map.values().iterator().hasNext());
      assertFalse(map.entries().iterator().hasNext());
    }
  }

  @Nested
  @DisplayName("Hash Strategy Tests")
  class HashStrategyTests {

    @Test
    @DisplayName("Custom hash strategy can be set on empty map")
    void setHashStrategy_onEmptyMap_works() {
      MyHashMap<String, Integer> stringMap = new MyHashMap<>();
      stringMap.setHashStrategy(new DJB2HashStrategy());

      stringMap.put("test", 42);
      assertEquals(Optional.of(42), stringMap.get("test"));
    }

    @Test
    @DisplayName("Setting hash strategy on non-empty map throws exception")
    void setHashStrategy_onNonEmptyMap_throwsException() {
      map.put("key", 42);
      assertThrows(IllegalStateException.class, () -> map.setHashStrategy(new DJB2HashStrategy()));
    }

    @Test
    @DisplayName("DJB2 hash strategy distributes keys well")
    void djb2HashStrategy_distributesWell() {
      MyHashMap<String, Integer> stringMap = new MyHashMap<>(16);
      stringMap.setHashStrategy(new DJB2HashStrategy());

      // Add many similar strings
      for (int i = 0; i < 100; i++) {
        stringMap.put("key" + i, i);
      }

      // All entries should be retrievable
      for (int i = 0; i < 100; i++) {
        assertEquals(Optional.of(i), stringMap.get("key" + i));
      }

      // Check distribution - average chain should be reasonable
      assertTrue(stringMap.getAverageChainLength() < 5.0);
    }

    @Test
    @DisplayName("Bad hash strategy causes poor distribution")
    void badHashStrategy_causesPoorDistribution() {
      // Hash strategy that always returns 0 (worst case)
      HashStrategy<String> badStrategy = (key, capacity) -> 0;

      MyHashMap<String, Integer> stringMap = new MyHashMap<>(16);
      stringMap.setHashStrategy(badStrategy);

      for (int i = 0; i < 100; i++) {
        stringMap.put("key" + i, i);
      }

      // All entries in single bucket - chain length equals total size
      double avgChainLength = stringMap.getAverageChainLength();
      assertEquals(100.0, avgChainLength, "All 100 entries should be in one bucket");

      // distribution[i] = count of buckets with chain length i
      // With 16 buckets and all 100 entries in bucket 0:
      // - 15 buckets have length 0
      // - 1 bucket has length 100
      int[] distribution = stringMap.getChainLengthDistribution();
      assertEquals(1, distribution[100]); // One bucket with 100 entries
    }
  }

  @Nested
  @DisplayName("Statistics Tests")
  class StatisticsTests {

    @Test
    @DisplayName("Current load factor calculated correctly")
    void currentLoadFactor_calculatedCorrectly() {
      MyHashMap<Integer, Integer> smallMap = new MyHashMap<>(10);
      assertEquals(0.0f, smallMap.currentLoadFactor());

      smallMap.put(1, 1);
      smallMap.put(2, 2);
      smallMap.put(3, 3);
      smallMap.put(4, 4);
      smallMap.put(5, 5);

      // 5 entries in 16-bucket map (10 rounds to 16)
      assertEquals(5.0f / 16, smallMap.currentLoadFactor(), 0.001f);
    }

    @Test
    @DisplayName("Average chain length calculated correctly")
    void averageChainLength_calculatedCorrectly() {
      MyHashMap<Integer, Integer> smallMap = new MyHashMap<>(4);

      assertEquals(0.0, smallMap.getAverageChainLength());

      // Force all into same bucket
      HashStrategy<Integer> singleBucket = (key, cap) -> 0;
      smallMap.setHashStrategy(singleBucket);

      smallMap.put(1, 1);
      smallMap.put(2, 2);
      smallMap.put(3, 3);

      // 3 entries in 1 bucket = average of 3
      assertEquals(3.0, smallMap.getAverageChainLength());
    }

    @Test
    @DisplayName("Chain length distribution is correct")
    void chainLengthDistribution_isCorrect() {
      MyHashMap<Integer, Integer> smallMap = new MyHashMap<>(4);

      // Empty map: all buckets have length 0
      int[] distribution = smallMap.getChainLengthDistribution();
      assertEquals(1, distribution.length);
      assertEquals(4, distribution[0]); // 4 empty buckets

      smallMap.put(0, 0);
      smallMap.put(1, 1);

      distribution = smallMap.getChainLengthDistribution();
      // Now we have some non-empty buckets
      assertTrue(distribution.length >= 1);
    }
  }

  @Nested
  @DisplayName("Edge Cases Tests")
  class EdgeCasesTests {

    @Test
    @DisplayName("Works with single entry")
    void singleEntry_works() {
      map.put("only", 1);
      assertEquals(1, map.size());
      assertEquals(Optional.of(1), map.get("only"));
      map.remove("only");
      assertTrue(map.isEmpty());
    }

    @Test
    @DisplayName("Many puts and removes maintain consistency")
    void manyPutsAndRemoves_maintainConsistency() {
      for (int i = 0; i < 1000; i++) {
        map.put("key" + i, i);
      }
      assertEquals(1000, map.size());

      for (int i = 0; i < 500; i++) {
        map.remove("key" + i);
      }
      assertEquals(500, map.size());

      for (int i = 500; i < 1000; i++) {
        assertEquals(Optional.of(i), map.get("key" + i));
      }
    }

    @Test
    @DisplayName("Duplicate puts update value")
    void duplicatePuts_updateValue() {
      map.put("key", 1);
      map.put("key", 2);
      map.put("key", 3);

      assertEquals(1, map.size());
      assertEquals(Optional.of(3), map.get("key"));
    }
  }

  @Nested
  @DisplayName("Mastery Check Tests")
  class MasteryCheckTests {

    @Test
    @DisplayName("Mastery check: 100,000 random strings with good distribution")
    void masteryCheck_100kStrings_goodDistribution() {
      MyHashMap<String, Integer> largeMap = new MyHashMap<>();

      for (int i = 0; i < 100_000; i++) {
        largeMap.put("key_" + i + "_value", i);
      }

      assertEquals(100_000, largeMap.size());

      // Verify retrieval
      for (int i = 0; i < 100_000; i++) {
        assertEquals(Optional.of(i), largeMap.get("key_" + i + "_value"));
      }

      // Average chain length should be close to 1 for well-distributed hash
      double avgChain = largeMap.getAverageChainLength();
      assertTrue(avgChain < 2.0, "Average chain length should be < 2, was: " + avgChain);
    }

    @Test
    @DisplayName("Mastery check: Intentionally bad hash causes O(n) degradation")
    void masteryCheck_badHash_causesONDegradation() {
      MyHashMap<String, Integer> badMap = new MyHashMap<>(16);

      // Force all keys to bucket 0
      badMap.setHashStrategy((key, capacity) -> 0);

      int count = 1000;
      for (int i = 0; i < count; i++) {
        badMap.put("key" + i, i);
      }

      // Average chain length equals size (all in one bucket)
      assertEquals((double) count, badMap.getAverageChainLength());

      // distribution[i] = count of buckets with chain length i
      // 1 bucket has all 1000 entries
      int[] distribution = badMap.getChainLengthDistribution();
      assertEquals(1, distribution[count]); // One bucket with 'count' entries

      // Still works, but O(n) per operation
      for (int i = 0; i < count; i++) {
        assertEquals(Optional.of(i), badMap.get("key" + i));
      }
    }

    @Test
    @DisplayName("Mastery check: Compare default hash vs DJB2")
    void masteryCheck_compareDefaultVsDJB2() {
      MyHashMap<String, Integer> defaultMap = new MyHashMap<>();
      MyHashMap<String, Integer> djb2Map = new MyHashMap<>();
      djb2Map.setHashStrategy(new DJB2HashStrategy());

      int count = 10_000;
      for (int i = 0; i < count; i++) {
        String key = "testKey" + i;
        defaultMap.put(key, i);
        djb2Map.put(key, i);
      }

      // Both should have good distribution
      double defaultAvg = defaultMap.getAverageChainLength();
      double djb2Avg = djb2Map.getAverageChainLength();

      assertTrue(defaultAvg < 3.0, "Default hash avg chain: " + defaultAvg);
      assertTrue(djb2Avg < 3.0, "DJB2 hash avg chain: " + djb2Avg);

      // Both retrieve correctly
      for (int i = 0; i < count; i++) {
        String key = "testKey" + i;
        assertEquals(Optional.of(i), defaultMap.get(key));
        assertEquals(Optional.of(i), djb2Map.get(key));
      }
    }
  }
}
