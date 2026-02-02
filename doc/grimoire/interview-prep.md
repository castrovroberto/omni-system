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

**Your Code**: `HitCounter`

### Design Decisions

| Decision | Rationale |
|----------|-----------|
| Circular buffer | O(1) space for fixed window |
| ReentrantReadWriteLock | Concurrent reads, exclusive writes |
| Bucket by second | Trade accuracy for performance |

### Interview Talking Points

> "I used read/write locks instead of `synchronized` because metrics reporting (reads) happens far more often than recording hits (writes). This allows multiple concurrent readers."

> "The circular buffer approach means O(1) space regardless of hit count — we only store one counter per second in the window."

### Contention Comparison

```
synchronized → All operations exclusive
ReadWriteLock → Multiple readers OR one writer
```

---

## 4. Heap Operations

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

## 5. Expertise Round Defense

Prepare to explain design decisions:

| Question | Your Answer |
|----------|-------------|
| "Why doubling capacity in MyArrayList?" | "Amortized O(1) append — n insertions cost O(n) total" |
| "Why Composite for file system?" | "Uniform treatment of files and folders simplifies recursion" |
| "Why wait/notify in BlockingQueue?" | "Monitor pattern — built-in Java primitive, no external deps" |
| "Striped vs full locking in ConcurrentHashMap?" | "Started with striped, refactored to full for correctness with non-thread-safe delegate" |

---

## Quick Reference

| Interview Topic | Your Implementation |
|-----------------|---------------------|
| File System Design | `VirtualFileSystem`, `DirectoryNode` |
| Merge K Sorted | `HeapChallenges.mergeKSortedLists()` |
| Hit Counter | `HitCounter` |
| Thread Pool | `ThreadPool`, `WorkerThread` |
| Producer-Consumer | `BlockingQueue` |
| Graph Algorithms | `GraphAlgorithms` (DFS, BFS, Dijkstra) |

---

*Part of the Omni-System Grimoire*
