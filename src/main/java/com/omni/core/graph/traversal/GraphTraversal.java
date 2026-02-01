package com.omni.core.graph.traversal;

import com.omni.core.graph.MyGraph;
import com.omni.core.list.MyArrayList;
import com.omni.core.list.MyList;
import com.omni.core.map.MyHashMap;

/**
 * Abstract template for graph traversal algorithms (Template Method pattern).
 *
 * <p>Subclasses define the frontier data structure (stack for DFS, queue for BFS) while the
 * traversal loop remains fixed.
 *
 * @param <V> the vertex type
 */
public abstract class GraphTraversal<V> {

  protected final MyGraph<V> graph;
  protected final V start;

  /**
   * Constructs a traversal for the given graph starting at the specified vertex.
   *
   * @param graph the graph to traverse
   * @param start the starting vertex
   */
  protected GraphTraversal(MyGraph<V> graph, V start) {
    this.graph = graph;
    this.start = start;
  }

  /**
   * Executes the traversal and returns visited vertices in order.
   *
   * @return list of vertices in traversal order
   */
  public final MyList<V> traverse() {
    MyList<V> result = new MyArrayList<>();
    MyHashMap<V, Boolean> visited = new MyHashMap<>();

    initFrontier();
    addToFrontier(start);

    while (!isFrontierEmpty()) {
      V vertex = removeFromFrontier();
      if (visited.containsKey(vertex)) {
        continue;
      }
      visited.put(vertex, true);
      processVertex(vertex, result);

      MyList<V> neighbors = graph.getNeighbors(vertex);
      for (int i = 0; i < neighbors.size(); i++) {
        V neighbor = neighbors.get(i);
        if (!visited.containsKey(neighbor)) {
          addToFrontier(neighbor);
        }
      }
    }
    return result;
  }

  /** Initializes the frontier data structure. */
  protected abstract void initFrontier();

  /**
   * Adds a vertex to the frontier.
   *
   * @param vertex the vertex to add
   */
  protected abstract void addToFrontier(V vertex);

  /**
   * Removes and returns the next vertex from the frontier.
   *
   * @return the next vertex
   */
  protected abstract V removeFromFrontier();

  /**
   * Returns true if the frontier is empty.
   *
   * @return true if empty
   */
  protected abstract boolean isFrontierEmpty();

  /**
   * Processes a visited vertex. Default implementation adds it to the result list.
   *
   * @param vertex the vertex being visited
   * @param result the result list
   */
  protected void processVertex(V vertex, MyList<V> result) {
    result.add(vertex);
  }
}
