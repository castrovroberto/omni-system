package com.omni.core.map;

import java.util.Optional;

/**
 * Base interface for all map implementations in the Omni-System.
 *
 * <p>A map is a collection that associates keys with values. Each key can map to at most one value.
 * Keys must properly implement {@code hashCode()} and {@code equals()}.
 *
 * <p>This interface uses {@link Optional} for return values to avoid null ambiguity - a map can
 * legitimately contain null values, so returning null from {@code get()} would be ambiguous.
 *
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 */
public interface MyMap<K, V> {

  /**
   * Associates the specified value with the specified key in this map.
   *
   * <p>If the map previously contained a mapping for the key, the old value is replaced by the
   * specified value.
   *
   * @param key key with which the specified value is to be associated (may be null if
   *     implementation permits)
   * @param value value to be associated with the specified key (may be null)
   * @return an {@link Optional} containing the previous value associated with the key, or empty if
   *     there was no mapping
   */
  Optional<V> put(K key, V value);

  /**
   * Returns the value to which the specified key is mapped.
   *
   * @param key the key whose associated value is to be returned
   * @return an {@link Optional} containing the value, or empty if the key is not present
   */
  Optional<V> get(K key);

  /**
   * Removes the mapping for the specified key from this map if present.
   *
   * @param key the key whose mapping is to be removed
   * @return an {@link Optional} containing the previous value associated with the key, or empty if
   *     there was no mapping
   */
  Optional<V> remove(K key);

  /**
   * Returns {@code true} if this map contains a mapping for the specified key.
   *
   * @param key the key whose presence is to be tested
   * @return {@code true} if this map contains a mapping for the specified key
   */
  boolean containsKey(K key);

  /**
   * Returns {@code true} if this map maps one or more keys to the specified value.
   *
   * @param value the value whose presence is to be tested
   * @return {@code true} if this map maps one or more keys to the specified value
   */
  boolean containsValue(V value);

  /**
   * Returns the number of key-value mappings in this map.
   *
   * @return the number of key-value mappings
   */
  int size();

  /**
   * Returns {@code true} if this map contains no key-value mappings.
   *
   * @return {@code true} if this map contains no key-value mappings
   */
  boolean isEmpty();

  /** Removes all of the mappings from this map. */
  void clear();

  /**
   * Returns an iterable view of the keys contained in this map.
   *
   * @return an iterable over the keys
   */
  Iterable<K> keys();

  /**
   * Returns an iterable view of the values contained in this map.
   *
   * @return an iterable over the values
   */
  Iterable<V> values();

  /**
   * Returns an iterable view of the key-value mappings contained in this map.
   *
   * @return an iterable over the entries
   */
  Iterable<Entry<K, V>> entries();

  /**
   * A map entry (key-value pair).
   *
   * @param <K> the type of the key
   * @param <V> the type of the value
   */
  interface Entry<K, V> {
    /**
     * Returns the key corresponding to this entry.
     *
     * @return the key
     */
    K getKey();

    /**
     * Returns the value corresponding to this entry.
     *
     * @return the value
     */
    V getValue();
  }
}
