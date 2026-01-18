package com.omni.app.log;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("EventLog Tests")
class EventLogTest {

  private EventLog eventLog;
  private SystemEvent event1;
  private SystemEvent event2;

  @BeforeEach
  void setUp() {
    eventLog = new EventLog();
    event1 =
        SystemEvent.builder()
            .timestamp(Instant.parse("2026-01-15T10:00:00Z"))
            .severity(SystemEvent.Severity.INFO)
            .source("service1")
            .message("Event 1")
            .build();

    event2 =
        SystemEvent.builder()
            .timestamp(Instant.parse("2026-01-15T10:01:00Z"))
            .severity(SystemEvent.Severity.WARNING)
            .source("service2")
            .message("Event 2")
            .build();
  }

  @Nested
  @DisplayName("Basic Operations")
  class BasicOperationsTests {

    @Test
    @DisplayName("New EventLog is empty")
    void newEventLog_isEmpty() {
      assertTrue(eventLog.isEmpty());
      assertEquals(0, eventLog.size());
    }

    @Test
    @DisplayName("Add event to empty log")
    void add_toEmptyLog_addsEvent() {
      eventLog.add(event1);

      assertFalse(eventLog.isEmpty());
      assertEquals(1, eventLog.size());
      assertEquals(event1, eventLog.get(0));
    }

    @Test
    @DisplayName("Add multiple events stores in order")
    void add_multipleEvents_storesInOrder() {
      eventLog.add(event1);
      eventLog.add(event2);

      assertEquals(2, eventLog.size());
      assertEquals(event1, eventLog.get(0));
      assertEquals(event2, eventLog.get(1));
    }

    @Test
    @DisplayName("Get event by index")
    void get_byIndex_returnsEvent() {
      eventLog.add(event1);
      eventLog.add(event2);

      assertEquals(event1, eventLog.get(0));
      assertEquals(event2, eventLog.get(1));
    }

    @Test
    @DisplayName("Size returns correct count")
    void size_returnsCorrectCount() {
      assertEquals(0, eventLog.size());

      eventLog.add(event1);
      assertEquals(1, eventLog.size());

      eventLog.add(event2);
      assertEquals(2, eventLog.size());
    }

    @Test
    @DisplayName("Clear removes all events")
    void clear_removesAllEvents() {
      eventLog.add(event1);
      eventLog.add(event2);

      eventLog.clear();

      assertTrue(eventLog.isEmpty());
      assertEquals(0, eventLog.size());
    }
  }

  @Nested
  @DisplayName("Edge Cases")
  class EdgeCaseTests {

    @Test
    @DisplayName("Add null event throws exception")
    void add_nullEvent_throwsException() {
      assertThrows(NullPointerException.class, () -> eventLog.add(null));
    }

    @Test
    @DisplayName("Get with invalid index throws exception")
    void get_invalidIndex_throwsException() {
      assertThrows(IndexOutOfBoundsException.class, () -> eventLog.get(0));

      eventLog.add(event1);
      assertThrows(IndexOutOfBoundsException.class, () -> eventLog.get(1));
      assertThrows(IndexOutOfBoundsException.class, () -> eventLog.get(-1));
    }
  }

  @Nested
  @DisplayName("Iteration Tests")
  class IterationTests {

    @Test
    @DisplayName("GetAll returns iterable")
    void getAll_returnsIterable() {
      eventLog.add(event1);
      eventLog.add(event2);

      int count = 0;
      for (SystemEvent event : eventLog.getAll()) {
        assertNotNull(event);
        count++;
      }

      assertEquals(2, count);
    }

    @Test
    @DisplayName("Iteration preserves order")
    void iteration_preservesOrder() {
      eventLog.add(event1);
      eventLog.add(event2);

      SystemEvent[] events = new SystemEvent[2];
      int index = 0;
      for (SystemEvent event : eventLog.getAll()) {
        events[index++] = event;
      }

      assertEquals(event1, events[0]);
      assertEquals(event2, events[1]);
    }
  }
}
