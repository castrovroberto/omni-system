package com.omni.core.graph;

import com.omni.core.list.MyArrayList;
import com.omni.core.list.MyLinkedList;
import com.omni.core.list.MyList;
import com.omni.core.map.MyHashMap;
import com.omni.core.tree.MyHeap;
import java.util.Comparator;

/**
 * Static utility class providing common graph algorithms.
 *
 * <p>All algorithms work with any {@link MyGraph} implementation.
 */
public final class GraphAlgorithms {

  private GraphAlgorithms() {
    // Utility class
  }

  /**
   * Depth-first search (recursive) from a start vertex.
   *
   * @param <V> the vertex type
   * @param graph the graph to traverse
   * @param start the starting vertex
   * @return list of vertices in DFS visit order
   */
  public static <V> MyList<V> dfsRecursive(MyGraph<V> graph, V start) {
    MyList<V> result = new MyArrayList<>();
    MyHashMap<V, Boolean> visited = new MyHashMap<>();
    dfsHelper(graph, start, visited, result);
    return result;
  }

  private static <V> void dfsHelper(
      MyGraph<V> graph, V vertex, MyHashMap<V, Boolean> visited, MyList<V> result) {
    if (visited.containsKey(vertex)) {
      return;
    }
    visited.put(vertex, true);
    result.add(vertex);
    MyList<V> neighbors = graph.getNeighbors(vertex);
    for (int i = 0; i < neighbors.size(); i++) {
      dfsHelper(graph, neighbors.get(i), visited, result);
    }
  }

  /**
   * Depth-first search (iterative) from a start vertex using a stack.
   *
   * @param <V> the vertex type
   * @param graph the graph to traverse
   * @param start the starting vertex
   * @return list of vertices in DFS visit order
   */
  public static <V> MyList<V> dfsIterative(MyGraph<V> graph, V start) {
    MyList<V> result = new MyArrayList<>();
    MyHashMap<V, Boolean> visited = new MyHashMap<>();
    MyLinkedList<V> stack = new MyLinkedList<>();

    stack.addFirst(start);
    while (!stack.isEmpty()) {
      V vertex = stack.removeFirst();
      if (visited.containsKey(vertex)) {
        continue;
      }
      visited.put(vertex, true);
      result.add(vertex);

      // Push neighbors in reverse order for consistent ordering
      MyList<V> neighbors = graph.getNeighbors(vertex);
      for (int i = neighbors.size() - 1; i >= 0; i--) {
        if (!visited.containsKey(neighbors.get(i))) {
          stack.addFirst(neighbors.get(i));
        }
      }
    }
    return result;
  }

  /**
   * Breadth-first search from a start vertex using a queue.
   *
   * @param <V> the vertex type
   * @param graph the graph to traverse
   * @param start the starting vertex
   * @return list of vertices in BFS visit order
   */
  public static <V> MyList<V> bfs(MyGraph<V> graph, V start) {
    MyList<V> result = new MyArrayList<>();
    MyHashMap<V, Boolean> visited = new MyHashMap<>();
    MyLinkedList<V> queue = new MyLinkedList<>();

    visited.put(start, true);
    queue.addLast(start);

    while (!queue.isEmpty()) {
      V vertex = queue.removeFirst();
      result.add(vertex);

      MyList<V> neighbors = graph.getNeighbors(vertex);
      for (int i = 0; i < neighbors.size(); i++) {
        V neighbor = neighbors.get(i);
        if (!visited.containsKey(neighbor)) {
          visited.put(neighbor, true);
          queue.addLast(neighbor);
        }
      }
    }
    return result;
  }

  /**
   * Dijkstra's shortest path algorithm. Computes shortest distances from source to all reachable
   * vertices.
   *
   * @param <V> the vertex type
   * @param graph the graph
   * @param source the source vertex
   * @return map from vertex to shortest distance from source
   */
  public static <V> MyHashMap<V, Double> dijkstra(MyGraph<V> graph, V source) {
    DijkstraResult<V> result = dijkstraCore(graph, source, null);
    return result.dist();
  }

  /**
   * Dijkstra's algorithm with path reconstruction.
   *
   * @param <V> the vertex type
   * @param graph the graph
   * @param source the source vertex
   * @param target the target vertex
   * @return list of vertices forming the shortest path, or empty list if unreachable
   */
  public static <V> MyList<V> dijkstraPath(MyGraph<V> graph, V source, V target) {
    DijkstraResult<V> result = dijkstraCore(graph, source, target);

    // Reconstruct path
    MyLinkedList<V> path = new MyLinkedList<>();
    double targetDist = result.dist().get(target).orElse(Double.POSITIVE_INFINITY);
    if (targetDist == Double.POSITIVE_INFINITY) {
      return path;
    }

    V current = target;
    while (current != null) {
      path.addFirst(current);
      current = result.prev().get(current).orElse(null);
    }
    return path;
  }

  private static <V> DijkstraResult<V> dijkstraCore(MyGraph<V> graph, V source, V target) {
    MyHashMap<V, Double> dist = new MyHashMap<>();
    MyHashMap<V, V> prev = new MyHashMap<>();
    MyHashMap<V, Boolean> visited = new MyHashMap<>();

    MyList<V> vertices = graph.getVertices();
    for (int i = 0; i < vertices.size(); i++) {
      dist.put(vertices.get(i), Double.POSITIVE_INFINITY);
    }
    dist.put(source, 0.0);

    MyHeap<DistVertex<V>> heap = new MyHeap<>(Comparator.comparingDouble(DistVertex::distance));
    heap.insert(new DistVertex<>(0.0, source));

    while (!heap.isEmpty()) {
      DistVertex<V> current = heap.extractRoot();
      V u = current.vertex();
      if (visited.containsKey(u)) {
        continue;
      }
      visited.put(u, true);

      if (target != null && target.equals(u)) {
        break;
      }

      MyList<Edge<V>> edges = graph.getEdges(u);
      for (int i = 0; i < edges.size(); i++) {
        Edge<V> edge = edges.get(i);
        V v = edge.target();
        double newDist = current.distance() + edge.weight();
        double oldDist = dist.get(v).orElse(Double.POSITIVE_INFINITY);
        if (newDist < oldDist) {
          dist.put(v, newDist);
          prev.put(v, u);
          heap.insert(new DistVertex<>(newDist, v));
        }
      }
    }
    return new DijkstraResult<>(dist, prev);
  }

  /** Result holder for Dijkstra's algorithm. */
  private record DijkstraResult<V>(MyHashMap<V, Double> dist, MyHashMap<V, V> prev) {}

  /**
   * Topological sort using 3-color DFS (Kahn-style coloring).
   *
   * @param <V> the vertex type
   * @param graph the directed graph
   * @return list of vertices in topological order
   * @throws CycleDetectedException if the graph contains a cycle
   * @throws IllegalArgumentException if the graph is not directed
   */
  public static <V> MyList<V> topologicalSort(MyGraph<V> graph) throws CycleDetectedException {
    if (!graph.isDirected()) {
      throw new IllegalArgumentException("Topological sort requires a directed graph");
    }

    // 0 = white (unvisited), 1 = gray (in progress), 2 = black (done)
    MyHashMap<V, Integer> color = new MyHashMap<>();
    MyList<V> vertices = graph.getVertices();
    for (int i = 0; i < vertices.size(); i++) {
      color.put(vertices.get(i), 0);
    }

    MyLinkedList<V> result = new MyLinkedList<>();
    for (int i = 0; i < vertices.size(); i++) {
      int c = color.get(vertices.get(i)).orElse(0);
      if (c == 0) {
        topoHelper(graph, vertices.get(i), color, result);
      }
    }
    return result;
  }

  private static <V> void topoHelper(
      MyGraph<V> graph, V vertex, MyHashMap<V, Integer> color, MyLinkedList<V> result)
      throws CycleDetectedException {
    color.put(vertex, 1); // gray

    MyList<V> neighbors = graph.getNeighbors(vertex);
    for (int i = 0; i < neighbors.size(); i++) {
      V neighbor = neighbors.get(i);
      int c = color.get(neighbor).orElse(0);
      if (c == 1) {
        throw new CycleDetectedException("Cycle detected involving vertex: " + neighbor);
      }
      if (c == 0) {
        topoHelper(graph, neighbor, color, result);
      }
    }

    color.put(vertex, 2); // black
    result.addFirst(vertex);
  }

  /** Helper record for Dijkstra's priority queue. */
  private record DistVertex<V>(double distance, V vertex) {}
}
