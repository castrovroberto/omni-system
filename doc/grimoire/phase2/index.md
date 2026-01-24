# Phase 2: Organization & Speed

> Hash maps, sorting algorithms, and key design patterns

---

## Overview

Phase 2 builds upon the linear foundations of Phase 1 to introduce fast key-value lookup and efficient sorting. By implementing hash tables from scratch, we internalize how constant-time access is achieved through hashing and collision resolution.

**Theme**: Fast Retrieval & Data Ordering
**Demo Feature**: User Authentication & Registry Service
**Status**: Complete

---

## Contents

### Theory

Conceptual explanations of data structures and algorithms.

| Document | Description |
|----------|-------------|
| [Hash Tables](theory/hash-tables.md) | How MyHashMap works: hashing, collision resolution, resizing |
| [Sorting Algorithms](theory/sorting-algorithms.md) | MergeSort and QuickSort: divide-and-conquer strategies |

### Implementation Notes

Design decisions, gotchas, and lessons learned.

| Document | Description |
|----------|-------------|
| [MyHashMap Notes](implementation/my-hashmap-notes.md) | Chaining, load factors, hash strategies |
| [SortAlgorithms Notes](implementation/sort-algorithms-notes.md) | Stability, pivot selection, optimizations |

### Benchmarks

Performance measurements and mastery check validation.

| Document | Description |
|----------|-------------|
| [Benchmark Results](benchmarks/results.md) | Hash distribution analysis, sorting comparisons |

### Application

How Phase 2 components power the demo application.

| Document | Description |
|----------|-------------|
| [Auth Service](application/auth-service.md) | SessionStore, UserRegistry, caching strategies |

### Blueprints

UML diagrams for visual reference.

| Diagram | Description |
|---------|-------------|
| [MyHashMap](blueprints/my-hashmap.puml) | Class diagram with collision handling |
| [SortAlgorithms](blueprints/sort-algorithms.puml) | Algorithm structure and flow |
| [Auth Service](blueprints/auth-service.puml) | Component relationships and patterns |
| [Design Patterns](blueprints/design-patterns.puml) | Strategy, Factory, Decorator patterns |

---

## Components Built

### Data Structures

| Component | Location | Tests |
|-----------|----------|-------|
| `MyMap<K,V>` | `com.omni.core.map.MyMap` | Interface |
| `MyHashMap<K,V>` | `com.omni.core.map.MyHashMap` | 45 tests |
| `CachingHashMap<K,V>` | `com.omni.core.map.CachingHashMap` | 22 tests |

### Hash Strategies

| Component | Location | Description |
|-----------|----------|-------------|
| `HashStrategy<K>` | `com.omni.core.map.hash.HashStrategy` | Strategy interface |
| `DefaultHashStrategy<K>` | `com.omni.core.map.hash.DefaultHashStrategy` | Uses Object.hashCode() |
| `DJB2HashStrategy` | `com.omni.core.map.hash.DJB2HashStrategy` | DJB2 algorithm for strings |

### Algorithms

| Component | Location | Tests |
|-----------|----------|-------|
| `mergeSort()` | `com.omni.core.algorithm.sort.SortAlgorithms` | 11 tests |
| `quickSort()` | `com.omni.core.algorithm.sort.SortAlgorithms` | 13 tests |
| `isSorted()` | `com.omni.core.algorithm.sort.SortAlgorithms` | 6 tests |

### Design Patterns

| Pattern | Implementation | Purpose |
|---------|----------------|---------|
| **Strategy** | `HashStrategy<K>` + implementations | Swappable hash functions |
| **Factory Method** | `User.create(UserType, String)` | Create user subtypes polymorphically |
| **Decorator** | `CachingHashMap<K,V>` | Add LRU caching to any MyMap |

### Demo Application

| Component | Uses | Purpose |
|-----------|------|---------|
| `User` | Factory Method | Abstract user with permissions |
| `AdminUser` | Extends User | Full system access |
| `DeveloperUser` | Extends User | Read/write access |
| `ViewerUser` | Extends User | Read-only access |
| `UserSession` | - | Authenticated session with expiration |
| `SessionStore` | MyHashMap | O(1) session lookup by token |
| `UserRegistry` | MyHashMap + SortAlgorithms | User management with sorted views |

---

## Mastery Checks

| Check | Status | Evidence |
|-------|--------|----------|
| 100K strings with good distribution | PASS | Average chain length < 2.0 |
| Intentional bad hash causes O(n) | PASS | All entries in single bucket |
| Compare default vs DJB2 hash | PASS | Both achieve good distribution |
| Sort 1M random integers | PASS | MergeSort completes successfully |
| QuickSort on sorted data | PASS | Median-of-three prevents O(n²) |
| Compare MergeSort vs QuickSort | PASS | Both complete in reasonable time |

---

## Key Learnings

### From MyHashMap
1. Power-of-2 capacity enables fast modulo via bitwise AND
2. Load factor of 0.75 balances space and time efficiency
3. Separate chaining is simple but has cache locality issues
4. Good hash distribution is critical for O(1) performance

### From Sorting Algorithms
1. MergeSort is stable; QuickSort is not
2. Median-of-three pivot prevents O(n²) on sorted input
3. Insertion sort is faster for small subarrays (< 10 elements)
4. Recursion depth matters for QuickSort (stack overflow risk)

### From Design Patterns
1. Strategy pattern enables runtime algorithm swapping
2. Factory Method encapsulates object creation complexity
3. Decorator adds behavior without modifying existing code
4. Composition over inheritance makes code more flexible

---

## Dependencies on Phase 1

Phase 2 builds on Phase 1 components:

```
MyHashMap
├── uses MyLinkedList (for collision chains)
└── uses Iterator pattern

SortAlgorithms
├── operates on MyList<T>
└── uses MyArrayList (for merge buffers)

SessionStore
├── uses MyHashMap<String, UserSession>
└── uses Iterator (for cleanup)

UserRegistry
├── uses MyHashMap<String, User>
├── uses MyArrayList (for collecting users)
└── uses SortAlgorithms (for sorted views)
```

---

## Next Steps

Phase 2 is complete. Proceed to [Phase 3: Hierarchies](../phase3/index.md) to build:
- Binary Search Tree (BST)
- AVL Tree (self-balancing)
- Min/Max Heap (Priority Queue)
- Composite, Command, and Visitor patterns
- File System & Job Scheduler demo application

---

*Part of the Omni-System Grimoire*
