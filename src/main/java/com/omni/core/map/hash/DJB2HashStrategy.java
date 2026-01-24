package com.omni.core.map.hash;

/**
 * DJB2 hash strategy for String keys.
 *
 * <p>DJB2 is a simple and effective hash function created by Daniel J. Bernstein. It has good
 * distribution properties and is fast to compute.
 *
 * <p>Algorithm:
 *
 * <pre>
 * hash = 5381
 * for each character c:
 *     hash = hash * 33 + c
 *     (equivalent to: hash = ((hash << 5) + hash) + c)
 * </pre>
 *
 * <p>The magic number 5381 and multiplier 33 were chosen empirically to provide good distribution.
 */
public class DJB2HashStrategy implements HashStrategy<String> {

  private static final long INITIAL_HASH = 5381L;

  @Override
  public int hash(String key, int capacity) {
    if (key == null) {
      return 0;
    }

    long hash = INITIAL_HASH;
    for (int i = 0; i < key.length(); i++) {
      char c = key.charAt(i);
      // hash * 33 + c, using bit shift for speed
      hash = ((hash << 5) + hash) + c;
    }

    return (int) (Math.abs(hash) % capacity);
  }
}
