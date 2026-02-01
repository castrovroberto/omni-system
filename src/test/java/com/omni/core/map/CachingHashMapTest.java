package com.omni.core.map;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("CachingHashMap (Decorator) Tests")
class CachingHashMapTest {

  private MyMap<String, Integer> backingMap;
  private CachingHashMap<String, Integer> cachingMap;

  @BeforeEach
  void setUp() {
    backingMap = new MyHashMap<>();
    cachingMap = new CachingHashMap<>(backingMap, 3); // Small cache for testing
  }

  @Nested
  @DisplayName("Basic Operations Tests")
  class BasicOperationsTests {

    @Test
    @DisplayName("Put stores in backing map")
    void put_storesInBackingMap() {
      cachingMap.put("key", 42);

      assertEquals(Optional.of(42), backingMap.get("key"));
    }

    @Test
    @DisplayName("Get retrieves from backing map on cache miss")
    void get_retrievesFromBackingMapOnMiss() {
      backingMap.put("key", 42);

      Optional<Integer> result = cachingMap.get("key");

      assertEquals(Optional.of(42), result);
      assertEquals(1, cachingMap.getCacheMisses());
    }

    @Test
    @DisplayName("Get returns from cache on hit")
    void get_returnsFromCacheOnHit() {
      cachingMap.put("key", 42);
      cachingMap.get("key"); // Cache miss - populates cache
      cachingMap.get("key"); // Cache hit

      assertEquals(1, cachingMap.getCacheHits());
      assertEquals(1, cachingMap.getCacheMisses());
    }

    @Test
    @DisplayName("Remove removes from both cache and backing")
    void remove_removesBoth() {
      cachingMap.put("key", 42);
      cachingMap.get("key"); // Populate cache

      cachingMap.remove("key");

      assertTrue(cachingMap.get("key").isEmpty());
      assertTrue(backingMap.get("key").isEmpty());
    }

    @Test
    @DisplayName("ContainsKey checks both cache and backing")
    void containsKey_checksBoth() {
      backingMap.put("backing", 1);
      cachingMap.put("cached", 2);
      cachingMap.get("cached"); // Populate cache

      assertTrue(cachingMap.containsKey("backing"));
      assertTrue(cachingMap.containsKey("cached"));
      assertFalse(cachingMap.containsKey("nonexistent"));
    }

    @Test
    @DisplayName("Size returns backing map size")
    void size_returnsBackingSize() {
      cachingMap.put("a", 1);
      cachingMap.put("b", 2);

      assertEquals(2, cachingMap.size());
    }

    @Test
    @DisplayName("Clear clears both cache and backing")
    void clear_clearsBoth() {
      cachingMap.put("key", 42);
      cachingMap.get("key");

      cachingMap.clear();

      assertEquals(0, cachingMap.size());
      assertEquals(0, cachingMap.getCacheSize());
    }
  }

  @Nested
  @DisplayName("LRU Eviction Tests")
  class LruEvictionTests {

    @Test
    @DisplayName("Cache evicts LRU entry when full")
    void cache_evictsLruWhenFull() {
      // Cache size is 3
      cachingMap.put("a", 1);
      cachingMap.get("a"); // Access a
      cachingMap.put("b", 2);
      cachingMap.get("b"); // Access b
      cachingMap.put("c", 3);
      cachingMap.get("c"); // Access c

      assertEquals(3, cachingMap.getCacheSize());

      // Add fourth entry - should evict "a" (least recently used)
      cachingMap.put("d", 4);
      cachingMap.get("d");

      assertEquals(3, cachingMap.getCacheSize());
      assertEquals(1, cachingMap.getEvictions());
    }

    @Test
    @DisplayName("Access updates LRU order")
    void access_updatesLruOrder() {
      cachingMap.put("a", 1);
      cachingMap.get("a");
      cachingMap.put("b", 2);
      cachingMap.get("b");
      cachingMap.put("c", 3);
      cachingMap.get("c");

      // Access "a" again - now "b" is LRU
      cachingMap.get("a");

      // Add new entry - should evict "b"
      cachingMap.put("d", 4);
      cachingMap.get("d");

      // "a" should still be cached (was accessed recently)
      assertEquals(1, cachingMap.getCacheHits()); // From accessing "a" second time
    }

    @Test
    @DisplayName("Evicted entries still accessible from backing")
    void evictedEntries_stillInBacking() {
      cachingMap.put("a", 1);
      cachingMap.get("a");
      cachingMap.put("b", 2);
      cachingMap.get("b");
      cachingMap.put("c", 3);
      cachingMap.get("c");
      cachingMap.put("d", 4);
      cachingMap.get("d"); // Evicts "a"

      // "a" still accessible via backing map (cache miss)
      assertEquals(Optional.of(1), cachingMap.get("a"));
    }
  }

  @Nested
  @DisplayName("Statistics Tests")
  class StatisticsTests {

    @Test
    @DisplayName("Hit ratio calculated correctly")
    void hitRatio_calculatedCorrectly() {
      cachingMap.put("key", 42);
      cachingMap.get("key"); // Miss
      cachingMap.get("key"); // Hit
      cachingMap.get("key"); // Hit

      assertEquals(2, cachingMap.getCacheHits());
      assertEquals(1, cachingMap.getCacheMisses());
      assertEquals(2.0 / 3.0, cachingMap.getHitRatio(), 0.001);
    }

    @Test
    @DisplayName("Hit ratio is 0 for no accesses")
    void hitRatio_zeroForNoAccesses() {
      assertEquals(0.0, cachingMap.getHitRatio());
    }

    @Test
    @DisplayName("Evictions counted correctly")
    void evictions_countedCorrectly() {
      for (int i = 0; i < 10; i++) {
        cachingMap.put("key" + i, i);
        cachingMap.get("key" + i);
      }

      // Cache size is 3, so 7 evictions
      assertEquals(7, cachingMap.getEvictions());
    }
  }

  @Nested
  @DisplayName("Cache Management Tests")
  class CacheManagementTests {

    @Test
    @DisplayName("ClearCache preserves backing map")
    void clearCache_preservesBacking() {
      cachingMap.put("key", 42);
      cachingMap.get("key");

      cachingMap.clearCache();

      assertEquals(0, cachingMap.getCacheSize());
      assertEquals(1, cachingMap.size()); // Still in backing
      assertEquals(Optional.of(42), cachingMap.get("key")); // Can still get
    }

    @Test
    @DisplayName("Preload adds to cache")
    void preload_addsToCache() {
      backingMap.put("key", 42);

      boolean preloaded = cachingMap.preload("key");

      assertTrue(preloaded);
      assertEquals(1, cachingMap.getCacheSize());
    }

    @Test
    @DisplayName("Preload returns false for missing key")
    void preload_returnsFalseForMissing() {
      boolean preloaded = cachingMap.preload("nonexistent");

      assertFalse(preloaded);
      assertEquals(0, cachingMap.getCacheSize());
    }

    @Test
    @DisplayName("MaxCacheSize is respected")
    void maxCacheSize_respected() {
      CachingHashMap<String, Integer> small = new CachingHashMap<>(new MyHashMap<>(), 2);

      for (int i = 0; i < 100; i++) {
        small.put("key" + i, i);
        small.get("key" + i);
      }

      assertEquals(2, small.getCacheSize());
      assertEquals(2, small.getMaxCacheSize());
    }
  }

  @Nested
  @DisplayName("Constructor Tests")
  class ConstructorTests {

    @Test
    @DisplayName("Default cache size is 100")
    void defaultCacheSize_is100() {
      CachingHashMap<String, Integer> map = new CachingHashMap<>(new MyHashMap<>());

      assertEquals(100, map.getMaxCacheSize());
    }

    @Test
    @DisplayName("Rejects cache size less than 1")
    void rejectsCacheSizeLessThan1() {
      assertThrows(
          IllegalArgumentException.class, () -> new CachingHashMap<>(new MyHashMap<>(), 0));
      assertThrows(
          IllegalArgumentException.class, () -> new CachingHashMap<>(new MyHashMap<>(), -1));
    }
  }

  @Nested
  @DisplayName("Decorator Pattern Tests")
  class DecoratorPatternTests {

    @Test
    @DisplayName("Decorator is transparent for iteration")
    void decorator_transparentForIteration() {
      cachingMap.put("a", 1);
      cachingMap.put("b", 2);
      cachingMap.put("c", 3);

      int count = 0;
      for (String key : cachingMap.keys()) {
        count++;
      }

      assertEquals(3, count);
    }

    @Test
    @DisplayName("Decorator implements full MyMap interface")
    void decorator_implementsFullInterface() {
      // All MyMap operations should work
      cachingMap.put("key", 42);
      assertEquals(Optional.of(42), cachingMap.get("key"));
      assertTrue(cachingMap.containsKey("key"));
      assertTrue(cachingMap.containsValue(42));
      assertEquals(1, cachingMap.size());
      assertFalse(cachingMap.isEmpty());

      cachingMap.remove("key");
      assertTrue(cachingMap.isEmpty());
    }

    @Test
    @DisplayName("Can stack decorators")
    void canStackDecorators() {
      // Create a decorator of a decorator
      MyMap<String, Integer> base = new MyHashMap<>();
      CachingHashMap<String, Integer> cache1 = new CachingHashMap<>(base, 5);
      CachingHashMap<String, Integer> cache2 = new CachingHashMap<>(cache1, 10);

      cache2.put("key", 42);
      assertEquals(Optional.of(42), cache2.get("key"));
    }
  }
}
