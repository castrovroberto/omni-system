package com.omni.core.concurrent;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLongArray;

/**
 * Lock-free hit counter using atomic operations.
 *
 * <p>
 * <b>Interview Question:</b> "Design Hit Counter" (LeetCode #362) - this is an
 * alternative
 * implementation to demonstrate understanding of lock-free concurrency.
 *
 * <p>
 * <b>Design:</b> Uses AtomicInteger[] and AtomicLongArray for CAS-based updates
 * without locks.
 *
 * <p>
 * <b>Interview Talking Points:</b>
 *
 * <ul>
 * <li>Why AtomicInteger over synchronized? Zero contention - CAS operations are
 * faster
 * <li>Trade-off: Slightly less accurate under extreme contention (lost updates
 * rare)
 * <li>Compare with ReentrantReadWriteLock version for read-heavy workloads
 * </ul>
 *
 * <p>
 * <b>Comparison with HitCounter:</b>
 *
 * <table border="1">
 * <tr>
 * <th>Approach</th>
 * <th>Contention</th>
 * <th>Best For</th>
 * </tr>
 * <tr>
 * <td>ReentrantReadWriteLock</td>
 * <td>Low for reads</td>
 * <td>Read-heavy workloads</td>
 * </tr>
 * <tr>
 * <td>AtomicInteger (this)</td>
 * <td>Lock-free</td>
 * <td>High-throughput writes</td>
 * </tr>
 * </table>
 */
public class AtomicHitCounter {

    private static final int DEFAULT_WINDOW_SECONDS = 300;

    private final int windowSeconds;
    private final AtomicInteger[] hits;
    private final AtomicLongArray timestamps;

    /** Creates a hit counter with default 5-minute window. */
    public AtomicHitCounter() {
        this(DEFAULT_WINDOW_SECONDS);
    }

    /**
     * Creates a hit counter with specified window size.
     *
     * @param windowSeconds the window size in seconds
     * @throws IllegalArgumentException if windowSeconds <= 0
     */
    public AtomicHitCounter(int windowSeconds) {
        if (windowSeconds <= 0) {
            throw new IllegalArgumentException("Window must be positive");
        }
        this.windowSeconds = windowSeconds;
        this.hits = new AtomicInteger[windowSeconds];
        this.timestamps = new AtomicLongArray(windowSeconds);

        for (int i = 0; i < windowSeconds; i++) {
            hits[i] = new AtomicInteger(0);
        }
    }

    /**
     * Records a hit at the given timestamp.
     *
     * <p>
     * <b>Thread Safety:</b> Uses CAS operations - lock-free but may retry under
     * contention.
     *
     * @param timestampSeconds the timestamp in seconds since epoch
     */
    public void hit(long timestampSeconds) {
        int bucket = (int) (timestampSeconds % windowSeconds);

        // CAS loop to update timestamp and reset if needed
        long currentTimestamp = timestamps.get(bucket);
        if (currentTimestamp != timestampSeconds) {
            // Try to claim this bucket for the new timestamp
            if (timestamps.compareAndSet(bucket, currentTimestamp, timestampSeconds)) {
                hits[bucket].set(1);
                return;
            }
            // Someone else updated, re-read
            currentTimestamp = timestamps.get(bucket);
        }

        // Same timestamp, just increment
        if (currentTimestamp == timestampSeconds) {
            hits[bucket].incrementAndGet();
        } else {
            // Timestamp changed again, recurse (rare)
            hit(timestampSeconds);
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
     * <p>
     * <b>Thread Safety:</b> Lock-free reads. May include in-flight updates.
     *
     * @param timestampSeconds the timestamp in seconds since epoch
     * @return the total hits in the window
     */
    public int getHits(long timestampSeconds) {
        int total = 0;
        for (int i = 0; i < windowSeconds; i++) {
            if (timestampSeconds - timestamps.get(i) < windowSeconds) {
                total += hits[i].get();
            }
        }
        return total;
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
