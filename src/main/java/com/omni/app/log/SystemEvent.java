package com.omni.app.log;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a system event/log entry in the Cloud Orchestrator.
 *
 * <p>Uses the Builder pattern to construct immutable event objects with multiple optional fields.
 */
public final class SystemEvent {

  public enum Severity {
    DEBUG,
    INFO,
    WARNING,
    ERROR,
    CRITICAL
  }

  private final Instant timestamp;
  private final Severity severity;
  private final String source;
  private final String message;
  private final Map<String, String> metadata;

  private SystemEvent(Builder builder) {
    this.timestamp = Objects.requireNonNull(builder.timestamp, "Timestamp is required");
    this.severity = Objects.requireNonNull(builder.severity, "Severity is required");
    this.source = Objects.requireNonNull(builder.source, "Source is required");
    this.message = Objects.requireNonNull(builder.message, "Message is required");
    this.metadata = Map.copyOf(builder.metadata); // Immutable copy
  }

  /**
   * Creates a new builder for constructing a SystemEvent.
   *
   * @return a new builder instance
   */
  public static Builder builder() {
    return new Builder();
  }

  public Instant getTimestamp() {
    return timestamp;
  }

  public Severity getSeverity() {
    return severity;
  }

  public String getSource() {
    return source;
  }

  public String getMessage() {
    return message;
  }

  public Map<String, String> getMetadata() {
    return metadata; // Already immutable
  }

  /**
   * Gets a specific metadata value by key.
   *
   * @param key the metadata key
   * @return the metadata value, or null if not present
   */
  public String getMetadata(String key) {
    return metadata.get(key);
  }

  @Override
  public String toString() {
    return String.format("[%s] %s %s: %s", timestamp, severity, source, message);
  }

  /**
   * Builder for constructing SystemEvent instances.
   *
   * <p>Example usage:
   *
   * <pre>
   * SystemEvent event = SystemEvent.builder()
   *     .timestamp(Instant.now())
   *     .severity(Severity.WARNING)
   *     .source("auth-service")
   *     .message("Login attempt failed")
   *     .metadata("ip", "192.168.1.1")
   *     .build();
   * </pre>
   */
  public static class Builder {
    private Instant timestamp;
    private Severity severity;
    private String source;
    private String message;
    private Map<String, String> metadata = new HashMap<>();

    public Builder timestamp(Instant timestamp) {
      this.timestamp = timestamp;
      return this;
    }

    public Builder severity(Severity severity) {
      this.severity = severity;
      return this;
    }

    public Builder source(String source) {
      this.source = source;
      return this;
    }

    public Builder message(String message) {
      this.message = message;
      return this;
    }

    /**
     * Adds a metadata key-value pair. Can be called multiple times to add multiple metadata
     * entries.
     *
     * @param key the metadata key
     * @param value the metadata value
     * @return this builder for method chaining
     */
    public Builder metadata(String key, String value) {
      this.metadata.put(key, value);
      return this;
    }

    /**
     * Builds the immutable SystemEvent instance.
     *
     * @return the constructed SystemEvent
     * @throws NullPointerException if required fields are missing
     */
    public SystemEvent build() {
      return new SystemEvent(this);
    }
  }
}
