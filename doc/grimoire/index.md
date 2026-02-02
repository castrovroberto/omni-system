# Omni-System Grimoire

> A living documentation of data structures, algorithms, and design patterns built from scratch.

## Overview

The Grimoire is your guide through the Omni-System implementation. Each phase builds upon the previous, progressing from fundamental data structures to complex interconnected systems.

## Navigation

> **📋 [Interview Preparation Guide](interview-prep.md)** — Connect your code to common interview questions

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
- **Iterator** - [Fail-fast iteration](phase1/patterns/iterator.md)
- **Builder** - [SystemEvent construction](phase1/patterns/builder.md)
- **Singleton** - [LogManager instance](phase1/patterns/singleton.md)

#### Demo Application
| Component | Uses | Purpose |
|-----------|------|---------|
| `LogManager` | Singleton | Central logging facility |
| `SystemEvent` | Builder | Immutable log entries |
| `EventLog` | MyArrayList | Chronological storage |
| `AlertQueue` | MyLinkedList | Priority alerts |

---

### Phase 2: Organization & Speed
*Fast retrieval and data ordering*

#### Theory
- [Hash Tables](phase2/theory/hash-tables.md) - Hashing, collision resolution, resizing
- [Sorting Algorithms](phase2/theory/sorting-algorithms.md) - MergeSort and QuickSort

#### Implementations
| Component | Source | Notes |
|-----------|--------|-------|
| `MyHashMap<K,V>` | [MyHashMap.java](../../src/main/java/com/omni/core/map/MyHashMap.java) | [Implementation Notes](phase2/implementation/my-hashmap-notes.md) |
| `CachingHashMap<K,V>` | [CachingHashMap.java](../../src/main/java/com/omni/core/map/CachingHashMap.java) | LRU cache decorator |
| `SortAlgorithms` | [SortAlgorithms.java](../../src/main/java/com/omni/core/algorithm/sort/SortAlgorithms.java) | [Implementation Notes](phase2/implementation/sort-algorithms-notes.md) |

#### Design Patterns
- **Strategy** - [HashStrategy for swappable algorithms](phase2/patterns/strategy.md)
- **Factory Method** - [User type creation](phase2/patterns/factory-method.md)
- **Decorator** - [CachingHashMap wrapper](phase2/patterns/decorator.md)

#### Demo Application
| Component | Uses | Purpose |
|-----------|------|---------|
| `SessionStore` | MyHashMap | O(1) session lookup |
| `UserRegistry` | MyHashMap + Sort | User management |

---

### Phase 3: Hierarchies
*Nested data and scheduling*

#### Theory
- [Binary Search Trees](phase3/theory/binary-search-trees.md) - Ordered tree with O(log n) average operations
- [AVL Trees](phase3/theory/avl-trees.md) - Self-balancing BST with guaranteed O(log n)
- [Heaps](phase3/theory/heaps.md) - Priority queue with O(log n) insert/extract

#### Implementations
| Component | Source | Notes |
|-----------|--------|-------|
| `BinarySearchTree<T>` | [BinarySearchTree.java](../../src/main/java/com/omni/core/tree/BinarySearchTree.java) | [Implementation Notes](phase3/implementation/bst-notes.md) |
| `AVLTree<T>` | [AVLTree.java](../../src/main/java/com/omni/core/tree/AVLTree.java) | Extends BST with rotations |
| `MyHeap<T>` | [MyHeap.java](../../src/main/java/com/omni/core/tree/MyHeap.java) | Array-backed, configurable comparator |

#### Design Patterns
- **Composite** - [FileSystemNode hierarchy](phase3/patterns/composite.md)
- **Command** - [Job/JobScheduler](phase3/patterns/command.md)
- **Visitor** - [FileSystemVisitor](phase3/patterns/visitor.md)

#### Demo Application
| Component | Uses | Purpose |
|-----------|------|---------|
| `VirtualFileSystem` | Composite | Hierarchical file/folder storage |
| `FileIndex` | AVL Tree | Fast file lookup by name |
| `JobScheduler` | Min-Heap | Priority-based job execution |

---

### Phase 4: Complexity
*Relationships and dependencies*

#### Theory
- [Graphs](phase4/theory/graphs.md) - Graph properties, representations, trade-offs
- [Graph Algorithms](phase4/theory/graph-algorithms.md) - DFS, BFS, Dijkstra, Topological Sort

#### Implementations
| Component | Source | Notes |
|-----------|--------|-------|
| `AdjacencyListGraph<V>` | [AdjacencyListGraph.java](../../src/main/java/com/omni/core/graph/AdjacencyListGraph.java) | [Implementation Notes](phase4/implementation/graph-notes.md) |
| `AdjacencyMatrixGraph<V>` | [AdjacencyMatrixGraph.java](../../src/main/java/com/omni/core/graph/AdjacencyMatrixGraph.java) | O(1) edge check |
| `GraphAlgorithms` | [GraphAlgorithms.java](../../src/main/java/com/omni/core/graph/GraphAlgorithms.java) | [Algorithm Notes](phase4/implementation/algorithms-notes.md) |

#### Design Patterns
- **Observer** - [ServiceObserver/Server](phase4/patterns/observer.md)
- **State** - [ServerState lifecycle](phase4/patterns/state.md)
- **Mediator** - [NetworkMediator](phase4/patterns/mediator.md)

#### Demo Application
| Component | Uses | Purpose |
|-----------|------|---------|
| `NetworkTopology` | AdjacencyListGraph | Model server connections |
| `DependencyResolver` | Topological Sort | Resolve service dependencies |
| `LatencyRouter` | Dijkstra | Find lowest-latency paths |

### Phase 5: Concurrency
*Thread safety and concurrent access*

#### Theory
- [Concurrency Basics](phase5/theory/concurrency.md) - Race conditions, synchronization, wait/notify
- [Producer-Consumer](phase5/theory/producer-consumer.md) - BlockingQueue pattern

#### Implementations
| Component | Source | Notes |
|-----------|--------|-------|
| `SynchronizedList<T>` | [SynchronizedList.java](../../src/main/java/com/omni/core/concurrent/SynchronizedList.java) | [Implementation Notes](phase5/implementation/synchronized-list-notes.md) |
| `BlockingQueue<T>` | [BlockingQueue.java](../../src/main/java/com/omni/core/concurrent/BlockingQueue.java) | [Implementation Notes](phase5/implementation/blocking-queue-notes.md) |
| `ConcurrentHashMap<K,V>` | [ConcurrentHashMap.java](../../src/main/java/com/omni/core/concurrent/ConcurrentHashMap.java) | [Implementation Notes](phase5/implementation/concurrent-hashmap-notes.md) |

#### Demo Application
| Component | Uses | Purpose |
|-----------|------|---------|
| `ThreadPool` | BlockingQueue | Fixed-size worker pool |
| `WorkerThread` | BlockingQueue | Task consumer |
| `TaskFuture<T>` | wait/notify | Async result handling |

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
