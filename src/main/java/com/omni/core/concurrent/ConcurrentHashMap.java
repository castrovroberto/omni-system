package com.omni.core.concurrent;

import com.omni.core.list.MyArrayList;
import com.omni.core.list.MyList;
import com.omni.core.map.MyHashMap;
import com.omni.core.map.MyMap;
import java.util.Optional;

/**
 * A thread-safe hash map using full synchronization.
 *
 * <p>All operations are synchronized on a single lock, providing strong consistency guarantees at
 * the cost of reduced concurrency compared to striped-locking approaches.
 *
 * <p>Trade-offs:
 *
 * <ul>
 *   <li>Simple and correct thread-safety
 *   <li>Lower throughput under high contention
 *   <li>Iterators are NOT thread-safe
 * </ul>
 *
 * @param <K> the key type
 * @param <V> the value type
 */
public class ConcurrentHashMap<K, V> implements MyMap<K, V> {

  private final MyMap<K, V> delegate;
  private final Object lock = new Object();

  /** Creates a new concurrent hash map. */
  public ConcurrentHashMap() {
    this.delegate = new MyHashMap<>();
  }

  @Override
  public Optional<V> put(K key, V value) {
    synchronized (lock) {
      return delegate.put(key, value);
    }
  }

  @Override
  public Optional<V> get(K key) {
    synchronized (lock) {
      return delegate.get(key);
    }
  }

  @Override
  public Optional<V> remove(K key) {
    synchronized (lock) {
      return delegate.remove(key);
    }
  }

  @Override
  public boolean containsKey(K key) {
    synchronized (lock) {
      return delegate.containsKey(key);
    }
  }

  @Override
  public boolean containsValue(V value) {
    synchronized (lock) {
      return delegate.containsValue(value);
    }
  }

  @Override
  public int size() {
    synchronized (lock) {
      return delegate.size();
    }
  }

  @Override
  public boolean isEmpty() {
    synchronized (lock) {
      return delegate.isEmpty();
    }
  }

  @Override
  public void clear() {
    synchronized (lock) {
      delegate.clear();
    }
  }

  @Override
  public Iterable<K> keys() {
    synchronized (lock) {
      // Return a snapshot copy
      MyList<K> result = new MyArrayList<>();
      for (K key : delegate.keys()) {
        result.add(key);
      }
      return result;
    }
  }

  @Override
  public Iterable<V> values() {
    synchronized (lock) {
      MyList<V> result = new MyArrayList<>();
      for (V value : delegate.values()) {
        result.add(value);
      }
      return result;
    }
  }

  @Override
  public Iterable<Entry<K, V>> entries() {
    synchronized (lock) {
      MyList<Entry<K, V>> result = new MyArrayList<>();
      for (Entry<K, V> entry : delegate.entries()) {
        result.add(entry);
      }
      return result;
    }
  }

  /**
   * Atomically puts a value only if the key is not already present.
   *
   * @param key the key
   * @param value the value
   * @return the existing value if present, or empty if the new value was put
   */
  public Optional<V> putIfAbsent(K key, V value) {
    synchronized (lock) {
      Optional<V> existing = delegate.get(key);
      if (existing.isEmpty()) {
        delegate.put(key, value);
      }
      return existing;
    }
  }

  /**
   * Atomically removes a key only if it maps to the specified value.
   *
   * @param key the key
   * @param value the expected value
   * @return true if removed
   */
  public boolean remove(K key, V value) {
    synchronized (lock) {
      Optional<V> existing = delegate.get(key);
      if (existing.isPresent() && existing.get().equals(value)) {
        delegate.remove(key);
        return true;
      }
      return false;
    }
  }
}
