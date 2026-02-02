# Graph Implementation Notes

## Design Decisions

### 1. Common Interface
```java
public interface MyGraph<V> {
    void addVertex(V vertex);
    void addEdge(V source, V target, double weight);
    boolean hasEdge(V source, V target);
    MyList<V> getNeighbors(V vertex);
    MyList<Edge<V>> getEdges(V vertex);
    // ...
}
```

Both `AdjacencyListGraph` and `AdjacencyMatrixGraph` implement this interface, enabling algorithm reuse.

### 2. Edge Record
```java
public record Edge<V>(V source, V target, double weight) {}
```
Immutable, compact representation for weighted edges.

### 3. Directed Mode Configuration
```java
public AdjacencyListGraph(boolean directed) {
    this.directed = directed;
}
```

Undirected graphs store each edge twice (A→B and B→A).

---

## Adjacency List Implementation

```java
private final MyHashMap<V, MyList<Edge<V>>> adjacencyMap;
```

### Adding Edge
```java
public void addEdge(V source, V target, double weight) {
    validateVertex(source);
    validateVertex(target);
    
    MyList<Edge<V>> edges = adjacencyMap.get(source).get();
    edges.add(new Edge<>(source, target, weight));
    
    if (!directed && !source.equals(target)) {
        // Add reverse edge for undirected
        MyList<Edge<V>> targetEdges = adjacencyMap.get(target).get();
        targetEdges.add(new Edge<>(target, source, weight));
    }
}
```

---

## Adjacency Matrix Implementation

```java
private double[][] matrix;
private final MyHashMap<V, Integer> vertexToIndex;
private final MyList<V> indexToVertex;
```

### Edge Check (O(1))
```java
public boolean hasEdge(V source, V target) {
    int i = vertexToIndex.get(source).orElseThrow();
    int j = vertexToIndex.get(target).orElseThrow();
    return matrix[i][j] != NO_EDGE;
}
```

### Matrix Resize
When adding vertex beyond capacity:
```java
private void resize(int newCapacity) {
    double[][] newMatrix = new double[newCapacity][newCapacity];
    for (int i = 0; i < matrix.length; i++) {
        System.arraycopy(matrix[i], 0, newMatrix[i], 0, matrix.length);
        for (int j = matrix.length; j < newCapacity; j++) {
            newMatrix[i][j] = NO_EDGE;
        }
    }
    // ...
}
```

---

## Gotchas

### 1. Self-Loops
```java
// Don't add reverse edge for self-loops in undirected graphs
if (!directed && !source.equals(target)) {
    addReverseEdge();
}
```

### 2. Edge Count Tracking
Undirected graphs: count edge once, not twice
```java
edgeCount++;  // Even though we store A→B and B→A
```

### 3. Vertex Removal
Must remove all incident edges:
```java
for (V other : getVertices()) {
    removeEdgeInternal(other, vertex);  // Edges pointing TO vertex
}
removeEdgeInternal(vertex, ...);  // Edges FROM vertex
adjacencyMap.remove(vertex);
```

### 4. Matrix NO_EDGE Sentinel
```java
private static final double NO_EDGE = Double.POSITIVE_INFINITY;
```
Using `∞` allows Dijkstra to work without special cases.
