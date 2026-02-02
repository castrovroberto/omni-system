# Thread Pool Demo

> A practical application of producer-consumer pattern.

## Overview

The `ThreadPool` class demonstrates:
- Producer-consumer with `BlockingQueue`
- Thread lifecycle management
- Asynchronous task execution

---

## Architecture

```
┌──────────────┐     ┌──────────────────┐     ┌─────────────┐
│   Client     │────▶│   ThreadPool     │────▶│   Workers   │
│   (submit)   │     │  (BlockingQueue) │     │  (execute)  │
└──────────────┘     └──────────────────┘     └─────────────┘
```

### Components

| Component | Role |
|-----------|------|
| `ThreadPool` | Manages workers, accepts tasks |
| `WorkerThread` | Pulls and executes tasks |
| `BlockingQueue` | Buffers pending tasks |
| `TaskFuture<T>` | Represents async result |
| `Task<T>` | Functional interface for work |

---

## Usage Example

```java
// Create pool with 4 workers
ThreadPool pool = new ThreadPool(4);

// Submit tasks (producers)
for (int i = 0; i < 100; i++) {
    final int taskId = i;
    pool.submit(() -> {
        System.out.println("Task " + taskId + " on " + 
            Thread.currentThread().getName());
    });
}

// Submit task with result
TaskFuture<Integer> future = pool.submit(() -> {
    return computeExpensiveResult();
});

// Wait for result
Integer result = future.get();

// Shutdown when done
pool.shutdown();
```

---

## Worker Thread Lifecycle

```
┌───────┐
│ Start │
└───┬───┘
    │
    ▼
┌───────────────────┐
│ Wait for task     │◀────────┐
│ (queue.take())    │         │
└─────────┬─────────┘         │
          │                   │
          ▼                   │
┌───────────────────┐         │
│ Execute task      │         │
│ (task.run())      │         │
└─────────┬─────────┘         │
          │                   │
          └───────────────────┘
          │
          ▼ (on shutdown)
┌───────────────────┐
│ InterruptedException │
│ → Exit loop       │
└───────────────────┘
```

---

## Key Implementation Details

### Task Submission

```java
public void submit(Runnable task) {
    if (isShutdown) {
        throw new IllegalStateException("Pool is shut down");
    }
    taskQueue.put(task);  // Blocks if queue full
}
```

### Worker Loop

```java
@Override
public void run() {
    while (running) {
        try {
            Runnable task = taskQueue.take();  // Blocks if empty
            task.run();
        } catch (InterruptedException e) {
            break;  // Shutdown requested
        }
    }
}
```

### Graceful Shutdown

```java
public void shutdown() throws InterruptedException {
    isShutdown = true;
    for (WorkerThread worker : workers) {
        worker.shutdown();  // Sets flag and interrupts
    }
    for (WorkerThread worker : workers) {
        worker.join(1000);  // Wait for completion
    }
}
```

---

## TaskFuture

Simple future for async results:

```java
public synchronized T get() throws Exception, InterruptedException {
    while (!done) {
        wait();  // Block until result ready
    }
    if (exception != null) throw exception;
    return result;
}
```

---

## Design Decisions

| Decision | Rationale |
|----------|-----------|
| Fixed worker count | Simpler than dynamic sizing |
| BlockingQueue for task buffer | Natural producer-consumer |
| Exception isolation | One bad task doesn't kill workers |
| Interrupt for shutdown | Clean thread termination |

---

## Potential Extensions

1. **Dynamic sizing** — Grow/shrink based on load
2. **Priority queue** — High-priority tasks first
3. **Scheduled execution** — Delayed/periodic tasks
4. **Cancellation** — Abort pending tasks
