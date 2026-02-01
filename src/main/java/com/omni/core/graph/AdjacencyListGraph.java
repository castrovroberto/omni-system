package com.omni.core.graph;

import com.omni.core.list.MyArrayList;
import com.omni.core.list.MyList;
import com.omni.core.map.MyHashMap;

/**
 * Graph implementation using adjacency lists.
 *
 * <p>Backed by {@link MyHashMap} mapping each vertex to a {@link MyList} of {@link Edge}s. Supports
 * both directed and undirected graphs.
 *
 * <pre>
 *   Directed:     A --5--> B
 *   Undirected:   A --5--- B  (stored as A->B and B->A)
 * </pre>
 *
 * @param <V> the vertex type
 */
public class AdjacencyListGraph<V> implements MyGraph<V> {

  private final MyHashMap<V, MyList<Edge<V>>> adjacencyMap;
  private final boolean directed;
  private int edgeCount;

  /**
   * Constructs a new graph.
   *
   * @param directed true for directed graph, false for undirected
   */
  public AdjacencyListGraph(boolean directed) {
    this.adjacencyMap = new MyHashMap<>();
    this.directed = directed;
    this.edgeCount = 0;
  }

  @Override
  public void addVertex(V vertex) {
    if (vertex == null) {
      throw new IllegalArgumentException("Vertex cannot be null");
    }
    if (!adjacencyMap.containsKey(vertex)) {
      adjacencyMap.put(vertex, new MyArrayList<>());
    }
  }

  @Override
  public boolean removeVertex(V vertex) {
    if (!adjacencyMap.containsKey(vertex)) {
      return false;
    }

    // Count edges to remove from this vertex
    MyList<Edge<V>> edges = adjacencyMap.get(vertex).orElse(new MyArrayList<>());
    edgeCount -= edges.size();

    // Remove edges pointing to this vertex from other vertices
    for (V other : getVertices()) {
      if (other.equals(vertex)) {
        continue;
      }
      MyList<Edge<V>> otherEdges = adjacencyMap.get(other).orElse(new MyArrayList<>());
      for (int i = otherEdges.size() - 1; i >= 0; i--) {
        if (otherEdges.get(i).target().equals(vertex)) {
          otherEdges.remove(i);
          if (directed) {
            edgeCount--;
          }
        }
      }
    }

    adjacencyMap.remove(vertex);
    return true;
  }

  @Override
  public void addEdge(V source, V target, double weight) {
    validateVertex(source);
    validateVertex(target);

    // Remove existing edge if present
    boolean existed = removeEdgeInternal(source, target);
    if (!directed && !source.equals(target)) {
      removeEdgeInternal(target, source);
    }

    MyList<Edge<V>> sourceEdges = adjacencyMap.get(source).orElse(new MyArrayList<>());
    sourceEdges.add(new Edge<>(source, target, weight));
    if (!existed) {
      edgeCount++;
    }

    if (!directed && !source.equals(target)) {
      MyList<Edge<V>> targetEdges = adjacencyMap.get(target).orElse(new MyArrayList<>());
      targetEdges.add(new Edge<>(target, source, weight));
    }
  }

  @Override
  public boolean removeEdge(V source, V target) {
    if (!hasVertex(source) || !hasVertex(target)) {
      return false;
    }

    boolean removed = removeEdgeInternal(source, target);
    if (removed) {
      edgeCount--;
      if (!directed && !source.equals(target)) {
        removeEdgeInternal(target, source);
      }
    }
    return removed;
  }

  private boolean removeEdgeInternal(V source, V target) {
    MyList<Edge<V>> edges = adjacencyMap.get(source).orElse(new MyArrayList<>());
    for (int i = 0; i < edges.size(); i++) {
      if (edges.get(i).target().equals(target)) {
        edges.remove(i);
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean hasVertex(V vertex) {
    return adjacencyMap.containsKey(vertex);
  }

  @Override
  public boolean hasEdge(V source, V target) {
    if (!hasVertex(source)) {
      return false;
    }
    MyList<Edge<V>> edges = adjacencyMap.get(source).orElse(new MyArrayList<>());
    for (int i = 0; i < edges.size(); i++) {
      if (edges.get(i).target().equals(target)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public double getEdgeWeight(V source, V target) {
    if (!hasVertex(source)) {
      throw new IllegalArgumentException("Edge does not exist: " + source + " -> " + target);
    }
    MyList<Edge<V>> edges = adjacencyMap.get(source).orElse(new MyArrayList<>());
    for (int i = 0; i < edges.size(); i++) {
      Edge<V> edge = edges.get(i);
      if (edge.target().equals(target)) {
        return edge.weight();
      }
    }
    throw new IllegalArgumentException("Edge does not exist: " + source + " -> " + target);
  }

  @Override
  public MyList<V> getNeighbors(V vertex) {
    validateVertex(vertex);
    MyList<Edge<V>> edges = adjacencyMap.get(vertex).orElse(new MyArrayList<>());
    MyList<V> neighbors = new MyArrayList<>();
    for (int i = 0; i < edges.size(); i++) {
      neighbors.add(edges.get(i).target());
    }
    return neighbors;
  }

  @Override
  public MyList<Edge<V>> getEdges(V vertex) {
    validateVertex(vertex);
    return adjacencyMap.get(vertex).orElse(new MyArrayList<>());
  }

  @Override
  public MyList<V> getVertices() {
    MyList<V> vertices = new MyArrayList<>();
    for (V key : adjacencyMap.keys()) {
      vertices.add(key);
    }
    return vertices;
  }

  @Override
  public int vertexCount() {
    return adjacencyMap.size();
  }

  @Override
  public int edgeCount() {
    return edgeCount;
  }

  @Override
  public boolean isDirected() {
    return directed;
  }

  private void validateVertex(V vertex) {
    if (!adjacencyMap.containsKey(vertex)) {
      throw new IllegalArgumentException("Vertex not in graph: " + vertex);
    }
  }
}
