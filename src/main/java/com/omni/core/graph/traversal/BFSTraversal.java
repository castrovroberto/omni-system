package com.omni.core.graph.traversal;

import com.omni.core.graph.MyGraph;
import com.omni.core.list.MyLinkedList;

/**
 * Breadth-first traversal using a FIFO frontier (queue).
 *
 * @param <V> the vertex type
 */
public class BFSTraversal<V> extends GraphTraversal<V> {

  private MyLinkedList<V> queue;

  /**
   * Constructs a BFS traversal.
   *
   * @param graph the graph to traverse
   * @param start the starting vertex
   */
  public BFSTraversal(MyGraph<V> graph, V start) {
    super(graph, start);
  }

  @Override
  protected void initFrontier() {
    queue = new MyLinkedList<>();
  }

  @Override
  protected void addToFrontier(V vertex) {
    queue.addLast(vertex);
  }

  @Override
  protected V removeFromFrontier() {
    return queue.removeFirst();
  }

  @Override
  protected boolean isFrontierEmpty() {
    return queue.isEmpty();
  }
}
