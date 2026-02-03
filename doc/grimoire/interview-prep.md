# Interview Preparation Guide

> Connecting Omni-System code to common interview patterns

---

## Overview

This guide maps your from-scratch implementations to classic interview questions, with ready-to-use talking points for technical discussions.

---

## 1. In-Memory File System (LeetCode #588)

**Your Code**: `VirtualFileSystem`, `DirectoryNode`

### Key Features

| Method | Complexity | Purpose |
|--------|------------|---------|
| `getChild(name)` | O(1) | HashMap lookup |
| `resolve(path)` | O(d) | Path traversal |
| `mkdirp(path)` | O(d) | Create intermediates |

### Interview Talking Points

> "When asked about scalability with millions of files, I refactored from `MyList` to `MyHashMap` for O(1) child lookup instead of O(n)."

> "I used the Composite pattern so files and directories share a common interface, simplifying recursive operations like `getSize()`."

### Code Reference
```java
// O(1) lookup by name
Optional<FileSystemNode> child = directory.getChild("config.json");

// mkdir -p equivalent
DirectoryNode deep = fs.mkdirp("/var/log/app/2024");
```

---

## 2. Merge K Sorted Lists (LeetCode #23)

**Your Code**: `HeapChallenges.mergeKSortedLists()`

### Algorithm

```
1. Insert head of each list into min-heap with list index
2. Extract min → add to result
3. Insert next from that list
4. Repeat until heap empty
```

**Time**: O(n log k) where n = total elements, k = number of lists

### Interview Talking Points

> "This mirrors external merge sort in databases — each sorted run is a list, and the heap efficiently finds the next smallest element across all runs."

> "I implemented `MyHeap` from scratch with `siftUp` and `siftDown` in O(log n) each."

### Code Reference
```java
MyList<MyList<Integer>> sortedLists = ...;
MyList<Integer> merged = HeapChallenges.mergeKSortedLists(sortedLists);
```

---

## 3. Hit Counter (LeetCode #362)

**Your Code**: `HitCounter`, `AtomicHitCounter`

### Design Decisions

| Decision | Rationale |
|----------|-----------|
| Circular buffer | O(1) space for fixed window |
| ReentrantReadWriteLock | Concurrent reads, exclusive writes |
| Bucket by second | Trade accuracy for performance |

### Two Implementations Compared

| Approach | Contention | Best For |
|----------|------------|----------|
| `HitCounter` (ReadWriteLock) | Low for reads | Read-heavy workloads |
| `AtomicHitCounter` (CAS) | Lock-free | High-throughput writes |

### Interview Talking Points

> "I used read/write locks instead of `synchronized` because metrics reporting (reads) happens far more often than recording hits (writes). This allows multiple concurrent readers."

> "For the atomic version, I use CAS operations to avoid lock contention entirely — better for high-throughput writes but slightly less predictable under extreme load."

### Contention Comparison

```
synchronized      → All operations exclusive
ReadWriteLock     → Multiple readers OR one writer  
AtomicInteger[]   → Lock-free, CAS-based updates
```

---

## 4. Log Storage System (LeetCode #635)

**Your Code**: `LogStorageSystem`

### Key Features

| Method | Complexity | Purpose |
|--------|------------|---------|
| `put(id, timestamp)` | O(log n) | Insert log entry |
| `retrieve(start, end, grano)` | O(log n + k) | Range query with granularity |

### Design: TreeMap for Range Queries

```java
// TreeMap provides O(log n) range queries
TreeMap<String, List<Integer>> logs = new TreeMap<>();

// subMap() returns a VIEW, not a copy - very efficient
logs.subMap(startKey, true, endKey, true).values()
```

### Interview Talking Points

> "I chose `TreeMap` over `HashMap` because the Red-Black tree structure enables O(log n) range queries via `subMap()` — essential for time-range retrieval."

> "The `subMap()` method returns a view, not a copy, making it memory-efficient for large log ranges."

### Granularity Handling

```
Year:   2017:01:01:00:00:00 → 2017
Month:  2017:01:01:00:00:00 → 2017:01  
Day:    2017:01:01:00:00:00 → 2017:01:01
```

---

## 5. Task Scheduler / Job Scheduler

**Your Code**: `JobScheduler` (uses `MyHeap`)

### Design

```java
// Min-heap by priority: lower value = higher priority
MyHeap<Job> jobQueue = new MyHeap<>(Comparator.comparingInt(Job::getPriority));
```

### Key Features

| Method | Complexity | Purpose |
|--------|------------|---------|
| `schedule(job)` | O(log n) | Add job to queue |
| `executeNext()` | O(log n) | Run highest-priority job |
| `executeAll()` | O(n log n) | Drain queue in priority order |

### Interview Talking Points

> "I used `PriorityQueue` semantics with a custom comparator — this naturally handles task scheduling where lower priority numbers mean 'run first'."

> "For LeetCode #621 (Task Scheduler with cooldown), you'd extend this with a cooling map to track when each task type can next run."

---

## 6. Trie / Word Search II (LeetCode #212/208)

**Your Code**: `Trie`, `Trie.TrieNode`

### Design Decisions

| Decision | Rationale |
|----------|-----------|
| `TrieNode[26]` for children | O(1) lookup, fixed alphabet |
| `isEndOfWord` flag | Distinguish prefixes from words |
| Exposed `getRoot()` | Enables backtracking in Word Search II |

### Key Features

| Method | Complexity | Purpose |
|--------|------------|---------|
| `insert(word)` | O(m) | Add word to trie |
| `search(word)` | O(m) | Exact match lookup |
| `startsWith(prefix)` | O(m) | Prefix existence check |

### Interview Talking Points

> "For Word Search II, I expose the root node so the backtracking algorithm can traverse the trie while exploring the board — this prunes search paths early."

> "I used a fixed `TrieNode[26]` array instead of `Map<Character, TrieNode>` for O(1) child lookup, accepting the memory trade-off for lowercase-only input."

### Memory Trade-off

```
Array children:  26 * 8 bytes = 208 bytes per node (fast, wastes space)
Map children:    ~40 bytes base + 48 bytes/entry (slower, compact for sparse tries)
```

---

## 7. Heap Operations

**Your Code**: `MyHeap`

### Complexity Cheat Sheet

| Operation | Time | Why |
|-----------|------|-----|
| `insert` | O(log n) | siftUp |
| `extractRoot` | O(log n) | siftDown |
| `peek` | O(1) | Array index 0 |
| `heapify` | O(n) | Floyd's algorithm |

### Interview Talking Points

> "Floyd's heapify is O(n) not O(n log n) because most nodes are near the leaves and require minimal sifting."

---

## 8. Java Concurrency Patterns

### When to Use What

| Pattern | Use Case | Example |
|---------|----------|---------|
| `synchronized` | Simple mutual exclusion | Single-writer cache |
| `ReentrantReadWriteLock` | Read-heavy workloads | Metrics dashboard |
| `AtomicInteger/Long` | Simple counters | Hit counter |
| `ConcurrentHashMap` | Concurrent map access | Session store |
| `CompletableFuture` | Async composition | Service orchestration |

### ConcurrentHashMap Internals

```
Java 7:  Segment-based locking (16 segments default)
Java 8+: CAS + synchronized on bucket nodes (finer granularity)
```

### ThreadPoolExecutor Configuration

```java
new ThreadPoolExecutor(
    corePoolSize,     // Threads kept alive even when idle
    maximumPoolSize,  // Max threads when queue is full
    keepAliveTime,    // Idle thread timeout
    TimeUnit.SECONDS,
    new LinkedBlockingQueue<>(queueCapacity)  // Bounded queue
);
```

---

## 9. Java Internals Quick Reference

### Collections Comparison

| Collection | Ordering | Access | Use Case |
|------------|----------|--------|----------|
| `HashMap` | None | O(1) | General key-value |
| `TreeMap` | Sorted | O(log n) | Range queries |
| `LinkedHashMap` | Insertion | O(1) | LRU cache |

### Memory Model Basics

| Region | Contains | Characteristics |
|--------|----------|-----------------|
| Stack | Primitives, references | Per-thread, fast |
| Heap | Objects | Shared, GC-managed |
| Metaspace | Class metadata | Replaces PermGen (Java 8+) |

### GC Generations

```
Young Gen:  New objects, Minor GC (fast, frequent)
Old Gen:    Long-lived objects, Major GC (slower, less frequent)
```

---

## 10. Expertise Round Defense

Prepare to explain design decisions:

| Question | Your Answer |
|----------|-------------|
| "Why doubling capacity in MyArrayList?" | "Amortized O(1) append — n insertions cost O(n) total" |
| "Why Composite for file system?" | "Uniform treatment of files and folders simplifies recursion" |
| "Why wait/notify in BlockingQueue?" | "Monitor pattern — built-in Java primitive, no external deps" |
| "Striped vs full locking in ConcurrentHashMap?" | "Started with striped, refactored to full for correctness with non-thread-safe delegate" |
| "Why TreeMap for log storage?" | "Red-Black tree enables O(log n) range queries via subMap()" |

---

## Quick Reference

| Interview Topic | Your Implementation |
|-----------------|---------------------|
| File System Design | `VirtualFileSystem`, `DirectoryNode` |
| Merge K Sorted | `HeapChallenges.mergeKSortedLists()` |
| Hit Counter | `HitCounter`, `AtomicHitCounter` |
| Log Storage | `LogStorageSystem` |
| Task Scheduler | `JobScheduler` |
| Trie / Word Search | `Trie`, `Trie.TrieNode` |
| Thread Pool | `ThreadPool`, `WorkerThread` |
| Producer-Consumer | `BlockingQueue` |
| Graph Algorithms | `GraphAlgorithms` (DFS, BFS, Dijkstra) |

---

*Part of the Omni-System Grimoire*
