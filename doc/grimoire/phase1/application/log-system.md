# Phase 1 Demo Application: Log Management System

> How the Phase 1 data structures power the Cloud Orchestrator's logging infrastructure

---

## Overview

The Log Management System demonstrates practical usage of Phase 1 components in a realistic backend scenario. It provides centralized logging for the Mock Cloud Orchestrator, handling both routine events and high-priority alerts.

```
┌─────────────────────────────────────────────────────────────┐
│                     LogManager (Singleton)                   │
│  ┌─────────────────────┐    ┌─────────────────────────────┐ │
│  │      EventLog       │    │        AlertQueue           │ │
│  │   (MyArrayList)     │    │      (MyLinkedList)         │ │
│  │                     │    │                             │ │
│  │  [Event0]           │    │  [CRITICAL] ←── newest      │ │
│  │  [Event1]           │    │  [ERROR]                    │ │
│  │  [Event2]           │    │  [ERROR]    ←── oldest      │ │
│  │  ...                │    │                             │ │
│  └─────────────────────┘    └─────────────────────────────┘ │
└─────────────────────────────────────────────────────────────┘
                              ↑
                    SystemEvent (Builder)
```

---

## Components

### SystemEvent (Builder Pattern)

Immutable log entry constructed via fluent builder API.

**Why Builder?**
- Multiple optional fields (metadata)
- Enforces required fields at compile time
- Produces immutable, thread-safe objects

```java
SystemEvent event = SystemEvent.builder()
    .timestamp(Instant.now())
    .severity(Severity.WARNING)
    .source("auth-service")
    .message("Login attempt failed for user: admin")
    .metadata("ip", "192.168.1.100")
    .metadata("attempts", "3")
    .build();
```

**Structure**:
```java
public final class SystemEvent {
    private final Instant timestamp;      // When it happened
    private final Severity severity;      // DEBUG, INFO, WARNING, ERROR, CRITICAL
    private final String source;          // Which service
    private final String message;         // What happened
    private final Map<String, String> metadata;  // Additional context
}
```

---

### EventLog (Uses MyArrayList)

Chronological storage for all system events.

**Why MyArrayList?**
- Events are append-only (O(1) amortized)
- Random access needed for log review (O(1))
- Memory-efficient for large volumes
- Good cache locality for iteration

```java
public class EventLog implements Iterable<SystemEvent> {
    private final MyList<SystemEvent> events = new MyArrayList<>();

    public void record(SystemEvent event) {
        events.add(event);  // O(1) amortized - perfect for logging
    }

    public SystemEvent get(int index) {
        return events.get(index);  // O(1) - fast random access
    }

    public int size() {
        return events.size();
    }

    @Override
    public Iterator<SystemEvent> iterator() {
        return events.iterator();  // For-each support
    }
}
```

**Usage in Cloud Orchestrator**:
```java
EventLog log = new EventLog();

// Record routine events
log.record(SystemEvent.builder()
    .timestamp(Instant.now())
    .severity(Severity.INFO)
    .source("scheduler")
    .message("Job batch completed: 150 jobs processed")
    .build());

// Later: review logs
for (SystemEvent event : log) {
    if (event.getSeverity() == Severity.ERROR) {
        alertAdmin(event);
    }
}
```

---

### AlertQueue (Uses MyLinkedList)

FIFO queue for high-priority alerts requiring immediate attention.

**Why MyLinkedList?**
- Fast insertion at front (O(1)) for new alerts
- Fast removal from front (O(1)) for processing
- No random access needed - always process oldest first
- Deque operations for priority handling

```java
public class AlertQueue {
    private final MyLinkedList<SystemEvent> alerts = new MyLinkedList<>();

    public void enqueue(SystemEvent alert) {
        alerts.addLast(alert);  // O(1) - add to end of queue
    }

    public void enqueuePriority(SystemEvent criticalAlert) {
        alerts.addFirst(criticalAlert);  // O(1) - jump to front
    }

    public SystemEvent dequeue() {
        return alerts.removeFirst();  // O(1) - process oldest
    }

    public SystemEvent peek() {
        return alerts.isEmpty() ? null : alerts.get(0);
    }

    public boolean hasAlerts() {
        return !alerts.isEmpty();
    }
}
```

**Usage in Cloud Orchestrator**:
```java
AlertQueue alerts = new AlertQueue();

// Server crash - critical alert
alerts.enqueuePriority(SystemEvent.builder()
    .timestamp(Instant.now())
    .severity(Severity.CRITICAL)
    .source("server-monitor")
    .message("Server db-primary-01 unresponsive")
    .metadata("lastHeartbeat", "2 minutes ago")
    .build());

// Process alerts in order
while (alerts.hasAlerts()) {
    SystemEvent alert = alerts.dequeue();
    notifyOnCall(alert);
    createIncidentTicket(alert);
}
```

---

### LogManager (Singleton Pattern)

Central facade managing all logging operations.

**Why Singleton?**
- Single point of log aggregation
- Consistent configuration across services
- Thread-safe access (prepared for Phase 5)
- Global accessibility without passing references

```java
public class LogManager {
    private static volatile LogManager instance;
    private final EventLog eventLog = new EventLog();
    private final AlertQueue alertQueue = new AlertQueue();

    private LogManager() {} // Private constructor

    public static LogManager getInstance() {
        if (instance == null) {
            synchronized (LogManager.class) {
                if (instance == null) {
                    instance = new LogManager();
                }
            }
        }
        return instance;
    }

    public void log(SystemEvent event) {
        eventLog.record(event);

        // Auto-route high-severity events to alert queue
        if (event.getSeverity() == Severity.ERROR ||
            event.getSeverity() == Severity.CRITICAL) {
            alertQueue.enqueue(event);
        }
    }

    public EventLog getEventLog() { return eventLog; }
    public AlertQueue getAlertQueue() { return alertQueue; }
}
```

**Usage in Cloud Orchestrator**:
```java
// Any service can log without dependency injection
LogManager logger = LogManager.getInstance();

logger.log(SystemEvent.builder()
    .timestamp(Instant.now())
    .severity(Severity.INFO)
    .source("api-gateway")
    .message("Request processed: GET /users/123")
    .metadata("latency", "45ms")
    .build());

// Check for alerts
if (logger.getAlertQueue().hasAlerts()) {
    triggerPagerDuty();
}
```

---

### LogSearcher (Uses Search Algorithms)

Utility for finding events using Phase 1 search algorithms.

**Binary Search**: For timestamp-based queries on chronological logs
**Linear Search**: For filtering by any field

```java
public class LogSearcher {

    // Fast search on sorted (chronological) logs
    public static int binarySearchByTimestamp(
            EventLog log, Instant targetTime) {

        return SearchAlgorithms.binarySearch(
            log.asList(),
            targetTime,
            Comparator.comparing(SystemEvent::getTimestamp)
        );
    }

    // Flexible search for any criteria
    public static SystemEvent findBySeverity(
            EventLog log, Severity severity) {

        for (SystemEvent event : log) {
            if (event.getSeverity() == severity) {
                return event;
            }
        }
        return null;
    }

    // Find all matching events
    public static MyList<SystemEvent> filterBySource(
            EventLog log, String source) {

        MyList<SystemEvent> results = new MyArrayList<>();
        for (SystemEvent event : log) {
            if (event.getSource().equals(source)) {
                results.add(event);
            }
        }
        return results;
    }
}
```

**Usage in Cloud Orchestrator**:
```java
EventLog log = LogManager.getInstance().getEventLog();

// Find events around a specific time (for incident investigation)
Instant incidentTime = Instant.parse("2026-01-24T14:30:00Z");
int index = LogSearcher.binarySearchByTimestamp(log, incidentTime);

// Get surrounding context (5 events before and after)
for (int i = Math.max(0, index - 5); i < Math.min(log.size(), index + 5); i++) {
    System.out.println(log.get(i));
}

// Find all auth-service errors
MyList<SystemEvent> authErrors = LogSearcher.filterBySource(log, "auth-service");
```

---

## Data Flow

```
┌──────────────┐     ┌─────────────┐     ┌────────────────┐
│   Service    │────▶│  LogManager │────▶│   EventLog     │
│  (any)       │     │  (Singleton)│     │  (MyArrayList) │
└──────────────┘     └─────────────┘     └────────────────┘
                            │
                            │ if ERROR/CRITICAL
                            ▼
                     ┌────────────────┐
                     │  AlertQueue    │
                     │ (MyLinkedList) │
                     └────────────────┘
                            │
                            ▼
                     ┌────────────────┐
                     │  Alert Handler │
                     │  (processes    │
                     │   FIFO order)  │
                     └────────────────┘
```

---

## Design Decisions

### Why Two Separate Structures?

| Concern | EventLog (ArrayList) | AlertQueue (LinkedList) |
|---------|---------------------|------------------------|
| Access pattern | Random (review any log) | Sequential (process oldest) |
| Primary operation | Append | Dequeue from front |
| Retention | Permanent record | Temporary until processed |
| Volume | High (all events) | Low (only alerts) |

### Why Not Just Filter EventLog?

Filtering EventLog for alerts would be O(n) each time. AlertQueue provides:
- O(1) check for pending alerts
- O(1) retrieval of next alert
- Natural FIFO processing order
- Priority insertion for critical events

---

## Integration Points

### Phase 2 (Upcoming)
- `SessionStore` will log authentication events
- `UserRegistry` will log user lifecycle events

### Phase 3 (Upcoming)
- `JobScheduler` will log job execution
- `VirtualFileSystem` will log file operations

### Phase 4 (Upcoming)
- `NetworkTopology` will log connection changes
- `DependencyResolver` will log resolution steps

---

## Example: Full Scenario

```java
// Simulate cloud orchestrator startup
LogManager logger = LogManager.getInstance();

// 1. System initialization
logger.log(SystemEvent.builder()
    .timestamp(Instant.now())
    .severity(Severity.INFO)
    .source("orchestrator")
    .message("System startup initiated")
    .build());

// 2. Services starting
for (String service : List.of("db", "cache", "api", "worker")) {
    logger.log(SystemEvent.builder()
        .timestamp(Instant.now())
        .severity(Severity.INFO)
        .source(service)
        .message("Service started successfully")
        .metadata("pid", String.valueOf(ProcessHandle.current().pid()))
        .build());
}

// 3. Something goes wrong
logger.log(SystemEvent.builder()
    .timestamp(Instant.now())
    .severity(Severity.ERROR)
    .source("db")
    .message("Connection pool exhausted")
    .metadata("activeConnections", "100")
    .metadata("maxConnections", "100")
    .build());

// 4. Check alerts
AlertQueue alerts = logger.getAlertQueue();
System.out.println("Pending alerts: " + alerts.size());

while (alerts.hasAlerts()) {
    SystemEvent alert = alerts.dequeue();
    System.out.println("ALERT: " + alert.getMessage());
}

// 5. Review full log
System.out.println("\n=== Full Event Log ===");
for (SystemEvent event : logger.getEventLog()) {
    System.out.printf("[%s] %s: %s%n",
        event.getSeverity(),
        event.getSource(),
        event.getMessage());
}
```

---

*Part of the Omni-System Grimoire - Phase 1: Foundation*
