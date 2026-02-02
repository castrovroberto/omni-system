# Builder Pattern

> Construct complex objects step-by-step with a fluent API.

## Problem
`SystemEvent` has required fields (timestamp, severity, source, message) and optional fields (metadata). Constructors become unwieldy with many parameters.

## Solution

```
SystemEvent (immutable)
    │
    └── Builder (nested class)
          + timestamp(Instant): Builder
          + severity(Severity): Builder
          + source(String): Builder
          + message(String): Builder
          + metadata(key, value): Builder
          + build(): SystemEvent
```

## Implementation

```java
public final class SystemEvent {
    private final Instant timestamp;
    private final Severity severity;
    private final String source;
    private final String message;
    private final Map<String, String> metadata;
    
    private SystemEvent(Builder builder) {
        this.timestamp = builder.timestamp;
        this.severity = builder.severity;
        this.source = builder.source;
        this.message = builder.message;
        this.metadata = Map.copyOf(builder.metadata);
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private Instant timestamp;
        private Severity severity;
        private String source;
        private String message;
        private final Map<String, String> metadata = new HashMap<>();
        
        public Builder timestamp(Instant timestamp) {
            this.timestamp = timestamp;
            return this;
        }
        
        public Builder metadata(String key, String value) {
            this.metadata.put(key, value);
            return this;
        }
        
        public SystemEvent build() {
            Objects.requireNonNull(timestamp, "timestamp required");
            Objects.requireNonNull(severity, "severity required");
            return new SystemEvent(this);
        }
    }
}
```

## Usage

```java
SystemEvent event = SystemEvent.builder()
    .timestamp(Instant.now())
    .severity(Severity.ERROR)
    .source("auth-service")
    .message("Login failed")
    .metadata("ip", "192.168.1.1")
    .metadata("attempts", "3")
    .build();
```

## Benefits
- **Readable**: Named methods > positional parameters
- **Immutable result**: Thread-safe, no defensive copies needed
- **Validation**: `build()` enforces required fields
- **Fluent API**: Method chaining for clean code

## Used In
- [SystemEvent](../../../../src/main/java/com/omni/app/log/SystemEvent.java)
