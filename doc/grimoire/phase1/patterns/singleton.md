# Singleton Pattern

> Ensure exactly one instance exists globally.

## Problem
`LogManager` must be a single point of log aggregation. Multiple instances would cause fragmented logs.

## Solution

```
LogManager
  - instance: LogManager (static)
  - LogManager() (private)
  + getInstance(): LogManager (static)
  + log(event): void
```

## Implementation (Thread-Safe, Lazy)

```java
public class LogManager {
    private static volatile LogManager instance;
    
    private final EventLog eventLog = new EventLog();
    private final AlertQueue alertQueue = new AlertQueue();
    
    // Private constructor prevents external instantiation
    private LogManager() {}
    
    public static LogManager getInstance() {
        if (instance == null) {                    // First check (no lock)
            synchronized (LogManager.class) {
                if (instance == null) {            // Second check (with lock)
                    instance = new LogManager();
                }
            }
        }
        return instance;
    }
    
    public void log(SystemEvent event) {
        eventLog.record(event);
        if (event.getSeverity().isHighPriority()) {
            alertQueue.enqueue(event);
        }
    }
}
```

## Double-Checked Locking

Why two null checks?

```
Thread 1                    Thread 2
─────────────────────────   ─────────────────────────
if (instance == null)       
    synchronized(class)     
        if (instance == null)
            instance = new  
        }                   if (instance == null)  // Already created!
    }                           return instance    // Skip sync block
return instance             return instance
```

The `volatile` keyword ensures visibility across threads.

## Usage

```java
// Any service can log without dependency injection
LogManager logger = LogManager.getInstance();
logger.log(event);

// Same instance everywhere
LogManager same = LogManager.getInstance();
assert logger == same;  // true
```

## Trade-offs

| Pro | Con |
|-----|-----|
| Global access | Hard to test (hidden dependency) |
| Lazy initialization | Global mutable state |
| Single instance | Difficult to subclass |

## When to Use
- ✅ System-wide resources (logging, configuration)
- ✅ Expensive-to-create objects
- ❌ When testability is critical (use dependency injection instead)

## Used In
- [LogManager](../../../../src/main/java/com/omni/app/log/LogManager.java)
