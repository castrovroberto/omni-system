# Graph Theory

> Modeling relationships between entities using vertices and edges.

## What is a Graph?

A graph G = (V, E) consists of:
- **V**: Set of vertices (nodes)
- **E**: Set of edges connecting vertices

```
Undirected:          Directed (Digraph):
    A                     A
   / \                   ↙ ↘
  B---C                 B → C
```

---

## Types of Graphs

| Type | Description |
|------|-------------|
| **Undirected** | Edges have no direction (A-B = B-A) |
| **Directed** | Edges have direction (A→B ≠ B→A) |
| **Weighted** | Edges have associated values |
| **Unweighted** | All edges treated equally |
| **Cyclic** | Contains at least one cycle |
| **Acyclic** | No cycles (DAG if directed) |

---

## Representations

### Adjacency List

Store neighbors for each vertex:

```
Graph:    A──5──B    Adjacency List:
          │     │    A: [(B,5), (C,3)]
          3     2    B: [(A,5), (C,2)]
          │     │    C: [(A,3), (B,2)]
          └──C──┘
```

**Implementation**:
```java
MyHashMap<V, MyList<Edge<V>>> adjacencyMap;
```

### Adjacency Matrix

2D array of edge weights:

```
    A   B   C
A [ ∞   5   3 ]
B [ 5   ∞   2 ]
C [ 3   2   ∞ ]
```

**Implementation**:
```java
double[][] matrix;            // Weights
MyHashMap<V, Integer> index;  // Vertex → row/col
```

---

## Space & Time Complexity

| Operation | Adj. List | Adj. Matrix |
|-----------|-----------|-------------|
| Space | O(V + E) | O(V²) |
| Add Vertex | O(1) | O(V²)* |
| Add Edge | O(1) | O(1) |
| Remove Edge | O(deg) | O(1) |
| Has Edge | O(deg) | **O(1)** |
| Get Neighbors | O(1) | O(V) |

*Matrix requires resizing array

---

## When to Use Which?

| Scenario | Best Choice |
|----------|-------------|
| Sparse graph (E << V²) | Adjacency List |
| Dense graph (E ≈ V²) | Adjacency Matrix |
| Frequent edge checks | Adjacency Matrix |
| Memory constrained | Adjacency List |
| Social networks | Adjacency List |
| Complete graphs | Adjacency Matrix |

---

## Key Terminology

| Term | Definition |
|------|------------|
| **Degree** | Number of edges incident to a vertex |
| **In-degree** | Edges pointing into vertex (directed) |
| **Out-degree** | Edges pointing out of vertex (directed) |
| **Path** | Sequence of vertices connected by edges |
| **Cycle** | Path that starts and ends at same vertex |
| **Connected** | Path exists between any two vertices |
| **DAG** | Directed Acyclic Graph |
