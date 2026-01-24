package com.omni.core.map.hash;

/**
 * Default hash strategy using Java's built-in {@code hashCode()} method.
 *
 * <p>This strategy delegates to the object's own hash code implementation, which is typically
 * well-optimized for the specific type. It handles null keys by mapping them to index 0.
 *
 * @param <K> the type of keys to hash
 */
public class DefaultHashStrategy<K> implements HashStrategy<K> {

  @Override
  public int hash(K key, int capacity) {
    if (key == null) {
      return 0;
    }
    // Use bitwise AND with (capacity - 1) for power-of-2 capacities
    // This is equivalent to modulo but faster
    // For non-power-of-2, fall back to modulo
    int h = key.hashCode();
    // Spread bits to reduce collisions from poor hashCode implementations
    h ^= (h >>> 16);
    return Math.abs(h % capacity);
  }
}
