# Phase 1: The Foundation

> Linear data structures, search algorithms, and core design patterns

---

## Overview

Phase 1 establishes the foundational building blocks of the Omni-System. By implementing dynamic arrays and linked lists from scratch, we internalize how memory management and pointer manipulation work at the lowest level.

**Theme**: Linearity & Memory
**Demo Feature**: Log Management System
**Status**: Complete

---

## Contents

### Theory

Conceptual explanations of data structures and their complexity analysis.

| Document | Description |
|----------|-------------|
| [Dynamic Arrays](theory/dynamic-arrays.md) | How MyArrayList works: resizing, amortized analysis, trade-offs |
| [Linked Lists](theory/linked-lists.md) | How MyLinkedList works: sentinel nodes, pointer manipulation |

### Implementation Notes

Design decisions, gotchas, and lessons learned during implementation.

| Document | Description |
|----------|-------------|
| [MyArrayList Notes](implementation/my-arraylist-notes.md) | 6 design decisions, 5 gotchas with solutions |
| [MyLinkedList Notes](implementation/my-linkedlist-notes.md) | 6 design decisions, 6 gotchas with solutions |

### Benchmarks

Performance measurements and mastery check validation.

| Document | Description |
|----------|-------------|
| [Benchmark Results](benchmarks/results.md) | Timing data, memory profiles, mastery check evidence |

### Application

How Phase 1 components power the demo application.

| Document | Description |
|----------|-------------|
| [Log System](application/log-system.md) | EventLog, AlertQueue, LogManager usage scenarios |

### Blueprints

UML diagrams for visual reference.

| Diagram | Description |
|---------|-------------|
| [MyArrayList](blueprints/my-arraylist.puml) | Class diagram with complexity notes |
| [MyLinkedList](blueprints/my-linkedlist.puml) | Class diagram with sentinel node structure |
| [Log System](blueprints/log-system.puml) | Component relationships and patterns |
| [Search Algorithms](blueprints/search-algorithms.puml) | Utility class structure |

---

## Components Built

### Data Structures

| Component | Location | Tests |
|-----------|----------|-------|
| `MyList<T>` | `com.omni.core.list.MyList` | Interface |
| `MyArrayList<T>` | `com.omni.core.list.MyArrayList` | 33 tests |
| `MyLinkedList<T>` | `com.omni.core.list.MyLinkedList` | 17 tests |

### Algorithms

| Component | Location | Tests |
|-----------|----------|-------|
| `linearSearch()` | `com.omni.core.algorithm.search.SearchAlgorithms` | 8 tests |
| `binarySearch()` | `com.omni.core.algorithm.search.SearchAlgorithms` | 10 tests |

### Design Patterns

| Pattern | Implementation | Purpose |
|---------|----------------|---------|
| **Iterator** | Inner classes in both list implementations | Traverse without exposing internals |
| **Builder** | `SystemEvent.Builder` | Construct immutable events fluently |
| **Singleton** | `LogManager.getInstance()` | Single global logging facility |

### Demo Application

| Component | Uses | Purpose |
|-----------|------|---------|
| `LogManager` | Singleton | Central logging facade |
| `SystemEvent` | Builder | Immutable log entry |
| `EventLog` | MyArrayList | Chronological event storage |
| `AlertQueue` | MyLinkedList | FIFO alert processing |
| `LogSearcher` | Search algorithms | Find events by criteria |

---

## Mastery Checks

| Check | Status | Evidence |
|-------|--------|----------|
| MyArrayList handles 1M integers | PASS | `MyArrayListTest.masteryCheck_oneMillionIntegers_worksCorrectly` |
| Memory grows logarithmically | PASS | ~20 resize events for 1M elements |
| MyLinkedList addFirst is O(1) | PASS | `MyLinkedListTest.masteryCheck_addFirst_isO1` |
| Binary search ~20 comparisons for 1M | PASS | `SearchAlgorithmsTest.masteryCheck_binarySearchOneMillionElements` |
| Binary faster than Linear | PASS | `SearchAlgorithmsTest.masteryCheck_linearVsBinarySearch` |

---

## Key Learnings

### From MyArrayList
1. Doubling capacity ensures amortized O(1) insertion
2. Always null out removed elements to help GC
3. Fail-fast iterators prevent subtle concurrency bugs
4. Bounds checking differs between `get()` and `add()`

### From MyLinkedList
1. Sentinel nodes eliminate entire categories of null-pointer bugs
2. Doubly-linked enables O(1) operations at both ends
3. Traversing from nearest end cuts average lookup in half
4. Reference cycles require explicit cleanup

### From Search Algorithms
1. Binary search is ~50-100x faster than linear for large datasets
2. Binary search requires sorted data (precondition)
3. Return `-(insertion_point + 1)` for not-found follows Java convention

---

## Next Steps

Phase 1 is complete. Proceed to [Phase 2: Organization & Speed](../phase2/index.md) to build:
- `MyHashMap` with collision handling
- Merge Sort and Quick Sort
- Strategy, Factory, and Decorator patterns
- User Authentication demo application

---

*Part of the Omni-System Grimoire*
