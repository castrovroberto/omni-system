package com.omni.core.map.hash;

/**
 * Strategy interface for computing hash values.
 *
 * <p>This interface enables swapping different hashing algorithms at runtime, following the
 * Strategy design pattern. Different hash functions have different trade-offs:
 *
 * <ul>
 *   <li>Speed vs. distribution quality
 *   <li>Memory usage
 *   <li>Cryptographic strength (not applicable for hash tables)
 * </ul>
 *
 * <p>For hash tables, we prioritize uniform distribution to minimize collisions.
 *
 * @param <K> the type of keys to hash
 */
public interface HashStrategy<K> {

  /**
   * Computes a hash value for the given key.
   *
   * <p>The returned value should be non-negative and less than {@code capacity}. Implementations
   * must ensure uniform distribution across the range [0, capacity) to minimize collisions.
   *
   * @param key the key to hash (may be null)
   * @param capacity the size of the hash table (always positive)
   * @return a non-negative integer less than capacity
   */
  int hash(K key, int capacity);
}
