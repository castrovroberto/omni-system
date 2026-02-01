package com.omni.core.graph.traversal;

import com.omni.core.graph.MyGraph;
import com.omni.core.list.MyLinkedList;

/**
 * Depth-first traversal using a LIFO frontier (stack).
 *
 * @param <V> the vertex type
 */
public class DFSTraversal<V> extends GraphTraversal<V> {

  private MyLinkedList<V> stack;

  /**
   * Constructs a DFS traversal.
   *
   * @param graph the graph to traverse
   * @param start the starting vertex
   */
  public DFSTraversal(MyGraph<V> graph, V start) {
    super(graph, start);
  }

  @Override
  protected void initFrontier() {
    stack = new MyLinkedList<>();
  }

  @Override
  protected void addToFrontier(V vertex) {
    stack.addFirst(vertex);
  }

  @Override
  protected V removeFromFrontier() {
    return stack.removeFirst();
  }

  @Override
  protected boolean isFrontierEmpty() {
    return stack.isEmpty();
  }
}
