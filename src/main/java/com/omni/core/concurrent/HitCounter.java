package com.omni.core.concurrent;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Thread-safe hit counter that tracks requests in a sliding time window.
 *
 * <p><b>Interview Question:</b> "Design Hit Counter" (LeetCode #362) - commonly asked for Java
 * roles to test understanding of concurrency primitives.
 *
 * <p><b>Design:</b> Uses circular buffer of seconds with separate read/write locks to allow
 * concurrent reads while updates are exclusive.
 *
 * <p><b>Interview Talking Points:</b>
 *
 * <ul>
 *   <li>Why ReentrantReadWriteLock over synchronized? Allows concurrent reads (metrics reporting)
 *   <li>Why circular buffer? O(1) space with fixed window size
 *   <li>Trade-off: Accuracy vs performance (bucket granularity)
 * </ul>
 */
public class HitCounter {

  private static final int DEFAULT_WINDOW_SECONDS = 300; // 5 minutes

  private final int windowSeconds;
  private final int[] hits;
  private final long[] timestamps;
  private final ReentrantReadWriteLock lock;

  /** Creates a hit counter with default 5-minute window. */
  public HitCounter() {
    this(DEFAULT_WINDOW_SECONDS);
  }

  /**
   * Creates a hit counter with specified window size.
   *
   * @param windowSeconds the window size in seconds
   */
  public HitCounter(int windowSeconds) {
    if (windowSeconds <= 0) {
      throw new IllegalArgumentException("Window must be positive");
    }
    this.windowSeconds = windowSeconds;
    this.hits = new int[windowSeconds];
    this.timestamps = new long[windowSeconds];
    this.lock = new ReentrantReadWriteLock();
  }

  /**
   * Records a hit at the given timestamp.
   *
   * <p><b>Thread Safety:</b> Uses write lock - exclusive access during update.
   *
   * @param timestampSeconds the timestamp in seconds since epoch
   */
  public void hit(long timestampSeconds) {
    lock.writeLock().lock();
    try {
      int bucket = (int) (timestampSeconds % windowSeconds);
      if (timestamps[bucket] != timestampSeconds) {
        // New second, reset bucket
        timestamps[bucket] = timestampSeconds;
        hits[bucket] = 1;
      } else {
        // Same second, increment
        hits[bucket]++;
      }
    } finally {
      lock.writeLock().unlock();
    }
  }

  /**
   * Records a hit at the current time.
   *
   * @see #hit(long)
   */
  public void hit() {
    hit(System.currentTimeMillis() / 1000);
  }

  /**
   * Gets the number of hits in the window ending at the given timestamp.
   *
   * <p><b>Thread Safety:</b> Uses read lock - allows concurrent reads.
   *
   * @param timestampSeconds the timestamp in seconds since epoch
   * @return the total hits in the window
   */
  public int getHits(long timestampSeconds) {
    lock.readLock().lock();
    try {
      int total = 0;
      for (int i = 0; i < windowSeconds; i++) {
        if (timestampSeconds - timestamps[i] < windowSeconds) {
          total += hits[i];
        }
      }
      return total;
    } finally {
      lock.readLock().unlock();
    }
  }

  /**
   * Gets the number of hits in the window ending now.
   *
   * @return the total hits in the window
   */
  public int getHits() {
    return getHits(System.currentTimeMillis() / 1000);
  }

  /**
   * Returns the window size in seconds.
   *
   * @return the window size
   */
  public int getWindowSeconds() {
    return windowSeconds;
  }
}
