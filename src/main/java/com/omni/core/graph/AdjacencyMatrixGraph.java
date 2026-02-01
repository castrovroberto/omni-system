package com.omni.core.graph;

import com.omni.core.list.MyArrayList;
import com.omni.core.list.MyList;
import com.omni.core.map.MyHashMap;
import java.util.Optional;

/**
 * Graph implementation using an adjacency matrix.
 *
 * <p>Backed by a {@code double[][]} matrix with {@link MyHashMap} for vertex-to-index mapping. Uses
 * {@link Double#POSITIVE_INFINITY} to indicate no edge. The matrix resizes automatically when
 * capacity is exceeded.
 *
 * <pre>
 *       A    B    C
 *   A [ INF  5.0  INF ]
 *   B [ INF  INF  3.0 ]
 *   C [ INF  INF  INF ]
 * </pre>
 *
 * @param <V> the vertex type
 */
public class AdjacencyMatrixGraph<V> implements MyGraph<V> {

  private static final int DEFAULT_CAPACITY = 16;

  private double[][] matrix;
  private final MyHashMap<V, Integer> vertexIndex;
  private final MyArrayList<V> indexToVertex;
  private final boolean directed;
  private int edgeCount;

  /**
   * Constructs a new graph.
   *
   * @param directed true for directed graph, false for undirected
   */
  public AdjacencyMatrixGraph(boolean directed) {
    this.directed = directed;
    this.matrix = new double[DEFAULT_CAPACITY][DEFAULT_CAPACITY];
    this.vertexIndex = new MyHashMap<>();
    this.indexToVertex = new MyArrayList<>();
    this.edgeCount = 0;
    initMatrix(matrix, 0, DEFAULT_CAPACITY);
  }

  @Override
  public void addVertex(V vertex) {
    if (vertex == null) {
      throw new IllegalArgumentException("Vertex cannot be null");
    }
    if (vertexIndex.containsKey(vertex)) {
      return;
    }
    int index = indexToVertex.size();
    if (index >= matrix.length) {
      resize();
    }
    vertexIndex.put(vertex, index);
    indexToVertex.add(vertex);
  }

  @Override
  public boolean removeVertex(V vertex) {
    Optional<Integer> optIdx = vertexIndex.get(vertex);
    if (optIdx.isEmpty()) {
      return false;
    }
    int idx = optIdx.get();
    int last = indexToVertex.size() - 1;

    // Count and remove edges involving this vertex
    for (int i = 0; i < indexToVertex.size(); i++) {
      if (matrix[idx][i] != Double.POSITIVE_INFINITY) {
        edgeCount--;
      }
      if (i != idx && matrix[i][idx] != Double.POSITIVE_INFINITY && directed) {
        edgeCount--;
      }
      matrix[idx][i] = Double.POSITIVE_INFINITY;
      matrix[i][idx] = Double.POSITIVE_INFINITY;
    }

    // Swap with last vertex if not already last
    if (idx != last) {
      V lastVertex = indexToVertex.get(last);
      // Copy last row/col to idx
      for (int i = 0; i < indexToVertex.size(); i++) {
        matrix[idx][i] = matrix[last][i];
        matrix[i][idx] = matrix[i][last];
        matrix[last][i] = Double.POSITIVE_INFINITY;
        matrix[i][last] = Double.POSITIVE_INFINITY;
      }
      matrix[idx][idx] = matrix[last][last];
      matrix[last][last] = Double.POSITIVE_INFINITY;

      vertexIndex.put(lastVertex, idx);
      indexToVertex.set(idx, lastVertex);
    }

    indexToVertex.remove(last);
    vertexIndex.remove(vertex);
    return true;
  }

  @Override
  public void addEdge(V source, V target, double weight) {
    validateVertex(source);
    validateVertex(target);
    int s = vertexIndex.get(source).get();
    int t = vertexIndex.get(target).get();

    boolean existed = matrix[s][t] != Double.POSITIVE_INFINITY;
    matrix[s][t] = weight;
    if (!directed) {
      matrix[t][s] = weight;
    }
    if (!existed) {
      edgeCount++;
    }
  }

  @Override
  public boolean removeEdge(V source, V target) {
    if (!hasVertex(source) || !hasVertex(target)) {
      return false;
    }
    int s = vertexIndex.get(source).get();
    int t = vertexIndex.get(target).get();
    if (matrix[s][t] == Double.POSITIVE_INFINITY) {
      return false;
    }
    matrix[s][t] = Double.POSITIVE_INFINITY;
    if (!directed) {
      matrix[t][s] = Double.POSITIVE_INFINITY;
    }
    edgeCount--;
    return true;
  }

  @Override
  public boolean hasVertex(V vertex) {
    return vertexIndex.containsKey(vertex);
  }

  @Override
  public boolean hasEdge(V source, V target) {
    if (!hasVertex(source) || !hasVertex(target)) {
      return false;
    }
    int s = vertexIndex.get(source).get();
    int t = vertexIndex.get(target).get();
    return matrix[s][t] != Double.POSITIVE_INFINITY;
  }

  @Override
  public double getEdgeWeight(V source, V target) {
    if (!hasEdge(source, target)) {
      throw new IllegalArgumentException("Edge does not exist: " + source + " -> " + target);
    }
    int s = vertexIndex.get(source).get();
    int t = vertexIndex.get(target).get();
    return matrix[s][t];
  }

  @Override
  public MyList<V> getNeighbors(V vertex) {
    validateVertex(vertex);
    int idx = vertexIndex.get(vertex).get();
    MyList<V> neighbors = new MyArrayList<>();
    for (int i = 0; i < indexToVertex.size(); i++) {
      if (matrix[idx][i] != Double.POSITIVE_INFINITY) {
        neighbors.add(indexToVertex.get(i));
      }
    }
    return neighbors;
  }

  @Override
  public MyList<Edge<V>> getEdges(V vertex) {
    validateVertex(vertex);
    int idx = vertexIndex.get(vertex).get();
    MyList<Edge<V>> edges = new MyArrayList<>();
    for (int i = 0; i < indexToVertex.size(); i++) {
      if (matrix[idx][i] != Double.POSITIVE_INFINITY) {
        edges.add(new Edge<>(vertex, indexToVertex.get(i), matrix[idx][i]));
      }
    }
    return edges;
  }

  @Override
  public MyList<V> getVertices() {
    MyList<V> vertices = new MyArrayList<>();
    for (int i = 0; i < indexToVertex.size(); i++) {
      vertices.add(indexToVertex.get(i));
    }
    return vertices;
  }

  @Override
  public int vertexCount() {
    return indexToVertex.size();
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
    if (!vertexIndex.containsKey(vertex)) {
      throw new IllegalArgumentException("Vertex not in graph: " + vertex);
    }
  }

  private void resize() {
    int newSize = matrix.length * 2;
    double[][] newMatrix = new double[newSize][newSize];
    initMatrix(newMatrix, 0, newSize);
    for (int i = 0; i < matrix.length; i++) {
      System.arraycopy(matrix[i], 0, newMatrix[i], 0, matrix.length);
    }
    matrix = newMatrix;
  }

  private void initMatrix(double[][] m, int from, int to) {
    for (int i = from; i < to; i++) {
      for (int j = 0; j < to; j++) {
        m[i][j] = Double.POSITIVE_INFINITY;
      }
    }
    // Also init rows before 'from' for new columns
    for (int i = 0; i < from; i++) {
      for (int j = from; j < to; j++) {
        m[i][j] = Double.POSITIVE_INFINITY;
      }
    }
  }
}
