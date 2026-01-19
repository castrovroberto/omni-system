package com.omni.app.log;

import java.util.Objects;

/**
 * Represents a time interval with start and end timestamps.
 *
 * <p>Uses the Builder pattern to construct immutable interval objects, consistent with {@link
 * SystemEvent}.
 *
 * <p>Intervals are commonly used for:
 *
 * <ul>
 *   <li>Maintenance windows
 *   <li>Scheduled downtime periods
 *   <li>Time-based event grouping
 *   <li>Resource reservation slots
 * </ul>
 */
public final class Interval implements Comparable<Interval> {

  private final long start;
  private final long end;

  private Interval(Builder builder) {
    if (builder.start > builder.end) {
      throw new IllegalArgumentException("Start time must be <= end time");
    }
    this.start = builder.start;
    this.end = builder.end;
  }

  /**
   * Creates a new builder for constructing an Interval.
   *
   * @return a new builder instance
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Static factory method for convenient interval creation.
   *
   * @param start the start time (inclusive)
   * @param end the end time (inclusive)
   * @return a new Interval instance
   * @throws IllegalArgumentException if start > end
   */
  public static Interval of(long start, long end) {
    return builder().start(start).end(end).build();
  }

  public long getStart() {
    return start;
  }

  public long getEnd() {
    return end;
  }

  /**
   * Checks if this interval overlaps with another interval.
   *
   * <p>Two intervals overlap if they share any common time points. Adjacent intervals (where one
   * ends exactly when another starts) are considered overlapping for merging purposes.
   *
   * <pre>
   *   Case 1: Overlapping
   *   this:  [-----]
   *   other:    [-----]
   *
   *   Case 2: Adjacent (considered overlapping)
   *   this:  [-----]
   *   other:       [-----]
   *
   *   Case 3: Non-overlapping
   *   this:  [-----]
   *   other:          [-----]
   * </pre>
   *
   * @param other the other interval to check
   * @return true if the intervals overlap or are adjacent
   * @throws IllegalArgumentException if other is null
   */
  public boolean overlaps(Interval other) {
    if (other == null) {
      throw new IllegalArgumentException("Interval cannot be null");
    }
    // Intervals overlap if neither is completely before the other
    // Using >= to consider adjacent intervals as overlapping (for merging)
    return this.start <= other.end && other.start <= this.end;
  }

  /**
   * Merges this interval with another overlapping interval.
   *
   * <p>The merged interval spans from the earliest start to the latest end.
   *
   * <pre>
   *   this:  [1----5]
   *   other:    [3----8]
   *   merged: [1------8]
   * </pre>
   *
   * @param other the other interval to merge with
   * @return a new Interval representing the merged range
   * @throws IllegalArgumentException if other is null or intervals don't overlap
   */
  public Interval merge(Interval other) {
    if (other == null) {
      throw new IllegalArgumentException("Interval cannot be null");
    }
    if (!this.overlaps(other)) {
      throw new IllegalArgumentException("Cannot merge non-overlapping intervals");
    }
    return of(Math.min(this.start, other.start), Math.max(this.end, other.end));
  }

  /**
   * Compares this interval to another based on start time.
   *
   * <p>If start times are equal, compares by end time.
   *
   * @param other the other interval to compare
   * @return negative if this starts before other, positive if after, zero if equal
   */
  @Override
  public int compareTo(Interval other) {
    int startCompare = Long.compare(this.start, other.start);
    if (startCompare != 0) {
      return startCompare;
    }
    return Long.compare(this.end, other.end);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Interval interval = (Interval) o;
    return start == interval.start && end == interval.end;
  }

  @Override
  public int hashCode() {
    return Objects.hash(start, end);
  }

  @Override
  public String toString() {
    return String.format("[%d, %d]", start, end);
  }

  /**
   * Builder for constructing Interval instances.
   *
   * <p>Example usage:
   *
   * <pre>
   * Interval interval = Interval.builder()
   *     .start(1000)
   *     .end(2000)
   *     .build();
   * </pre>
   */
  public static class Builder {
    private long start;
    private long end;

    public Builder start(long start) {
      this.start = start;
      return this;
    }

    public Builder end(long end) {
      this.end = end;
      return this;
    }

    /**
     * Builds the immutable Interval instance.
     *
     * @return the constructed Interval
     * @throws IllegalArgumentException if start > end
     */
    public Interval build() {
      return new Interval(this);
    }
  }
}
