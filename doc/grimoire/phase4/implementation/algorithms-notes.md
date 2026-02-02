# Algorithm Implementation Notes

## DFS: Recursive vs Iterative

### Recursive
```java
public static <V> void dfsRecursive(MyGraph<V> graph, V start) {
    MyHashMap<V, Boolean> visited = new MyHashMap<>();
    dfsHelper(graph, start, visited);
}

private static <V> void dfsHelper(graph, vertex, visited) {
    if (visited.containsKey(vertex)) return;
    visited.put(vertex, true);
    // process vertex
    for (V neighbor : graph.getNeighbors(vertex)) {
        dfsHelper(graph, neighbor, visited);
    }
}
```

### Iterative (Avoids Stack Overflow)
```java
public static <V> void dfsIterative(MyGraph<V> graph, V start) {
    MyLinkedList<V> stack = new MyLinkedList<>();
    MyHashMap<V, Boolean> visited = new MyHashMap<>();
    
    stack.addFirst(start);
    while (!stack.isEmpty()) {
        V vertex = stack.removeFirst();
        if (visited.containsKey(vertex)) continue;
        visited.put(vertex, true);
        // Push neighbors in reverse for consistent order
        for (int i = neighbors.size() - 1; i >= 0; i--) {
            stack.addFirst(neighbors.get(i));
        }
    }
}
```

---

## BFS: Queue-Based

```java
MyLinkedList<V> queue = new MyLinkedList<>();
visited.put(start, true);  // Mark BEFORE enqueueing
queue.addLast(start);

while (!queue.isEmpty()) {
    V vertex = queue.removeFirst();
    for (V neighbor : graph.getNeighbors(vertex)) {
        if (!visited.containsKey(neighbor)) {
            visited.put(neighbor, true);  // Mark when discovered
            queue.addLast(neighbor);
        }
    }
}
```

**Key difference from DFS:** Mark visited when *discovered*, not when *processed*.

---

## Dijkstra: Priority Queue

```java
MyHeap<DistVertex<V>> heap = new MyHeap<>(
    Comparator.comparingDouble(DistVertex::distance)
);

// Relaxation
if (dist[u] + weight < dist[v]) {
    dist[v] = dist[u] + weight;
    prev[v] = u;  // For path reconstruction
    heap.insert(new DistVertex<>(dist[v], v));
}
```

### Gotchas

1. **Stale entries in heap**: We might insert same vertex multiple times
   ```java
   if (visited.containsKey(u)) continue;  // Skip stale entries
   ```

2. **Path reconstruction**: Work backwards from target
   ```java
   V current = target;
   while (current != null) {
       path.addFirst(current);
       current = prev.get(current);
   }
   ```

---

## Topological Sort: 3-Color DFS

```java
// Colors: 0=WHITE (unvisited), 1=GRAY (in progress), 2=BLACK (done)

private static void topoHelper(V vertex, colors, result) {
    colors.put(vertex, 1);  // GRAY - starting
    
    for (V neighbor : graph.getNeighbors(vertex)) {
        int color = colors.get(neighbor);
        if (color == 1) {
            throw new CycleDetectedException();  // Back edge!
        }
        if (color == 0) {
            topoHelper(neighbor, colors, result);
        }
    }
    
    colors.put(vertex, 2);  // BLACK - finished
    result.addFirst(vertex);  // Add to FRONT
}
```

### Why 3 Colors?
- **WHITE→GRAY**: Detect back edges within current DFS path
- **GRAY→BLACK**: Safe to not revisit
- **Finding GRAY neighbor**: Cycle detected!

---

## Complexity Verification

| Algorithm | Expected | Measured |
|-----------|----------|----------|
| DFS (1000V, 5000E) | O(V+E) | ~2ms |
| BFS (1000V, 5000E) | O(V+E) | ~2ms |
| Dijkstra (1000V, 5000E) | O((V+E)logV) | ~15ms |
| TopoSort (1000V, DAG) | O(V+E) | ~3ms |
