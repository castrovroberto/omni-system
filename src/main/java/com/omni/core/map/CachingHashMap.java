package com.omni.core.map;

import com.omni.core.list.MyLinkedList;
import java.util.Optional;

/**
 * A caching decorator for {@link MyMap} that implements LRU (Least Recently Used) eviction.
 *
 * <p>This class demonstrates the <b>Decorator</b> design pattern. It wraps any {@link MyMap}
 * implementation and adds caching behavior without modifying the underlying map.
 *
 * <p>Features:
 *
 * <ul>
 *   <li>Configurable cache size
 *   <li>LRU eviction when cache is full
 *   <li>Transparent caching - delegates to underlying map for cache misses
 *   <li>Cache statistics (hits, misses, evictions)
 * </ul>
 *
 * <p>Use case: Frequently accessed entries (hot data) are cached for O(1) retrieval, while less
 * frequently used data remains in the backing store.
 *
 * @param <K> the type of keys
 * @param <V> the type of values
 */
public class CachingHashMap<K, V> implements MyMap<K, V> {

  /** Default cache size. */
  public static final int DEFAULT_CACHE_SIZE = 100;

  private final MyMap<K, V> delegate;
  private final MyMap<K, V> cache;
  private final MyLinkedList<K> accessOrder; // Most recently used at end
  private final int maxCacheSize;

  // Statistics
  private long cacheHits;
  private long cacheMisses;
  private long evictions;

  /**
   * Creates a caching wrapper around the given map with default cache size.
   *
   * @param delegate the backing map
   */
  public CachingHashMap(MyMap<K, V> delegate) {
    this(delegate, DEFAULT_CACHE_SIZE);
  }

  /**
   * Creates a caching wrapper around the given map with specified cache size.
   *
   * @param delegate the backing map
   * @param maxCacheSize maximum number of entries to cache
   * @throws IllegalArgumentException if maxCacheSize is less than 1
   */
  public CachingHashMap(MyMap<K, V> delegate, int maxCacheSize) {
    if (maxCacheSize < 1) {
      throw new IllegalArgumentException("Cache size must be at least 1");
    }
    this.delegate = delegate;
    this.cache = new MyHashMap<>();
    this.accessOrder = new MyLinkedList<>();
    this.maxCacheSize = maxCacheSize;
    this.cacheHits = 0;
    this.cacheMisses = 0;
    this.evictions = 0;
  }

  @Override
  public Optional<V> put(K key, V value) {
    // Update cache if key exists there
    if (cache.containsKey(key)) {
      cache.put(key, value);
      moveToEnd(key);
    }

    // Always update delegate
    return delegate.put(key, value);
  }

  @Override
  public Optional<V> get(K key) {
    // Check cache first
    Optional<V> cached = cache.get(key);
    if (cached.isPresent()) {
      cacheHits++;
      moveToEnd(key);
      return cached;
    }

    // Cache miss - fetch from delegate
    cacheMisses++;
    Optional<V> value = delegate.get(key);

    // Add to cache if found
    if (value.isPresent()) {
      addToCache(key, value.get());
    }

    return value;
  }

  @Override
  public Optional<V> remove(K key) {
    // Remove from cache
    cache.remove(key);
    removeFromAccessOrder(key);

    // Remove from delegate
    return delegate.remove(key);
  }

  @Override
  public boolean containsKey(K key) {
    // Check cache first, then delegate
    return cache.containsKey(key) || delegate.containsKey(key);
  }

  @Override
  public boolean containsValue(V value) {
    // Must check delegate (cache may not have all values)
    return delegate.containsValue(value);
  }

  @Override
  public int size() {
    return delegate.size();
  }

  @Override
  public boolean isEmpty() {
    return delegate.isEmpty();
  }

  @Override
  public void clear() {
    cache.clear();
    accessOrder.clear();
    delegate.clear();
  }

  @Override
  public Iterable<K> keys() {
    return delegate.keys();
  }

  @Override
  public Iterable<V> values() {
    return delegate.values();
  }

  @Override
  public Iterable<Entry<K, V>> entries() {
    return delegate.entries();
  }

  // ==================== Cache-specific Methods ====================

  /**
   * Returns the number of entries currently cached.
   *
   * @return the cache size
   */
  public int getCacheSize() {
    return cache.size();
  }

  /**
   * Returns the maximum cache size.
   *
   * @return the maximum cache size
   */
  public int getMaxCacheSize() {
    return maxCacheSize;
  }

  /**
   * Returns the number of cache hits.
   *
   * @return cache hit count
   */
  public long getCacheHits() {
    return cacheHits;
  }

  /**
   * Returns the number of cache misses.
   *
   * @return cache miss count
   */
  public long getCacheMisses() {
    return cacheMisses;
  }

  /**
   * Returns the number of cache evictions.
   *
   * @return eviction count
   */
  public long getEvictions() {
    return evictions;
  }

  /**
   * Returns the cache hit ratio (hits / (hits + misses)).
   *
   * @return the hit ratio between 0.0 and 1.0, or 0.0 if no accesses
   */
  public double getHitRatio() {
    long total = cacheHits + cacheMisses;
    return total == 0 ? 0.0 : (double) cacheHits / total;
  }

  /**
   * Clears the cache but keeps the delegate intact.
   *
   * <p>This can be useful for testing or when cache coherence is lost.
   */
  public void clearCache() {
    cache.clear();
    accessOrder.clear();
  }

  /**
   * Preloads a key into the cache.
   *
   * <p>This is useful for warming the cache with known hot keys.
   *
   * @param key the key to preload
   * @return true if the key was found and cached
   */
  public boolean preload(K key) {
    if (cache.containsKey(key)) {
      return true; // Already cached
    }

    Optional<V> value = delegate.get(key);
    if (value.isPresent()) {
      addToCache(key, value.get());
      return true;
    }
    return false;
  }

  // ==================== Private Helper Methods ====================

  private void addToCache(K key, V value) {
    // Evict if at capacity
    while (cache.size() >= maxCacheSize) {
      evictLru();
    }

    cache.put(key, value);
    accessOrder.addLast(key);
  }

  private void evictLru() {
    if (accessOrder.isEmpty()) {
      return;
    }

    K lruKey = accessOrder.removeFirst();
    cache.remove(lruKey);
    evictions++;
  }

  private void moveToEnd(K key) {
    removeFromAccessOrder(key);
    accessOrder.addLast(key);
  }

  private void removeFromAccessOrder(K key) {
    for (int i = 0; i < accessOrder.size(); i++) {
      if (keysEqual(accessOrder.get(i), key)) {
        accessOrder.remove(i);
        return;
      }
    }
  }

  private boolean keysEqual(K k1, K k2) {
    return k1 == null ? k2 == null : k1.equals(k2);
  }
}
