package com.omni.app.log;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("LogSearcher Tests")
class LogSearcherTest {

  private EventLog eventLog;
  private SystemEvent event1;
  private SystemEvent event2;
  private SystemEvent event3;

  @BeforeEach
  void setUp() {
    eventLog = new EventLog();

    Instant baseTime = Instant.parse("2026-01-15T10:00:00Z");
    event1 =
        SystemEvent.builder()
            .timestamp(baseTime)
            .severity(SystemEvent.Severity.INFO)
            .source("service1")
            .message("Event 1")
            .build();

    event2 =
        SystemEvent.builder()
            .timestamp(baseTime.plusSeconds(60))
            .severity(SystemEvent.Severity.WARNING)
            .source("service2")
            .message("Event 2")
            .build();

    event3 =
        SystemEvent.builder()
            .timestamp(baseTime.plusSeconds(120))
            .severity(SystemEvent.Severity.ERROR)
            .source("service1")
            .message("Event 3")
            .build();
  }

  @Nested
  @DisplayName("Linear Search Tests")
  class LinearSearchTests {

    @Test
    @DisplayName("SearchByTimestamp finds event")
    void searchByTimestamp_findsEvent() {
      eventLog.add(event1);
      eventLog.add(event2);
      eventLog.add(event3);

      int index = LogSearcher.searchByTimestamp(eventLog, event2.getTimestamp());

      assertEquals(1, index);
    }

    @Test
    @DisplayName("SearchByTimestamp returns -1 when not found")
    void searchByTimestamp_notFound_returnsMinusOne() {
      eventLog.add(event1);
      eventLog.add(event2);

      Instant notFound = Instant.parse("2026-01-15T11:00:00Z");
      int index = LogSearcher.searchByTimestamp(eventLog, notFound);

      assertEquals(-1, index);
    }

    @Test
    @DisplayName("SearchByTimestamp with null log returns -1")
    void searchByTimestamp_nullLog_returnsMinusOne() {
      int index = LogSearcher.searchByTimestamp(null, Instant.now());
      assertEquals(-1, index);
    }

    @Test
    @DisplayName("SearchByTimestamp with null timestamp returns -1")
    void searchByTimestamp_nullTimestamp_returnsMinusOne() {
      eventLog.add(event1);
      int index = LogSearcher.searchByTimestamp(eventLog, null);
      assertEquals(-1, index);
    }

    @Test
    @DisplayName("SearchBySeverity finds event")
    void searchBySeverity_findsEvent() {
      eventLog.add(event1);
      eventLog.add(event2);
      eventLog.add(event3);

      int index = LogSearcher.searchBySeverity(eventLog, SystemEvent.Severity.WARNING);

      assertEquals(1, index);
    }

    @Test
    @DisplayName("SearchBySeverity returns -1 when not found")
    void searchBySeverity_notFound_returnsMinusOne() {
      eventLog.add(event1);
      eventLog.add(event2);

      int index = LogSearcher.searchBySeverity(eventLog, SystemEvent.Severity.CRITICAL);

      assertEquals(-1, index);
    }

    @Test
    @DisplayName("SearchBySource finds event")
    void searchBySource_findsEvent() {
      eventLog.add(event1);
      eventLog.add(event2);
      eventLog.add(event3);

      int index = LogSearcher.searchBySource(eventLog, "service2");

      assertEquals(1, index);
    }

    @Test
    @DisplayName("SearchBySource finds first occurrence")
    void searchBySource_findsFirstOccurrence() {
      eventLog.add(event1);
      eventLog.add(event2);
      eventLog.add(event3);

      // event1 and event3 both have source "service1"
      int index = LogSearcher.searchBySource(eventLog, "service1");

      assertEquals(0, index); // Should find first occurrence
    }

    @Test
    @DisplayName("SearchBySource returns -1 when not found")
    void searchBySource_notFound_returnsMinusOne() {
      eventLog.add(event1);
      eventLog.add(event2);

      int index = LogSearcher.searchBySource(eventLog, "nonexistent");

      assertEquals(-1, index);
    }
  }

  @Nested
  @DisplayName("Binary Search Tests")
  class BinarySearchTests {

    @Test
    @DisplayName("BinarySearchByTimestamp finds event in sorted log")
    void binarySearchByTimestamp_inSortedLog_findsEvent() {
      // Add events in sorted order (by timestamp)
      eventLog.add(event1);
      eventLog.add(event2);
      eventLog.add(event3);

      int index = LogSearcher.binarySearchByTimestamp(eventLog, event2.getTimestamp());

      assertEquals(1, index);
    }

    @Test
    @DisplayName("BinarySearchByTimestamp returns negative value when not found")
    void binarySearchByTimestamp_notFound_returnsNegative() {
      eventLog.add(event1);
      eventLog.add(event3); // Skip event2

      Instant notFound = event2.getTimestamp();
      int index = LogSearcher.binarySearchByTimestamp(eventLog, notFound);

      // Should return negative value (insertion point)
      assertTrue(index < 0);
    }

    @Test
    @DisplayName("BinarySearchByTimestamp with null log throws exception")
    void binarySearchByTimestamp_nullLog_throwsException() {
      assertThrows(
          IllegalArgumentException.class,
          () -> LogSearcher.binarySearchByTimestamp(null, Instant.now()));
    }

    @Test
    @DisplayName("BinarySearchByTimestamp with null timestamp throws exception")
    void binarySearchByTimestamp_nullTimestamp_throwsException() {
      eventLog.add(event1);
      assertThrows(
          IllegalArgumentException.class,
          () -> LogSearcher.binarySearchByTimestamp(eventLog, null));
    }
  }
}
