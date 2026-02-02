# Phase 5: Concurrency

**Theme:** Thread Safety and Concurrent Access

> Master the art of writing correct, thread-safe code.

---

## Contents

### Theory

Foundational concepts behind concurrent programming.

| Topic | Description |
|-------|-------------|
| [Concurrency Basics](theory/concurrency.md) | Race conditions, synchronization, wait/notify |
| [Producer-Consumer](theory/producer-consumer.md) | BlockingQueue and the producer-consumer pattern |

### Implementation Notes

Detailed notes on our concurrent data structure implementations.

| Component | Notes |
|-----------|-------|
| [SynchronizedList](implementation/synchronized-list-notes.md) | Thread-safe wrapper pattern |
| [BlockingQueue](implementation/blocking-queue-notes.md) | Wait/notify signaling |
| [ConcurrentHashMap](implementation/concurrent-hashmap-notes.md) | Full synchronization approach |

### Benchmarks

Performance measurements and analysis.

| Benchmark | Description |
|-----------|-------------|
| [Results](benchmarks/results.md) | Thread contention and throughput measurements |

### Application Demo

How concurrent components power the ThreadPool demo.

| Application | Description |
|-------------|-------------|
| [Thread Pool](application/thread-pool-demo.md) | Producer-consumer task execution |

### Blueprints

UML diagrams for visual reference.

| Diagram | Description |
|---------|-------------|
| [Concurrent Structures](blueprints/concurrent-structures.puml) | Class diagram with synchronization |
| [ThreadPool](blueprints/thread-pool.puml) | Worker and task flow |
| [Producer-Consumer](blueprints/producer-consumer.puml) | Sequence diagram of blocking behavior |

---

## Components Built

### Concurrent Data Structures

| Component | Location | Tests |
|-----------|----------|-------|
| `SynchronizedList<T>` | `com.omni.core.concurrent.SynchronizedList` | 7 tests |
| `BlockingQueue<T>` | `com.omni.core.concurrent.BlockingQueue` | 7 tests |
| `ConcurrentHashMap<K,V>` | `com.omni.core.concurrent.ConcurrentHashMap` | 8 tests |

### Demo Application

| Component | Location | Tests |
|-----------|----------|-------|
| `ThreadPool` | `com.omni.app.worker.ThreadPool` | 7 tests |
| `WorkerThread` | `com.omni.app.worker.WorkerThread` | (via pool tests) |
| `TaskFuture<T>` | `com.omni.app.worker.TaskFuture` | (via pool tests) |
| `Task<T>` | `com.omni.app.worker.Task` | (interface) |

---

## Complexity Summary

| Operation | SynchronizedList | BlockingQueue | ConcurrentHashMap |
|-----------|------------------|---------------|-------------------|
| Add/Put | O(1)* | O(1)* | O(1)* |
| Get | O(1) or O(n)** | - | O(1)* |
| Remove | O(n) | O(1)* | O(1)* |
| Contains | O(n) | - | O(1)* |

*May block on lock acquisition  
**Depends on underlying list implementation

---

## Learning Objectives

After completing this phase, you should understand:

1. **Race Conditions** - Why concurrent access to shared data causes bugs
2. **Synchronization** - Using `synchronized` to protect critical sections
3. **Wait/Notify** - Coordinating threads with blocking operations
4. **Thread Safety** - Designing classes that work correctly under concurrency
5. **Producer-Consumer** - A fundamental pattern for concurrent work distribution

---

## Mastery Checks

1. [ ] Explain why `SynchronizedList.iterator()` isn't thread-safe
2. [ ] Demonstrate a race condition without synchronization
3. [ ] Implement a countdown latch using wait/notify
4. [ ] Benchmark multi-threaded vs single-threaded map operations
5. [ ] Design a thread-safe cache with TTL expiration
