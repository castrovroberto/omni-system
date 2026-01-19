package com.omni.app.log;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Interval Tests")
class IntervalTest {

  @Nested
  @DisplayName("Creation Tests")
  class CreationTests {

    @Test
    @DisplayName("Creates interval with builder pattern")
    void builder_validInterval_createsSuccessfully() {
      Interval interval = Interval.builder().start(10).end(20).build();

      assertEquals(10, interval.getStart());
      assertEquals(20, interval.getEnd());
    }

    @Test
    @DisplayName("Creates interval with static factory method")
    void of_validInterval_createsSuccessfully() {
      Interval interval = Interval.of(5, 15);

      assertEquals(5, interval.getStart());
      assertEquals(15, interval.getEnd());
    }

    @Test
    @DisplayName("Allows point interval where start equals end")
    void of_pointInterval_createsSuccessfully() {
      Interval interval = Interval.of(10, 10);

      assertEquals(10, interval.getStart());
      assertEquals(10, interval.getEnd());
    }

    @Test
    @DisplayName("Throws exception when start is greater than end")
    void of_startGreaterThanEnd_throwsException() {
      assertThrows(IllegalArgumentException.class, () -> Interval.of(20, 10));
    }
  }

  @Nested
  @DisplayName("Overlap Tests")
  class OverlapTests {

    @Test
    @DisplayName("Detects overlapping intervals")
    void overlaps_overlappingIntervals_returnsTrue() {
      Interval interval1 = Interval.of(1, 5);
      Interval interval2 = Interval.of(3, 8);

      assertTrue(interval1.overlaps(interval2));
      assertTrue(interval2.overlaps(interval1));
    }

    @Test
    @DisplayName("Detects adjacent intervals as overlapping")
    void overlaps_adjacentIntervals_returnsTrue() {
      Interval interval1 = Interval.of(1, 5);
      Interval interval2 = Interval.of(5, 10);

      assertTrue(interval1.overlaps(interval2));
      assertTrue(interval2.overlaps(interval1));
    }

    @Test
    @DisplayName("Detects non-overlapping intervals")
    void overlaps_nonOverlappingIntervals_returnsFalse() {
      Interval interval1 = Interval.of(1, 5);
      Interval interval2 = Interval.of(7, 10);

      assertFalse(interval1.overlaps(interval2));
      assertFalse(interval2.overlaps(interval1));
    }

    @Test
    @DisplayName("Detects contained interval as overlapping")
    void overlaps_containedInterval_returnsTrue() {
      Interval outer = Interval.of(1, 10);
      Interval inner = Interval.of(3, 7);

      assertTrue(outer.overlaps(inner));
      assertTrue(inner.overlaps(outer));
    }

    @Test
    @DisplayName("Detects identical intervals as overlapping")
    void overlaps_identicalIntervals_returnsTrue() {
      Interval interval1 = Interval.of(5, 10);
      Interval interval2 = Interval.of(5, 10);

      assertTrue(interval1.overlaps(interval2));
    }

    @Test
    @DisplayName("Throws exception for null interval")
    void overlaps_nullInterval_throwsException() {
      Interval interval = Interval.of(1, 5);

      assertThrows(IllegalArgumentException.class, () -> interval.overlaps(null));
    }
  }

  @Nested
  @DisplayName("Merge Tests")
  class MergeTests {

    @Test
    @DisplayName("Merges overlapping intervals correctly")
    void merge_overlappingIntervals_returnsMerged() {
      Interval interval1 = Interval.of(1, 5);
      Interval interval2 = Interval.of(3, 8);

      Interval merged = interval1.merge(interval2);

      assertEquals(1, merged.getStart());
      assertEquals(8, merged.getEnd());
    }

    @Test
    @DisplayName("Merges adjacent intervals correctly")
    void merge_adjacentIntervals_returnsMerged() {
      Interval interval1 = Interval.of(1, 5);
      Interval interval2 = Interval.of(5, 10);

      Interval merged = interval1.merge(interval2);

      assertEquals(1, merged.getStart());
      assertEquals(10, merged.getEnd());
    }

    @Test
    @DisplayName("Merges when one interval contains another")
    void merge_containedInterval_returnsOuter() {
      Interval outer = Interval.of(1, 10);
      Interval inner = Interval.of(3, 7);

      Interval merged = outer.merge(inner);

      assertEquals(1, merged.getStart());
      assertEquals(10, merged.getEnd());
    }

    @Test
    @DisplayName("Throws exception when merging non-overlapping intervals")
    void merge_nonOverlappingIntervals_throwsException() {
      Interval interval1 = Interval.of(1, 5);
      Interval interval2 = Interval.of(7, 10);

      assertThrows(IllegalArgumentException.class, () -> interval1.merge(interval2));
    }

    @Test
    @DisplayName("Throws exception for null interval")
    void merge_nullInterval_throwsException() {
      Interval interval = Interval.of(1, 5);

      assertThrows(IllegalArgumentException.class, () -> interval.merge(null));
    }
  }

  @Nested
  @DisplayName("Comparison Tests")
  class ComparisonTests {

    @Test
    @DisplayName("Compares by start time first")
    void compareTo_differentStartTimes_comparesCorrectly() {
      Interval earlier = Interval.of(1, 10);
      Interval later = Interval.of(5, 8);

      assertTrue(earlier.compareTo(later) < 0);
      assertTrue(later.compareTo(earlier) > 0);
    }

    @Test
    @DisplayName("Compares by end time when start times are equal")
    void compareTo_sameStartDifferentEnd_comparesCorrectly() {
      Interval shorter = Interval.of(5, 8);
      Interval longer = Interval.of(5, 12);

      assertTrue(shorter.compareTo(longer) < 0);
      assertTrue(longer.compareTo(shorter) > 0);
    }

    @Test
    @DisplayName("Returns zero for equal intervals")
    void compareTo_equalIntervals_returnsZero() {
      Interval interval1 = Interval.of(5, 10);
      Interval interval2 = Interval.of(5, 10);

      assertEquals(0, interval1.compareTo(interval2));
    }
  }

  @Nested
  @DisplayName("Equality Tests")
  class EqualityTests {

    @Test
    @DisplayName("Equal intervals are equal")
    void equals_equalIntervals_returnsTrue() {
      Interval interval1 = Interval.of(1, 5);
      Interval interval2 = Interval.of(1, 5);

      assertEquals(interval1, interval2);
      assertEquals(interval1.hashCode(), interval2.hashCode());
    }

    @Test
    @DisplayName("Different intervals are not equal")
    void equals_differentIntervals_returnsFalse() {
      Interval interval1 = Interval.of(1, 5);
      Interval interval2 = Interval.of(1, 6);
      Interval interval3 = Interval.of(2, 5);

      assertNotEquals(interval1, interval2);
      assertNotEquals(interval1, interval3);
    }

    @Test
    @DisplayName("Interval is not equal to null")
    void equals_null_returnsFalse() {
      Interval interval = Interval.of(1, 5);

      assertNotEquals(null, interval);
    }

    @Test
    @DisplayName("Interval is not equal to other types")
    void equals_otherType_returnsFalse() {
      Interval interval = Interval.of(1, 5);

      assertNotEquals("not an interval", interval);
    }
  }

  @Nested
  @DisplayName("String Representation Tests")
  class ToStringTests {

    @Test
    @DisplayName("toString returns readable format")
    void toString_returnsReadableFormat() {
      Interval interval = Interval.of(10, 20);

      assertEquals("[10, 20]", interval.toString());
    }
  }
}
