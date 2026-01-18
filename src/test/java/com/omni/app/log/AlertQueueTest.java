package com.omni.app.log;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("AlertQueue Tests")
class AlertQueueTest {

  private AlertQueue alertQueue;
  private SystemEvent alert1;
  private SystemEvent alert2;

  @BeforeEach
  void setUp() {
    alertQueue = new AlertQueue();
    alert1 =
        SystemEvent.builder()
            .timestamp(Instant.parse("2026-01-15T10:00:00Z"))
            .severity(SystemEvent.Severity.ERROR)
            .source("service1")
            .message("Alert 1")
            .build();

    alert2 =
        SystemEvent.builder()
            .timestamp(Instant.parse("2026-01-15T10:01:00Z"))
            .severity(SystemEvent.Severity.CRITICAL)
            .source("service2")
            .message("Alert 2")
            .build();
  }

  @Nested
  @DisplayName("Basic Operations")
  class BasicOperationsTests {

    @Test
    @DisplayName("New AlertQueue is empty")
    void newAlertQueue_isEmpty() {
      assertTrue(alertQueue.isEmpty());
      assertEquals(0, alertQueue.size());
    }

    @Test
    @DisplayName("AddAlert adds alert to queue")
    void addAlert_addsAlertToQueue() {
      alertQueue.addAlert(alert1);

      assertFalse(alertQueue.isEmpty());
      assertEquals(1, alertQueue.size());
    }

    @Test
    @DisplayName("AddAlert adds to front (LIFO insertion)")
    void addAlert_addsToFront() {
      alertQueue.addAlert(alert1);
      alertQueue.addAlert(alert2);

      // Last added should be first (addFirst behavior)
      assertEquals(alert2, alertQueue.peekNextAlert());
    }

    @Test
    @DisplayName("GetNextAlert removes and returns first alert")
    void getNextAlert_removesAndReturnsFirst() {
      alertQueue.addAlert(alert1);
      alertQueue.addAlert(alert2);

      SystemEvent first = alertQueue.getNextAlert();
      assertEquals(alert2, first); // Last added is first (LIFO insertion)
      assertEquals(1, alertQueue.size());
      assertEquals(alert1, alertQueue.peekNextAlert());
    }

    @Test
    @DisplayName("PeekNextAlert does not remove alert")
    void peekNextAlert_doesNotRemove() {
      alertQueue.addAlert(alert1);

      SystemEvent peeked = alertQueue.peekNextAlert();
      assertEquals(alert1, peeked);
      assertEquals(1, alertQueue.size());

      // Peek again - should be same
      assertEquals(alert1, alertQueue.peekNextAlert());
      assertEquals(1, alertQueue.size());
    }

    @Test
    @DisplayName("Clear removes all alerts")
    void clear_removesAllAlerts() {
      alertQueue.addAlert(alert1);
      alertQueue.addAlert(alert2);

      alertQueue.clear();

      assertTrue(alertQueue.isEmpty());
      assertEquals(0, alertQueue.size());
    }
  }

  @Nested
  @DisplayName("Edge Cases")
  class EdgeCaseTests {

    @Test
    @DisplayName("AddAlert with null throws exception")
    void addAlert_nullAlert_throwsException() {
      assertThrows(NullPointerException.class, () -> alertQueue.addAlert(null));
    }

    @Test
    @DisplayName("GetNextAlert on empty queue throws exception")
    void getNextAlert_emptyQueue_throwsException() {
      assertThrows(java.util.NoSuchElementException.class, () -> alertQueue.getNextAlert());
    }

    @Test
    @DisplayName("PeekNextAlert on empty queue throws exception")
    void peekNextAlert_emptyQueue_throwsException() {
      assertThrows(java.util.NoSuchElementException.class, () -> alertQueue.peekNextAlert());
    }
  }

  @Nested
  @DisplayName("Performance Tests")
  class PerformanceTests {

    @Test
    @DisplayName("AddAlert is O(1) - demonstrates fast insertion")
    void addAlert_isO1() {
      // Add many alerts - should be fast (O(1) per operation)
      for (int i = 0; i < 100_000; i++) {
        SystemEvent alert =
            SystemEvent.builder()
                .timestamp(Instant.now())
                .severity(SystemEvent.Severity.ERROR)
                .source("test")
                .message("Alert " + i)
                .build();
        alertQueue.addAlert(alert);
      }

      assertEquals(100_000, alertQueue.size());
    }
  }
}
