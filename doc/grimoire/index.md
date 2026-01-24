# Omni-System Grimoire

> A living documentation of data structures, algorithms, and design patterns built from scratch.

## Overview

The Grimoire is your guide through the Omni-System implementation. Each phase builds upon the previous, progressing from fundamental data structures to complex interconnected systems.

## Navigation

### Phase 1: Foundation
*Linear data structures and object creation patterns*

#### Theory
- [Dynamic Arrays](phase1/theory/dynamic-arrays.md) - Resizable arrays with amortized O(1) append
- [Linked Lists](phase1/theory/linked-lists.md) - Doubly-linked nodes with O(1) end operations

#### Implementations
| Component | Source | Notes |
|-----------|--------|-------|
| `MyList<T>` | [MyList.java](../../src/main/java/com/omni/core/list/MyList.java) | Base interface for list implementations |
| `MyArrayList<T>` | [MyArrayList.java](../../src/main/java/com/omni/core/list/MyArrayList.java) | [Implementation Notes](phase1/implementation/my-arraylist-notes.md) |
| `MyLinkedList<T>` | [MyLinkedList.java](../../src/main/java/com/omni/core/list/MyLinkedList.java) | [Implementation Notes](phase1/implementation/my-linkedlist-notes.md) |

#### Design Patterns
- **Iterator** - Implemented via `Iterable<T>` with fail-fast behavior
- **Builder** - *Planned: SystemEvent construction*
- **Singleton** - *Planned: LogManager*

#### Demo Application
- *Planned: Log Management System*

---

### Phase 2: Organization & Speed
*Fast retrieval and data ordering*

- Hash Maps and Hash Strategies
- Sorting Algorithms (Merge Sort, Quick Sort)
- Strategy, Factory Method, Decorator patterns

*Coming soon...*

---

### Phase 3: Hierarchies
*Nested data and scheduling*

- Binary Search Trees
- AVL Trees (Self-Balancing)
- Heaps and Priority Queues
- Composite, Command, Visitor patterns

*Coming soon...*

---

### Phase 4: Complexity
*Relationships and dependencies*

- Graphs (Adjacency List, Adjacency Matrix)
- Graph Algorithms (DFS, BFS, Dijkstra, Topological Sort)
- Observer, State, Template Method patterns

*Coming soon...*

---

### Phase 5: Concurrency
*Thread safety and concurrent access*

- Synchronized wrappers
- Concurrent data structures
- Producer-consumer patterns

*Coming soon...*

---

## Quick Reference

### Complexity Cheat Sheet

| Structure | Access | Search | Insert | Delete |
|-----------|--------|--------|--------|--------|
| MyArrayList | O(1) | O(n) | O(n)* | O(n) |
| MyLinkedList | O(n) | O(n) | O(1)** | O(1)** |

*O(1) amortized at end
**O(1) at known position; O(n) to find position

### When to Use What

| Scenario | Best Choice |
|----------|-------------|
| Random access by index | MyArrayList |
| Frequent add/remove at ends | MyLinkedList |
| Unknown size, grows frequently | MyLinkedList |
| Memory efficiency | MyArrayList |
| Iterator-based removal | MyLinkedList |

---

## Document Conventions

- **O(n)** - Big-O notation for time/space complexity
- **Amortized** - Average cost over a sequence of operations
- **Sentinel** - Dummy node to simplify boundary conditions
- **Fail-fast** - Immediately detect concurrent modification

---

*This grimoire evolves with the implementation. Check back as new phases are completed.*
