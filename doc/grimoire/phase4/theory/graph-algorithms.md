# Graph Algorithms

> Traversal, pathfinding, and ordering algorithms for graphs.

---

## Depth-First Search (DFS)

Explores as far as possible before backtracking.

### Algorithm

```
DFS(graph, start):
  visited = {}
  stack = [start]
  
  while stack not empty:
    v = stack.pop()
    if v in visited: continue
    visited.add(v)
    process(v)
    for neighbor in graph.neighbors(v):
      stack.push(neighbor)
```

### Visualization

```
    A                Visit order: A, B, D, E, C
   / \               (Goes deep first)
  B   C
 / \
D   E
```

### Properties
- Time: O(V + E)
- Space: O(V) for visited + O(V) stack
- Use: Topological sort, cycle detection, pathfinding

---

## Breadth-First Search (BFS)

Explores neighbors level by level.

### Algorithm

```
BFS(graph, start):
  visited = {start}
  queue = [start]
  
  while queue not empty:
    v = queue.dequeue()
    process(v)
    for neighbor in graph.neighbors(v):
      if neighbor not in visited:
        visited.add(neighbor)
        queue.enqueue(neighbor)
```

### Visualization

```
    A                Visit order: A, B, C, D, E
   / \               (Level by level)
  B   C
 / \
D   E
```

### Properties
- Time: O(V + E)
- Space: O(V)
- Use: **Shortest path (unweighted)**, level-order traversal

---

## Dijkstra's Algorithm

Finds shortest weighted paths from source to all vertices.

### Algorithm

```
Dijkstra(graph, source):
  dist[*] = ∞
  dist[source] = 0
  pq = min-heap with (0, source)
  
  while pq not empty:
    (d, u) = pq.extractMin()
    if u visited: continue
    mark u visited
    
    for edge (u → v, weight):
      if dist[u] + weight < dist[v]:
        dist[v] = dist[u] + weight
        pq.insert((dist[v], v))
  
  return dist
```

### Visualization

```
    A──1──B            From A:
    │     │            A: 0
    4     2            B: 1
    │     │            C: 3 (via B, not direct)
    └──5──C

dist[C] = min(5, 1+2) = 3
```

### Properties
- Time: O((V + E) log V) with binary heap
- Space: O(V)
- Requirement: **Non-negative edge weights**
- Use: GPS routing, network latency

---

## Topological Sort

Orders vertices so all edges point forward. Only valid for DAGs.

### Algorithm (3-Color DFS)

```
TopoSort(graph):
  color[*] = WHITE
  result = []
  
  for v in vertices:
    if color[v] == WHITE:
      visit(v)
  
  return result

visit(v):
  color[v] = GRAY           // In progress
  for neighbor in neighbors(v):
    if color[neighbor] == GRAY:
      throw CycleDetected   // Back edge!
    if color[neighbor] == WHITE:
      visit(neighbor)
  color[v] = BLACK          // Done
  result.addFirst(v)        // Add to front
```

### Visualization

```
Dependency graph:          Topological order:
A → B → D                  [A, C, B, D]
└→ C ─┘                    or [C, A, B, D]
```

### Properties
- Time: O(V + E)
- Space: O(V)
- Use Cases:
  - Build systems (compile order)
  - Package managers (install order)
  - Task scheduling

---

## Complexity Summary

| Algorithm | Time | Space | Use Case |
|-----------|------|-------|----------|
| DFS | O(V+E) | O(V) | Exploration, cycles |
| BFS | O(V+E) | O(V) | Shortest unweighted |
| Dijkstra | O((V+E)log V) | O(V) | Shortest weighted |
| Topo Sort | O(V+E) | O(V) | Ordering DAGs |
