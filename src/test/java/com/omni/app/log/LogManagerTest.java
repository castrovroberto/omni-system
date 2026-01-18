package com.omni.app.log;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("LogManager Tests - Singleton Pattern")
class LogManagerTest {

  @BeforeEach
  void setUp() throws Exception {
    // Reset singleton instance for each test
    Field instanceField = LogManager.class.getDeclaredField("instance");
    instanceField.setAccessible(true);
    instanceField.set(null, null);
  }

  @Nested
  @DisplayName("Singleton Pattern Tests")
  class SingletonPatternTests {

    @Test
    @DisplayName("GetInstance returns same instance")
    void getInstance_returnsSameInstance() {
      LogManager instance1 = LogManager.getInstance();
      LogManager instance2 = LogManager.getInstance();

      assertNotNull(instance1);
      assertSame(instance1, instance2, "Singleton should return same instance");
    }

    @Test
    @DisplayName("GetInstance is thread-safe")
    void getInstance_isThreadSafe() throws InterruptedException {
      final LogManager[] instances = new LogManager[10];
      Thread[] threads = new Thread[10];

      for (int i = 0; i < 10; i++) {
        final int index = i;
        threads[i] =
            new Thread(
                () -> {
                  instances[index] = LogManager.getInstance();
                });
      }

      for (Thread thread : threads) {
        thread.start();
      }

      for (Thread thread : threads) {
        thread.join();
      }

      // All instances should be the same
      LogManager first = instances[0];
      for (LogManager instance : instances) {
        assertSame(first, instance, "All instances should be the same");
      }
    }
  }

  @Nested
  @DisplayName("Logging Functionality")
  class LoggingFunctionalityTests {

    @Test
    @DisplayName("Log adds event to event log")
    void log_addsEventToEventLog() {
      LogManager logger = LogManager.getInstance();
      SystemEvent event =
          SystemEvent.builder()
              .timestamp(Instant.now())
              .severity(SystemEvent.Severity.INFO)
              .source("test")
              .message("Test message")
              .build();

      logger.log(event);

      assertEquals(1, logger.getEventCount());
      assertEquals(event, logger.getEventLog().get(0));
    }

    @Test
    @DisplayName("Log with null event throws exception")
    void log_nullEvent_throwsException() {
      LogManager logger = LogManager.getInstance();
      assertThrows(NullPointerException.class, () -> logger.log(null));
    }

    @Test
    @DisplayName("Log ERROR events adds to alert queue")
    void log_errorEvent_addsToAlertQueue() {
      LogManager logger = LogManager.getInstance();
      SystemEvent errorEvent =
          SystemEvent.builder()
              .timestamp(Instant.now())
              .severity(SystemEvent.Severity.ERROR)
              .source("test")
              .message("Error message")
              .build();

      logger.log(errorEvent);

      assertEquals(1, logger.getEventCount());
      assertEquals(1, logger.getAlertCount());
      assertEquals(errorEvent, logger.getAlertQueue().peekNextAlert());
    }

    @Test
    @DisplayName("Log CRITICAL events adds to alert queue")
    void log_criticalEvent_addsToAlertQueue() {
      LogManager logger = LogManager.getInstance();
      SystemEvent criticalEvent =
          SystemEvent.builder()
              .timestamp(Instant.now())
              .severity(SystemEvent.Severity.CRITICAL)
              .source("test")
              .message("Critical message")
              .build();

      logger.log(criticalEvent);

      assertEquals(1, logger.getEventCount());
      assertEquals(1, logger.getAlertCount());
      assertEquals(criticalEvent, logger.getAlertQueue().peekNextAlert());
    }

    @Test
    @DisplayName("Log INFO events does not add to alert queue")
    void log_infoEvent_doesNotAddToAlertQueue() {
      LogManager logger = LogManager.getInstance();
      SystemEvent infoEvent =
          SystemEvent.builder()
              .timestamp(Instant.now())
              .severity(SystemEvent.Severity.INFO)
              .source("test")
              .message("Info message")
              .build();

      logger.log(infoEvent);

      assertEquals(1, logger.getEventCount());
      assertEquals(0, logger.getAlertCount());
    }

    @Test
    @DisplayName("Clear removes all logs and alerts")
    void clear_removesAllLogsAndAlerts() {
      LogManager logger = LogManager.getInstance();

      logger.log(
          SystemEvent.builder()
              .timestamp(Instant.now())
              .severity(SystemEvent.Severity.INFO)
              .source("test")
              .message("Info")
              .build());

      logger.log(
          SystemEvent.builder()
              .timestamp(Instant.now())
              .severity(SystemEvent.Severity.ERROR)
              .source("test")
              .message("Error")
              .build());

      assertEquals(2, logger.getEventCount());
      assertEquals(1, logger.getAlertCount());

      logger.clear();

      assertEquals(0, logger.getEventCount());
      assertEquals(0, logger.getAlertCount());
    }
  }
}
