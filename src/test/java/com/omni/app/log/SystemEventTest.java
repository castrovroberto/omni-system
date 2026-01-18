package com.omni.app.log;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("SystemEvent Tests - Builder Pattern")
class SystemEventTest {

  @Nested
  @DisplayName("Builder Pattern Tests")
  class BuilderPatternTests {

    @Test
    @DisplayName("Builder creates valid SystemEvent")
    void builder_createsValidSystemEvent() {
      Instant timestamp = Instant.now();
      SystemEvent event =
          SystemEvent.builder()
              .timestamp(timestamp)
              .severity(SystemEvent.Severity.INFO)
              .source("test-service")
              .message("Test message")
              .build();

      assertNotNull(event);
      assertEquals(timestamp, event.getTimestamp());
      assertEquals(SystemEvent.Severity.INFO, event.getSeverity());
      assertEquals("test-service", event.getSource());
      assertEquals("Test message", event.getMessage());
    }

    @Test
    @DisplayName("Builder with all fields creates complete event")
    void builder_withAllFields_createsCompleteEvent() {
      Instant timestamp = Instant.parse("2026-01-15T10:30:00Z");
      SystemEvent event =
          SystemEvent.builder()
              .timestamp(timestamp)
              .severity(SystemEvent.Severity.WARNING)
              .source("auth-service")
              .message("Login attempt failed")
              .metadata("ip", "192.168.1.1")
              .metadata("user", "testuser")
              .build();

      assertEquals(timestamp, event.getTimestamp());
      assertEquals(SystemEvent.Severity.WARNING, event.getSeverity());
      assertEquals("auth-service", event.getSource());
      assertEquals("Login attempt failed", event.getMessage());
      assertEquals("192.168.1.1", event.getMetadata("ip"));
      assertEquals("testuser", event.getMetadata("user"));
    }

    @Test
    @DisplayName("Builder with multiple metadata entries")
    void builder_withMultipleMetadata_createsEvent() {
      SystemEvent event =
          SystemEvent.builder()
              .timestamp(Instant.now())
              .severity(SystemEvent.Severity.ERROR)
              .source("db-service")
              .message("Connection failed")
              .metadata("host", "db.example.com")
              .metadata("port", "5432")
              .metadata("retries", "3")
              .build();

      Map<String, String> metadata = event.getMetadata();
      assertEquals(3, metadata.size());
      assertEquals("db.example.com", event.getMetadata("host"));
      assertEquals("5432", event.getMetadata("port"));
      assertEquals("3", event.getMetadata("retries"));
    }

    @Test
    @DisplayName("Builder missing required field throws exception")
    void builder_missingRequiredField_throwsException() {
      // Missing timestamp
      assertThrows(
          NullPointerException.class,
          () ->
              SystemEvent.builder()
                  .severity(SystemEvent.Severity.INFO)
                  .source("test")
                  .message("test")
                  .build());

      // Missing severity
      assertThrows(
          NullPointerException.class,
          () ->
              SystemEvent.builder()
                  .timestamp(Instant.now())
                  .source("test")
                  .message("test")
                  .build());

      // Missing source
      assertThrows(
          NullPointerException.class,
          () ->
              SystemEvent.builder()
                  .timestamp(Instant.now())
                  .severity(SystemEvent.Severity.INFO)
                  .message("test")
                  .build());

      // Missing message
      assertThrows(
          NullPointerException.class,
          () ->
              SystemEvent.builder()
                  .timestamp(Instant.now())
                  .severity(SystemEvent.Severity.INFO)
                  .source("test")
                  .build());
    }

    @Test
    @DisplayName("Builder metadata is immutable")
    void builder_metadataIsImmutable() {
      SystemEvent event =
          SystemEvent.builder()
              .timestamp(Instant.now())
              .severity(SystemEvent.Severity.INFO)
              .source("test")
              .message("test")
              .metadata("key", "value")
              .build();

      Map<String, String> metadata = event.getMetadata();
      assertThrows(UnsupportedOperationException.class, () -> metadata.put("new", "value"));
    }

    @Test
    @DisplayName("SystemEvent toString returns formatted string")
    void toString_returnsFormattedString() {
      Instant timestamp = Instant.parse("2026-01-15T10:30:00Z");
      SystemEvent event =
          SystemEvent.builder()
              .timestamp(timestamp)
              .severity(SystemEvent.Severity.WARNING)
              .source("test-service")
              .message("Test message")
              .build();

      String str = event.toString();
      assertNotNull(str);
      assertTrue(str.contains("WARNING"));
      assertTrue(str.contains("test-service"));
      assertTrue(str.contains("Test message"));
    }

    @Test
    @DisplayName("All severity levels work")
    void builder_allSeverityLevels_work() {
      for (SystemEvent.Severity severity : SystemEvent.Severity.values()) {
        SystemEvent event =
            SystemEvent.builder()
                .timestamp(Instant.now())
                .severity(severity)
                .source("test")
                .message("test")
                .build();

        assertEquals(severity, event.getSeverity());
      }
    }
  }
}
