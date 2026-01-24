package com.omni.core.map;

import com.omni.core.list.MyArrayList;
import com.omni.core.list.MyLinkedList;
import com.omni.core.list.MyList;
import com.omni.core.map.hash.DefaultHashStrategy;
import com.omni.core.map.hash.HashStrategy;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * A hash table implementation of the {@link MyMap} interface.
 *
 * <p>This implementation uses separate chaining for collision resolution, where each bucket
 * contains a {@link MyLinkedList} of entries that hash to the same index.
 *
 * <p>Key features:
 *
 * <ul>
 *   <li>O(1) average time for get, put, and remove operations
 *   <li>Automatic resizing when load factor threshold is exceeded
 *   <li>Configurable hash strategy via Strategy pattern
 *   <li>Supports null keys and values
 * </ul>
 *
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 */
public class MyHashMap<K, V> implements MyMap<K, V> {

  /** Default initial capacity - must be a power of 2. */
  private static final int DEFAULT_INITIAL_CAPACITY = 16;

  /** Default load factor threshold for resizing. */
  private static final float DEFAULT_LOAD_FACTOR = 0.75f;

  /** Maximum capacity. */
  private static final int MAXIMUM_CAPACITY = 1 << 30;

  /** The hash table buckets. Each bucket is a linked list of entries. */
  private MyList<MapEntry<K, V>>[] buckets;

  /** The number of key-value mappings in this map. */
  private int size;

  /** The load factor for this hash table. */
  private final float loadFactor;

  /** The threshold for resizing (capacity * loadFactor). */
  private int threshold;

  /** The hash strategy used to compute bucket indices. */
  private HashStrategy<K> hashStrategy;

  /** Constructs an empty map with default initial capacity and load factor. */
  public MyHashMap() {
    this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
  }

  /**
   * Constructs an empty map with the specified initial capacity and default load factor.
   *
   * @param initialCapacity the initial capacity
   * @throws IllegalArgumentException if the initial capacity is negative
   */
  public MyHashMap(int initialCapacity) {
    this(initialCapacity, DEFAULT_LOAD_FACTOR);
  }

  /**
   * Constructs an empty map with the specified initial capacity and load factor.
   *
   * @param initialCapacity the initial capacity
   * @param loadFactor the load factor threshold for resizing
   * @throws IllegalArgumentException if the initial capacity is negative or load factor is
   *     non-positive
   */
  @SuppressWarnings("unchecked")
  public MyHashMap(int initialCapacity, float loadFactor) {
    if (initialCapacity < 0) {
      throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
    }
    if (loadFactor <= 0 || Float.isNaN(loadFactor)) {
      throw new IllegalArgumentException("Illegal load factor: " + loadFactor);
    }

    // Ensure capacity is a power of 2
    int capacity = tableSizeFor(Math.min(initialCapacity, MAXIMUM_CAPACITY));
    this.loadFactor = loadFactor;
    this.threshold = (int) (capacity * loadFactor);
    this.buckets = (MyList<MapEntry<K, V>>[]) new MyList[capacity];
    this.hashStrategy = new DefaultHashStrategy<>();
  }

  /**
   * Sets a custom hash strategy for this map.
   *
   * <p>Note: Changing the hash strategy on a non-empty map will cause lookups to fail for existing
   * entries. Only call this on an empty map.
   *
   * @param strategy the hash strategy to use
   * @throws IllegalStateException if the map is not empty
   */
  public void setHashStrategy(HashStrategy<K> strategy) {
    if (size > 0) {
      throw new IllegalStateException("Cannot change hash strategy on non-empty map");
    }
    this.hashStrategy = strategy;
  }

  /**
   * Returns the current hash strategy.
   *
   * @return the hash strategy
   */
  public HashStrategy<K> getHashStrategy() {
    return hashStrategy;
  }

  @Override
  public Optional<V> put(K key, V value) {
    int index = getBucketIndex(key);

    // Initialize bucket if needed
    if (buckets[index] == null) {
      buckets[index] = new MyLinkedList<>();
    }

    // Check if key already exists
    MyList<MapEntry<K, V>> bucket = buckets[index];
    for (MapEntry<K, V> entry : bucket) {
      if (keysEqual(entry.key, key)) {
        V oldValue = entry.value;
        entry.value = value;
        return Optional.ofNullable(oldValue);
      }
    }

    // Add new entry
    bucket.add(new MapEntry<>(key, value));
    size++;

    // Resize if needed
    if (size > threshold) {
      resize();
    }

    return Optional.empty();
  }

  @Override
  public Optional<V> get(K key) {
    int index = getBucketIndex(key);
    MyList<MapEntry<K, V>> bucket = buckets[index];

    if (bucket == null) {
      return Optional.empty();
    }

    for (MapEntry<K, V> entry : bucket) {
      if (keysEqual(entry.key, key)) {
        return Optional.ofNullable(entry.value);
      }
    }

    return Optional.empty();
  }

  @Override
  public Optional<V> remove(K key) {
    int index = getBucketIndex(key);
    MyList<MapEntry<K, V>> bucket = buckets[index];

    if (bucket == null) {
      return Optional.empty();
    }

    for (int i = 0; i < bucket.size(); i++) {
      MapEntry<K, V> entry = bucket.get(i);
      if (keysEqual(entry.key, key)) {
        bucket.remove(i);
        size--;
        return Optional.ofNullable(entry.value);
      }
    }

    return Optional.empty();
  }

  @Override
  public boolean containsKey(K key) {
    return get(key).isPresent() || (get(key).isEmpty() && hasNullValueForKey(key));
  }

  /**
   * Helper to check if key exists with null value.
   */
  private boolean hasNullValueForKey(K key) {
    int index = getBucketIndex(key);
    MyList<MapEntry<K, V>> bucket = buckets[index];

    if (bucket == null) {
      return false;
    }

    for (MapEntry<K, V> entry : bucket) {
      if (keysEqual(entry.key, key)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean containsValue(V value) {
    for (MyList<MapEntry<K, V>> bucket : buckets) {
      if (bucket != null) {
        for (MapEntry<K, V> entry : bucket) {
          if (valuesEqual(entry.value, value)) {
            return true;
          }
        }
      }
    }
    return false;
  }

  @Override
  public int size() {
    return size;
  }

  @Override
  public boolean isEmpty() {
    return size == 0;
  }

  @Override
  @SuppressWarnings("unchecked")
  public void clear() {
    buckets = (MyList<MapEntry<K, V>>[]) new MyList[buckets.length];
    size = 0;
  }

  @Override
  public Iterable<K> keys() {
    return () -> new KeyIterator();
  }

  @Override
  public Iterable<V> values() {
    return () -> new ValueIterator();
  }

  @Override
  public Iterable<Entry<K, V>> entries() {
    return () -> new EntryIterator();
  }

  /**
   * Returns the current capacity (number of buckets).
   *
   * @return the capacity
   */
  public int capacity() {
    return buckets.length;
  }

  /**
   * Returns the current load factor (size / capacity).
   *
   * @return the current load factor
   */
  public float currentLoadFactor() {
    return (float) size / buckets.length;
  }

  /**
   * Returns the configured load factor threshold.
   *
   * @return the load factor threshold
   */
  public float getLoadFactor() {
    return loadFactor;
  }

  /**
   * Returns statistics about bucket chain lengths. Useful for analyzing hash distribution.
   *
   * @return an array where index i contains the count of buckets with chain length i
   */
  public int[] getChainLengthDistribution() {
    int maxLength = 0;
    for (MyList<MapEntry<K, V>> bucket : buckets) {
      if (bucket != null && bucket.size() > maxLength) {
        maxLength = bucket.size();
      }
    }

    int[] distribution = new int[maxLength + 1];
    for (MyList<MapEntry<K, V>> bucket : buckets) {
      int length = bucket == null ? 0 : bucket.size();
      distribution[length]++;
    }
    return distribution;
  }

  /**
   * Returns the average chain length for non-empty buckets.
   *
   * @return the average chain length, or 0 if map is empty
   */
  public double getAverageChainLength() {
    if (size == 0) {
      return 0.0;
    }

    int nonEmptyBuckets = 0;
    for (MyList<MapEntry<K, V>> bucket : buckets) {
      if (bucket != null && !bucket.isEmpty()) {
        nonEmptyBuckets++;
      }
    }

    return nonEmptyBuckets == 0 ? 0.0 : (double) size / nonEmptyBuckets;
  }

  // ==================== Private Helper Methods ====================

  /**
   * Returns the bucket index for the given key.
   */
  private int getBucketIndex(K key) {
    return hashStrategy.hash(key, buckets.length);
  }

  /**
   * Compares two keys for equality, handling nulls.
   */
  private boolean keysEqual(K k1, K k2) {
    return k1 == null ? k2 == null : k1.equals(k2);
  }

  /**
   * Compares two values for equality, handling nulls.
   */
  private boolean valuesEqual(V v1, V v2) {
    return v1 == null ? v2 == null : v1.equals(v2);
  }

  /**
   * Doubles the capacity and rehashes all entries.
   */
  @SuppressWarnings("unchecked")
  private void resize() {
    int oldCapacity = buckets.length;
    if (oldCapacity >= MAXIMUM_CAPACITY) {
      threshold = Integer.MAX_VALUE;
      return;
    }

    int newCapacity = oldCapacity << 1; // Double capacity
    MyList<MapEntry<K, V>>[] oldBuckets = buckets;
    buckets = (MyList<MapEntry<K, V>>[]) new MyList[newCapacity];
    threshold = (int) (newCapacity * loadFactor);

    // Rehash all entries
    for (MyList<MapEntry<K, V>> bucket : oldBuckets) {
      if (bucket != null) {
        for (MapEntry<K, V> entry : bucket) {
          int newIndex = getBucketIndex(entry.key);
          if (buckets[newIndex] == null) {
            buckets[newIndex] = new MyLinkedList<>();
          }
          buckets[newIndex].add(entry);
        }
      }
    }
  }

  /**
   * Returns a power of two size for the given target capacity.
   */
  private static int tableSizeFor(int cap) {
    int n = cap - 1;
    n |= n >>> 1;
    n |= n >>> 2;
    n |= n >>> 4;
    n |= n >>> 8;
    n |= n >>> 16;
    return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
  }

  // ==================== Inner Classes ====================

  /**
   * A map entry holding key and value.
   */
  private static class MapEntry<K, V> implements Entry<K, V> {
    final K key;
    V value;

    MapEntry(K key, V value) {
      this.key = key;
      this.value = value;
    }

    @Override
    public K getKey() {
      return key;
    }

    @Override
    public V getValue() {
      return value;
    }

    @Override
    public String toString() {
      return key + "=" + value;
    }
  }

  /**
   * Base iterator for traversing all entries.
   */
  private abstract class HashIterator<T> implements Iterator<T> {
    private int bucketIndex;
    private Iterator<MapEntry<K, V>> bucketIterator;
    private MapEntry<K, V> next;

    HashIterator() {
      bucketIndex = 0;
      bucketIterator = null;
      advanceToNext();
    }

    private void advanceToNext() {
      next = null;

      // First, try to get next from current bucket iterator
      if (bucketIterator != null && bucketIterator.hasNext()) {
        next = bucketIterator.next();
        return;
      }

      // Find next non-empty bucket
      while (bucketIndex < buckets.length) {
        if (buckets[bucketIndex] != null && !buckets[bucketIndex].isEmpty()) {
          bucketIterator = buckets[bucketIndex].iterator();
          bucketIndex++; // Move to next bucket for future iterations
          if (bucketIterator.hasNext()) {
            next = bucketIterator.next();
            return;
          }
        } else {
          bucketIndex++;
        }
      }
    }

    @Override
    public boolean hasNext() {
      return next != null;
    }

    protected MapEntry<K, V> nextEntry() {
      if (next == null) {
        throw new NoSuchElementException();
      }
      MapEntry<K, V> current = next;
      advanceToNext();
      return current;
    }
  }

  private class KeyIterator extends HashIterator<K> {
    @Override
    public K next() {
      return nextEntry().key;
    }
  }

  private class ValueIterator extends HashIterator<V> {
    @Override
    public V next() {
      return nextEntry().value;
    }
  }

  private class EntryIterator extends HashIterator<Entry<K, V>> {
    @Override
    public Entry<K, V> next() {
      return nextEntry();
    }
  }
}
