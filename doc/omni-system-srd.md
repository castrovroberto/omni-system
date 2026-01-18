# Omni-System: Software Requirements Document

**Version:** 1.0
**Type:** Educational Project
**Language:** Java 17+
**Author:** Roberto Castro
**Date:** January 2026

---

## Table of Contents

1. [Executive Summary](#1-executive-summary)
2. [Project Vision & Educational Goals](#2-project-vision--educational-goals)
3. [Critical Analysis of Original Briefing](#3-critical-analysis-of-original-briefing)
4. [Revised Architecture](#4-revised-architecture)
5. [Phase Specifications](#5-phase-specifications)
6. [Interface Contracts](#6-interface-contracts)
7. [Testing Strategy](#7-testing-strategy)
8. [Documentation Standards](#8-documentation-standards)
9. [Success Criteria](#9-success-criteria)
10. [Appendices](#10-appendices)

---

## 1. Executive Summary

The Omni-System is an **educational project** designed to achieve deep mastery of data structures, algorithms, and design patterns through the construction of a Mock Cloud Resource Orchestrator. The project follows a bottom-up pedagogical approach: starting from primitive memory management concepts and progressively building toward complex interconnected systems.

**This is NOT a production system.** The goal is learning, not deployment. Every implementation decision should prioritize clarity and understanding over optimization or enterprise patterns.

### 1.1 Core Deliverables

| Deliverable | Description |
|-------------|-------------|
| `com.omni.core` | Custom implementations of fundamental data structures and algorithms |
| `com.omni.app` | Demo application showcasing real-world usage scenarios |
| `com.omni.test` | Comprehensive test suite with benchmarks |
| `doc/grimoire/` | Living documentation of theory, implementation, and learnings |

---

## 2. Project Vision & Educational Goals

### 2.1 Learning Objectives

By completing this project, you will:

1. **Internalize** how data structures work at the memory/pointer level
2. **Understand** algorithmic complexity through empirical measurement
3. **Apply** design patterns in contexts where they solve real problems
4. **Develop** the ability to choose the right tool for each problem
5. **Practice** test-driven development and benchmarking methodology

### 2.2 Prerequisites

| Area | Expected Knowledge |
|------|-------------------|
| Java | Generics, Collections API (as reference), OOP principles |
| Algorithms | Big-O notation, basic complexity analysis |
| Tools | Git, Maven/Gradle, JUnit basics |
| Concepts | Memory model (stack vs heap), references vs values |

### 2.3 Anti-Goals (What This Project Is NOT)

- **Not production-ready**: No security hardening, no distributed systems concerns
- **Not feature-complete**: Only implement what teaches the concept
- **Not optimized**: Prefer readable code over micro-optimizations
- **Not using libraries**: The whole point is building from scratch

---

## 3. Critical Analysis of Original Briefing

### 3.1 Strengths of the Original Design

| Aspect | Assessment |
|--------|------------|
| **Pedagogical Progression** | Excellent. The phase structure mirrors how systems evolved historically. |
| **Practical Context** | Strong. The Cloud Orchestrator theme gives meaning to each structure. |
| **Pattern Integration** | Good intent. Patterns are tied to real use cases. |
| **Mastery Checks** | Valuable. Concrete validation criteria prevent superficial implementation. |

### 3.2 Identified Weaknesses & Proposed Solutions

#### Issue 1: Forced Pattern Assignments

**Problem:** Some pattern assignments feel artificial:
- **Adapter** in Phase 2 for "legacy data sources" - there are no legacy sources in a greenfield project
- **Mediator** in Phase 4 - adds complexity without clear educational benefit at that stage

**Solution:** Replace with more naturally-fitting patterns:
- Phase 2: Use **Decorator** instead of Adapter (e.g., CachingHashMap wrapping MyHashMap)
- Phase 4: Keep Observer (excellent fit), replace Mediator with **Template Method** for graph traversal algorithms

#### Issue 2: Missing Interface Contracts

**Problem:** No explicit API specifications. This leads to:
- Inconsistent implementations across phases
- Difficulty testing against expected behavior
- No clear "done" criteria

**Solution:** Define explicit interfaces in Section 6 before implementation begins.

#### Issue 3: Testing Strategy is Implicit

**Problem:** "Mastery Checks" are mentioned but no systematic testing approach is defined.

**Solution:** Add Section 7 with:
- Unit testing requirements
- Property-based testing for data structures
- Benchmark methodology
- Mutation testing for algorithm correctness

#### Issue 4: Missing Error Handling Philosophy

**Problem:** No guidance on how to handle edge cases, invalid inputs, or failure modes.

**Solution:** Define explicit error handling strategy:
- Use unchecked exceptions for programmer errors (e.g., `IndexOutOfBoundsException`)
- Use checked exceptions for recoverable errors (e.g., `DuplicateKeyException`)
- Never return null from collection operations - use `Optional<T>`

#### Issue 5: Concurrency Ignored

**Problem:** A "Cloud Orchestrator" naturally involves concurrent operations, but the briefing ignores thread safety entirely.

**Solution:** Add **Phase 5: Concurrency** before documentation phase:
- Thread-safe wrappers for core data structures
- Concurrent HashMap implementation
- Lock-free queue (optional advanced topic)

#### Issue 6: Missing Complexity Budget

**Problem:** No guidance on when to stop. Students might over-engineer.

**Solution:** Add explicit scope limits:
- Maximum lines of code per component
- Required vs optional features clearly marked
- "Good enough" criteria for each phase

### 3.3 Revised Phase Structure

| Phase | Original | Revised |
|-------|----------|---------|
| 1 | Foundation (Arrays, Lists) | **Same** - excellent starting point |
| 2 | Key-Value & Sorting | **Same** - but replace Adapter with Decorator |
| 3 | Trees & Priority | **Same** - but add Red-Black Tree as optional |
| 4 | Graphs & Networks | **Same** - but replace Mediator with Template Method |
| 5 | Documentation | **NEW: Concurrency** - thread-safe wrappers |
| 6 | - | **Documentation** (moved) |

---

## 4. Revised Architecture

### 4.1 Package Structure

```
com.omni/
├── core/                    # The "Standard Library"
│   ├── list/               # Phase 1: Linear structures
│   │   ├── MyList.java     # Interface
│   │   ├── MyArrayList.java
│   │   └── MyLinkedList.java
│   ├── map/                # Phase 2: Key-Value structures
│   │   ├── MyMap.java
│   │   ├── MyHashMap.java
│   │   └── hash/
│   │       ├── HashStrategy.java
│   │       └── DJB2Hash.java
│   ├── tree/               # Phase 3: Hierarchical structures
│   │   ├── MyTree.java
│   │   ├── BinarySearchTree.java
│   │   ├── AVLTree.java
│   │   └── MyHeap.java
│   ├── graph/              # Phase 4: Network structures
│   │   ├── MyGraph.java
│   │   ├── AdjacencyListGraph.java
│   │   └── AdjacencyMatrixGraph.java
│   ├── algorithm/          # Cross-cutting algorithms
│   │   ├── search/
│   │   ├── sort/
│   │   └── graph/
│   └── concurrent/         # Phase 5: Thread-safe variants
│       ├── SynchronizedList.java
│       └── ConcurrentHashMap.java
│
├── app/                    # The Demo Application
│   ├── log/               # Phase 1 Demo
│   │   ├── LogManager.java
│   │   ├── SystemEvent.java
│   │   └── AlertQueue.java
│   ├── auth/              # Phase 2 Demo
│   │   ├── AuthService.java
│   │   ├── UserRegistry.java
│   │   └── SessionStore.java
│   ├── fs/                # Phase 3 Demo
│   │   ├── VirtualFileSystem.java
│   │   ├── FileNode.java
│   │   └── JobScheduler.java
│   └── network/           # Phase 4 Demo
│       ├── NetworkTopology.java
│       ├── DependencyResolver.java
│       └── ServiceNode.java
│
└── test/                  # Test Suite (mirrors main structure)
    ├── core/
    ├── app/
    └── benchmark/
```

### 4.2 Dependency Graph

```
Phase 1 ──────────────────────────────────┐
  │                                        │
  ▼                                        │
Phase 2 (uses MyArrayList for chaining)   │
  │                                        │
  ▼                                        │
Phase 3 (uses MyArrayList for heap)       │
  │                                        │
  ▼                                        │
Phase 4 (uses MyHashMap, MyHeap)          │
  │                                        │
  ▼                                        │
Phase 5 (wraps all previous structures) ◄─┘
```

### 4.3 Design Pattern Mapping (Revised)

| Phase | Pattern | Application | Justification |
|-------|---------|-------------|---------------|
| 1 | **Iterator** | Traversing MyArrayList/MyLinkedList | Natural fit - Java's Iterable contract |
| 1 | **Builder** | Constructing SystemEvent objects | Multiple optional fields, immutable result |
| 1 | **Singleton** | LogManager instance | Exactly one log sink in the system |
| 2 | **Strategy** | Swappable hash functions | Clear interface, multiple implementations |
| 2 | **Factory Method** | Creating User subtypes | Polymorphic object creation |
| 2 | **Decorator** | CachingHashMap wrapping MyHashMap | Adds behavior without modifying core class |
| 3 | **Composite** | FileNode hierarchy | Files and Folders treated uniformly |
| 3 | **Command** | Job objects in scheduler | Encapsulated operations with undo support |
| 3 | **Visitor** | File system operations | Add operations without modifying nodes |
| 4 | **Observer** | Service dependency notifications | Event-driven updates on state change |
| 4 | **State** | Server lifecycle management | Clean state transitions |
| 4 | **Template Method** | Graph traversal algorithms | DFS/BFS share structure, differ in details |

---

## 5. Phase Specifications

### 5.1 Phase 1: The Foundation

**Duration Estimate:** 2-3 weeks
**Theme:** Linear data and object creation
**Demo Feature:** Log Management System

#### 5.1.1 Data Structures

##### MyArrayList<T>

| Requirement | Priority | Notes |
|-------------|----------|-------|
| Generic type parameter | Required | `<T>` not bounded |
| Dynamic resizing | Required | Double capacity when full |
| Implements `Iterable<T>` | Required | For foreach support |
| `get(int index)` - O(1) | Required | Direct array access |
| `add(T element)` - amortized O(1) | Required | Append to end |
| `add(int index, T element)` - O(n) | Required | Shift elements right |
| `remove(int index)` - O(n) | Required | Shift elements left |
| `size()` - O(1) | Required | Track count separately |
| `contains(T element)` - O(n) | Optional | Linear search |
| Initial capacity configuration | Optional | Constructor parameter |

**Mastery Check:** Insert 1,000,000 integers. Memory should grow logarithmically with resize events, not linearly.

##### MyLinkedList<T> (Doubly Linked)

| Requirement | Priority | Notes |
|-------------|----------|-------|
| Generic type parameter | Required | |
| Inner `Node<T>` class | Required | `prev`, `next`, `value` fields |
| `addFirst(T)` / `addLast(T)` - O(1) | Required | Pointer manipulation only |
| `removeFirst()` / `removeLast()` - O(1) | Required | |
| `get(int index)` - O(n) | Required | Traverse from nearest end |
| Implements `Iterable<T>` | Required | |
| `reverse()` - O(n) | Optional | In-place reversal |

**Mastery Check:** Demonstrate that `addFirst()` is O(1) by timing 100,000 insertions vs ArrayList's `add(0, x)`.

#### 5.1.2 Algorithms

##### Linear Search

```java
<T> int linearSearch(MyList<T> list, T target, Comparator<T> comparator);
```

- Returns index of first match, or -1
- Must work on any `MyList` implementation

##### Binary Search

```java
<T> int binarySearch(MyList<T> sortedList, T target, Comparator<T> comparator);
```

- Precondition: list must be sorted
- Returns index, or `-(insertion_point + 1)` if not found (Java convention)

**Mastery Check:** Compare search times on a sorted list of 1,000,000 elements. Binary search should be ~20 comparisons, linear should be ~500,000 average.

#### 5.1.3 Design Patterns

##### Iterator Pattern

- `MyArrayList` and `MyLinkedList` implement `Iterable<T>`
- Create inner class implementing `Iterator<T>`
- Support `hasNext()`, `next()`, `remove()`
- Fail-fast behavior on concurrent modification (track modification count)

##### Builder Pattern

```java
SystemEvent event = SystemEvent.builder()
    .timestamp(Instant.now())
    .severity(Severity.WARNING)
    .source("auth-service")
    .message("Login attempt failed")
    .metadata("ip", "192.168.1.1")
    .build();
```

##### Singleton Pattern

```java
LogManager logger = LogManager.getInstance();
//logger.log(event);
```

- Lazy initialization
- Thread-safe (prepare for Phase 5)
- Consider: why Singleton is often an antipattern, but acceptable here

#### 5.1.4 Demo Application: Log Management

| Component | Uses | Purpose |
|-----------|------|---------|
| `LogManager` | Singleton | Central logging facility |
| `SystemEvent` | Builder | Immutable log entry |
| `EventLog` | MyArrayList | Stores all events chronologically |
| `AlertQueue` | MyLinkedList | High-priority alerts (fast insertion) |
| `LogSearcher` | Binary/Linear Search | Find events by criteria |

---

### 5.2 Phase 2: Organization & Speed

**Duration Estimate:** 2-3 weeks
**Theme:** Fast retrieval and data ordering
**Demo Feature:** User Authentication & Registry Service

#### 5.2.1 Data Structures

##### MyHashMap<K, V>

| Requirement | Priority | Notes |
|-------------|----------|-------|
| Generic key and value types | Required | |
| Configurable initial capacity | Required | Power of 2 recommended |
| Load factor threshold | Required | Default 0.75 |
| Rehashing on threshold | Required | Double buckets, redistribute |
| `put(K, V)` - amortized O(1) | Required | |
| `get(K)` - O(1) average | Required | |
| `remove(K)` - O(1) average | Required | |
| `containsKey(K)` - O(1) average | Required | |
| Collision handling: Chaining | Required | Use MyLinkedList |
| Collision handling: Open Addressing | Optional | Linear probing variant |
| Key iteration via `keySet()` | Required | Returns `Iterable<K>` |

**Strategy Pattern Integration:**

```java
public interface HashStrategy<K> {
    int hash(K key, int capacity);
}

public class DJB2HashStrategy implements HashStrategy<String> {
    @Override
    public int hash(String key, int capacity) {
        long hash = 5381;
        for (char c : key.toCharArray()) {
            hash = ((hash << 5) + hash) + c;
        }
        return (int) (Math.abs(hash) % capacity);
    }
}
```

**Mastery Check:**
1. Insert 100,000 random strings. Measure average chain length.
2. Insert 100,000 strings with intentionally bad hash (all same bucket). Observe O(n) degradation.
3. Compare custom hash vs `Object.hashCode()`.

#### 5.2.2 Algorithms

##### Merge Sort

```java
<T> void mergeSort(MyList<T> list, Comparator<T> comparator);
```

- Stable sort
- O(n log n) guaranteed
- Requires O(n) auxiliary space
- Implement recursively first, then iteratively (optional)

##### Quick Sort

```java
<T> void quickSort(MyList<T> list, Comparator<T> comparator);
```

- In-place (O(log n) stack space)
- O(n log n) average, O(n²) worst case
- Implement pivot selection strategies:
  - First element (naive)
  - Median-of-three (required)
  - Random pivot (optional)

**Mastery Check:**
1. Sort 1,000,000 random integers with both algorithms. Compare times.
2. Sort already-sorted data. Observe QuickSort degradation with naive pivot.
3. Sort with Median-of-three. Verify performance recovery.

#### 5.2.3 Design Patterns

##### Factory Method Pattern

```java
public abstract class User {
    public abstract Set<Permission> getPermissions();

    public static User create(UserType type, String username) {
        return switch (type) {
            case ADMIN -> new AdminUser(username);
            case DEVELOPER -> new DeveloperUser(username);
            case VIEWER -> new ViewerUser(username);
        };
    }
}
```

##### Decorator Pattern (Revised from Adapter)

```java
public class CachingHashMap<K, V> implements MyMap<K, V> {
    private final MyMap<K, V> delegate;
    private final MyMap<K, V> cache;
    private final int cacheSize;

    // LRU caching behavior wrapping any MyMap implementation
}
```

This is more educational than Adapter because:
- Demonstrates composition over inheritance
- Shows how to add behavior without modifying existing code
- Prepares for understanding Java's `Collections.synchronizedMap()`

#### 5.2.4 Demo Application: Auth Service

| Component | Uses | Purpose |
|-----------|------|---------|
| `SessionStore` | MyHashMap | AuthToken → UserSession mapping |
| `UserRegistry` | MyHashMap + Sorting | User management with sorted views |
| `UserFactory` | Factory Method | Creates Admin/Dev/Viewer users |
| `CachedSessionStore` | Decorator | Adds LRU caching to session lookups |

---

### 5.3 Phase 3: Hierarchies

**Duration Estimate:** 3-4 weeks
**Theme:** Nested data and scheduling
**Demo Feature:** File System & Job Scheduler

#### 5.3.1 Data Structures

##### Binary Search Tree (BST)

| Requirement | Priority | Notes |
|-------------|----------|-------|
| Generic type with Comparable bound | Required | `<T extends Comparable<T>>` |
| `insert(T)` | Required | |
| `find(T)` - returns `Optional<T>` | Required | Never return null |
| `delete(T)` | Required | Handle all 3 cases |
| `min()` / `max()` | Required | |
| In-order traversal | Required | Returns sorted sequence |
| Pre-order / Post-order | Required | |
| Level-order (BFS) | Required | |
| `height()` | Required | For balance verification |

##### AVL Tree (Self-Balancing BST)

| Requirement | Priority | Notes |
|-------------|----------|-------|
| Extends/wraps BST logic | Required | |
| Balance factor tracking | Required | height(left) - height(right) |
| Left rotation | Required | |
| Right rotation | Required | |
| Left-Right rotation | Required | |
| Right-Left rotation | Required | |
| Auto-rebalance on insert/delete | Required | |

**Mastery Check:**
1. Insert 1-100 in sequence into BST. Observe degeneration to linked list (height = 99).
2. Insert same sequence into AVL. Verify height ≤ 7.
3. Visualize tree structure before and after rotations.

##### Min-Heap / Max-Heap (Priority Queue)

| Requirement | Priority | Notes |
|-------------|----------|-------|
| Array-based implementation | Required | Use MyArrayList as backing |
| `insert(T)` - O(log n) | Required | Bubble up |
| `extractMin/Max()` - O(log n) | Required | Bubble down |
| `peek()` - O(1) | Required | |
| `heapify(MyList<T>)` - O(n) | Required | Floyd's algorithm |
| Configurable Comparator | Required | For min/max flexibility |

**Mastery Check:** Implement Heap Sort using your heap. Compare with Merge/Quick sort.

#### 5.3.2 Algorithms

##### Tree Traversals

All traversals should be implemented both:
- Recursively (simpler, limited by stack)
- Iteratively with explicit stack (for deep trees)

##### Visitor Pattern for Traversal Operations

```java
public interface FileSystemVisitor {
    void visitFile(FileNode file);
    void visitDirectory(DirectoryNode dir);
}

public class DiskUsageVisitor implements FileSystemVisitor {
    private long totalBytes = 0;

    @Override
    public void visitFile(FileNode file) {
        totalBytes += file.getSize();
    }

    @Override
    public void visitDirectory(DirectoryNode dir) {
        // Directories contribute to count but not size
    }

    public long getTotalBytes() { return totalBytes; }
}
```

#### 5.3.3 Design Patterns

##### Composite Pattern

```java
public interface FileSystemNode {
    String getName();
    long getSize();
    void accept(FileSystemVisitor visitor);
}

public class FileNode implements FileSystemNode { /* leaf */ }
public class DirectoryNode implements FileSystemNode {
    private MyList<FileSystemNode> children; // can contain both
}
```

##### Command Pattern

```java
public interface Job {
    void execute();
    void undo();  // optional but educational
    int getPriority();
}

public class DeleteFileJob implements Job {
    private final FileSystemNode target;
    private FileSystemNode backup;  // for undo

    @Override
    public void execute() {
        backup = target.clone();
        fileSystem.delete(target);
    }

    @Override
    public void undo() {
        fileSystem.restore(backup);
    }
}
```

#### 5.3.4 Demo Application: File System & Scheduler

| Component | Uses | Purpose |
|-----------|------|---------|
| `VirtualFileSystem` | Composite (Tree) | Hierarchical file/folder storage |
| `FileIndex` | AVL Tree | Fast file lookup by name |
| `JobScheduler` | Min-Heap | Priority-based job execution |
| `DiskUsageCalculator` | Visitor | Calculate folder sizes recursively |

---

### 5.4 Phase 4: Complexity

**Duration Estimate:** 3-4 weeks
**Theme:** Relationships and dependencies
**Demo Feature:** Network Topology & Dependency Resolver

#### 5.4.1 Data Structures

##### Graph (Adjacency List)

| Requirement | Priority | Notes |
|-------------|----------|-------|
| Generic vertex type | Required | `<V>` |
| Weighted edges | Required | `Edge<V>` with weight |
| Directed and undirected modes | Required | Configuration option |
| `addVertex(V)` | Required | |
| `addEdge(V, V, weight)` | Required | |
| `removeVertex(V)` | Required | Also removes incident edges |
| `removeEdge(V, V)` | Required | |
| `getNeighbors(V)` | Required | Returns adjacent vertices |
| `getEdges(V)` | Required | Returns edges with weights |
| `getAllVertices()` | Required | |
| `hasEdge(V, V)` | Required | |

**Implementation:** Use `MyHashMap<V, MyList<Edge<V>>>` for adjacency list.

##### Graph (Adjacency Matrix)

| Requirement | Priority | Notes |
|-------------|----------|-------|
| Same interface as Adjacency List | Required | |
| 2D array backing | Required | `double[][]` for weights |
| Vertex-to-index mapping | Required | MyHashMap<V, Integer> |
| Space: O(V²) | Informational | Trade-off with list |

**Mastery Check:**
1. Create same graph with both representations.
2. Compare memory usage for sparse graph (E << V²) vs dense graph (E ≈ V²).
3. Benchmark `hasEdge()` - matrix should be O(1), list should be O(degree).

#### 5.4.2 Algorithms

##### Depth-First Search (DFS)

```java
<V> void dfs(Graph<V> graph, V start, Consumer<V> visitor);
```

- Implement recursively and iteratively
- Track visited vertices to handle cycles

##### Breadth-First Search (BFS)

```java
<V> void bfs(Graph<V> graph, V start, Consumer<V> visitor);
```

- Uses queue (implement with MyLinkedList)
- Returns level-order traversal

##### Dijkstra's Shortest Path

```java
<V> Map<V, PathInfo<V>> dijkstra(Graph<V> graph, V source);
```

Where `PathInfo` contains:
- `distance`: shortest distance from source
- `predecessor`: previous vertex in the shortest path

**Must use your MyHeap** as the priority queue.

##### Topological Sort

```java
<V> MyList<V> topologicalSort(Graph<V> dag) throws CycleDetectedException;
```

- Only valid for Directed Acyclic Graphs
- **Must detect cycles** and throw exception (critical educational point)
- Implement using DFS with coloring (WHITE/GRAY/BLACK)

**Mastery Check:**
1. Create a dependency graph: A→B, B→C, A→C
2. Run topological sort. Verify valid ordering (C before B before A).
3. Add edge C→A (creates cycle). Verify exception is thrown.

#### 5.4.3 Design Patterns

##### Observer Pattern

```java
public interface ServiceObserver {
    void onServiceStateChanged(ServiceNode service, ServiceState newState);
}

public class ServiceNode {
    private MyList<ServiceObserver> observers = new MyArrayList<>();
    private ServiceState state;

    public void setState(ServiceState newState) {
        this.state = newState;
        notifyObservers();
    }

    private void notifyObservers() {
        for (ServiceObserver observer : observers) {
            observer.onServiceStateChanged(this, state);
        }
    }
}
```

##### State Pattern

```java
public interface ServerState {
    void start(Server server);
    void stop(Server server);
    void crash(Server server);
}

public class BootingState implements ServerState {
    @Override
    public void start(Server server) {
        // Complete boot sequence
        server.setState(new RunningState());
    }

    @Override
    public void stop(Server server) {
        throw new IllegalStateException("Cannot stop while booting");
    }

    @Override
    public void crash(Server server) {
        server.setState(new CrashedState());
    }
}
```

##### Template Method Pattern (Revised from Mediator)

```java
public abstract class GraphTraversal<V> {

    // Template method
    public final void traverse(Graph<V> graph, V start) {
        MySet<V> visited = new MyHashSet<>();
        initializeStructure();
        addToStructure(start);

        while (!isStructureEmpty()) {
            V current = removeFromStructure();
            if (visited.contains(current)) continue;
            visited.add(current);

            processVertex(current);

            for (V neighbor : graph.getNeighbors(current)) {
                if (!visited.contains(neighbor)) {
                    addToStructure(neighbor);
                }
            }
        }
    }

    // Hooks for subclasses
    protected abstract void initializeStructure();
    protected abstract void addToStructure(V vertex);
    protected abstract V removeFromStructure();
    protected abstract boolean isStructureEmpty();
    protected void processVertex(V vertex) { } // optional hook
}

public class DFSTraversal<V> extends GraphTraversal<V> {
    private MyStack<V> stack;
    // Uses stack (LIFO) - goes deep first
}

public class BFSTraversal<V> extends GraphTraversal<V> {
    private MyQueue<V> queue;
    // Uses queue (FIFO) - explores level by level
}
```

This replacement is superior because:
- Shows how algorithm structure can be shared
- Demonstrates the "Hollywood Principle" (don't call us, we'll call you)
- More directly relevant to graph algorithms being implemented

#### 5.4.4 Demo Application: Network Topology

| Component | Uses | Purpose |
|-----------|------|---------|
| `NetworkTopology` | Graph (Adjacency List) | Server/service connections |
| `DependencyResolver` | Topological Sort | Determine startup order |
| `LatencyRouter` | Dijkstra | Find fastest network path |
| `ServiceMonitor` | Observer | React to service failures |
| `ServerLifecycle` | State | Manage server states |

---

### 5.5 Phase 5: Concurrency (NEW)

**Duration Estimate:** 2 weeks
**Theme:** Thread safety and concurrent access
**Demo Feature:** Thread-safe orchestrator components

#### 5.5.1 Learning Objectives

- Understand race conditions and why they occur
- Learn synchronization primitives (synchronized, locks)
- Understand trade-offs between safety and performance
- Prepare for real-world concurrent programming

#### 5.5.2 Components

##### SynchronizedList<T>

Wrapper around any `MyList<T>` that synchronizes all operations.

```java
public class SynchronizedList<T> implements MyList<T> {
    private final MyList<T> delegate;
    private final Object lock = new Object();

    @Override
    public void add(T element) {
        synchronized (lock) {
            delegate.add(element);
        }
    }

    // Note: Iterator is NOT thread-safe - document this!
}
```

##### ConcurrentHashMap<K, V> (Simplified)

Use striped locking for better concurrency than full synchronization.

```java
public class ConcurrentHashMap<K, V> implements MyMap<K, V> {
    private static final int LOCK_COUNT = 16;
    private final Object[] locks = new Object[LOCK_COUNT];
    private final MyMap<K, V> delegate;

    private Object getLockFor(K key) {
        return locks[Math.abs(key.hashCode()) % LOCK_COUNT];
    }
}
```

##### BlockingQueue<T>

Producer-consumer pattern implementation.

```java
public class BlockingQueue<T> {
    private final MyLinkedList<T> queue = new MyLinkedList<>();
    private final int capacity;

    public synchronized void put(T item) throws InterruptedException {
        while (queue.size() >= capacity) {
            wait();
        }
        queue.addLast(item);
        notifyAll();
    }

    public synchronized T take() throws InterruptedException {
        while (queue.isEmpty()) {
            wait();
        }
        T item = queue.removeFirst();
        notifyAll();
        return item;
    }
}
```

#### 5.5.3 Mastery Checks

1. Create race condition demo: multiple threads incrementing counter without synchronization
2. Fix with SynchronizedList - verify correctness
3. Benchmark synchronized vs concurrent hashmap with 8 threads
4. Implement producer-consumer with BlockingQueue

---

## 6. Interface Contracts

### 6.1 Core Interfaces

```java
/**
 * Base interface for all list implementations.
 * Guarantees: null elements are permitted unless implementation specifies otherwise.
 */
public interface MyList<T> extends Iterable<T> {

    /**
     * Appends element to the end of the list.
     * @param element the element to add (might be null)
     * @throws IllegalStateException if list is at capacity and cannot grow
     */
    void add(T element);

    /**
     * Inserts element at specified index.
     * @param index position to insert (0 <= index <= size)
     * @param element the element to insert
     * @throws IndexOutOfBoundsException if index < 0 or index > size
     */
    void add(int index, T element);

    /**
     * Returns element at specified index.
     * @param index position to retrieve (0 <= index < size)
     * @return the element at that position
     * @throws IndexOutOfBoundsException if index < 0 or index >= size
     */
    T get(int index);

    /**
     * Removes and returns element at specified index.
     * @param index position to remove (0 <= index < size)
     * @return the removed element
     * @throws IndexOutOfBoundsException if index < 0 or index >= size
     */
    T remove(int index);

    /**
     * Returns the number of elements.
     * @return element count (always >= 0)
     */
    int size();

    /**
     * Returns true if list contains no elements.
     * @return size() == 0
     */
    boolean isEmpty();

    /**
     * Removes all elements from the list.
     * After this call, size() returns 0.
     */
    void clear();
}
```

```java
/**
 * Base interface for all map implementations.
 * Keys must properly implement hashCode() and equals().
 */
public interface MyMap<K, V> {

    /**
     * Associates value with key. Replaces existing value if key present.
     * @param key the key (might be null if implementation permits)
     * @param value the value (might be null)
     * @return previous value associated with key, or empty if none
     */
    Optional<V> put(K key, V value);

    /**
     * Returns value associated with key.
     * @param key the key to look up
     * @return the value, or empty if key not present
     */
    Optional<V> get(K key);

    /**
     * Removes mapping for key.
     * @param key the key to remove
     * @return the previous value, or empty if key was not present
     */
    Optional<V> remove(K key);

    /**
     * Returns true if map contains the specified key.
     */
    boolean containsKey(K key);

    /**
     * Returns number of key-value mappings.
     */
    int size();

    /**
     * Returns iterable over all keys.
     */
    Iterable<K> keys();

    /**
     * Returns iterable over all values.
     */
    Iterable<V> values();

    /**
     * Returns iterable over all entries.
     */
    Iterable<Entry<K, V>> entries();

    interface Entry<K, V> {
        K getKey();
        V getValue();
    }
}
```

```java
/**
 * Base interface for graph implementations.
 */
public interface MyGraph<V> {

    void addVertex(V vertex);
    void removeVertex(V vertex);
    void addEdge(V from, V to, double weight);
    void addEdge(V from, V to); // weight = 1.0
    void removeEdge(V from, V to);

    boolean hasVertex(V vertex);
    boolean hasEdge(V from, V to);

    Optional<Double> getEdgeWeight(V from, V to);
    Iterable<V> getNeighbors(V vertex);
    Iterable<V> getAllVertices();

    int vertexCount();
    int edgeCount();

    boolean isDirected();
}
```

---

## 7. Testing Strategy

### 7.1 Unit Testing Requirements

Every data structure and algorithm must have:

| Test Category | Description | Example |
|---------------|-------------|---------|
| **Happy Path** | Normal operation | Add/remove elements in sequence |
| **Edge Cases** | Boundary conditions | Empty list, single element, capacity boundaries |
| **Error Cases** | Invalid inputs | Negative indices, null keys (if disallowed) |
| **Contract Tests** | Interface guarantees | Size consistency, iterator behavior |

### 7.2 Property-Based Testing

For data structures, verify invariants hold across random operations:

```java
@Property
void listSizeMaintained(@ForAll List<@From("operations") ListOp> ops) {
    MyList<Integer> list = new MyArrayList<>();
    int expectedSize = 0;

    for (ListOp op : ops) {
        op.apply(list);
        expectedSize = op.updateExpectedSize(expectedSize);
        assertThat(list.size()).isEqualTo(expectedSize);
    }
}
```

### 7.3 Benchmarking Methodology

Use JMH (Java Microbenchmark Harness) for performance testing:

```java
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
public class ArrayListBenchmark {

    @Param({"100", "10000", "1000000"})
    private int size;

    @Benchmark
    public void addToEnd(Blackhole bh) {
        MyArrayList<Integer> list = new MyArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(i);
        }
        bh.consume(list);
    }

    @Benchmark
    public void addToStart(Blackhole bh) {
        MyArrayList<Integer> list = new MyArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(0, i);
        }
        bh.consume(list);
    }
}
```

### 7.4 Comparison Benchmarks

Every custom implementation should be benchmarked against Java's standard library:

| Your Implementation | Java Standard | Metrics to Compare |
|--------------------|---------------|-------------------|
| MyArrayList | ArrayList | add, get, iteration |
| MyLinkedList | LinkedList | addFirst, addLast, iteration |
| MyHashMap | HashMap | put, get, collision handling |
| AVLTree | TreeMap | insert, find, range queries |
| MyHeap | PriorityQueue | insert, extract |

---

## 8. Documentation Standards

### 8.1 The Grimoire Structure

```
doc/grimoire/
├── phase1/
│   ├── theory/
│   │   ├── dynamic-arrays.md
│   │   └── linked-lists.md
│   ├── blueprints/
│   │   ├── my-arraylist.puml      # PlantUML diagram
│   │   └── my-linkedlist.puml
│   ├── implementation/
│   │   └── notes.md               # Design decisions, gotchas
│   ├── benchmarks/
│   │   └── results.md             # JMH output analysis
│   └── application/
│       └── log-system.md          # How it powers the demo
├── phase2/
│   └── ...
└── index.md                       # Navigation and overview
```

### 8.2 Theory Document Template

```markdown
# [Data Structure Name]

## Overview
[2-3 sentences describing what it is and primary use cases]

## Complexity Analysis

| Operation | Average Case | Worst Case | Notes |
|-----------|--------------|------------|-------|
| ...       | ...          | ...        | ...   |

## Space Complexity
[Analysis of memory usage]

## Trade-offs
[When to use vs alternatives]

## Visual Representation
[ASCII diagram or link to image]

## Real-World Applications
[Where this is used in production systems]
```

### 8.3 Implementation Notes Template

```markdown
# [Component] Implementation Notes

## Design Decisions

### Decision 1: [Topic]
- **Options considered:** A, B, C
- **Chosen:** B
- **Rationale:** [Why]

## Gotchas & Lessons Learned

### Issue: [Description]
- **Symptom:** [What went wrong]
- **Root cause:** [Why]
- **Solution:** [How fixed]
- **Lesson:** [What to remember]

## Future Improvements
[What would make this better, but out of scope for educational purposes]
```

---

## 9. Success Criteria

### 9.1 Phase Completion Checklist

Each phase is complete when:

- [ ] All required data structures implemented
- [ ] All required algorithms implemented
- [ ] All design patterns applied correctly
- [ ] Unit tests achieve >90% code coverage
- [ ] Mastery checks pass
- [ ] Benchmarks completed and documented
- [ ] Grimoire documentation written
- [ ] Demo application functional

### 9.2 Project Completion Criteria

The project is complete when:

- [ ] All 5 phases completed per checklist above
- [ ] Demo application runs end-to-end scenario
- [ ] Grimoire is comprehensive and navigable
- [ ] Can explain any implementation decision verbally
- [ ] Can reproduce core algorithms from memory (whiteboard test)

### 9.3 Stretch Goals (Optional)

| Goal | Phase | Description |
|------|-------|-------------|
| Red-Black Tree | 3 | Alternative to AVL with different trade-offs |
| Lock-free Queue | 5 | CAS-based concurrent data structure |
| A* Algorithm | 4 | Heuristic-based pathfinding |
| B-Tree | 3 | Database index structure |
| Bloom Filter | 2 | Probabilistic data structure |

---

## 10. Appendices

### 10.1 Recommended Reading

| Topic | Resource |
|-------|----------|
| Algorithms | "Introduction to Algorithms" (CLRS) |
| Design Patterns | "Head First Design Patterns" |
| Java Concurrency | "Java Concurrency in Practice" |
| Data Structures | "Data Structures and Algorithms in Java" (Goodrich) |

### 10.2 Tools & Setup

```bash
# Project structure (Maven)
mvn archetype:generate \
  -DgroupId=com.omni \
  -DartifactId=omni-system \
  -DarchetypeArtifactId=maven-archetype-quickstart \
  -DinteractiveMode=false

# Dependencies (pom.xml)
- JUnit 5 for testing
- JMH for benchmarking
- AssertJ for fluent assertions
- jqwik for property-based testing (optional)
```

### 10.3 Glossary

| Term | Definition |
|------|------------|
| Amortized | Average cost over a sequence of operations |
| Collision | When two keys hash to the same bucket |
| Invariant | Property that must always be true |
| Fail-fast | Immediately report errors rather than silently continue |
| Load factor | Ratio of entries to buckets in a hash table |

---

## Document History

| Version | Date | Changes |
|---------|------|---------|
| 1.0 | Jan 2026 | Initial SRD based on briefing analysis |

---

*This document is a living specification. Update it as the project evolves.*
