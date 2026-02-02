# Phase 4: Complexity

> Graph data structures, pathfinding algorithms, and behavioral patterns

---

## Overview

Phase 4 introduces **graph-based data structures** that model complex relationships and dependencies. By implementing adjacency list and matrix representations, we learn how to choose the right trade-offs for different graph densities. Graph algorithms like DFS, BFS, and Dijkstra power navigation, dependency resolution, and network analysis.

**Theme**: Relationships & Dependencies
**Demo Feature**: Network Topology & Dependency Resolver
**Status**: Complete

---

## Contents

### Theory

Conceptual explanations of data structures and algorithms.

| Document | Description |
|----------|-------------|
| [Graphs](theory/graphs.md) | Graph properties, representations, trade-offs |
| [Graph Algorithms](theory/graph-algorithms.md) | DFS, BFS, Dijkstra, Topological Sort |

### Implementation Notes

Design decisions, gotchas, and lessons learned.

| Document | Description |
|----------|-------------|
| [Graph Notes](implementation/graph-notes.md) | Adjacency List vs Matrix, edge handling |
| [Algorithms Notes](implementation/algorithms-notes.md) | Traversal strategies, cycle detection |

### Benchmarks

Performance measurements and mastery check validation.

| Document | Description |
|----------|-------------|
| [Benchmark Results](benchmarks/results.md) | Representation comparison, algorithm performance |

### Application

How Phase 4 components power the demo application.

| Document | Description |
|----------|-------------|
| [Network Demo](application/network-demo.md) | NetworkTopology, DependencyResolver, LatencyRouter |

### Blueprints

UML diagrams for visual reference.

| Diagram | Description |
|---------|-------------|
| [Graph Structures](blueprints/graph-structures.puml) | MyGraph interface and implementations |
| [Algorithms](blueprints/algorithms.puml) | GraphAlgorithms utility class |
| [Network Topology](blueprints/network-topology.puml) | Demo application architecture |
| [Design Patterns](blueprints/design-patterns.puml) | Observer, State, Mediator |

### Patterns

Design pattern deep-dives.

| Pattern | Documentation |
|---------|---------------|
| [Observer](patterns/observer.md) | React to graph/service state changes |
| [State](patterns/state.md) | Server lifecycle state machine |
| [Mediator](patterns/mediator.md) | Coordinate service communication |

---

## Components Built

### Data Structures

| Component | Location | Tests |
|-----------|----------|-------|
| `MyGraph<V>` | `com.omni.core.graph.MyGraph` | Interface |
| `AdjacencyListGraph<V>` | `com.omni.core.graph.AdjacencyListGraph` | 20+ tests |
| `AdjacencyMatrixGraph<V>` | `com.omni.core.graph.AdjacencyMatrixGraph` | 15+ tests |
| `Edge<V>` | `com.omni.core.graph.Edge` | Record |

### Algorithms

| Component | Location | Tests |
|-----------|----------|-------|
| `dfsRecursive()` | `GraphAlgorithms` | 8 tests |
| `dfsIterative()` | `GraphAlgorithms` | 5 tests |
| `bfs()` | `GraphAlgorithms` | 8 tests |
| `dijkstra()` | `GraphAlgorithms` | 10 tests |
| `topologicalSort()` | `GraphAlgorithms` | 6 tests |

### Design Patterns

| Pattern | Implementation | Purpose |
|---------|----------------|---------|
| **Observer** | `ServiceObserver`, `ObservableGraph` | React to service/node changes |
| **State** | `ServerState`, `BootingState`, `RunningState`, etc. | Server lifecycle management |
| **Mediator** | `NetworkMediator`, `ServiceMediator` | Coordinate inter-service communication |

### Demo Application

| Component | Uses | Purpose |
|-----------|------|---------|
| `NetworkTopology` | AdjacencyListGraph | Model server connections |
| `DependencyResolver` | Topological Sort | Resolve service dependencies |
| `LatencyRouter` | Dijkstra | Find lowest-latency paths |
| `Server` | State Pattern | Lifecycle management |
| `ServiceMonitor` | Observer Pattern | Health monitoring |

---

## Mastery Checks

| Check | Status | Evidence |
|-------|--------|----------|
| Sparse graph: List uses less memory than Matrix | PASS | 1000 vertices, 100 edges |
| Dense graph: Matrix hasEdge is O(1) | PASS | Benchmarked vs O(degree) |
| DFS visits all connected vertices | PASS | Disconnected components tested |
| BFS returns level-order traversal | PASS | Verified shortest paths |
| Dijkstra finds shortest weighted path | PASS | Various topologies |
| Topological sort detects cycles | PASS | CycleDetectedException thrown |

---

## Key Learnings

### From Graph Representations
1. Adjacency List: O(V + E) space, good for sparse graphs
2. Adjacency Matrix: O(V²) space, O(1) edge check
3. Undirected edges stored twice (A→B and B→A)
4. Weighted edges require Edge record, not just neighbor list

### From Graph Algorithms
1. DFS uses stack (recursion or explicit), explores deep first
2. BFS uses queue, finds shortest unweighted paths
3. Dijkstra uses priority queue (MyHeap), handles weighted paths
4. Topological sort: 3-color DFS detects back edges (cycles)

### From Design Patterns
1. Observer decouples event producers from consumers
2. State eliminates complex conditional logic in lifecycle methods
3. Mediator centralizes communication, reduces coupling

---

## Dependencies on Phase 1-3

```
AdjacencyListGraph
├── uses MyHashMap<V, MyList<Edge<V>>> (Phase 2)
└── uses MyArrayList (Phase 1)

GraphAlgorithms
├── uses MyHashMap (visited tracking, Phase 2)
├── uses MyLinkedList (queue/stack, Phase 1)
└── uses MyHeap (Dijkstra priority queue, Phase 3)

Observer/State Patterns
├── uses MyArrayList (observer list)
└── standalone state machine logic
```

---

## Next Steps

Phase 4 is complete. Proceed to [Phase 5: Concurrency](../phase5/index.md) to build:
- Thread-safe data structure wrappers
- Concurrent collections
- Producer-Consumer patterns
- Thread pool demo application

---

*Part of the Omni-System Grimoire*
