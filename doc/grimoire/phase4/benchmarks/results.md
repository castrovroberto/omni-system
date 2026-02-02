# Phase 4 Benchmark Results

## Representation Comparison

### Memory Usage (1000 Vertices)

| Edges | Adj. List | Adj. Matrix | Winner |
|-------|-----------|-------------|--------|
| 100 (sparse) | ~50 KB | ~8 MB | **List** |
| 10,000 | ~400 KB | ~8 MB | **List** |
| 100,000 | ~4 MB | ~8 MB | **List** |
| 500,000 (dense) | ~20 MB | ~8 MB | **Matrix** |

**Crossover point:** When E > V²/2, matrix becomes more efficient.

### hasEdge() Performance

| Representation | Time (1M calls) |
|----------------|-----------------|
| Adjacency List | 45 ms (O(deg)) |
| Adjacency Matrix | 8 ms (O(1)) |

Matrix is **~5x faster** for edge existence checks.

---

## Algorithm Performance

### DFS/BFS (V=1000, E=5000)

| Algorithm | Recursive | Iterative |
|-----------|-----------|-----------|
| DFS | 2.1 ms | 2.4 ms |
| BFS | - | 1.9 ms |

Negligible difference; iterative avoids stack overflow on deep graphs.

### Dijkstra (Various Sizes)

| Vertices | Edges | Time |
|----------|-------|------|
| 100 | 500 | 1 ms |
| 1,000 | 5,000 | 12 ms |
| 10,000 | 50,000 | 180 ms |
| 100,000 | 500,000 | 2.8 s |

Growth matches O((V+E) log V).

### Topological Sort

| Graph | Time | Result |
|-------|------|--------|
| 1000V DAG | 3 ms | Valid ordering |
| 1000V with cycle | 0.5 ms | CycleDetectedException |

Cycle detection short-circuits early.

---

## Mastery Check Results

| Check | Status | Evidence |
|-------|--------|----------|
| List better for sparse | PASS | 50KB vs 8MB for 100 edges |
| Matrix O(1) hasEdge | PASS | 8ms vs 45ms for 1M calls |
| DFS visits all reachable | PASS | Disconnected test |
| BFS level order | PASS | Shortest unweighted path |
| Dijkstra correctness | PASS | Verified vs Floyd-Warshall |
| TopoSort detects cycles | PASS | Exception on back edge |

---

*Benchmarks run on: MacBook Pro M1, Java 17*
